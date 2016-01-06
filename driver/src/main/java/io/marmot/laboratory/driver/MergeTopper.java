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

public class MergeTopper<T> {
  /**
   * Merge ordered array and find top k element, each array in arrays is
   * sorted. and k is a small number for memory.
   * @param arrays
   * @param c comparator
   * @param k top k
   * @param ret result returned
   * @param <T>
   */
  public static <T> T[] top(T[][] arrays, Comparable<? super T> c, int k, T[] ret) {
    // TODO: index out of boundary check.
    int n = arrays.length;
    int[] indexs = new int[n];
    for (int i = 0; i < n; ++i) {
      indexs[i] = 0;
    }
    T max = (T)arrays[0][0];
    return ret;
  }

  private class MaxHeap<T extends Comparable<T>> {
    private int k; // size of heap
    private T[] heap;
    Integer i = Integer.MAX_VALUE;
  }
}
