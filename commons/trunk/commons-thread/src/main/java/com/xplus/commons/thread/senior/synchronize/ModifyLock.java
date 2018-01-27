package com.xplus.commons.thread.senior.synchronize;

public class ModifyLock {
  
  private String attr;
  private String attr2;

  public String getAttr() {
    return attr;
  }

  public void setAttr(String attr) {
    this.attr = attr;
  }
  
  public String getAttr2() {
    return attr2;
  }

  public void setAttr2(String attr2) {
    this.attr2 = attr2;
  }

  public synchronized void changeValue(String attr, String attr2) {// 修改对象的值，并不修改锁。
    System.out.println("当前对象" + Thread.currentThread().getName() + "开始。");
    this.setAttr(attr);
    this.setAttr2(attr2);
    System.out.println("当前对象" + Thread.currentThread().getName() + "修改内容为：" + this.toString());
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("当前对象" + Thread.currentThread().getName() + "结束。");
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("ModifyLock [attr=");
    builder.append(attr);
    builder.append(", attr2=");
    builder.append(attr2);
    builder.append("]");
    return builder.toString();
  }
  
  public static void main(String[] args) {
    final ModifyLock lock = new ModifyLock();
    Thread t1 = new Thread(new Runnable() {
      @Override
      public void run() {
        lock.changeValue("A", "B");
      }
    }, "t1");
    Thread t2 = new Thread(new Runnable() {
      @Override
      public void run() {
        lock.changeValue("C", "D");
      }
    }, "t2");
    t1.start();
    try {
      Thread.sleep(200);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    t2.start();
  }

}
