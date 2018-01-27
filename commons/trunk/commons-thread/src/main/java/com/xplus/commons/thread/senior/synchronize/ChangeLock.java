package com.xplus.commons.thread.senior.synchronize;

public class ChangeLock {
  
  private String lock = "lock";
  public void method() {
    synchronized(lock) {
      try {
        System.out.println("当前线程" + Thread.currentThread().getName() + "开始。" + System.currentTimeMillis());
        lock = "chnage lock";//修改了锁，锁发生变化。
        Thread.sleep(2000);
        System.out.println("当前线程" + Thread.currentThread().getName() + "结束。" + System.currentTimeMillis());
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public static void main(String[] args) {
    final ChangeLock lock = new ChangeLock();
    Thread t1 = new Thread(new Runnable() {
      @Override
      public void run() {
        lock.method();
      }
    }, "t1");
    Thread t2 = new Thread(new Runnable() {
      @Override
      public void run() {
        lock.method();
      }
    }, "t2");
    t1.start();
    try {
      Thread.sleep(200);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    t2.start();
  }
  
}
