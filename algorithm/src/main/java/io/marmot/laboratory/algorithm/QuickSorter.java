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

import java.util.Comparator;

/**
 * Quick sort and quick select by partition.
 */
public class QuickSorter {

  /**
   * Select the kth value from the array based the comparator.
   * @param array, from where.
   * @param c, comparator.
   * @param k, the kth.
   * @param <T>
   * @return, the index.
   */
  public static <T extends Comparable<? super T>> T select(
      T[] array,
      Comparator<? super T> c,
      int k) {
    return select(array, c, 0, array.length - 1, k);
  }

  /**
   * Select the kth value from the array based the comparator.
   * @param array, from where.
   * @param c, comparator.
   * @param begin
   * @param end
   * @param k, the kth.
   * @param <T>
   * @return, the index.
   */
  public static <T extends Comparable<? super T>> T select(
      T[] array,
      Comparator<? super T> c,
      int begin,
      int end,
      int k) {
    // Caution, the first element is indexed by zero.
    // Readable vs Effective!
    // --k;
    if (begin == end) {
      return array[begin];
    }
    int index = partition(array, c, begin, end);
    // if (k + begin <= index) {
    if (k - 1 + begin <= index) {
      // return select(array, c, begin, index, k + 1);
      return select(array, c, begin, index, k);

    } else {
      // the has index - begin + 1 ele in the small partition, so we need
      // select the k - (index - begin + 1) in the big partition.
      // return select(array, c, index + 1, end, k - (index - begin + 1) + 1);
      return select(array, c, index + 1, end, k - (index - begin + 1));
    }
  }

  /**
   * Quick sort
   * @param array
   * @param c
   * @param <T>
   */
  public static <T extends Comparable<? super T>> void sort(
      T[] array,
      Comparator<? super T> c) {
    sort(array, c, 0, array.length - 1);
  }

  /**
   * Quick sort.
   * @param array
   * @param c
   * @param begin
   * @param end
   * @param <T>
   */
  public static <T extends Comparable<? super T>> void sort(
      T[] array,
      Comparator<? super T> c,
      int begin,
      int end) {
    if (begin < end) {
      int index = partition(array, c, begin, end);
      sort(array, c, begin, index);
      sort(array, c, index + 1, end);
    }
  }

  /**
   * Hoare partition used by quick sort and quick select.
   * @param array
   * @param c
   * @param begin
   * @param end
   * @param <T>
   * @return  the index of povit.
   */
  private static <T extends Comparable<? super T>> int partition(
      T[] array,
      Comparator<? super T> c,
      int begin,
      int end) {
    T pivot = array[(begin + end) / 2];
    int i = begin - 1;
    int j = end + 1;
    while (true) {
      do {
        --j;
      } while (c.compare(pivot, array[j]) < 0);
      do {
        ++i;
      } while (c.compare(pivot, array[i]) > 0);
      if (i < j) {
        T tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
      } else return j;
    }
  }
}
