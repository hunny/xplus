package com.xplus.commons.thread.senior.concurrent.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class UseSemaphore {

  public static void main(String[] args) {
    final Semaphore semaphore = new Semaphore(5);
    ExecutorService executor = Executors.newFixedThreadPool(20);
    for (int i = 0; i < 20; i++) {
      final int NO = i;
      Runnable run = new Runnable() {
        @Override
        public void run() {
          try {
            // 获得许可
            semaphore.acquire();
            // 模拟实际业务
            long millis = (long)(Math.random() * 10000);
            System.out.println("执行任务: " + NO + "， 预计耗时：" + millis + "ms。");
            Thread.sleep(millis);
            System.out.println("" + NO + "执行完毕，耗时：" + millis + "ms。");
          } catch (InterruptedException e) {
            e.printStackTrace();
          } finally {
            // 使用完许可，释放
            semaphore.release();
          }
        }
      };
      System.out.println("提交任务：" + NO);
      executor.execute(run);
    }
    executor.shutdown();
  }

}
