package com.xplus.commons.thread.senior.threadpattern.masterworker;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Master {

  // 1. 装载任务的并发集合
  private ConcurrentLinkedQueue<MyData> dataQueue = new ConcurrentLinkedQueue<>();

  // 2. 使用HashMap装载Worker对象。
  private Map<String, Thread> workMap = new HashMap<>();

  // 3. 使用一个并发容器装载处理后的结果集合
  private ConcurrentHashMap<String, Integer> resultMap = new ConcurrentHashMap<>();

  // 4. 通过构造方法，在开始时决定有多少个Worker处理任务。
  public Master(Worker worker, int count) {
    // 每一个Worker都有一个待处理任务dataQueue的引用，方便取数据。
    // 每一个Worker都有一个任务处理结果resultMap的引用，方便回传结果。
    worker.setDataQueue(this.dataQueue);
    worker.setResultMap(this.resultMap);
    for (int i = 0; i < count; i++) {
      workMap.put("worker-" + i, new Thread(worker));
    }
  }

  // 5. 追加待处理的任务数据
  public void submit(MyData myData) {
    this.dataQueue.add(myData);
  }

  // 6. 提交任务数据，启动应用程序让worker进行并行处理
  public void execute() {
    for (Map.Entry<String, Thread> map : this.workMap.entrySet()) {
      map.getValue().start();
    }
  }

  // 7. 是否已经完成。
  public boolean isComplete() {
    for (Map.Entry<String, Thread> map : this.workMap.entrySet()) {
      if (map.getValue().getState() != Thread.State.TERMINATED) {// 只要有线程还没有终止，就没有完成。
        return false;
      }
    }
    return true;
  }

  // 8. 返回结果。
  public Integer getResult() {
    Integer result = 0;
    for (Map.Entry<String, Integer> map : this.resultMap.entrySet()) {
      result += map.getValue();// 汇总，计算所有逻辑。 
    }
    return result;
  }

}
