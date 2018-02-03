package com.xplus.commons.thread.senior.concurrent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;

/**
 * 若有新的任务需要执行，如果线程池实际线程数小于corePoolSize，则优先创建线程，若大于corePoolSize，则会将任务加入队列，若队列已满，则在总线程不大于maximumPoolSize的前提下，创建新的线程，若线程数大于maximumPoolSize，则执行拒绝策略。或其它自定义方式。
 * 
 */
public class ThreadPoolExecutorFixedTest {

  // 有界队列测试
  public static void main(String[] args) {
    ThreadPoolExecutor pool = new ThreadPoolExecutor(//
        1, // corePoolSize，即使它们处于空闲状态，也要保留在线程池中的线程数，除非设置了allowCoreThreadTimeOut。
        2, // maxSize，池中允许的最大线程数
        60, // 当线程数大于核心线程数时，这是多余空闲线程在终止之前等待新任务的最大时间。
        TimeUnit.SECONDS, //
        new ArrayBlockingQueue<Runnable>(3) // 指定有界队列
//        , new AbortPolicy() // 默认该策略，直接抛出异常阻止系统正常工作。
        , new CallerRunsPolicy() // 只要线程池未关闭，该策略直接在调用者线程中，运行当前被丢弃的任务。
//        , new DiscardPolicy() // 丢弃无法处理的任务，不给予任何处理。
//        , new DiscardOldestPolicy() // 使用该策略，则会丢弃最老的一个请求。
    );
    MyTask t1 = new MyTask("1", "任务1");
    MyTask t2 = new MyTask("2", "任务2");
    MyTask t3 = new MyTask("3", "任务3");
    MyTask t4 = new MyTask("4", "任务4");
    MyTask t5 = new MyTask("5", "任务5");
    MyTask t6 = new MyTask("6", "任务6");// 取决于拒绝策略，该任务是否会被执行。
    MyTask t7 = new MyTask("7", "任务7");// 取决于拒绝策略，该任务是否会被执行。

    pool.execute(t1);
    pool.execute(t2);
    pool.execute(t3);
    pool.execute(t4);
    pool.execute(t5);
    pool.execute(t6);
    pool.execute(t7);

    pool.shutdown();
  }

}

class MyTask implements Runnable {

  private String id;
  private String name;

  public MyTask(String id, String name) {
    this.id = id;
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public void run() {
    System.out.println("当前线程：" //
        + Thread.currentThread().getName() //
        + "值：" + this);
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("MyTask [id=");
    builder.append(id);
    builder.append(", name=");
    builder.append(name);
    builder.append("]");
    return builder.toString();
  }

}
