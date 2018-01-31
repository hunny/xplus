package com.xplus.commons.thread.senior.queue.netbar;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class Customer implements Delayed {

  private String name;
  private long offlineTime;

  public Customer(String name, long offlineTime) {
    this.name = name;
    this.offlineTime = offlineTime;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getOfflineTime() {
    return offlineTime;
  }

  public void setOfflineTime(long offlineTime) {
    this.offlineTime = offlineTime;
  }

  /**
   * 排序使用。
   */
  @Override
  public int compareTo(Delayed o) {//此处的算法很重要。
    long result = this.getDelay(TimeUnit.NANOSECONDS) //
        - o.getDelay(TimeUnit.NANOSECONDS);
    if (result < 0) {
      return -1;
    } else if (result > 0) {
      return 1;
    }
    return 0;
  }

  /**
   * 用来判断是否到了截止时间 。
   */
  @Override
  public long getDelay(TimeUnit unit) {
    long r = unit.convert(offlineTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    return r;
  }

}
