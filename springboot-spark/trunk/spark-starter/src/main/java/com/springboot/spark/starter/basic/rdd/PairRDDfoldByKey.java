package com.springboot.spark.starter.basic.rdd;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

/**
 */
public class PairRDDfoldByKey implements Serializable {

  private static final long serialVersionUID = 5924321403879655951L;

  private final static SparkConf sparkConf = new SparkConf() //
      .setAppName(PairRDDfoldByKey.class.getName()) //
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
    // [(A,65), (B,66), (C,67), (D,68), (E,69)]

    // 该函数用于RDD[K,V]根据K将V做折叠、合并处理，其中的参数zeroValue表示先根据映射函数将zeroValue应用于V,进行初始化V,再将映射函数应用于初始化后的V。
    JavaPairRDD<String, Integer> foldByKey = javaPairRDD.foldByKey(0,
        new Function2<Integer, Integer, Integer>() {
          private static final long serialVersionUID = 501814650371495610L;

          // 将rdd1中每个key对应的V进行累加，注意zeroValue=0,需要先初始化V,映射函数为+操
          // 作，比如("A",0), ("A",2)，先将zeroValue应用于每个V,得到：("A",0+0), ("A",2+0)，即：
          // ("A",0), ("A",2)，再将映射函数应用于初始化后的V，最后得到(A,0+2),即(A,2)
          @Override
          public Integer call(Integer v1, Integer v2) throws Exception {
            System.err.println(MessageFormat.format("foldByKey->Key:{0}|Value:{1}", v1, v2));
            return v1 + v2;
          }
        });
    System.err.println(foldByKey.collect());
    // [(B,66), (A,65), (C,67), (E,69), (D,68)]

    // 以zeroValue为单位，依次进行折叠。
    Tuple2<String, Integer> fold = javaPairRDD.fold(new Tuple2<String, Integer>("A", 16),
        new Function2<Tuple2<String, Integer>, Tuple2<String, Integer>, Tuple2<String, Integer>>() {
          private static final long serialVersionUID = -6437742745628945630L;

          @Override
          public Tuple2<String, Integer> call(Tuple2<String, Integer> v1,
              Tuple2<String, Integer> v2) throws Exception {
            System.err.println(MessageFormat.format("fold Tuple2->v1:{0}|v2:{1}", v1, v2));
            return new Tuple2<String, Integer>(v1._1() + v2._1(), v1._2() + v2._2());
          }
        });
    System.err.println(fold);
    //(AAABCDE,367)
  }

  public static void main(String[] args) {
    new PairRDDfoldByKey().using();
    sc.close();
  }

}
