package com.springboot.spark.starter.basic.rdd;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * 
 */
public class JavaRDDglom implements Serializable {

  private static final long serialVersionUID = 5924321403879655951L;

  private final static SparkConf sparkConf = new SparkConf() //
      .setAppName(JavaRDDglom.class.getName()) //
      .setMaster("local") //
  ; //

  public final static JavaSparkContext sc = new JavaSparkContext(sparkConf);

  public void usingGlom() {
    JavaRDD<Integer> rdd1 = sc.parallelize(Arrays.asList(1, 2, 3, 4, 5));
    // 将RDD的每个分区中的类型为T的元素转换换数组Array[T]
    JavaRDD<List<Integer>> rdd2 = rdd1.glom();
    System.out.println("大小：" + rdd2.count() + ", 数据：" + rdd2.collect());
    //输出
    //大小：1, 数据：[[1, 2, 3, 4, 5]]
  }

  public static void main(String[] args) {
    new JavaRDDglom().usingGlom();
    sc.close();
  }

}
