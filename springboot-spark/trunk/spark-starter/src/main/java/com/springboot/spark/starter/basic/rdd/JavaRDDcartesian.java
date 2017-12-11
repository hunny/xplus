package com.springboot.spark.starter.basic.rdd;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * 对两个RDD中的所有元素进行笛卡尔积操作
 */
public class JavaRDDcartesian implements Serializable {

  private static final long serialVersionUID = 5924321403879655951L;

  private final static SparkConf sparkConf = new SparkConf() //
      .setAppName("JavaRDDcartesian") //
      .setMaster("local") //
  ; //

  public final static JavaSparkContext sc = new JavaSparkContext(sparkConf);

  public void usingCartesian() {
    JavaRDD<Integer> rdd1 = sc.parallelize(Arrays.asList(1, 2, 3, 4, 5));
    JavaRDD<Integer> rdd2 = sc.parallelize(Arrays.asList(5, 6, 7, 8, 9, 10));
    // 对两个RDD中的所有元素进行笛卡尔积操作
    JavaPairRDD<Integer, Integer> rdd3 = rdd1.cartesian(rdd2);
    System.out.println("大小：" + rdd3.count() + ", 数据：" + rdd3.collect());
  }

  public static void main(String[] args) {
    new JavaRDDcartesian().usingCartesian();
    sc.close();
  }

}
