package com.xplus.commons.thread.junior.callablerunnable;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CallableDemo {
  public static void main(String[] args) throws InterruptedException, ExecutionException {
    ExecutorService exec = Executors.newCachedThreadPool();
    ArrayList<Future<String>> results = new ArrayList<Future<String>>();
    for (int i = 0; i < 10; i++) {
      results.add(exec.submit(new TaskCallable(i)));
    }
    for (Future<String> future : results) {
      if (future.isDone()) {
        System.out.println(future.get());
      } else {
        System.out.println("Future result is not yet complete");
      }
    }
    for (Future<String> future : results) {
      System.out.println(future.get());
    }
    exec.shutdown();
  }
}

class TaskCallable implements Callable<String> {

  private int id;

  public TaskCallable(int id) {
    this.id = id;
  }

  @Override
  public String call() throws Exception {
    Thread.sleep(new Random().nextInt(1000));
    return "Result of TaskCallable " + id;
  }

}
