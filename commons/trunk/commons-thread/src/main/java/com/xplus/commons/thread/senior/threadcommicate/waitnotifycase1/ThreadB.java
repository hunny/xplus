package com.xplus.commons.thread.senior.threadcommicate.waitnotifycase1;

class ThreadB implements Runnable {

  private Object mLock;

  public ThreadB(Object lock) {
    mLock = lock;
  }

  public void run() {
    synchronized (mLock) {
      for (int i = 0; i < 10; i++) {
        MyList.add();
        if (MyList.getSize() == 5) {
          mLock.notify();
          System.out.println("已发出notify通知");
        }
        System.out.println("增加" + (i + 1) + "条数据");
      }
    }
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("同步方法之外的方法");
  }

}
