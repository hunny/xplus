package com.springboot.spark.starter.basic.rdd;

import java.io.Serializable;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaFutureAction;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;

/**
 * 
 */
public class JavaRDDforeach implements Serializable {

  private static final long serialVersionUID = 5924321403879655951L;

  private final static SparkConf sparkConf = new SparkConf() //
      .setAppName(JavaRDDforeach.class.getName()) //
      .setMaster("local") //
  ; //

  public final static JavaSparkContext sc = new JavaSparkContext(sparkConf);

  public void using() throws InterruptedException, ExecutionException {
    JavaRDD<Integer> rdd1 = sc.parallelize(Arrays.asList(1, 2, 3, 4, 5, 3, 5));
    rdd1.foreach(new VoidFunction<Integer>() {
      private static final long serialVersionUID = -3596600139708805107L;

      @Override
      public void call(Integer t) throws Exception {
        System.err.print("元素：" + t + ",");
      }
    });
    // 输出
    // 元素：1,元素：2,元素：3,元素：4,元素：5,元素：3,元素：5,
    JavaFutureAction<Void> foreachAsync = rdd1.foreachAsync(new VoidFunction<Integer>() {
      private static final long serialVersionUID = -8449909925571012215L;

      @Override
      public void call(Integer t) throws Exception {
        System.err.print("元素：" + t + "|");
      }
    });
    long start = System.currentTimeMillis();
    // Waits if necessary for the computation to complete, and then retrieves its result.
    foreachAsync.get();
    System.err.println("计算完成,耗时：" + (System.currentTimeMillis() - start) + "ms");
    // 计算完成,耗时：23ms
  }

  public static void main(String[] args) throws Exception {
    new JavaRDDforeach().using();
    sc.close();
  }

}
