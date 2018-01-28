package com.xplus.commons.thread.senior.threadcommicate;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MyQueue {
  
  private List<Object> values = new LinkedList<>();
  private AtomicInteger count = new AtomicInteger(0);
  private int minSize = 0;
  private int maxSize = 0;
  private Object lock = new Object();
  
  public MyQueue(int len) {
    this.maxSize = len;
  }
  
  public void put(Object obj) {
    synchronized(lock) {
      while (count.get() == this.maxSize) {
        try {
          lock.wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      values.add(obj);
      count.incrementAndGet();
      lock.notify();
      System.out.println("添加元素：" + obj);
    }
  }
  
  public Object take() {
    Object obj = null;
    synchronized(lock) {
      while(count.get() == this.minSize) {
        try {
          lock.wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      obj = values.remove(0);
      count.decrementAndGet();
      lock.notify();
      System.out.println("移除元素：" + obj);
    }
    return obj;
  }
  
  public static void main(String[] args) {
    final MyQueue queue = new MyQueue(3);
    queue.put("a");
    queue.put("b");
    queue.put("c");
//    queue.put("d");//此处如果追加，则会锁在主线程上，下面的都会执行不了。
    System.out.println("开始操作Queue");
    Thread t1 = new Thread(new Runnable() {
      @Override
      public void run() {
        queue.put("e");
        queue.put("f");
      }
    }, "t1");
    
    Thread t2 = new Thread(new Runnable() {
      @Override
      public void run() {
        queue.take();
        queue.take();
        queue.take();
        queue.take();
      }
    }, "t2");
    t1.start();
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    t2.start();
    
  }
  
}
