package com.xplus.commons.thread.senior.concurrent.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreDemo {

  public static void main(String[] args) {
    final Semaphore semaphore = new Semaphore(2);
    ExecutorService executorService = Executors.newCachedThreadPool();
    for (int i = 0; i < 10; i++) {
      final int index = i;
      executorService.execute(new Runnable() {
        public void run() {
          try {
            semaphore.acquire();
            System.out.println("线程:" + Thread.currentThread().getName() + "获得许可:" + index);
            TimeUnit.SECONDS.sleep(3);
            semaphore.release();
            System.out.println("允许TASK个数：" + semaphore.availablePermits());
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      });
    }
    executorService.shutdown();
  }
  
}