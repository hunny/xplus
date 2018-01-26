package com.xplus.commons.thread.senior.synchronize;

/**
 * 脏读演示。
 */
public class DirtyReadDemo {

  private String username = "admin";
  private String password = "root";
  
  public synchronized void setValue(String username, String password) {
    this.username = username;
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    this.password = password;
    System.out.println("最终结果username:" + username + ",password:" + password);
  }
  
  public void getDirtyValue() {//没有加synchronized关键字，对象级是异步的，无需获得锁。
    System.out.println("脏读获取username:" + username + ",password:" + password);
  }
  
  public synchronized void getCorrectValue() {//加了synchronized关键字，保证对象级是同步的。
    System.out.println("正确获取username:" + username + ",password:" + password);
  }
  
  public static void main(String[] args) throws Exception {
    final DirtyReadDemo demo = new DirtyReadDemo();
    Thread t = new Thread() {
      @Override
      public void run() {
        demo.setValue("foo", "bar");
      }
    };
    t.start();
    Thread.sleep(500);
    demo.getDirtyValue();
    demo.getCorrectValue();
  }
  
}
