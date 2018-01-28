package com.xplus.commons.thread.senior.threadcommicate.singleton;

public class DoubleSingleton {

  private static DoubleSingleton singleton = null;

  public static DoubleSingleton getInstance() {
    if (null == singleton) {
      try {
        Thread.sleep(200);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      synchronized (DoubleSingleton.class) {// 同步锁
        if (null == singleton) {// 多线程需要双重检测
          singleton = new DoubleSingleton();
        }
      }
    }
    return singleton;
  }

  public static void main(String[] args) {
    Thread t1 = new Thread(new Runnable() {
      @Override
      public void run() {
        System.out.println("当前线程" //
            + Thread.currentThread().getName() //
            + "HashCode:" + DoubleSingleton.getInstance().hashCode());
      }
    }, "t1");
    Thread t2 = new Thread(new Runnable() {
      @Override
      public void run() {
        System.out.println("当前线程" //
            + Thread.currentThread().getName() //
            + "HashCode:" + DoubleSingleton.getInstance().hashCode());
      }
    }, "t2");
    Thread t3 = new Thread(new Runnable() {
      @Override
      public void run() {
        System.out.println("当前线程" //
            + Thread.currentThread().getName() //
            + "HashCode:" + DoubleSingleton.getInstance().hashCode());
      }
    }, "t3");
    t1.start();
    t2.start();
    t3.start();
  }

}
