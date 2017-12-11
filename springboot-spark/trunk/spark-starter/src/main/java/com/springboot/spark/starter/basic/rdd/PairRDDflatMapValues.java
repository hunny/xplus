package com.springboot.spark.starter.basic.rdd;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

/**
 */
public class PairRDDflatMapValues implements Serializable {

  private static final long serialVersionUID = 5924321403879655951L;

  private final static SparkConf sparkConf = new SparkConf() //
      .setAppName(PairRDDflatMapValues.class.getName()) //
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

    //同基本转换操作中的flatMap，只不过flatMapValues是针对[K,V]中的V值进行flatMap操作。
    JavaPairRDD<String, String> flatMapValues = javaPairRDD
        .flatMapValues(new Function<Integer, Iterable<String>>() {
          private static final long serialVersionUID = 968359175366832488L;

          @Override
          public Iterable<String> call(Integer v1) throws Exception {
            List<String> result = new LinkedList<>();
            int random = new Random().nextInt(5);
            result.add("Value:" + v1);
            result.add("Random:" + random);
            for (int i = 0; i < random; i++) {
              result.add("Random Value:" + i);
            }
            return result;
          }
        });
    System.err.println(flatMapValues.collect());
    // [(1,Value:10), (1,Random:2), (1,Random Value:0), (1,Random Value:1),
    // (2,Value:20), (2,Random:3), (2,Random Value:0), (2,Random Value:1),
    // (2,Random Value:2), (3,Value:30), (3,Random:2), (3,Random Value:0),
    // (3,Random Value:1), (4,Value:40), (4,Random:4), (4,Random Value:0),
    // (4,Random Value:1), (4,Random Value:2), (4,Random Value:3), (5,Value:50),
    // (5,Random:1), (5,Random Value:0), (30,Value:300), (30,Random:2),
    // (30,Random Value:0), (30,Random Value:1), (42,Value:420), (42,Random:4),
    // (42,Random Value:0), (42,Random Value:1), (42,Random Value:2), (42,Random
    // Value:3)]
  }

  public static void main(String[] args) {
    new PairRDDflatMapValues().using();
    sc.close();
  }

}
