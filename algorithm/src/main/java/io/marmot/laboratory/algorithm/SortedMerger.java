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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SortedMerger {
  // Merge sort algorithm for sorted lists.
  public static <T extends Comparable<? super T>> List<T> sort(
      List<List<T>> lists,
      Comparator<? super T> c) {
    int top = 0;
    for (List<T> list : lists) {
      top += list.size();
    }
    return top(lists, c, top);
  }

  // Merge top algorithm for sorted lists.
  public static <T extends Comparable<? super T>> List<T> top(
      List<List<T>> lists,
      Comparator<? super T> c,
      int top) {
    int length = lists.size();
    // cursor of each list. -1 for has reached the end.
    int cursor[] = new int[length];
    for (int i = 0; i < length; ++i) {
      cursor[i] = 0;
    }
    List<T> ret = new ArrayList<T>(top);
    // Seed
    T seed = lists.get(0).get(0);
    int incre = 0;
    int count = 0;
    while (count < top) {
      int i = 0;
      for (; i < length; ++i) {
        if (cursor[i] != -1) {
          seed = lists.get(i).get(cursor[i]);
          incre = i;
          break;
        }
      }
      for (++i; i < length; ++i) {
        if (cursor[i] != -1 &&
            c.compare(seed, lists.get(i).get(cursor[i])) > 0) {
          seed = lists.get(i).get(cursor[i]);
          incre = i;
        }
      }
      ret.add(seed);
      ++count;
      if (++cursor[incre] >= lists.get(incre).size()) {
        // reach the end.
        cursor[incre] = -1;
      }
    }
    return ret;
  }
}
