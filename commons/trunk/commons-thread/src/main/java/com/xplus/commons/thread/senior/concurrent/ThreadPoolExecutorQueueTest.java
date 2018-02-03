package com.xplus.commons.thread.senior.concurrent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolExecutorQueueTest implements Runnable {

  private static AtomicInteger count = new AtomicInteger(0);

  @Override
  public void run() {
    int temp = count.incrementAndGet();
    System.out.println("任务：" + temp);
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) throws Exception {
    BlockingQueue<Runnable> queue = //
//         new LinkedBlockingQueue<>();
        new ArrayBlockingQueue<>(10);
    ExecutorService pool = new ThreadPoolExecutor( //
        5, //
        10, //
        60, //
        TimeUnit.SECONDS, //
        queue //
    );
    for (int i = 0; i < 20; i++) {
      pool.execute(new ThreadPoolExecutorQueueTest());
    }
    Thread.sleep(500);
    System.out.println("Queue Size: " + queue.size());
    Thread.sleep(2000);
    pool.shutdown();
  }

}
