package com.xplus.commons.thread.senior.synchronize;

public class SyncException {

  private int i = 0;

  public synchronized void opt() {
    while (true) {
      try {
        i++;
        Thread.sleep(200);
        System.out.println(Thread.currentThread().getName() + ", i = " + i);
        if (i == 10) {
          Integer.parseInt("a");
        }
      } catch (Exception e) {
        e.printStackTrace();
        System.out.println("log info i = " + i);
        throw new RuntimeException("如果不抛出异常，则会无限循环下去。");
      }
    }
  }

  public static void main(String[] args) {
    final SyncException se = new SyncException();
    Thread t = new Thread(new Runnable() {
      @Override
      public void run() {
        se.opt();
      }
    }, "t1");
    t.start();
  }
}
