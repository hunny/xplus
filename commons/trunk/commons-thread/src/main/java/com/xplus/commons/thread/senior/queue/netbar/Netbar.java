package com.xplus.commons.thread.senior.queue.netbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.DelayQueue;

public class Netbar implements Runnable {

  private DelayQueue<Customer> queue = new DelayQueue<>();

  public volatile boolean open = true;

  public void online(String name, int money) {
    Customer customer = new Customer(name, 1000 * money + System.currentTimeMillis());
    System.out.println("客户:" + name //
        + ", 充值：" + money //
        + "，上线。" //
        + format(new Date(customer.getOfflineTime())));

//    this.queue.add(customer);
//    this.queue.put(customer);
    this.queue.offer(customer);
  }

  public void offline(Customer customer) {
    System.out.println("客户:" + customer.getName() //
        + ", 下线：" //
        + format(new Date()));
  }
  
  private String format(Date date) {
    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") //
        .format(date);
  }

  @Override
  public void run() {
    while (open) {
      try {
//        Customer customer = this.queue.poll(1, TimeUnit.SECONDS);
        Customer customer = this.queue.take();
        if (null == customer) {
          continue;
        }
        offline(customer);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public static void main(String[] args) throws InterruptedException {
    System.out.println("开门营业");
    Netbar netbar = new Netbar();
    Thread t = new Thread(netbar);
    t.start();
    Thread.sleep(500);
//    netbar.online("临时", 0);//为啥第一个必须放一个比较小的，才可以 满足排序的问题呢？
    netbar.online("张三", 5);
    netbar.online("李四", 1);
    netbar.online("小强", 7);
    netbar.online("王五", 3);
  }

}
