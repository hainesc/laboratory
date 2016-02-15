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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BucketCounterTest {
  private int total = 1024 * 256;
  private int range = 16384; // [0, 16384)
  private int top = 128;
  private Random random = new Random(0);

  private List<Integer> list = new ArrayList<>(total);
  RecordReader reader = new RecordReader() {
    int index = 0;
    @Override
    public Record next() throws IOException {
      if (++index < total) {
        return new IntRecord(list.get(index));
      }
      return null;
    }

    @Override
    public void close() throws IOException {
      // Do nothing.
    }
  };

  @Before
  public void setUp() {
    for (int i = 0; i < total; ++i) {
      list.add(i, random.nextInt(range));
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testBucketCounter()
      throws ExecutionException, InterruptedException, IOException {
    ExecutorService service = Executors.newSingleThreadExecutor();
    BucketCounter<IntRecord> counter = new BucketCounter<>(reader, top);
    Future future = service.submit(counter);
        List<Map.Entry<IntRecord, Integer>> cret =
        (List<Map.Entry<IntRecord, Integer>>) future.get();
    int max = Math.min(top, cret.size());

    Map<Integer, Long> test = list.stream()
        .collect(Collectors.groupingBy(Function.<Integer>identity(),
            Collectors.counting()));

    for (int i = 0; i < max; ++i) {
      Assert.assertEquals(new Long(cret.get(i).getValue()),
          test.get(cret.get(i).getKey().getV()));
    }
  }
}
