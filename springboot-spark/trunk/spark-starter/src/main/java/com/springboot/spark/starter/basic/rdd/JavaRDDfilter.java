package com.springboot.spark.starter.basic.rdd;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

/**
 * 
 */
public class JavaRDDfilter implements Serializable {

  private static final long serialVersionUID = 5924321403879655951L;

  private final static SparkConf sparkConf = new SparkConf() //
      .setAppName(JavaRDDfilter.class.getName()) //
      .setMaster("local") //
  ; //

  public final static JavaSparkContext sc = new JavaSparkContext(sparkConf);

  public void using() {
    JavaRDD<Integer> rdd1 = sc.parallelize(Arrays.asList(1, 2, 3, 4, 5));
    JavaRDD<Integer> filter = rdd1.filter(new Function<Integer, Boolean>() {
      private static final long serialVersionUID = -3421983147305036874L;

      @Override
      public Boolean call(Integer v1) throws Exception {
        return v1 % 2 == 0;
      }
    });
    System.err.println("大小：" + filter.count() + ", 数据：" + filter.collect());
    // 输出
    // 大小：2, 数据：[2, 4]
  }

  public static void main(String[] args) {
    new JavaRDDfilter().using();
    sc.close();
  }

}
