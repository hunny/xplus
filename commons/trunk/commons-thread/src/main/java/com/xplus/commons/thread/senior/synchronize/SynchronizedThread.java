package com.xplus.commons.thread.senior.synchronize;

/**
 * 多个线程一个锁。
 */
public class SynchronizedThread extends Thread {

  private int count = 5;
  
  @Override
  public synchronized void run() {//多个线程一个锁的竞争，此处是线程安全的。
    count --;
    System.out.println("当前线程：" + Thread.currentThread().getName() + ",count值：" + count);
  }
  
  public static void main(String[] args) throws Exception {
    SynchronizedThread m = new SynchronizedThread();
    Thread t1 = new Thread(m, "t1");
    Thread t2 = new Thread(m, "t2");
    Thread t3 = new Thread(m, "t3");
    Thread t4 = new Thread(m, "t4");
    Thread t5 = new Thread(m, "t5");
    t1.start();
    t2.start();
    t3.start();
    t4.start();
    t5.start();
    // t1.start(); //将抛出异常 java.lang.IllegalThreadStateException
  }
  
}
