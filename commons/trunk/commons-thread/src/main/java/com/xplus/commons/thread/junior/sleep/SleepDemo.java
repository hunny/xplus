package com.xplus.commons.thread.junior.sleep;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SleepDemo {
  public static void main(String[] args) {
    ExecutorService service = Executors.newCachedThreadPool();
    for (int i = 0; i < 5; i++) {
      service.execute(new SleepTask());
    }
    service.shutdown();
  }
}

class SleepTask implements Runnable {
  public void run() {
    int sleppTime = (int) (1 + Math.random() * (10 - 1));
    try {
      TimeUnit.MICROSECONDS.sleep(sleppTime);
      Thread.sleep(sleppTime);
      System.out.println(Thread.currentThread().getName() + "休眠" + sleppTime * 2 + "ms");
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
