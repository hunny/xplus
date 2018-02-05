package com.xplus.commons.thread.senior.threadpattern.consumerproducter;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {

  // 共享缓存区
  private BlockingQueue<PData> queue;

  // 随机对象
  private Random r = new Random();

  public Consumer(BlockingQueue<PData> queue) {
    this.queue = queue;
  }

  @Override
  public void run() {
    while (true) {
      try {
        // 获取数据
        PData data = this.queue.take();
        // 模拟耗时操作。
        Thread.sleep(r.nextInt(1000));
        System.out.println("当前线程:" //
            + Thread.currentThread().getName() //
            + "消费成功，获取数据：" //
            + data);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

}
