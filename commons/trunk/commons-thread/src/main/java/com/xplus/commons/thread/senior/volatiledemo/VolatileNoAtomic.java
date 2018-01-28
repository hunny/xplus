package com.xplus.commons.thread.senior.volatiledemo;

import java.util.Random;

/**
 * 演示volatile没有原子特性，最终结果不一定是10000
 */
public class VolatileNoAtomic extends Thread {

  private volatile static int count = 0;
  
  private static void addCount() {
    for (int i = 0; i < 1000; i++) {
      count ++;
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
    VolatileNoAtomic [] arr = new VolatileNoAtomic[10];
    int num = 10;
    for (int i = 0; i < num; i++) {
      arr[i] = new VolatileNoAtomic();
    }
    
    for (int i = 0; i < num; i++) {
      arr[i].start();
    }
    
  }

}
