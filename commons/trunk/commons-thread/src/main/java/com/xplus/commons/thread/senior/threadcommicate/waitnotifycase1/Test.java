package com.xplus.commons.thread.senior.threadcommicate.waitnotifycase1;

public class Test {

  public static void main(String[] args) throws InterruptedException {

    Object lock = new Object();
    ThreadA ta = new ThreadA(lock);
    Thread tta = new Thread(ta);

    tta.start();
    Thread.sleep(50);

    ThreadB tb = new ThreadB(lock);
    Thread ttb = new Thread(tb);
    ttb.start();

  }
}
