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
public class PairRDDsubtractByKey implements Serializable {

  private static final long serialVersionUID = 5924321403879655951L;

  private final static SparkConf sparkConf = new SparkConf() //
      .setAppName(PairRDDsubtractByKey.class.getName()) //
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
      private static final long serialVersionUID = 4172878058463748739L;

      @Override
      public Tuple2<String, Integer> call(String x) throws Exception {
        return new Tuple2<String, Integer>(x, Integer.valueOf(x) * -1);
      }
    };
    JavaPairRDD<String, Integer> javaPairRDD = rdd.mapToPair(keyData);
    JavaPairRDD<String, Integer> javaPairRDD2 = sc.parallelize(Arrays.asList("3", "4", "5"))
        .mapToPair(new PairFunction<String, String, Integer>() {
          private static final long serialVersionUID = 8598252966534641094L;

          @Override
          public Tuple2<String, Integer> call(String x) throws Exception {
            return new Tuple2<String, Integer>(x, Integer.valueOf(x) * 3);
          }
        });
    // Return the key-value pairs in this RDD to the master as a Map.
    Map<String, Integer> map = javaPairRDD.collectAsMap();
    System.err.println("javaPairRDD.mapToPair()=>");
    System.err.println(map);

    // subtractByKey和基本转换操作中的subtract类似
    // 只不过这里是针对K的，返回在主RDD中出现，并且不在otherRDD中出现的元素。
    JavaPairRDD<String, Integer> subtractByKey = javaPairRDD
        .subtractByKey(javaPairRDD2);
    System.err.println(subtractByKey.collect());
    // [(1,-1), (2,-2)]
  }

  public static void main(String[] args) {
    new PairRDDsubtractByKey().using();
    sc.close();
  }

}
