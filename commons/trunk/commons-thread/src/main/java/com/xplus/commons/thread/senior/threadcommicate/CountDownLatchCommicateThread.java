package com.xplus.commons.thread.senior.threadcommicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * 演示使用CountDownLatch来进行线程间的通信。
 */
public class CountDownLatchCommicateThread {

  private List<String> list = new ArrayList<>();

  public void add(String value) {
    list.add(value);
  }

  public int getSize() {
    return list.size();
  }

  public static void main(String[] args) {

    final CountDownLatchCommicateThread demo = new CountDownLatchCommicateThread();

    final CountDownLatch latch = new CountDownLatch(1);

    Thread thread1 = new Thread(new Runnable() {
      @Override
      public void run() {
        for (int i = 0; i < 10; i++) {
          String value = String.valueOf(new Random().nextInt(100));
          demo.add(value);
          System.out.println(value);
          try {
            Thread.sleep(500);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          if (demo.getSize() == 5) {
            System.out.println("当前线程" //
                + Thread.currentThread().getName() //
                + "已经发出通知。" //
            );
            latch.countDown();
          }
        }
      }
    }, "t1");

    Thread thread2 = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          System.out.println(Thread.currentThread().getName() + "进入。");
          latch.await();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        System.out.println( //
            "当前线程" //
                + Thread.currentThread().getName() //
                + "收到通知，值为" //
                + demo.getSize() //
                + "，线程停止。" //
        );
        throw new RuntimeException("线程停止。");
      }
    }, "t2");

    thread1.start();
    thread2.start(); //
  }

}
