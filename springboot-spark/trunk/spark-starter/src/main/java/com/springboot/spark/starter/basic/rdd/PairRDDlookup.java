package com.springboot.spark.starter.basic.rdd;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public class PairRDDlookup implements Serializable {

  private static final long serialVersionUID = 5924321403879655951L;

  private final static SparkConf sparkConf = new SparkConf() //
      .setAppName(PairRDDlookup.class.getName()) //
      .setMaster("local") //
  ; //

  public final static JavaSparkContext sc = new JavaSparkContext(sparkConf);

  public void using() {
    JavaRDD<Integer> rdd1 = sc.parallelize(Arrays.asList(1, 2, 3, 4, 5));
    JavaPairRDD<String, Integer> mapToPair = rdd1
        .mapToPair(new PairFunction<Integer, String, Integer>() {
          private static final long serialVersionUID = -6972854698307709791L;

          @Override
          public Tuple2<String, Integer> call(Integer t) throws Exception {
            return new Tuple2<String, Integer>("K" + t, t);
          }
        });
    List<Integer> lookup = mapToPair.lookup("K2");
    System.err.println("lookup size: " + lookup.size() + ", lookup=" + lookup);
    // lookup size: 1, lookup=[2]

  }

  public static void main(String[] args) {
    new PairRDDlookup().using();
    sc.close();
  }

}
