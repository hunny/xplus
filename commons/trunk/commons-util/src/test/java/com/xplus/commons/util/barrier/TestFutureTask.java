package com.xplus.commons.util.barrier;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TestFutureTask {

  public static void main(String[] args) throws InterruptedException, ExecutionException {
    final ExecutorService exec = Executors.newFixedThreadPool(5);
    Callable<String> call = new Callable<String>() {
      public String call() throws Exception {
        Thread.sleep(1000 * 3);// 休眠指定的时间，此处表示该操作比较耗时
        return "Other less important but longtime things.";
      }
    };
    Future<String> task = exec.submit(call);
    // Thread.sleep(1000 * 3);
    System.out.println("Let's do important things.");
    // 如果线程内部的事情还未处理完，即还没有返回结果，则会阻塞
    boolean isDone = task.isDone();
    System.out.println("isDone:" + isDone);
//    while (!isDone) {
//      isDone = task.isDone();
//    }
    String obj = task.get();
    isDone = task.isDone();
    System.out.println("isDone:" + isDone);
    System.out.println(obj);
    // 关闭线程池
    exec.shutdown();
  }

}
