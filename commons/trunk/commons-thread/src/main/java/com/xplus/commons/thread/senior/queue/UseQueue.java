package com.xplus.commons.thread.senior.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class UseQueue {

  public static void main(String[] args) {
    // useConcurrentLinkedQueue();
    // useArrayBlockingQueue();
    useLinkedBlockingQueue();
    useSynchronousQueue();
    usePriorityBlockingQueue();
  }

  public static void useConcurrentLinkedQueue() {
    System.out.println("===============useConcurrentLinkedQueue");
    // 高性能无阻塞无界队列
    ConcurrentLinkedQueue<String> q = new ConcurrentLinkedQueue<>();
    q.offer("a");
    q.add("b");
    q.add("c");
    System.out.println(q.poll());
    System.out.println(q.size());
    System.out.println(q.peek());
    System.out.println(q.size());
  }

  public static void useArrayBlockingQueue() {
    System.out.println("===============useArrayBlockingQueue");
    final ArrayBlockingQueue<String> q = new ArrayBlockingQueue<>(5);
    q.add("a");
    try {
      q.put("b");// wait for space to become available if the queue is full.
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    q.offer("c");
    q.add("d");
    q.add("e");
    try {
      q.add("e");
    } catch (Exception e) {
      System.out.println("可预见的Queue满异常：" + e.getMessage());
    }
    try {
      q.offer("g", 1, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      System.out.println("可预见的Queue超时异常：" + e.getMessage());
    }
    System.out.println("Queue中的元素为：" + q);
    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          System.out.println("等待被拿走");
          Thread.sleep(500);
          System.out.println("已经拿走元素：" + q.take());// 拿走第一个元素
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();
    try {
      // 准备放入元素
      System.err.println("准备放入元素。");
      q.put("g");// wait for space to become available if the queue is full.
      System.err.println("放入元素完成。");
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("变动后元素为Queue中的元素为：" + q);
  }

  public static void useLinkedBlockingQueue() {
    System.out.println("===============useLinkedBlockingQueue");
    LinkedBlockingQueue<String> q = new LinkedBlockingQueue<>(2);
    q.add("1");
    try {
      q.put("2");
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    q.offer("3");
    try {
      q.add("4");
    } catch (Exception e) {
      System.out.println("Queue满的异常：" + e.getMessage());
    }
    System.out.println("Queue大小：" + q.size());
    List<String> list = new ArrayList<>();
    System.out.println("元素个数:" + q.drainTo(list, 4));
    System.out.println("大小：" + list.size());
    for (String str : list) {
      System.out.println("元素：" + str);
    }
  }

  public static void useSynchronousQueue() {
    System.out.println("===============useSynchronousQueue");
    SynchronousQueue<String> q = new SynchronousQueue<>();
    try {
      q.add("12345");
    } catch (Exception e) {
      System.out.println("可预见的异常：" + e.getMessage());
    }
  }

  public static void usePriorityBlockingQueue() {
    System.out.println("===============usePriorityBlockingQueue");

    PriorityBlockingQueue<Task> q = new PriorityBlockingQueue<Task>();
    q.add(new Task(3, "任务3"));
    q.put(new Task(9, "任务9"));
    q.add(new Task(0, "任务0"));
    q.add(new Task(2, "任务7"));
    q.add(new Task(14, "任务14"));
    q.offer(new Task(8, "任务8"));
    Task task = null;
    while ((task = q.poll()) != null) {
      System.out.println(task);
    }
  }

  public static class Task implements Comparable<Task> {
    private int id;
    private String name;

    public Task(int id, String name) {
      super();
      this.id = id;
      this.name = name;
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("Task [id=");
      builder.append(id);
      builder.append(", name=");
      builder.append(name);
      builder.append("]");
      return builder.toString();
    }

    @Override
    public int compareTo(Task task) {
      return this.getId() > task.getId() ? 1 : (this.getId() < task.getId() ? -1 : 0);
    }

  }

}
