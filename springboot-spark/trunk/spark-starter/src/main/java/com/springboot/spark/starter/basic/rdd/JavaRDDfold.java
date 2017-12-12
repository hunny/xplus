package com.springboot.spark.starter.basic.rdd;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;

public class JavaRDDfold implements Serializable {

  private static final long serialVersionUID = 5924321403879655951L;

  private final static SparkConf sparkConf = new SparkConf() //
      .setAppName(JavaRDDfold.class.getName()) //
      .setMaster("local") //
  ; //

  public final static JavaSparkContext sc = new JavaSparkContext(sparkConf);

  public void using() {
    JavaRDD<Integer> rdd1 = sc.parallelize(Arrays.asList(1, 2, 3, 4, 5));
    Integer fold = rdd1.fold(0, new Function2<Integer, Integer, Integer>() {
      private static final long serialVersionUID = -3049080982645160523L;

      @Override
      public Integer call(Integer v1, Integer v2) throws Exception {
        System.err.println("v1=" + v1 + ",v2=" + v2);
        return v1 + v2;
      }
    });
    System.err.println(fold);
    // v1=0,v2=1
    // v1=1,v2=2
    // v1=3,v2=3
    // v1=6,v2=4
    // v1=10,v2=5
    //
    // v1=0,v2=15
    //
    // 15
  }

  public static void main(String[] args) {
    new JavaRDDfold().using();
    sc.close();
  }

}
