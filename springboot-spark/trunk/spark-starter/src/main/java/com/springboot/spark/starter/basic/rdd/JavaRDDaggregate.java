package com.springboot.spark.starter.basic.rdd;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;

/**
 * 
 */
public class JavaRDDaggregate implements Serializable {

  private static final long serialVersionUID = 5924321403879655951L;

  private final static SparkConf sparkConf = new SparkConf() //
      .setAppName(JavaRDDaggregate.class.getName()) //
      .setMaster("local") //
  ; //

  public final static JavaSparkContext sc = new JavaSparkContext(sparkConf);

  public void using() {
    JavaRDD<Integer> rdd1 = sc.parallelize(Arrays.asList(1, 2, 3, 4, 5));
    // aggregate用户聚合RDD中的元素，先使用seqOp将RDD中每个分区中的T类型元素聚合成U类型，
    // 再使用combOp将之前每个分区聚合后的U类型聚合成U类型，特别注意seqOp和combOp都会使用zeroValue的值，zeroValue的类型为U。
    Function2<String, Integer, String> seqOp = new Function2<String, Integer, String>() {
      private static final long serialVersionUID = 2225818833729847812L;

      @Override
      public String call(String v1, Integer v2) throws Exception {
        System.err.println("#seqOp_v1=" + v1 + "_v2=" + v2 + "#");
        return "#seqOp_v1=" + v1 + "_v2=" + v2 + "#";
      }
    };

    Function2<String, String, String> combOp = new Function2<String, String, String>() {
      private static final long serialVersionUID = 1860025412839692779L;

      @Override
      public String call(String v1, String v2) throws Exception {
        System.err.println("|combOp_v1=" + v1 + "_v2=" + v2 + "|");
        return "|combOp_v1=" + v1 + "_v2=" + v2 + "|";
      }
    };
    System.err.println("Aggregate get started");
    String aggregate = rdd1.aggregate("ZERO", seqOp, combOp);
    System.err.println("The result is:");
    System.err.println(aggregate);
    // Aggregate get started
    // #seqOp_v1=ZERO_v2=1#
    // #seqOp_v1=#seqOp_v1=ZERO_v2=1#_v2=2#
    // #seqOp_v1=#seqOp_v1=#seqOp_v1=ZERO_v2=1#_v2=2#_v2=3#
    // #seqOp_v1=#seqOp_v1=#seqOp_v1=#seqOp_v1=ZERO_v2=1#_v2=2#_v2=3#_v2=4#
    // #seqOp_v1=#seqOp_v1=#seqOp_v1=#seqOp_v1=#seqOp_v1=ZERO_v2=1#_v2=2#_v2=3#_v2=4#_v2=5#
    //
    // |combOp_v1=ZERO_v2=#seqOp_v1=#seqOp_v1=#seqOp_v1=#seqOp_v1=#seqOp_v1=ZERO_v2=1#_v2=2#_v2=3#_v2=4#_v2=5#|
    // The result is:
    // |combOp_v1=ZERO_v2=#seqOp_v1=#seqOp_v1=#seqOp_v1=#seqOp_v1=#seqOp_v1=ZERO_v2=1#_v2=2#_v2=3#_v2=4#_v2=5#|

  }

  public static void main(String[] args) {
    new JavaRDDaggregate().using();
    sc.close();
  }

}
