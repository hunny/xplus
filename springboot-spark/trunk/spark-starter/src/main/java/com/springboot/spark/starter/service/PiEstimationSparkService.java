package com.springboot.spark.starter.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.spark.starter.profile.SparkPiEstimation;

@Service
@SparkPiEstimation
public class PiEstimationSparkService implements Serializable {

  private static final long serialVersionUID = 6826495218096979825L;

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private JavaSparkContext javaSparkContext;

  /**
   * 数学原理，根据随机选择XY为0到1的点落在半径为1的圆内的概率
   * 
   * @param samples
   * @return
   */
  public double estimate(final int samples) {

    List<Integer> l = new ArrayList<>(samples);
    for (int i = 0; i < samples; i++) {
      l.add(i);
    }

    long count = javaSparkContext.parallelize(l).filter(i -> {
      double x = Math.random();
      double y = Math.random();
      return x * x + y * y < 1;
    }).count();
//    javaSparkContext.stop();
    double result = 4.0 * count / samples;

    logger.info("Pi is roughly " + result);
    return result;
  }

  /**
   * Computes an approximation to pi Usage: JavaSparkPi [slices]
   * 数学原理，根据随机选择XY为-1到1的点落在半径为1的圆内的概率
   * http://stackoverflow.com/questions/34892522/the-principle-of-spark-pi
   * 
   * @param slices,
   *          unit 100.
   * @return
   */
  public double mapReduce(final int slices) {

    int n = 100 * slices;// slices * 100个点
    
    List<Integer> l = new ArrayList<Integer>(n);
    for (int i = 0; i < n; i++) {
      l.add(i);
    }

    JavaRDD<Integer> dataSet = javaSparkContext.parallelize(l, slices);

    int count = dataSet.map(new PiMapFunction()).reduce(new PiReduceFunction2());
//    javaSparkContext.stop();

    double result = 4.0 * count / n;
    logger.info("Pi is roughly " + 4.0 * count / n);// 这个是根据圆和正方形的面积

    return result;
  }

}
