package io.marmot.laboratory.algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by haines on 1/5/16.
 */
public class KthSelector {
  /**
   * select the kth element from an unsorted List. return the index.
   * see https://en.wikipedia.org/wiki/Median_of_medians
   * @param list
   * @param begin
   * @param end
   * @param c
   * @param k
   * @param <T>
   * @return
   */
  @SuppressWarnings("unchecked")
  public static <T extends Comparable<? super T>> int select(
      ArrayList<T> list,
      int begin,
      int end,
      Comparator<? super T> c,
      int k) {
    if (begin == end) {
      return begin;
    }

    int group = list.size() / 5;


    return -1;
  }

  private static <T extends Comparator<? super T>> int partition(
      ArrayList<T> list,
      int left,
      int right,
      T poviter) {
    return -1;
  }

  private static <T extends Comparator<? super T>> int pivot(
      ArrayList<T> list,
      int left,
      int right) {
    return -1;
  }
}
