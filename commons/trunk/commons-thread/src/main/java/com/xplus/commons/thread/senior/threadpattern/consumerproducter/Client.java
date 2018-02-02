package com.xplus.commons.thread.senior.threadpattern.consumerproducter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Client {
  
  public static void main(String[] args) {
    // 内存缓冲区
    BlockingQueue<PData> queue = new LinkedBlockingQueue<>();
    // 生产者
    Producer p1 = new Producer(queue);
    Producer p2 = new Producer(queue);
    Producer p3 = new Producer(queue);
    
    // 消费者
    Consumer c1 = new Consumer(queue);
    Consumer c2 = new Consumer(queue);
    Consumer c3 = new Consumer(queue);
    
    //创建线程池运行。
    // 这是一个缓存的线程池，可以创建无穷大的线程。
    // 没有任务的时候不创建线程。空闲线程存活的时间为60s(默认值)。
    ExecutorService cachePool = Executors.newCachedThreadPool();
    cachePool.execute(p1);
    cachePool.execute(p2);
    cachePool.execute(p3);
    cachePool.execute(c1);
    cachePool.execute(c2);
    cachePool.execute(c3);
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    p1.stop();
    p2.stop();
    p3.stop();
    System.out.println("执行完毕。");
  }
  
}
