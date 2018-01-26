package com.xplus.commons.thread.senior.synchronize;

/**
 * 演示在对象方法上使用synchronized，表示锁定对象类级别的锁。
 */
public class MultiObjectLockThread {

  private static int num = 0;

  public synchronized void print(String flag) {// 没有使用static
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

  public static void main(String[] args) {
    // 两个不同的对象
    final MultiObjectLockThread m1 = new MultiObjectLockThread();
    final MultiObjectLockThread m2 = new MultiObjectLockThread();
    Thread t1 = new Thread(new Runnable() {
      @Override
      public void run() {
        m1.print("1");
      }
    });
    Thread t2 = new Thread(new Runnable() {
      @Override
      public void run() {
        m2.print("2");
      }
    });
    t1.start();
    t2.start();
  }

}
