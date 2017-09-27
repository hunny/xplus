package com.xplus.commons.util.threadpool;

import java.util.Random;

public class WorkerThread implements Runnable {

  private String cmd;
  
  public WorkerThread(String cmd) {
    this.cmd = cmd;
  }
  
  @Override
  public void run() {
    System.out.println("WorkerThread:" + cmd);
    try {
      Thread.sleep(new Random().nextInt(10) * 1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("WorkerThread:" + cmd + " finish.");
  }

}
