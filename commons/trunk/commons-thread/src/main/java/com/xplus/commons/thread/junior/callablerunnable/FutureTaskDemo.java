package com.xplus.commons.thread.junior.callablerunnable;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class FutureTaskDemo {
  
  public static void main(String[] args) throws InterruptedException, ExecutionException {
    ExecutorService executor = Executors.newCachedThreadPool();
    Task task = new Task();
    FutureTask<Integer> futureTask = new FutureTask<Integer>(task);
    executor.submit(futureTask);
    executor.shutdown();

    System.out.println("主线程在执行任务");
    System.out.println("task运行结果" + futureTask.get());
    System.out.println("所有任务执行完毕");
  }
}

class Task implements Callable<Integer> {

  @Override
  public Integer call() throws Exception {
    int i = new Random().nextInt(1000);
    Thread.sleep(i);
    return i;
  }

}