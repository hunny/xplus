package com.springboot.spark.starter.basic.rdd;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

/**
 */
public class PairRDDgroupByKey implements Serializable {

  private static final long serialVersionUID = 5924321403879655951L;
  
  private final static SparkConf sparkConf = new SparkConf() //
      .setAppName(PairRDDgroupByKey.class.getName()) //
      .setMaster("local") //
      ; //
  
  public final static JavaSparkContext sc = new JavaSparkContext(sparkConf);

  public void createPairRDD() {
    // Create PairRDD
    JavaRDD<String> rdd = sc.parallelize(Arrays.asList("1", "2", "3", "4", "5", "3", "4"));
    // A function that returns key-value pairs (Tuple2<K, V>), and can be used to construct PairRDDs.
    // 第一个为target，第二个为key，第三个为value。
    PairFunction<String, String, Integer> keyData = new PairFunction<String, String, Integer>() {
      private static final long serialVersionUID = 1L;
      @Override
      public Tuple2<String, Integer> call(String x) throws Exception {
        return new Tuple2<String, Integer>(x, Integer.valueOf(x));
      }
    };
    JavaPairRDD<String, Integer> javaPairRDD = rdd.mapToPair(keyData);
    // Return the key-value pairs in this RDD to the master as a Map.
    Map<String, Integer> map = javaPairRDD.collectAsMap();
    System.err.println("javaPairRDD.mapToPair()=>");
    System.err.println(map);
    
    JavaPairRDD<String, Integer> javaPairRDD2 = javaPairRDD.reduceByKey(new Function2<Integer, Integer, Integer>() {
      private static final long serialVersionUID = -7954451885217698351L;
      @Override
      public Integer call(Integer v1, Integer v2) throws Exception {
        return v1 + v2;
      }
    });
    map = javaPairRDD2.collectAsMap();
    System.err.println("javaPairRDD.reduceByKey()=>");
    System.err.println(map);
  }
  
  public static void main(String[] args) {
    new PairRDDgroupByKey().createPairRDD();
    sc.close();
  }

}
