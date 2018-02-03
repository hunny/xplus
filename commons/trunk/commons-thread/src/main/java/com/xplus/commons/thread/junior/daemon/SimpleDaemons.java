package com.xplus.commons.thread.junior.daemon;

import java.util.concurrent.TimeUnit;

public class SimpleDaemons implements Runnable {

  public void run() {
    try {
      while (true) {
        TimeUnit.MILLISECONDS.sleep(10);
        System.out.println(Thread.currentThread() + " " + this);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) throws InterruptedException {
    for (int i = 0; i < 10; i++) {
      Thread daemon = new Thread(new SimpleDaemons());
      daemon.setDaemon(true);
      daemon.start();
    }
    System.out.println("所有后台线程启动。");
    // 一旦执行完成后程序就退出,后台线程也就随之结束.
    // 而休眠的情况下,后台线程不会立即结束,后台线程得以创建并运行. 
    TimeUnit.MILLISECONDS.sleep(175);
    //休眠时间到，
    // 后台线程会随主非后台线程（此处是主线程）的结束而结束。
    // 非后台线程不会随此处的主线程的休眠时间结束而结束。
  }

}
