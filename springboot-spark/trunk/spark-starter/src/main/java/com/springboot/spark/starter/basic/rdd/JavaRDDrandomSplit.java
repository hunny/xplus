package com.springboot.spark.starter.basic.rdd;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * 
 */
public class JavaRDDrandomSplit implements Serializable {

  private static final long serialVersionUID = 5924321403879655951L;

  private final static SparkConf sparkConf = new SparkConf() //
      .setAppName(JavaRDDrandomSplit.class.getName()) //
      .setMaster("local") //
  ; //

  public final static JavaSparkContext sc = new JavaSparkContext(sparkConf);

  public void using() {
    JavaRDD<Integer> rdd = sc.parallelize(Arrays.asList(1, 2, 3, 4, 5, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16));
    // 根据weight权重值将一个RDD划分成多个RDD,权重越高划分得到的元素较多的几率就越大
    JavaRDD<Integer>[] rdds = rdd.randomSplit(new double[]{
        1.0, 2.0, 7.0
    });
    for (JavaRDD<Integer> arr : rdds) {
      System.out.println("个数:" + arr.count() + "|值:" + arr.collect());
    }
    //输出
    //个数:3|值:[1, 3, 12]
    //个数:4|值:[2, 13, 14, 15]
    //个数:8|值:[4, 5, 7, 8, 9, 10, 11, 16]
  }

  public static void main(String[] args) {
    new JavaRDDrandomSplit().using();
    sc.close();
  }

}
