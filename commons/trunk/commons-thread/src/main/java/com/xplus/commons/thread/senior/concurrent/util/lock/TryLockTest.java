package com.xplus.commons.thread.senior.concurrent.util.lock;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TryLockTest {
  
  private ArrayList<Integer> arrayList = new ArrayList<Integer>();
  private Lock lock = new ReentrantLock(); // 注意这个地方

  public static void main(String[] args) {
    final TryLockTest test = new TryLockTest();

    new Thread() {
      public void run() {
        test.demo(Thread.currentThread());
      };
    }.start();

    new Thread() {
      public void run() {
        test.demo(Thread.currentThread());
      };
    }.start();
  }

  public void demo(Thread thread) {
    if (lock.tryLock()) {
      try {
        System.out.println(thread.getName() + "得到了锁");
        for (int i = 0; i < 20; i++) {
          Thread.sleep(new Random().nextInt(100));
          arrayList.add(i);
        }
      } catch (Exception e) {
      } finally {
        System.out.println(thread.getName() + "释放了锁");
        lock.unlock();
      }
    } else {
      System.out.println(thread.getName() + "获取锁失败，试着重新获取锁。");
      try {
        Thread.sleep(new Random().nextInt(1000));
        demo(thread);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
