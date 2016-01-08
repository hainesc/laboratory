package io.marmot.laboratory.algorithm;

import com.google.common.collect.MinMaxPriorityQueue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Test MaxMinHeap, meanwhile a sample show how to use MinMaxPriorityQueue
 * as a Min Or Max heap.
 */

public class MaxMinHeapTest {
  // Top 12.
  MinMaxPriorityQueue<Integer> heap;
  @Before
  public void setUp() {
    heap = MinMaxPriorityQueue.orderedBy(new Comparator<Integer>() {
      @Override
      public int compare(Integer i1, Integer i2) {
        return i1.compareTo(i2);
      }
    }).maximumSize(12).create();

  }

  @Test
  @SuppressWarnings("unchecked")
  public void test() {
    // fix random seed.
    List list = new ArrayList<>(12 + 1024);
    Random r = new Random(1);
    for (int i = 0; i < 12; ++i) {
      int ele = Math.abs(r.nextInt()) % 512;
      heap.add(ele);
      list.add(i, ele);
    }

    for (int i = 0; i < 1024; ++i) {
      int ele = Math.abs(r.nextInt()) % 512;
      if (ele > heap.peekFirst()) {
        heap.removeFirst();
        heap.add(ele);
      }
      list.add(i + 12, ele);
    }

    Collections.sort(list);
    // The max value not in the heap.
    Integer max = (Integer) list.get(1024 - 1);
    Assert.assertEquals(heap.size(), 12);
    for (int i = 0; i < 12; ++i) {
      Assert.assertTrue(max <= heap.removeFirst());
    }
  }
}
