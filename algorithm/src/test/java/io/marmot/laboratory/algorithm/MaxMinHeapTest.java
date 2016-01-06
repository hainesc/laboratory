package io.marmot.laboratory.algorithm;

import com.google.common.collect.MinMaxPriorityQueue;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;
import java.util.Random;

/**
 * Test MaxMinHeap, meanwhile a sample show how to use MinMaxPriorityQueue
 * as a Min Or Max heap.
 */

public class MaxMinHeapTest {
  MinMaxPriorityQueue<Integer> heap;
  @Before
  public void setUp() {
    heap = MinMaxPriorityQueue.orderedBy(new Comparator<Integer>() {
      @Override
      public int compare(Integer i1, Integer i2) {
        return i1.compareTo(i2);
      }
    }).maximumSize(12 + 1).create();
    /*
    heap.add(4);
    heap.add(8);
    heap.add(2);
    heap.add(13);
    heap.add(82);
    heap.add(12);
    heap.add(45);
    heap.add(63);
    heap.add(33);
    heap.add(46);
    heap.add(46);
    */
    Random r = new Random();
    for (int i = 0; i < 12; ++i) {
      int x = Math.abs(r.nextInt());
      System.out.println("Data coming: " + x);
      heap.add(x);
    }

    for (int i = 0; i < 512; ++i) {
      int x = Math.abs(r.nextInt());
      System.out.println("Data coming: " + x);
      int y = heap.peekFirst();
      if (x > y) {
        heap.add(x);
        System.out.println("Data popping: " + heap.removeFirst());
      }

    }
    boolean b = r.nextBoolean();
    System.out.println(b);
  }

  @Test
  public void test() {
    /*
    Assert.assertEquals(heap.removeFirst(), (Integer) 82);
    Assert.assertEquals(heap.removeFirst(), (Integer) 63);
    Assert.assertEquals(heap.removeFirst(), (Integer) 46);
    Assert.assertEquals(heap.removeFirst(), (Integer) 46);
    */
  }

}
