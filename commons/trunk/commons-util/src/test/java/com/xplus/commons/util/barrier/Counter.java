package com.xplus.commons.util.barrier;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class Counter {

  public static AtomicInteger count = new AtomicInteger(0);

  public volatile static int volatileCount = 0;

  public volatile static int synchronizedCount = 0;

  public static void incOfAtomicInteger() {

    // 这里延迟1毫秒，使得结果明显
    try {
      Thread.sleep(1);
    } catch (InterruptedException e) {
    }
    count.getAndIncrement();
  }

  public static void incOfVolatile() {
    // 这里延迟5毫秒，使得结果明显
    try {
      Thread.sleep(5);
    } catch (InterruptedException e) {
    }
    volatileCount++;
  }

  public static void incOfSynchronized() {
    // 这里延迟5毫秒，使得结果明显
    try {
      Thread.sleep(5);
    } catch (InterruptedException e) {
    }
    synchronized (Counter.class) {
      synchronizedCount++;
    }
  }

  public static void main(String[] args) {
    run(new Increment() {
      @Override
      public void inc() {
        Counter.incOfVolatile();
      }

      @Override
      public void show() {
        // 这里每次运行的值都有可能不同,可能为1000
        System.out.println("运行结果:Counter.incOfVolatile=" + Counter.volatileCount);
      }
    });
    run(new Increment() {
      @Override
      public void inc() {
        Counter.incOfSynchronized();
      }
      @Override
      public void show() {
        // 这里每次运行的值都有可能不同,可能为1000
        System.out.println("运行结果:Counter.incOfSynchronized=" + Counter.synchronizedCount);
      }
    });
    run(new Increment() {
      @Override
      public void inc() {
        Counter.incOfAtomicInteger();
      }
      @Override
      public void show() {
        // 这里每次运行的值都有可能不同,可能为1000
        System.out.println("运行结果:Counter.incOfAtomicInteger=" + Counter.count.get());
      }
    });
  }

  public static void run(Increment increment) {
    final CountDownLatch latch = new CountDownLatch(1000);
    // 同时启动1000个线程，去进行i++计算，看看实际结果
    for (int i = 0; i < 1000; i++) {
      new Thread(new Runnable() {
        @Override
        public void run() {
          increment.inc();
          latch.countDown();
        }
      }).start();
    }
    try {
      latch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    increment.show();
  }

  interface Increment {
    void inc();
    void show();
  }

}
