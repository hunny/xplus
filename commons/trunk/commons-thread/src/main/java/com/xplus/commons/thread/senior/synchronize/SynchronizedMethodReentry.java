package com.xplus.commons.thread.senior.synchronize;

/**
 * 演示synchronized锁的重入
 * <p>
 * synchronized拥有锁重入的功能， 也就是在使用synchronized时，一个线程得到了一个对象的锁后，再次请求此对象时可以再次得到该对象的锁。
 * 
 */
public class SynchronizedMethodReentry {

  public synchronized void method1() {
    System.out.println("method1");
    method2();
  }
  
  public synchronized void method2() {
    System.out.println("method2");
    method3();
  }
  
  public synchronized void method3() {
    System.out.println("method3");
  }
  
  public static void main(String[] args) {
    final SynchronizedMethodReentry m = new SynchronizedMethodReentry();
    Thread t = new Thread(new Runnable() {
      @Override
      public void run() {
        m.method1();
      }
    });
    t.start();
  }
  
}
