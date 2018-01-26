package com.xplus.commons.thread.senior.synchronize;

/**
 * 演示在静态方法上使用synchronized，表示锁定.class类级别的锁。
 */
public class MultiStaticClassLockThread {

  private static int num = 0;
  
  // 演示在静态方法上使用synchronized，表示锁定.class类级别的锁。
  public static synchronized void print(String flag) {// 此处有static静态方法
    try {
      if ("1".equals(flag)) {
        num = 1;
        System.out.println("1 set num.");
        Thread.sleep(1000);
      } else {
        num = 2;
        System.out.println("2 set num.");
      }
      System.out.println("flag:" + flag + ", num:" + num);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @SuppressWarnings("static-access")
  public static void main(String[] args) {
    // 两个不同的对象
    final MultiStaticClassLockThread m1 = new MultiStaticClassLockThread();
    final MultiStaticClassLockThread m2 = new MultiStaticClassLockThread();
    Thread t1 = new Thread(new Runnable() {
      @Override
      public void run() {
        m1.print("1");//等同于使用MultiLockThread.print("1");
      }
    });
    Thread t2 = new Thread(new Runnable() {
      @Override
      public void run() {
        m2.print("2");//等同于使用MultiLockThread.print("2");
      }
    });
    t1.start();
    t2.start();
  }
  
}
