/**
 * The MIT License (MIT)
 * Copyright (c) 2016 Haines Chan
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

import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class BucketorTest {
  private final String to = "/tmp/bucket_";
  private final int bucket_cnt = 16;
  private final int top = 1024;
  private int total = 1024 * 16;
  private int range = 16384; // [0,16384)
  @SuppressWarnings("unchecked")
  RecordWriter<IntRecord>[] writers = new RecordWriter[bucket_cnt];
  private Bucketor<IntRecord> bucketor;

  @Before
  public void setUp() throws Exception {
    RecordReader reader = new RecordReader<IntRecord>() {
      final Random r = new Random(0);
      int i = 0;
      @Override
      public IntRecord next() throws IOException {

        if (++i <= total) {
          // Sleep for short circuit test.
          try {
            Thread.sleep(1);
          } catch (InterruptedException ie) {
            throw new IOException("Interrupted.");
          }
          return new IntRecord(r.nextInt(range));
        }
        return null;
      }
      @Override
      public void close() throws IOException {
        // Do nothing.
      }
    };
    for (int i = 0; i < bucket_cnt; ++i) {
      writers[i] = new IntRecordWriter(to + i);
    }

    Shuffler<IntRecord> shuffler = new Shuffler<IntRecord>() {
      @Override
      public int shuffle(IntRecord i, int bucket_cnt) {
        // return i.getV() / bucket_cnt;
        return bucket_cnt - i.getV() / (range / bucket_cnt) - 1;
      }
    };

    bucketor = new Bucketor<>(shuffler, reader, writers, bucket_cnt, null);
    bucketor.setMonitor(bucketor.new TopKShortCircuitMonitor(top));
    bucketor.process();
    while (! bucketor.flushed()) {
      Thread.sleep(1000);
    }
  }

  @Test
  public void testBucketor() throws Exception {
    int cnt = 0;
    for (int i = 0; i < bucket_cnt; ++i) {
      RecordReader<IntRecord> r = new IntRecordReader(to + i);
      IntRecord v;

      while ((v = r.next()) != null) {
        // Assert that records in the same bucket have the same shuffle value.
        Assert.assertEquals(i,
            bucket_cnt - v.getV() / (range / bucket_cnt) - 1);
        ++cnt;
      }
    }
    // Test for short circuit, top <= cnt <= total since short circuit.
    Assert.assertTrue("Very very small probability that cnt == total",
        cnt < total);
    Assert.assertTrue("Very very small probability that cnt == top",
        cnt > top);
  }

  @After
  public void tearDown() {
    try {
      for (int i = 0; i < bucket_cnt; ++i) {
        writers[i].close();
        new File(to + i).deleteOnExit();
      }
    } catch (IOException e) {
      // Do nothing.
    }
  }
}