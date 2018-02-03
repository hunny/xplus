package com.xplus.commons.thread.senior.concurrent.util;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CyclicBarrierDemo {

  private CyclicBarrier barrier = new CyclicBarrier(4, new Runnable() {
    @Override
    public void run() {
      System.out.println("我是可选参数，但是一旦使用，当线程到达屏障时，优先执行。");
    }
  });

  private Random r = new Random();

  class TaskDemo implements Runnable {

    private String id;

    public TaskDemo(String id) {
      this.id = id;
    }

    @Override
    public void run() {
      try {
        Thread.sleep(r.nextInt(1000));
        System.out.println("线程：" //
        + Thread.currentThread().getName() //
        + "执行的任务：" + id //
        + "将等待。"
        );
        barrier.await();//等待其它线程
        System.out.println("-------线程 " + id + " 执行完毕。");
      } catch (InterruptedException | BrokenBarrierException e) {
        e.printStackTrace();
      }
    }

  }
  
  public TaskDemo buildTask(String id) {
    return new TaskDemo(id);
  }
  
  public static void main(String[] args) {
    CyclicBarrierDemo demo = new CyclicBarrierDemo();
    ExecutorService pool = Executors.newFixedThreadPool(4);
    pool.submit(demo.buildTask("1"));
    pool.submit(demo.buildTask("2"));
    pool.submit(demo.buildTask("3"));
    pool.submit(demo.buildTask("4"));
    pool.shutdown();
  }

}
