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

import java.util.Comparator;

public class QuickSorterTest {

  @Before
  public void setUp() {
  }

  @Test
  public void testQuickSort() {
    Integer[] array = {45, 42, 13, 87, 36, 55, 67, 13, 90, 54, 51, 9};
    QuickSorter.sort(array, new Comparator<Integer>() {
      @Override
      public int compare(Integer i1, Integer i2) {
        return i1.compareTo(i2);
      }
    });
    Assert.assertEquals(array[0], (Integer) 9);
    Assert.assertEquals(array[1], (Integer) 13);
    Assert.assertEquals(array[2], (Integer) 13);
    Assert.assertEquals(array[3], (Integer) 36);
    Assert.assertEquals(array[4], (Integer) 42);
    Assert.assertEquals(array[5], (Integer) 45);
    Assert.assertEquals(array[6], (Integer) 51);
    Assert.assertEquals(array[7], (Integer) 54);
    Assert.assertEquals(array[8], (Integer) 55);
    Assert.assertEquals(array[9], (Integer) 67);
    Assert.assertEquals(array[10], (Integer) 87);
    Assert.assertEquals(array[11], (Integer) 90);
  }

  @Test
  public void testQuickSelect() {
    Integer[] array = {45, 42, 13, 87, 36, 55, 67, 13, 90, 54, 51, 9};

    Integer i0 = QuickSorter.select(array, new Comparator<Integer>() {
      @Override
      public int compare(Integer o1, Integer o2) {
        return o1.compareTo(o2);
      }
    }, 1);
    Assert.assertEquals((Integer) 9, i0);

    Integer i1 = QuickSorter.select(array, new Comparator<Integer>() {
      @Override
      public int compare(Integer o1, Integer o2) {
        return o1.compareTo(o2);
      }
    }, 9);
    Assert.assertEquals((Integer) 55, i1);

    Integer i2 = QuickSorter.select(array, new Comparator<Integer>() {
      @Override
      public int compare(Integer o1, Integer o2) {
        return o1.compareTo(o2);
      }
    }, 5);
    Assert.assertEquals((Integer) 42, i2);
  }

  @After
  public void tearDown() {
  }
}
