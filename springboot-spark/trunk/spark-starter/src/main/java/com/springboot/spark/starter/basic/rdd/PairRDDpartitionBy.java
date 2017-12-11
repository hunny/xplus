package com.springboot.spark.starter.basic.rdd;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.spark.HashPartitioner;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

/**
 */
public class PairRDDpartitionBy implements Serializable {

  private static final long serialVersionUID = 5924321403879655951L;

  private final static SparkConf sparkConf = new SparkConf() //
      .setAppName(PairRDDpartitionBy.class.getName()) //
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
        return new Tuple2<String, Integer>(x, Integer.valueOf(x));
      }
    };
    JavaPairRDD<String, Integer> javaPairRDD = rdd.mapToPair(keyData);
    System.err.println(javaPairRDD.mapPartitionsWithIndex(
        new Function2<Integer, Iterator<Tuple2<String, Integer>>, Iterator<String>>() {
          private static final long serialVersionUID = -241605037175411549L;

          @Override
          public Iterator<String> call(Integer v1, Iterator<Tuple2<String, Integer>> v2)
              throws Exception {
            List<String> result = new LinkedList<>();
            while (v2.hasNext()) {
              result.add("part_" + v1 + "|value_" + v2.next()._1);
            }
            return result.iterator();
          }
        }, false).collect());
    // [part_0|value_1, part_1|value_2, part_2|value_3, part_2|value_4,
    // part_3|value_5, part_4|value_30, part_4|value_42]
    JavaPairRDD<String, Integer> newPairRDD = javaPairRDD.partitionBy(new HashPartitioner(2));
    System.err.println(newPairRDD.mapPartitionsWithIndex(
        new Function2<Integer, Iterator<Tuple2<String, Integer>>, Iterator<String>>() {
          private static final long serialVersionUID = -241605037175411549L;

          @Override
          public Iterator<String> call(Integer v1, Iterator<Tuple2<String, Integer>> v2)
              throws Exception {
            List<String> result = new LinkedList<>();
            while (v2.hasNext()) {
              result.add("part_" + v1 + "|value_" + v2.next()._1);
            }
            return result.iterator();
          }
        }, false).collect());
    // [part_0|value_2, part_0|value_4, part_0|value_42, part_1|value_1,
    // part_1|value_3, part_1|value_5, part_1|value_30]
  }

  public static void main(String[] args) {
    new PairRDDpartitionBy().using();
    sc.close();
  }

}
