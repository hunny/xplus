package com.springboot.spark.starter.basic.rdd;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

public class JavaRDDgroupBy implements Serializable {

  private static final long serialVersionUID = 5924321403879655951L;
  
  private final static SparkConf sparkConf = new SparkConf() //
      .setAppName(JavaRDDgroupBy.class.getName()) //
      .setMaster("local") //
      ; //
  
  public final static JavaSparkContext sc = new JavaSparkContext(sparkConf);

  public void usingGroupBy() {
    JavaRDD<String> rdd = sc.parallelize(Arrays.asList("1", "2", "3", "4", "5"));
    JavaPairRDD<Integer, Iterable<String>> pairRDD = rdd.groupBy(new Function<String, Integer>() {
      private static final long serialVersionUID = -4295106396514429535L;
      @Override
      public Integer call(String v1) throws Exception {
        return Integer.valueOf(v1) % 2;
      }
    });
    Map<Integer, Iterable<String>> map = pairRDD.collectAsMap();
    for (Map.Entry<Integer, Iterable<String>> m : map.entrySet()) {
      System.out.println("+Key:" + m.getKey());
      Iterable<String> iterable = m.getValue();
      System.out.println(" -Value:");
      iterable.forEach(action -> {
        System.out.print("  ");
        System.out.print(action);
        System.out.println();
      });
    }
  }
  
  public static void main(String[] args) {
    new JavaRDDgroupBy().usingGroupBy();
    sc.close();
  }

}
