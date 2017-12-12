package com.springboot.spark.starter.basic.rdd;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

/**
 * 
 */
public class JavaRDDkeyBy implements Serializable {

  private static final long serialVersionUID = 5924321403879655951L;

  private final static SparkConf sparkConf = new SparkConf() //
      .setAppName(JavaRDDkeyBy.class.getName()) //
      .setMaster("local") //
  ; //

  public final static JavaSparkContext sc = new JavaSparkContext(sparkConf);

  public void using() {
    JavaRDD<Integer> rdd1 = sc.parallelize(Arrays.asList(1, 2, 3, 4, 5));
    JavaPairRDD<String, Integer> keyBy = rdd1.keyBy(new Function<Integer, String>() {
      private static final long serialVersionUID = -1679894692341090777L;

      @Override
      public String call(Integer v1) throws Exception {
        return v1 + "@" + v1 * 2;
      }
    });
    System.err.println("大小：" + keyBy.count() + ", 数据：" + keyBy.collect());
    // 输出
    // 大小：5, 数据：[(1@2,1), (2@4,2), (3@6,3), (4@8,4), (5@10,5)]
  }

  public static void main(String[] args) {
    new JavaRDDkeyBy().using();
    sc.close();
  }

}
