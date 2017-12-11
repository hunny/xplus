package com.springboot.spark.starter.basic.rdd;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

/**
 */
public class PairRDDcombineByKey implements Serializable {

  private static final long serialVersionUID = 5924321403879655951L;

  private final static SparkConf sparkConf = new SparkConf() //
      .setAppName(PairRDDcombineByKey.class.getName()) //
      .setMaster("local[1]") //
  ; //

  public final static JavaSparkContext sc = new JavaSparkContext(sparkConf);

  public void using() {
    // Create PairRDD
    JavaRDD<String> rdd = sc.parallelize(Arrays.asList("A", "B", "C", "D", "E"));
    // A function that returns key-value pairs (Tuple2<K, V>), and can be used
    // to construct PairRDDs.
    // 第一个为target，第二个为key，第三个为value。
    PairFunction<String, String, Integer> keyData = new PairFunction<String, String, Integer>() {
      private static final long serialVersionUID = 3054288968619423363L;

      @Override
      public Tuple2<String, Integer> call(String x) throws Exception {
        return new Tuple2<String, Integer>(x, (int) x.charAt(0));
      }
    };
    JavaPairRDD<String, Integer> javaPairRDD = rdd.mapToPair(keyData);
    System.err.println(javaPairRDD.collect());
    //[(A,65), (B,66), (C,67), (D,68), (E,69)]

    // 组合器函数，用于将V类型转换成C类型，输入参数为RDD[K,V]中的V,输出为C
    Function<Integer, String> createCombiner = new Function<Integer, String>() {
      private static final long serialVersionUID = 9121705234747026796L;

      @Override
      public String call(Integer v1) throws Exception {
        System.err.println(MessageFormat.format("createCombiner->{0}", v1));
        return v1 + "_";
      }
    };

    // 合并值函数，将一个C类型和一个V类型值合并成一个C类型，输入参数为(C,V)，输出为C
    Function2<String, Integer, String> mergeValue = new Function2<String, Integer, String>() {
      private static final long serialVersionUID = -6586125978841453993L;

      @Override
      public String call(String v1, Integer v2) throws Exception {//未执行,原因暂时未知
        System.err.println(MessageFormat.format("mergeValue->Key:{0}|Value:{1}", v1, v2));
        return v1 + "@" + v2;
      }
    };

    // 合并组合器函数，用于将两个C类型值合并成一个C类型，输入参数为(C,C)，输出为C
    Function2<String, String, String> mergeCombiners = new Function2<String, String, String>() {
      private static final long serialVersionUID = -5657795874677612498L;

      @Override
      public String call(String v1, String v2) throws Exception {//未执行,原因暂时未知
        System.err.println(MessageFormat.format("mergeCombiners->Key:{0}|Key:{1}", v1, v2));
        return v1 + "+" + v2;
      }

    };
    
    // 该函数用于将RDD[K,V]转换成RDD[K,C],这里的V类型和C类型可以相同也可以不同。
    JavaPairRDD<String, String> combineByKey = javaPairRDD.combineByKey(createCombiner, mergeValue,
        mergeCombiners);
    System.err.println(combineByKey.collect());
    // [(B,createCombiner->66), (E,createCombiner->69), (C,createCombiner->67), (A,createCombiner->65), (D,createCombiner->68)]
  }

  public static void main(String[] args) {
    new PairRDDcombineByKey().using();
    sc.close();
  }

}
