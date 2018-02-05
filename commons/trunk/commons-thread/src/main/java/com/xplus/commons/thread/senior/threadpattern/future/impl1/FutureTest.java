package com.xplus.commons.thread.senior.threadpattern.future.impl1;

public class FutureTest {

  public static void main(String[] args) {
    ClientOpt opt = new ClientOpt();
    Req req = opt.get("请求查询");
    System.out.println("请求已经发送成功，等待处理结果。");
    System.out.println("主线程处理其它事务。");
    String result = req.getData();
    System.out.println("异步处理结果：" + result);
  }

}
