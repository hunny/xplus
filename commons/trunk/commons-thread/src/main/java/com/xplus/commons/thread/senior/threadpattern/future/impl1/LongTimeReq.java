package com.xplus.commons.thread.senior.threadpattern.future.impl1;

public class LongTimeReq implements Req {

  private String result;

  public LongTimeReq(String str) {
    System.out.println("根据" //
        + str //
        + "进行操作，这是一个比较耗时的操作。");
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("操作完毕，获取结果。");
    result = "查询结果。";
  }

  @Override
  public String getData() {
    return result;
  }

}
