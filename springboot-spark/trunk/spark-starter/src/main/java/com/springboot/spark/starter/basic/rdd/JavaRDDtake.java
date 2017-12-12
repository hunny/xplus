package com.springboot.spark.starter.basic.rdd;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * 
 */
public class JavaRDDtake implements Serializable {

  private static final long serialVersionUID = 5924321403879655951L;

  private final static SparkConf sparkConf = new SparkConf() //
      .setAppName(JavaRDDtake.class.getName()) //
      .setMaster("local") //
  ; //

  public final static JavaSparkContext sc = new JavaSparkContext(sparkConf);

  public void using() {
    JavaRDD<Integer> rdd1 = sc.parallelize(Arrays.asList(5, 2, 3, 4, 1));
    // take用于获取RDD中从0到num-1下标的元素，不排序。
    List<Integer> take = rdd1.take(2);
    System.err.println(take);
    // [5, 2]
    
    //top函数用于从RDD中，按照默认（降序）或者指定的排序规则，返回前num个元素。
    List<Integer> top = rdd1.top(2);
    System.err.println(top);
    // [5, 4]
    
    // takeOrdered和top类似，只不过以和top相反的顺序返回元素。
    List<Integer> takeOrdered = rdd1.takeOrdered(2);
    System.err.println(takeOrdered);
    // [1, 2]
  }

  public static void main(String[] args) {
    new JavaRDDtake().using();
    sc.close();
  }

}
