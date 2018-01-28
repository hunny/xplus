package com.xplus.commons.thread.senior.volatiledemo;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 演示实现原子特性，最终结果是10000
 */
public class VolatileAtomicImpl extends Thread {

  private static AtomicInteger count = new AtomicInteger(0);
  
  private static void addCount() {
    for (int i = 0; i < 1000; i++) {
      count.incrementAndGet();
    }
    try {
      Thread.sleep(new Random().nextInt(10));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println(count);
  }
  
  @Override
  public void run() {
    addCount();
  }
  
  public static void main(String[] args) {
    VolatileAtomicImpl [] arr = new VolatileAtomicImpl[10];
    int num = 10;
    for (int i = 0; i < num; i++) {
      arr[i] = new VolatileAtomicImpl();
    }
    
    for (int i = 0; i < num; i++) {
      arr[i].start();
    }
    
  }

}
