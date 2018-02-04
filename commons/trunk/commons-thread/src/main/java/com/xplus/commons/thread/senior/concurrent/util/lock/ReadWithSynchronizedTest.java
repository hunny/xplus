package com.xplus.commons.thread.senior.concurrent.util.lock;

import java.util.Random;

public class ReadWithSynchronizedTest {

  // 输出结果会是，直到thread1执行完读操作之后，才会打印thread2执行读操作的信息。
  public static void main(String[] args) {
    final ReadWithSynchronizedTest test = new ReadWithSynchronizedTest();
    for(int i = 0; i < 5; i++) {
      new Thread() {
        public void run() {
          test.get(Thread.currentThread());
        };
      }.start();
    }
  }

  public synchronized void get(Thread thread) {
    System.out.println(thread.getName() + "正在进行读操作");
    try {
      Thread.sleep(new Random().nextInt(1000));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println(thread.getName() + "读操作完毕");
  }
}
