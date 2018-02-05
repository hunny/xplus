package com.xplus.commons.thread.senior.concurrent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutorServiceTest {

  public static void main(String[] args) {

    System.out.println("Program Start At:" //
        + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    Temp command = new Temp();
    ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);
    ScheduledFuture<?> scheduleTask = //
        schedule.scheduleWithFixedDelay(command, 1, 3, TimeUnit.SECONDS);
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    scheduleTask.cancel(true);
    
    System.out.println("isCancelled:" + scheduleTask.isCancelled());
    System.out.println("isDone:" + scheduleTask.isDone());

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    schedule.shutdown();
  }

}

class Temp implements Runnable {

  @Override
  public void run() {
    System.out.println("run at:" //
        + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
  }

}
