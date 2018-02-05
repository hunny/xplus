package com.xplus.commons.thread.senior.threadpattern.masterworker;

import java.util.Random;

public class MasterWorkerTest {

  public static void main(String[] args) {

    Random random = new Random();
    Master master = new Master(new Worker(), 100);
    for (int i = 0; i < 100; i++) {
      MyData mData = new MyData();
      mData.setId(i);
      mData.setName("name" + i);
      mData.setData(random.nextInt(1000));
      master.submit(mData);
    }

    master.execute();// 提交执行并行运算。

    long start = System.currentTimeMillis();

    while (true) {
      if (master.isComplete()) {
        long end = System.currentTimeMillis() - start;
        Integer result = master.getResult();
        System.out.println("结果：" + result + ", 时长：" + end + "ms");
        break;
      }
    }

  }

}
