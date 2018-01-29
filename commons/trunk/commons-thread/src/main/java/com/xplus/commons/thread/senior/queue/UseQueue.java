package com.xplus.commons.thread.senior.queue;

import java.util.concurrent.ConcurrentLinkedQueue;

public class UseQueue {

  public static void main(String[] args) {
    useLinkedQueue();
  }
  
  public static void useLinkedQueue() {
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

}
