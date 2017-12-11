package com.springboot.spark.starter.basic.rdd;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * 
 */
public class JavaRDDdistinct implements Serializable {

  private static final long serialVersionUID = 5924321403879655951L;

  private final static SparkConf sparkConf = new SparkConf() //
      .setAppName("JavaRDDdistinct") //
      .setMaster("local") //
  ; //

  public final static JavaSparkContext sc = new JavaSparkContext(sparkConf);

  public void usingDistinct() {
    JavaRDD<Integer> rdd1 = sc.parallelize(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 5, 6, 7, 8, 9, 10));
    // 对RDD中的元素进行去重
    JavaRDD<Integer> rdd2 = rdd1.distinct();
    System.out.println("RDD1大小：" + rdd1.count() + ", 数据：" + rdd1.collect());
    System.out.println("RDD2大小：" + rdd2.count() + ", 数据：" + rdd2.collect());
  }

  public static void main(String[] args) {
    new JavaRDDdistinct().usingDistinct();
    sc.close();
  }

}
