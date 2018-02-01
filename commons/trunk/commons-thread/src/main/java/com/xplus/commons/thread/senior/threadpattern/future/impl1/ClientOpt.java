package com.xplus.commons.thread.senior.threadpattern.future.impl1;

public class ClientOpt {

  public Req get(final String str) {
    // 1. 想要一个代理对象（Data接口的实现类）。
    // 先返回给发送请求的客户端，通知请求已经被收到。
    // 可以做其它的事情。
    final AsyncReq futureData = new AsyncReq();
    // 2. 启动一个新的线程，来加载真实的数据，传递给这个代理对象。
    new Thread(new Runnable() {
      @Override
      public void run() {
        // 3. 这个新的线程可以的加载真实对象，然后传递给代理对象。
        LongTimeReq realData = new LongTimeReq(str);
        futureData.setRealData(realData);
      }
    }).start();
    return futureData;
  }

}
