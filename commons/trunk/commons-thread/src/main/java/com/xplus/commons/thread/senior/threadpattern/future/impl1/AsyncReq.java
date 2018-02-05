package com.xplus.commons.thread.senior.threadpattern.future.impl1;

/**
 * 模拟一个异步请求。
 */
public class AsyncReq implements Req {

  private LongTimeReq realData;
  
  private boolean isReady = false;
  
  public synchronized void setRealData(LongTimeReq realData) {
    // 如果已经加载完毕，直接返回。
    if (isReady) {
      return;
    }
    // 如果没有加载完毕，进行真实对象的加载
    this.realData = realData;
    isReady = true;
    // 线程通知
    notify();
  }
  
  @Override
  public synchronized String getData() {
    // 如果没有加载好，程序要一直处于阻塞状态。
    while(!isReady) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    // 加载完成后直接获取数据即可。
    return this.realData.getData();
  }

}
