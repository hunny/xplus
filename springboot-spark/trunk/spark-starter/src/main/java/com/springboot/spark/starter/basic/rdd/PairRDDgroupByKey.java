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
 */
public class PairRDDgroupByKey implements Serializable {

  private static final long serialVersionUID = 5924321403879655951L;

  private final static SparkConf sparkConf = new SparkConf() //
      .setAppName(PairRDDgroupByKey.class.getName()) //
      .setMaster("local") //
  ; //

  public final static JavaSparkContext sc = new JavaSparkContext(sparkConf);

  public void using() {
    // Create PairRDD
    JavaRDD<String> rdd = sc.parallelize(Arrays.asList("1", "2", "3", "4", "5", "3", "4"));
    // A function that returns key-value pairs (Tuple2<K, V>), and can be used
    // to construct PairRDDs.
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

    // 该函数用于将RDD[K,V]中每个K对应的V值，合并到一个集合Iterable[V]中
    JavaPairRDD<String, Iterable<Integer>> groupByKey = javaPairRDD.groupByKey();
    Map<String, Iterable<Integer>> collectAsMap = groupByKey.collectAsMap();
    System.err.println("groupByKey.groupByKey()=>");
    for (Map.Entry<String, Iterable<Integer>> m : collectAsMap.entrySet()) {
      System.err.println("+  " + m.getKey());
      Iterable<Integer> value = m.getValue();
      for (Integer v : value) {
        System.err.println(" - " + v);
      }
    }
    // groupByKey.groupByKey()=>
    // + 2
    // - 2
    // + 5
    // - 5
    // + 1
    // - 1
    // + 4
    // - 4
    // - 4
    // + 3
    // - 3
    // - 3
  }

  public static void main(String[] args) {
    new PairRDDgroupByKey().using();
    sc.close();
  }

}
