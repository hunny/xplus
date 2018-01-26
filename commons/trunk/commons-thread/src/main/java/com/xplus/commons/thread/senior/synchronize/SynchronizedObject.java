package com.xplus.commons.thread.senior.synchronize;

public class SynchronizedObject {

  public synchronized void method1() {
    System.out.println(Thread.currentThread().getName());
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.err.println("method1执行完毕。");
  }
  
  public void method2() {
    System.out.println(Thread.currentThread().getName());
  }
  public synchronized void method3() {
    System.out.println(Thread.currentThread().getName());
  }
  
  public static void main(String[] args) {
    final SynchronizedObject obj = new SynchronizedObject();
    // t1线程持有object对象的Lock锁，
    // t2线程可以以异步的方式调用对象中非synchronized的方法，
    // t3线程需要调用带synchronized同步的方法，则需要等待。
    Thread t1 = new Thread(new Runnable() {
      @Override
      public void run() {
        obj.method1();
      }
    }, "t1");
    Thread t2 = new Thread(new Runnable() {
      @Override
      public void run() {
        obj.method2();
      }
    }, "t2");
    Thread t3 = new Thread(new Runnable() {
      @Override
      public void run() {
        obj.method3();
      }
    }, "t3");
    t1.start();
    t2.start();
    t3.start();
  }
  
}
