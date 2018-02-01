package com.xplus.commons.thread.senior.threadpattern.masterworker;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Worker implements Runnable {

  private ConcurrentLinkedQueue<MyData> dataQueue;
  private ConcurrentHashMap<String, Integer> resultMap;
  
  @Override
  public void run() {

    while (true) {
      MyData mData = this.dataQueue.poll();//从队列中取出第一个元素并从队列中移除。
      if (null == mData) {
        break;
      }
      // 业务逻辑处理方法
      Integer result = handle(mData);
      this.resultMap.put(Integer.toString(mData.getId()), result);
    }
  }

  protected Integer handle(MyData mData) {
    Integer result = null;
    try {
      Thread.sleep(500);// 模拟耗时的任务操作。
      result = mData.getData();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return result;
  }

  public void setDataQueue(ConcurrentLinkedQueue<MyData> dataQueue) {
    this.dataQueue = dataQueue;
  }

  public void setResultMap(ConcurrentHashMap<String, Integer> resultMap) {
    this.resultMap = resultMap;
  }

}
