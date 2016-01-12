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
 * Median of medians algorithm, Select the K'th element from an array.
 * see https://en.wikipedia.org/wiki/Median_of_medians.
 */

public class KthSelector {

  /**
   * Select the K'th element from an unsorted array.
   * @param array
   * @param c the comparator.
   * @param k the K'th.
   * @param <T>
   * @return the K'th value.
   */
  public static <T extends Comparable<? super T>> T select(
      T[] array,
      Comparator<? super T> c,
      int k) {
    return array[select(array, c, 0, array.length - 1, k)];
  }
  /**
   * select the K'th element from an unsorted array, return the index.
   * see https://en.wikipedia.org/wiki/Median_of_medians
   * @param array
   * @param begin the begin.
   * @param end the end.
   * @param c the comparator.
   * @param k the K'th.
   * @param <T>
   * @return the index of the K'th value.
   */
  @SuppressWarnings("unchecked")
  public static <T extends Comparable<? super T>> int select(
      T[] array,
      Comparator<? super T> c,
      int begin,
      int end,
      int k) {
    if (begin == end) {
      return begin;
    }
    while (begin != end)  {
      int current = pivot(array, c, begin, end);
      current = partition(array, c, begin, end, current);
      if (k == current) {
        return k;
      }
      else if (k < current) {
        end = current;
      } else {
        begin = current + 1;
      }
    }
    // Unreachable.
    throw new RuntimeException("Should never reach here.");
  }

  /**
   * Hoare partition used by quick sort and quick select.
   * @param array
   * @param c the comparator.
   * @param begin the begin.
   * @param end the end.
   * @param index, the index of chosen pivot.
   * @param <T>
   * @return the index of pivot.
   */
  private static <T extends Comparable<? super T>> int partition(
      T[] array,
      Comparator<? super T> c,
      int begin,
      int end,
      int index) {
    T pivot = array[index];
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

  /**
   * Actual median-of-medians algorithm, It divides its input into groups of
   * at most five elements, computes the median of each of those groups using
   * some subroutine, then recursively computes the true median of the n/5
   * medians found in the previous step.
   * @param array
   * @param c the comparator.
   * @param begin the begin.
   * @param end the end.
   * @param <T>
   * @return the index of median.
   */
  private static <T extends Comparable<? super T>> int pivot(
      T[] array,
      Comparator<? super T> c,
      int begin,
      int end) {
    if (end - begin < 5) {
      for (int i = begin; i < end; ++i) {
        for (int k = i; k < end; ++k) {
          if (c.compare(array[i], array[k]) > 0) {
            T tmp = array[i];
            array[i] = array[k];
            array[k] = tmp;
          }
        }
      }
      return (begin + end) / 2;
    }

    for (int i = begin; i < end; i += 5) {
      // inner end.
      int r = i + 4;
      if (r > end) {
        r = end;
      }
      int median5 = median5(array, c, i, r);
      T tmp = array[median5];
      array[median5] = array[begin + (i - begin) / 5];
      array[begin + (i - begin) / 5] = tmp;
    }
    return select(array, c, begin, begin + (end - begin) / 5,
        begin + (end - begin) / 10);
  }

  /**
   * Sort array no more than 5 elements and return the index of median.
   * @param array
   * @param c the comparator.
   * @param begin the begin.
   * @param end the end.
   * @param <T>
   * @return return the index of median.
   */
  private static <T extends Comparable<? super T>> int median5(
      T[] array,
      Comparator<? super T> c,
      int begin,
      int end) {
    if (begin - end >= 5) {
      throw new RuntimeException
          ("Should never called while having more than five elements.");
    }
    // Select sort.
    for (int i = begin; i <= end; ++i) {
      for (int k = i; k <= end; ++k) {
        if (c.compare(array[i], array[k]) > 0) {
          T tmp = array[i];
          array[i] = array[k];
          array[k] = tmp;
        }
      }
    }
    return (begin + end) / 2;
  }
}
