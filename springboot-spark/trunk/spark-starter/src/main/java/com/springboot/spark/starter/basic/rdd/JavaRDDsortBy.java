package com.springboot.spark.starter.basic.rdd;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

/**
 * 
 */
public class JavaRDDsortBy implements Serializable {

  private static final long serialVersionUID = 5924321403879655951L;

  private final static SparkConf sparkConf = new SparkConf() //
      .setAppName(JavaRDDsortBy.class.getName()) //
      .setMaster("local") //
  ; //

  public final static JavaSparkContext sc = new JavaSparkContext(sparkConf);

  public void using() {
    JavaRDD<Integer> rdd1 = sc.parallelize(Arrays.asList(1, 2, 3, 4, 5));
    JavaRDD<Integer> sortBy = rdd1.sortBy(new Function<Integer, Double>() {
      private static final long serialVersionUID = -6083884077578548358L;

      @Override
      public Double call(Integer v1) throws Exception {
        Double sort = new Random().nextDouble();
        System.err.println("值：" + v1 + ", 权重：" + sort);
        return sort;
      }
    }, true, 3);
    // 值：1, 权重：0.004154557798361047
    // 值：2, 权重：0.11009478999534428
    // 值：3, 权重：0.6175719538681138
    // 值：4, 权重：0.8807099123933061
    // 值：5, 权重：0.6067648078017458
    System.err.println("大小：" + sortBy.count() + ", 数据：" + sortBy.collect());
    // 输出
    // 大小：5, 数据：[1, 2, 5, 3, 4]
  }

  public static void main(String[] args) {
    new JavaRDDsortBy().using();
    sc.close();
  }

}
