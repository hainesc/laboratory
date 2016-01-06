/**
 * The MIT License (MIT)
 * Copyright (c) 2015 Haines Chan
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

package io.marmot.laboratory.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Bucketor<T extends Record> {
  /**
   * A bucketor is made up of three modules, which are bucket reader(s),
   * bucket writer(s) and short circuit strategy.
   */
  private final int bucket_cnt;
  // A reader is a thread that reads records from somewhere.
  private BucketReader<T> reader;
  // Writers are threads that write bucketed record to somewhere.
  private BucketWriter<T>[] writers;
  // A short circuit strategy is a thread that monitors the process of
  // reader and modifies omit array which indexed whether the records
  // should omitted.
  private ShortCircuitMonitor monitor;
  private boolean endOfFile;
  private boolean[] flushed;
  private boolean[] omit;
  private int[] count;
  // private RecordReader rr;
  @SuppressWarnings("unchecked")
  public Bucketor(Shuffler<T> shuffler,
                  RecordReader rr,
                  RecordWriter[] rws,
                  int bucket_cnt,
                  ShortCircuitMonitor monitor)
      throws FileNotFoundException {
    this.bucket_cnt = bucket_cnt;
    BlockingQueue<Record>[] blockingQueues =
        new LinkedBlockingQueue[bucket_cnt];
    for (int i = 0; i < bucket_cnt; ++i) {
      blockingQueues[i] = new LinkedBlockingQueue<>();
    }
    this.reader = new BucketReader(shuffler, blockingQueues, rr);
    this.writers = new BucketWriter[bucket_cnt];
    this.flushed = new boolean[bucket_cnt];
    this.count = new int[bucket_cnt];
    this.omit = new boolean[bucket_cnt];
    for (int i = 0; i < bucket_cnt; ++i) {
      this.writers[i] = new BucketWriter<>(rws[i], i, blockingQueues[i]);
      flushed[i] = false;
      count[i] = 0;
      omit[i] = false;
    }
    this.endOfFile = false;
    this.monitor = monitor;
  }

  public void process() {
    reader.start();
    for (int i = 0; i < bucket_cnt; ++i) {
      this.writers[i].start();
    }
    if (monitor != null) {
      monitor.start();
    }

  }

  public boolean flushed() {
    // Another choice is for(...) { if false return false. }
    boolean ret = true;
    for (int i = 0; i < bucket_cnt; ++i) {
      ret &= flushed[i];
    }
    return ret;
  }

  // TODO: callable and return value.
  public int[] getCount() {
    return count;
  }

  public void setMonitor(ShortCircuitMonitor m) {
    this.monitor = m;
  }

  @SuppressWarnings("unchecked")
  private class BucketReader<T extends Record> extends Thread {
    private Shuffler<T> shuffler;
    RecordReader rr;
    private BlockingQueue[] blockingQueues;
    /**
     *
     * @param shuffler the shuffler which shuffler object to an int.
     * @param blockingQueues share with writer.
     * @param rr the record reader.
     */
    public BucketReader(Shuffler<T> shuffler,
                        BlockingQueue[] blockingQueues,
                        RecordReader rr) {
      this.shuffler = shuffler;
      this.blockingQueues = blockingQueues;
      this.rr = rr;
    }
    @Override
    public void run() {
      try {
        Record r;
        while ((r = rr.next()) != null) {
          // Decide shuffle to which bucket.
          int index = shuffler.shuffle((T)r, bucket_cnt);
          if (!omit[index]) {
            blockingQueues[index].add(r);
            ++count[index];
          }
        }
        endOfFile = true;
      } catch (IOException ioe) {
        System.err.println("Error while reading the records.");
        System.exit(1);
      } finally {
        try {
          rr.close();
        } catch (IOException e) {
          // Do nothing.
        }
      }
    }
  }

  private class BucketWriter<T extends Record> extends Thread {
    private RecordWriter rw;
    // id needed here for flushed array.
    private int id;
    private BlockingQueue blockingQueue;
    /**
     *
     * @param rw Record writer.
     * @param blockingQueue share with reader.
     * @throws FileNotFoundException
     */
    public BucketWriter(RecordWriter rw, int id,
                        BlockingQueue blockingQueue)
        throws FileNotFoundException {
      this.rw = rw;
      this.id = id;
      this.blockingQueue = blockingQueue;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run() {
      try {
        // TODO: need not write to disk in some case. popping is enough
        // for example, what you need is top k value, and values in great
        // buckets have count > k, so the value in little bucket need not
        // write to disk.
        Record r;
        while (! endOfFile) {
          // Read and sleep while the data continuously come in.
          while ((r = (Record) blockingQueue.poll(50, TimeUnit.MILLISECONDS))
              != null) {
            rw.write(r);
          }
        }
        // Read all of the data from queue and exit.
        while (! blockingQueue.isEmpty()) {
          rw.write((Record) blockingQueue.poll());
        }
        // Flush to disk.
        rw.flush();
        flushed[id] = true;
      } catch (Exception e) {
        System.err.println("Error while writing record to disk.");
        System.exit(1);
      } finally {
        try {
          rw.close();
        } catch (IOException e) {
          // Do nothing.
        }
      }
    }
  }

  public class TopKShortCircuitMonitor extends ShortCircuitMonitor {
    // No lock, because it is just an optimization, lock on endOFile, omit
    // and count is forbidden since reader thread will not be interrupted.
    private int top;

    /**
     * Top K short circuit optimizer. If your expectation is the top k in the
     * set, and you have the buckets in order, then, if have more the k values
     * in big buckets, then the litter ones can be omitted.
     * @param top
     */
    public TopKShortCircuitMonitor(int top) {
      this.top = top;
    }
    @Override
    public void run() {
      while (!endOfFile) {
        int already = 0;
        try {
          // TODO: parameterize this time.
          Thread.sleep(3);
          int i = 0;
          for (; i < bucket_cnt; ++i) {
            already += count[i];
            if (already >= top) {
              break;
            }
          }
          for (++i; i < bucket_cnt; ++i) {
            if (omit[i]) {
              break;
            }
            omit[i] = true;
          }
        } catch (InterruptedException ie) {
          // Do nothing, just kill itself.
          // Or do this by shouldRun = false.
          Thread.currentThread().interrupt();
        }
      }
    }
  }

  // More short circuit monitors here. keep in mind lock should be avoided
  // for the purpose that the reader should not be interrupted.
  // * Main work should rarely be interrupted by optimization. *

}
