package com.xplus.commons.thread.senior.synchronize;

public class StringConstantLock {

  public void method() {
    synchronized("字符串常量") {//字符串常量池，相当于使用的是类级别的锁。
      try {
        while (true) {
          System.out.println("method当前线程开始" + Thread.currentThread().getName());
          Thread.sleep(500);
          System.out.println("method当前线程结束" + Thread.currentThread().getName());
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
  
  public void method2() {
    synchronized(new String("字符串常量")) {// 对象锁。
      try {
        while (true) {
          System.out.println("method2当前线程开始" + Thread.currentThread().getName());
          Thread.sleep(500);
          System.out.println("method2当前线程结束" + Thread.currentThread().getName());
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
  
  public static void main(String[] args) {
    final StringConstantLock lock = new StringConstantLock();
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
    Thread t3 = new Thread(new Runnable() {
      @Override
      public void run() {
        lock.method2();
      }
    }, "t3");
    Thread t4 = new Thread(new Runnable() {
      @Override
      public void run() {
        lock.method2();
      }
    }, "t4");
    t1.start();
    t2.start();
    t3.start();
    t4.start();
  }
  
}
