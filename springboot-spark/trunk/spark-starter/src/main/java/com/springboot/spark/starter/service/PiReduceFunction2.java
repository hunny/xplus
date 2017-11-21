package com.springboot.spark.starter.service;

import java.text.MessageFormat;

import org.apache.spark.api.java.function.Function2;

import com.springboot.spark.starter.profile.SparkPiEstimation;

@SparkPiEstimation
public class PiReduceFunction2 implements Function2<Integer, Integer, Integer> {// org.apache.spark.api.java.function.Function2是spark自己的类，含义是前两个Integer是输入类型，最后一个Integer是返回类型

  private static final long serialVersionUID = -4051515329808687031L;

  @Override
  public Integer call(Integer integer, Integer integer2) {// integer是上所有之前两两求和的总和，integer2是下一个map输出的结果
    // logger.info("reduce结果值：'{}', '{}'", integer, integer2);
    System.out.println(MessageFormat.format("reduce结果值：{0}, {1}", integer, integer2));
    /**
     * 打印出两套差不多这个东西 0 1 1 1 2 1 3 0 3 1 4 0 4 1 5 1 ... 可见上面即加起来的过程 然后又打印出 75 76
     * 可见是两个slices所有的0和1加起来，然后两个slices的和再相加
     */
    return integer + integer2;
  }

}
