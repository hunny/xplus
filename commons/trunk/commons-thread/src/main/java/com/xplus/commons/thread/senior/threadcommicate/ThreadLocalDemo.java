package com.xplus.commons.thread.senior.threadcommicate;

public class ThreadLocalDemo {

  private static ThreadLocal<String> value = new ThreadLocal<>();

  public String getValue() {
    return value.get();
  }

  public void setValue(String val) {
    value.set(val);
  }

  public static void main(String[] args) {
    final ThreadLocalDemo demo = new ThreadLocalDemo();
    Thread t1 = new Thread(new Runnable() {
      @Override
      public void run() {
        demo.setValue("123");
        System.out.println("当前线程" //
            + Thread.currentThread().getName() //
            + "的值：" //
            + demo.getValue());
      }
    });
    Thread t2 = new Thread(new Runnable() {
      @Override
      public void run() {
        System.out.println("当前线程" //
            + Thread.currentThread().getName() //
            + "的值：" //
            + demo.getValue());
      }
    });
    t1.start();
    try {
      Thread.sleep(200);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    t2.start();
  }

}
