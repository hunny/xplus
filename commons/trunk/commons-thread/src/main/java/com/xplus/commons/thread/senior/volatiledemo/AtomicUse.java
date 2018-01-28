package com.xplus.commons.thread.senior.volatiledemo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicUse {

  // atomic类只保证本身方法原子性，并不保证多次调用操作的原子性
  private static AtomicInteger count = new AtomicInteger(0);
  
  // 如果需要保证多次调用的原子性，需要在方法上加synchronized
  public static int addCount() {
    
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    
    count.addAndGet(1);
    count.addAndGet(2);
    count.addAndGet(3);
    count.addAndGet(4);
    
    return count.get();
  }
  
  public static void main(String[] args) {
    List<Thread> list = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      list.add(new Thread(new Runnable() {
        @Override
        public void run() {
          System.out.println(AtomicUse.addCount());
        }
      }));
    }
    for (Thread t : list) {
      t.start();
    }
  }
  
}
