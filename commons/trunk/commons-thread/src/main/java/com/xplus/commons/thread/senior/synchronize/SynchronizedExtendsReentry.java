package com.xplus.commons.thread.senior.synchronize;

public class SynchronizedExtendsReentry {

  static class Main {
    public int i = 10;
    public synchronized void mainMethod() {
      i--;
      System.out.println("Main print i = " + i);
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
  
  static class Sub extends Main {
    public synchronized void subMethod() {
      try {
        while (i > 0) {
          i--;
          System.out.println("Sub print i = " + i);
          Thread.sleep(100);
          this.mainMethod();
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
  
  public static void main(String[] args) {
    // 子类父类，都加了synchronized关键字，锁重入调用是线程安全的。
    // 子方法中，不使用synchronized关键字，调用父类中有synchronized，效果是一样的。
    Thread t = new Thread(new Runnable() {
      @Override
      public void run() {
        Sub sub = new Sub();
        sub.subMethod();
      }
    });
    t.start();
  }
  
}
