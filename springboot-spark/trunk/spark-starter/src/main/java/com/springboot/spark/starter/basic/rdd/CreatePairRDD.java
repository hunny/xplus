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

/**
 * parallelize
 * PairFunction
 * mapToPair
 * collectAsMap
 */
public class CreatePairRDD implements Serializable {

  private static final long serialVersionUID = 5924321403879655951L;
  
  private final static SparkConf sparkConf = new SparkConf() //
      .setAppName(CreatePairRDD.class.getName()) //
      .setMaster("local") //
      ; //
  
  public final static JavaSparkContext sc = new JavaSparkContext(sparkConf);

  public void createPairRDD() {
    // Create PairRDD
    JavaRDD<String> rdd = sc.parallelize(Arrays.asList("1", "2", "3", "4", "5"));
    // A function that returns key-value pairs (Tuple2<K, V>), and can be used to construct PairRDDs.
    // 第一个为target，第二个为key，第三个为value。
    PairFunction<String, String, String> keyData = new PairFunction<String, String, String>() {
      private static final long serialVersionUID = 1L;
      @Override
      public Tuple2<String, String> call(String x) throws Exception {
        return new Tuple2<String, String>(x.split(" ")[0] + "K", x + "V");
      }
    };
    JavaPairRDD<String, String> javaPairRDD = rdd.mapToPair(keyData);
    // Return the key-value pairs in this RDD to the master as a Map.
    Map<String, String> map = javaPairRDD.collectAsMap();
    System.err.println("javaPairRDD.collectAsMap()=>");
    System.err.println(map);
    // Output result:
    // {3K=3V, 5K=5V, 2K=2V, 4K=4V, 1K=1V}
  }
  
  public static void main(String[] args) {
    new CreatePairRDD().createPairRDD();
    sc.close();
  }

}
