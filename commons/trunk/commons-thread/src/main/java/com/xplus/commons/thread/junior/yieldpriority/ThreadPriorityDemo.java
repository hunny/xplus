package com.xplus.commons.thread.junior.yieldpriority;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPriorityDemo implements Runnable {

  private int countDown = 5;
  private int priority;

  public ThreadPriorityDemo(int priority) {
    this.priority = priority;
  }

  public String toString() {
    return "name:" //
        + Thread.currentThread().getName() //
        + " priority: " //
        + Thread.currentThread().getPriority() //
        + ":prority:" //
        + ": " + countDown;
  }

  public void run() {
    Thread.currentThread().setPriority(priority);
    while (true) {
      for (int i = 1; i < 10000; i++) {
        if (i % 1000 == 0) {
          Thread.yield();
        }
      }
      System.out.println(this);
      if (--countDown == 0)
        return;
    }
  }

  public static void main(String[] args) {
    ExecutorService exec = Executors.newCachedThreadPool();
    for (int i = 0; i < 5; i++) {
      exec.execute(new ThreadPriorityDemo(Thread.MIN_PRIORITY));
      exec.execute(new ThreadPriorityDemo(Thread.MAX_PRIORITY));
    }
    exec.shutdown();
  }
}
