package com.xplus.commons.thread.senior.concurrent.util;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class UseFuture implements Callable<String> {

  private String task;

  public UseFuture(String task) {
    this.task = task;
  }

  @Override
  public String call() throws Exception {
    // 模耗时的操作
    Thread.sleep(new Random().nextInt(3000));
    String result = this.task + "处理完成。";
    return result;
  }

  public static void main(String[] args) throws Exception {
    // 创建一个固定线程的线程池
    ExecutorService executor = Executors.newFixedThreadPool(1);
    // 提交任务future
    Future<String> f1 = executor.submit(new UseFuture("我的任务1"));
    long start = System.currentTimeMillis();
    System.out.println("操作结果：" + f1.get());
    System.out.println("耗时：" + (System.currentTimeMillis() - start) + "ms");
    
    // 构造FutureTask，并且传入真正进行业务逻辑处理的类
    // 该类一定要是实现了Callable接口的类。
    FutureTask<String> future = //
        new FutureTask<String>(//
            new UseFuture("我的任务2"));
    executor.submit(future);
    start = System.currentTimeMillis();
    System.out.println("操作结果：" + future.get());
    System.out.println("耗时：" + (System.currentTimeMillis() - start) + "ms");
    
    executor.shutdown();
  }
}
