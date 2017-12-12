package com.springboot.spark.starter.basic.rdd;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * 
 */
public class JavaRDDfirst implements Serializable {

  private static final long serialVersionUID = 5924321403879655951L;

  private final static SparkConf sparkConf = new SparkConf() //
      .setAppName(JavaRDDfirst.class.getName()) //
      .setMaster("local") //
  ; //

  public final static JavaSparkContext sc = new JavaSparkContext(sparkConf);

  public void using() {
    JavaRDD<Integer> rdd1 = sc.parallelize(Arrays.asList(1, 2, 3, 4, 5));
    rdd1.cache();
    // first返回RDD中的第一个元素，不排序。
    Integer first = rdd1.first();
    System.err.println("大小：" + rdd1.count() + ", 数据：" + rdd1.collect() + ", 第一个元素:" + first);
    // 输出
    // 大小：5, 数据：[1, 2, 3, 4, 5], 第一个元素:1
    List<Integer> top = rdd1.top(3);
    System.err.println("大小：" + top.size() + ", 数据：" + top);
    // 输出
    // 大小：3, 数据：[5, 4, 3]
    top = rdd1.top(3, new TopComparator());
    System.err.println("大小：" + top.size() + ", 数据：" + top);
    // 输出
    // 大小：3, 数据：[1, 2, 3]
    Integer max = rdd1.max(new TopComparator());
    Integer min = rdd1.min(new TopComparator());
    System.err.println("max：" + max + ", min：" + min);
    // 输出
    // max：1, min：5
  }
  
  class TopComparator implements Comparator<Integer>, Serializable {
    private static final long serialVersionUID = -6600503353995561659L;

    @Override
    public int compare(Integer o1, Integer o2) {
      return o2 - o1;
    }
  }

  public static void main(String[] args) {
    new JavaRDDfirst().using();
    sc.close();
  }

}
