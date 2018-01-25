package com.xplus.commons.thread.senior.volatiledemo;

public class NoVolatileThread extends Thread {

  private boolean running = true;
  
  public void setRunning(boolean running) {
    this.running = running;
    System.out.println("setRunning当前线程名称：" + Thread.currentThread().getName());
  }
  
  public boolean getRunning() {
    System.out.println("getRunning当前线程名称：" + Thread.currentThread().getName());
    return this.running;
  }
  
  @Override
  public void run() {
    System.out.println("进入Run方法。当前线程名称：" + Thread.currentThread().getName());
    while(this.running == true) {
      //Do Nothing.空循环，直到running=false
    }
    System.out.println("线程停止。");
  }
  
  public static void main(String[] args) throws InterruptedException {
    NoVolatileThread thread = new NoVolatileThread();
    thread.start();
    Thread.sleep(2000);
    thread.setRunning(false);// 想从外面控制，停止线程，实际上没有达到逻辑效果。
    System.out.println("running被设置成了false。");
    Thread.sleep(1000);
    System.out.println("线程中的running值：" + thread.getRunning());
  }

}
