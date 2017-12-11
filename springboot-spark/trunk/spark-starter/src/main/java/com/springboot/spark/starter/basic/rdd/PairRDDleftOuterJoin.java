package com.springboot.spark.starter.basic.rdd;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.Optional;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

/**
 */
public class PairRDDleftOuterJoin implements Serializable {

  private static final long serialVersionUID = 5924321403879655951L;

  private final static SparkConf sparkConf = new SparkConf() //
      .setAppName(PairRDDleftOuterJoin.class.getName()) //
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

    // leftOuterJoin类似于SQL中的左外关联left outer
    // join，返回结果以前面的RDD为主，关联不上的记录为空。只能用于两个RDD之间的关联，如果要多个RDD关联，多关联几次即可。
    // 参数numPartitions用于指定结果的分区数
    // 参数partitioner用于指定分区函数
    JavaPairRDD<String, Tuple2<Integer, Optional<Integer>>> leftOuterJoin = javaPairRDD.leftOuterJoin(javaPairRDD2);
     System.err.println(leftOuterJoin.collect());
    // [(4,(-4,Optional[12])), (4,(-4,Optional[12])), (5,(-5,Optional[15])), (2,(-2,Optional.empty)), (3,(-3,Optional[9])), (3,(-3,Optional[9])), (1,(-1,Optional.empty))]
  }

  public static void main(String[] args) {
    new PairRDDleftOuterJoin().using();
    sc.close();
  }

}
