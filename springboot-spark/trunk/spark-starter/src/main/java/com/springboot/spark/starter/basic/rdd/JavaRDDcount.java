package com.springboot.spark.starter.basic.rdd;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * 
 */
public class JavaRDDcount implements Serializable {

  private static final long serialVersionUID = 5924321403879655951L;

  private final static SparkConf sparkConf = new SparkConf() //
      .setAppName(JavaRDDcount.class.getName()) //
      .setMaster("local") //
  ; //

  public final static JavaSparkContext sc = new JavaSparkContext(sparkConf);

  public void using() {
    JavaRDD<Integer> rdd1 = sc.parallelize(Arrays.asList(1, 2, 3, 4, 5));
    // count返回RDD中的元素数量。
    long count = rdd1.count();
    System.err.println("元素个数：" + count + ", 数据：" + rdd1.collect());
    // 输出
    // 元素个数：5, 数据：[1, 2, 3, 4, 5]
  }

  public static void main(String[] args) {
    new JavaRDDcount().using();
    sc.close();
  }

}
