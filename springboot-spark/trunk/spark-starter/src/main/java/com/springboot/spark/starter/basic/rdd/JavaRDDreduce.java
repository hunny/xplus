package com.springboot.spark.starter.basic.rdd;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;

public class JavaRDDreduce implements Serializable {

  private static final long serialVersionUID = 5924321403879655951L;

  private final static SparkConf sparkConf = new SparkConf() //
      .setAppName(JavaRDDreduce.class.getName()) //
      .setMaster("local") //
  ; //

  public final static JavaSparkContext sc = new JavaSparkContext(sparkConf);

  public void usingReduce() {
    JavaRDD<String> rdd = sc.parallelize(Arrays.asList("1", "2", "3", "4", "5"));
    rdd.cache();
    String str = rdd.reduce(new Function2<String, String, String>() {
      private static final long serialVersionUID = 1L;

      @Override
      public String call(String v1, String v2) throws Exception {
        return v1 + "Q" + v2;
      }
    });
    System.err.println(str);
    //输出
    //1Q2Q3Q4Q5
  }

  public static void main(String[] args) {
    new JavaRDDreduce().usingReduce();
    sc.close();
  }

}
