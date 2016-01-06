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
import java.util.Comparator;
import java.util.List;

public class SortedMergerTest {
  private List<List<Integer>> lists = new ArrayList<List<Integer>>(8);
  @Before
  public void setUp() {
    ArrayList<Integer> l0 = new ArrayList<>(10);
    for (int i = 0; i < 10; ++i) {
      l0.add(i, 100 - 7 * i);
    }
    lists.add(l0);

    ArrayList<Integer> l1 = new ArrayList<>(10);
    for (int i = 0; i < 10; ++i) {
      l1.add(i, 50 - i);
    }
    lists.add(l1);

    ArrayList<Integer> l2 = new ArrayList<>(7);
    for (int i = 0; i < 7; ++i) {
      l2.add(i, 55 - 2 * i);
    }
    lists.add(l2);

    ArrayList<Integer> l3 = new ArrayList<>(9);
    for (int i = 0; i < 9; ++i) {
      l3.add(i, 63 - 3 * i);
    }
    lists.add(l3);

    ArrayList<Integer> l4 = new ArrayList<>(8);
    for (int i = 0; i < 8; ++i) {
      l4.add(i, 70 - 4 * i);
    }
    lists.add(l4);

    ArrayList<Integer> l5 = new ArrayList<>(14);
    for (int i = 0; i < 14; ++i) {
      l5.add(i, 80 - 5 * i);
    }
    lists.add(l5);

    ArrayList<Integer> l6 = new ArrayList<>(10);
    for (int i = 0; i < 10; ++i) {
      l6.add(i, 54 - 2 * i);
    }
    lists.add(l6);

    ArrayList<Integer> l7 = new ArrayList<>(10);
    for (int i = 0; i < 10; ++i) {
      l7.add(i, 53 - 2 * i);
    }
    lists.add(l7);
  }

  @Test
  public void test() {
    Comparator<Integer> c = new Comparator<Integer>() {
      @Override
      public int compare(Integer o1, Integer o2) {
        return o2.compareTo(o1);
      }
    };
    List<Integer> ret = SortedMerger.sort(lists, c);
    Assert.assertEquals(ret.size(), 78);
    Assert.assertEquals(ret.get(0), (Integer) 100);
    Assert.assertEquals(ret.get(77), (Integer) 15);
  }

  @After
  public void tearDown() {
    // Do nothing.
  }
}
