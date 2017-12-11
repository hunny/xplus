package com.springboot.spark.starter.basic.rdd;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * 
 */
public class JavaRDDsubtract implements Serializable {

  private static final long serialVersionUID = 5924321403879655951L;

  private final static SparkConf sparkConf = new SparkConf() //
      .setAppName(JavaRDDsubtract.class.getName()) //
      .setMaster("local") //
  ; //

  public final static JavaSparkContext sc = new JavaSparkContext(sparkConf);

  public void usingUnion() {
    JavaRDD<Integer> rdd1 = sc.parallelize(Arrays.asList(1, 2, 3, 4, 5));
    JavaRDD<Integer> rdd2 = sc.parallelize(Arrays.asList(4, 5, 6, 7, 8, 9, 10));
    // 集合rdd1减掉集合rdd2中的元素。
    JavaRDD<Integer> rdd3 = rdd1.subtract(rdd2);
    System.out.println("大小：" + rdd3.count() + ", 数据：" + rdd3.collect());
  }

  public static void main(String[] args) {
    new JavaRDDsubtract().usingUnion();
    sc.close();
  }

}
