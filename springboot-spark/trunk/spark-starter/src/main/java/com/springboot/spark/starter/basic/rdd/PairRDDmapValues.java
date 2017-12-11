package com.springboot.spark.starter.basic.rdd;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

/**
 */
public class PairRDDmapValues implements Serializable {

  private static final long serialVersionUID = 5924321403879655951L;

  private final static SparkConf sparkConf = new SparkConf() //
      .setAppName(PairRDDmapValues.class.getName()) //
      .setMaster("local[5]") //
  ; //

  public final static JavaSparkContext sc = new JavaSparkContext(sparkConf);

  public void using() {
    // Create PairRDD
    JavaRDD<String> rdd = sc.parallelize(Arrays.asList("1", "2", "3", "4", "5", "30", "42"));
    // A function that returns key-value pairs (Tuple2<K, V>), and can be used
    // to construct PairRDDs.
    // 第一个为target，第二个为key，第三个为value。
    PairFunction<String, String, Integer> keyData = new PairFunction<String, String, Integer>() {
      private static final long serialVersionUID = 1L;

      @Override
      public Tuple2<String, Integer> call(String x) throws Exception {
        return new Tuple2<String, Integer>(x, Integer.valueOf(x) * 10);
      }
    };
    JavaPairRDD<String, Integer> javaPairRDD = rdd.mapToPair(keyData);
    System.err.println(javaPairRDD.collect());
    // [(1,10), (2,20), (3,30), (4,40), (5,50), (30,300), (42,420)]

    // 同基本转换操作中的map，只不过mapValues是针对[K,V]中的V值进行map操作。
    JavaPairRDD<String, String> mapValues = javaPairRDD.mapValues(new Function<Integer, String>() {
      private static final long serialVersionUID = -5028810986485230014L;

      @Override
      public String call(Integer v1) throws Exception {
        return "Value:" + v1;
      }
    });
    System.err.println(mapValues.collect());
    // [(1,Value:10), (2,Value:20), (3,Value:30), (4,Value:40), (5,Value:50),
    // (30,Value:300), (42,Value:420)]
  }

  public static void main(String[] args) {
    new PairRDDmapValues().using();
    sc.close();
  }

}
