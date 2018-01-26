package com.xplus.commons.thread.senior.lock.case1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 演示volatile关键字的使用。
 */
public class DemoVolatileThread {

  // list使用volatile修饰，使多个线程实时都可见。
  private volatile List<String> list = new ArrayList<>();

  public void add(String value) {
    list.add(value);
  }

  public int getSize() {
    return list.size();
  }

  public static void main(String[] args) {
    final DemoVolatileThread demo = new DemoVolatileThread();
    Thread thread1 = new Thread() {
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
        }
      }
    };
    Thread thread2 = new Thread() {
      @Override
      public void run() {
        while (true) {
          if (demo.getSize() == 5) {
            System.out.println("Size=" + demo.getSize() //
                + ", " + Thread.currentThread().getName());
            throw new RuntimeException("结束线程。");
          }
        }
      }
    };
    thread1.start();
    thread2.start();
  }

}
