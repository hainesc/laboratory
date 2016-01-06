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

package io.marmot.laboratory.driver;

import io.marmot.laboratory.utils.Bucketor;
import io.marmot.laboratory.utils.RecordReader;
import io.marmot.laboratory.utils.RecordWriter;
import io.marmot.laboratory.utils.Shuffler;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Laboratory.
 */
public class Driver {
  @SuppressWarnings("unchecked")
  public static void main(String[] args) throws Exception {
    String from = "/tmp/data";
    String to = "/tmp/bucket_";
    Shuffler<IntRecord> shuffler = new Shuffler<IntRecord>() {
      @Override
      public int shuffle(IntRecord i, int bucket_cnt) {
        return bucket_cnt - i.getV() / bucket_cnt - 1;
        // return i.getV() / bucket_cnt;
      }
    };
    final int bucket_cnt = 13;
    RecordReader<IntRecord> rr = new IntRecordReader(from);
    RecordWriter<IntRecord>[] rws = new RecordWriter[bucket_cnt];
    for (int i = 0; i < bucket_cnt; ++i) {
      rws[i] = new IntRecordWriter(to + i);
    }
    final int top = 25000;
    Bucketor bucketor = new Bucketor(shuffler, rr, rws, bucket_cnt, null);
    bucketor.setMonitor(bucketor.new TopKShortCircuitMonitor(top));
    bucketor.process();
    while (!bucketor.flushed()) {
      Thread.sleep(1000);
    }
    rr.close();

    Comparator<IntRecord> comparator = new Comparator<IntRecord>() {
      @Override
      public int compare(IntRecord o1, IntRecord o2) {
        return new Integer(o1.getV()).compareTo(o2.getV());
      }
    };

    ExecutorService service = Executors.newSingleThreadExecutor();
    for (int i = 0; i < bucket_cnt; ++i) {
      RecordReader<IntRecord> crr = new IntRecordReader(to + i);
      BucketCounter bc = new BucketCounter(crr, top);
      Future future = service.submit(bc);
      List<Map.Entry<IntRecord, Integer>> list = (List<Map.Entry<IntRecord,Integer>>) future.get();
      int max = Math.min(top, list.size());
      System.out.println("TOP " + max + " in bucket this bucket is: ");
      for (Map.Entry<IntRecord, Integer> ele : list) {
        System.out.println(ele.getKey().toString() + ": " +
            ele.getValue());
      }
    }
    service.shutdown();

  }
}

