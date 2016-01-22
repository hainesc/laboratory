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

package io.marmot.laboratory.algorithm;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class TopKTest {
  private final static Random r = new Random(1);
  private final static int length = 1024;
  private final static int bound = 2048;
  private final static int times = 8;
  private ArrayList<Integer> sorted;
  private Integer[] array;

  @Before
  public void setUp() {
    ArrayList<Integer> shuffled = new ArrayList<>(length);
    sorted = new ArrayList<>(length);
    for (int i = 0; i < length; ++i) {
      int x = r.nextInt(bound);
      sorted.add(i, x);
      shuffled.add(i, x);
    }
    Collections.sort(sorted);
    array = shuffled.toArray(new Integer[length]);
  }

  @Test
  public void testMediansSelect() {
    for (int i = 0; i < times; ++i) {
      int k = r.nextInt(length) + 1;
      Integer kth = KthSelector.select(array, new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
          return o1.compareTo(o2);
        }
      }, k);
      Assert.assertEquals(sorted.get(k - 1), kth);
    }
  }
  @After
  public void tearDown() {
  }
}