package com.xplus.commons.thread.senior.concurrent.util;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BarrierRunnerDemo implements Runnable {

  private CyclicBarrier barrier;
  private String name;

  public BarrierRunnerDemo(String name, CyclicBarrier barrier) {
    this.name = name;
    this.barrier = barrier;
  }

  @Override
  public void run() {
    try {
      Thread.sleep(1000 * (new Random().nextInt(5)));
      System.out.println(name + " Ready!");
      barrier.await();
    } catch (InterruptedException | BrokenBarrierException e) {
      e.printStackTrace();
    }
    System.out.println(name + " Run!");
  }

  public static void main(String[] args) {
    int num = 5;
    CyclicBarrier barrier = new CyclicBarrier(num, new Runnable() {
      @Override
      public void run() {
        System.out.println("发令枪声响，砰！");
      }
    });
    ExecutorService pool = Executors.newFixedThreadPool(num);

    for (int i = 0; i < num; i++) {
      pool.execute(new BarrierRunnerDemo("运动员" + (i + 1), barrier));
    }
    pool.shutdown();
  }

}
