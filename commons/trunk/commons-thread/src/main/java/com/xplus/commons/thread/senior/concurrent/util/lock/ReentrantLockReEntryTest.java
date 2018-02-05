package com.xplus.commons.thread.senior.concurrent.util.lock;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockReEntryTest {

  private ReentrantLock lock = new ReentrantLock();

  public void testReentrantLock() {
    // 线程获得锁
    lock.lock();
    try {
      System.out.println(Thread.currentThread().getName() + " get lock");
      Thread.sleep(new Random().nextInt(1000));
      lock.lock();
      try {
        System.out.println(Thread.currentThread().getName() + " get lock again");
        Thread.sleep(new Random().nextInt(1000));
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        // 线程释放锁
        lock.unlock();
        System.out.println(Thread.currentThread().getName() + " release lock again");
      }
      System.out.println("都执行完成。");
    } catch (

    InterruptedException e) {
      e.printStackTrace();
    } finally {
      // 线程释放锁
      lock.unlock();
      System.out.println(Thread.currentThread().getName() + " release lock");
    }
  }

  public static void main(String[] args) {
    final ReentrantLockReEntryTest test2 = new ReentrantLockReEntryTest();
    Thread thread = new Thread(new Runnable() {
      public void run() {
        test2.testReentrantLock();
      }
    }, "A");
    thread.start();
  }

}
