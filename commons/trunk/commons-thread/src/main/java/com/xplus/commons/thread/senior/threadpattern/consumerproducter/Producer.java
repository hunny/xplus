package com.xplus.commons.thread.senior.threadpattern.consumerproducter;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Producer implements Runnable {

  // 共享缓存区
  private BlockingQueue<PData> queue;
  // 多线程间是否启动变量，有强制从主内存中刷新的功能。即时返回线程的状态。
  private volatile boolean isRunning = true;
  // Id生成器
  private static AtomicInteger count = new AtomicInteger();
  // 随机数生成对象
  private static Random r = new Random();

  public Producer(BlockingQueue<PData> queue) {
    this.queue = queue;
  }

  @Override
  public void run() {
    while (isRunning) {
      try {
        // 随机休眠0-1000ms，表示获取数据产生的耗时。
        Thread.sleep(r.nextInt(1000));
        // 获取的数据进行累计
        int id = count.incrementAndGet();
        // 生产数据
        PData pData = new PData(id, "数据-" + id);
        System.out.println("当前线程：" //
            + Thread.currentThread().getName() //
            + "获取数据" //
            + pData //
            + "放入缓存中" //
        );
        if (!this.queue.offer(pData, 2, TimeUnit.SECONDS)) {
          System.err.println("提交缓冲区数据失败。");
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public void stop() {
    this.isRunning = false;
  }

}
