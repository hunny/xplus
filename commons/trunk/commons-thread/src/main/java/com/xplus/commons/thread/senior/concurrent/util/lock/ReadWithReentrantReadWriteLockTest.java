package com.xplus.commons.thread.senior.concurrent.util.lock;

import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWithReentrantReadWriteLockTest {

  private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

  public static void main(String[] args) {
    final ReadWithReentrantReadWriteLockTest test = new ReadWithReentrantReadWriteLockTest();
    for (int i = 0; i < 5; i++) {
      new Thread() {
        public void run() {
          test.get(Thread.currentThread());
        };
      }.start();
    }
  }

  public void get(Thread thread) {
    rwl.readLock().lock();
    try {
      System.out.println(thread.getName() + "正在进行读操作");
      try {
        Thread.sleep(new Random().nextInt(1000));
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println(thread.getName() + "读操作完毕");
    } finally {
      rwl.readLock().unlock();
    }
  }
}
