package com.springboot.spark.starter.basic.rdd;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * 
 */
public class JavaRDDcountByValue implements Serializable {

  private static final long serialVersionUID = 5924321403879655951L;

  private final static SparkConf sparkConf = new SparkConf() //
      .setAppName(JavaRDDcountByValue.class.getName()) //
      .setMaster("local") //
  ; //

  public final static JavaSparkContext sc = new JavaSparkContext(sparkConf);

  public void using() {
    JavaRDD<Integer> rdd1 = sc.parallelize(Arrays.asList(1, 2, 3, 4, 5, 3, 5));
    Map<Integer, Long> countByValue = rdd1.countByValue();
    System.err.println("元素个数：" + countByValue.size() + ", 数据：" + countByValue);
    // 输出
    // 元素个数：5, 数据：{5=2, 1=1, 2=1, 3=2, 4=1}
  }

  public static void main(String[] args) {
    new JavaRDDcountByValue().using();
    sc.close();
  }

}
