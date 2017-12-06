package com.springboot.spark.starter.basic.rdd;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public class CreatePairRDD implements Serializable {

  private static final long serialVersionUID = 5924321403879655951L;
  
  private final static SparkConf sparkConf = new SparkConf() //
      .setAppName("CreatePairRDD") //
      .setMaster("local") //
      ; //
  
  public final static JavaSparkContext sc = new JavaSparkContext(sparkConf);

  public void createPairRDD() {
    JavaRDD<String> rdd = sc.parallelize(Arrays.asList("1", "2", "3", "4", "5"));
    PairFunction<String, String, String> keyData = new PairFunction<String, String, String>() {
      private static final long serialVersionUID = 1L;

      @Override
      public Tuple2<String, String> call(String x) throws Exception {
        return new Tuple2<String, String>(x.split(" ")[0], x);
      }
    };
    JavaPairRDD<String, String> javaPairRDD = rdd.mapToPair(keyData);
    Map<String, String> map = javaPairRDD.collectAsMap();
    System.err.println("javaPairRDD.toDebugString()=>");
    System.err.println(javaPairRDD.toDebugString());
    System.err.println("javaPairRDD.collectAsMap()=>");
    System.err.println(map);
  }
  
  public static void main(String[] args) {
    new CreatePairRDD().createPairRDD();
    sc.close();
  }

}
