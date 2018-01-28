package com.xplus.commons.thread.senior.threadcommicate.waitnotifycase1;

public class ThreadA implements Runnable {

  private Object mLock;

  public ThreadA(Object lock) {
    mLock = lock;
  }

  public void run() {
    synchronized (mLock) {
      if (MyList.getSize() != 5) {
        try {
          System.out.println("before wait");
          mLock.wait();
          System.out.println("after wait");
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }

  }

}
