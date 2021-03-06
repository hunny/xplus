package com.xplus.commons.thread.senior.synchronize;

public class NoSynchronizedThread extends Thread {

  private int count = 5;
  
  @Override
  public void run() {//此处没有synchronized关键字，非线程安全。
    count --;
    System.out.println("当前线程：" + Thread.currentThread().getName() + ",count值：" + count);
  }
  
  public static void main(String[] args) throws Exception {
    NoSynchronizedThread m = new NoSynchronizedThread();
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
