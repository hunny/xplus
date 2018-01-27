package com.xplus.commons.thread.senior.synchronize;

public class ObjectLock {

  public void method1() {
    synchronized (this) {// 对象锁
      System.out.println("执行method1" + System.currentTimeMillis());
      try {
        Thread.sleep(1000);
        System.out.println("执行method1完毕" + System.currentTimeMillis());
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public void method2() {
    synchronized (ObjectLock.class) {// 类锁
      System.out.println("执行method2" + System.currentTimeMillis());
      try {
        Thread.sleep(1000);
        System.out.println("执行method2完毕" + System.currentTimeMillis());
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  private Object lock = new Object();

  private void method3() { // 任何对象锁
    synchronized (lock) {
      System.out.println("执行method3" + System.currentTimeMillis());
      try {
        Thread.sleep(1000);
        System.out.println("执行method3完毕" + System.currentTimeMillis());
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public static void main(String[] args) {
    final ObjectLock obj = new ObjectLock();
    Thread t1 = new Thread(new Runnable() {
      @Override
      public void run() {
        obj.method1();
      }
    });
    Thread t2 = new Thread(new Runnable() {
      @Override
      public void run() {
        obj.method2();
      }
    });
    Thread t3 = new Thread(new Runnable() {
      @Override
      public void run() {
        obj.method3();
      }
    });
    // 三个锁的类不一样，所以相互之间是异步的。
    t1.start();
    t2.start();
    t3.start();
  }

}
