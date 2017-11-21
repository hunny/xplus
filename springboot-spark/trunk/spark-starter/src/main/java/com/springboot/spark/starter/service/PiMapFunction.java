package com.springboot.spark.starter.service;

import java.text.MessageFormat;

import org.apache.spark.api.java.function.Function;

import com.springboot.spark.starter.profile.SparkPiEstimation;

@SparkPiEstimation
public class PiMapFunction implements Function<Integer, Integer> { // org.apache.spark.api.java.function.Function是spark自己的类，含义是前一个Integer是输入类型，后一个Integer是返回类型

  private static final long serialVersionUID = -1480329650370378887L;

  @Override
  public Integer call(Integer integer) {// 每个dataSet的元素调用一次，一共被调用了slices *
                                        // 100次
    double x = Math.random() * 2 - 1;
    double y = Math.random() * 2 - 1;
    // logger.info("map随机样例：'{}', '{}'", x, y);
    System.out.println(MessageFormat.format("map随机样例：{0}, {1}", x, y));
    return (x * x + y * y < 1) ? 1 : 0;// 每次流向reduce层的确实只有1个数
  }

}
