package com.xplus.commons.thread.senior.threadcommicate.waitnotifycase2;

public class TraditionalThreadCommunication {

  public static void main(String[] args) {

    final Business business = new Business();
    
    new Thread(new Runnable() {
      @Override
      public void run() {
        for (int i = 1; i <= 10; i++) {
          business.sub(i);
        }
      }
    }).start();

    for (int i = 1; i <= 10; i++) {
      business.main(i);
    }
  }

}

class Business {

  private boolean shouldBeSub = true;

  public synchronized void sub(int i) {
    while (!shouldBeSub) {
      try {
        this.wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    for (int j = 1; j <= 10; j++) {
      System.out.println("sub thread sequence of " + j + ",loop of " + i);
    }
    shouldBeSub = false;
    this.notify();
  }

  public synchronized void main(int i) {
    while (shouldBeSub) {
      try {
        this.wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    for (int j = 1; j <= 100; j++) {
      System.out.println("main thread sequence of " + j + ",loop of " + i);
    }
    shouldBeSub = true;
    this.notify();
  }
}