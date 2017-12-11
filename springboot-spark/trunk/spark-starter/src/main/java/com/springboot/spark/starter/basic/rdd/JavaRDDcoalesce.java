package com.springboot.spark.starter.basic.rdd;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;

/**
 * 
 */
@SuppressWarnings("static-method")
public class JavaRDDcoalesce implements Serializable {

  private static final long serialVersionUID = 5924321403879655951L;

  private final static SparkConf sparkConf = new SparkConf() //
      .setAppName("JavaRDDcoalesce") //
      .setMaster("local[1]") //默认时，分区分配一个
  ; //

  public final static JavaSparkContext sc = new JavaSparkContext(sparkConf);

  public void usingCoalesce() {
    JavaRDD<String> rdd = sc.parallelize(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));

    mapPartationWithIndex(rdd);
    //输出一个分区
    //[分区：0|值为：1, 分区：0|值为：2, 分区：0|值为：3, 分区：0|值为：4, 分区：0|值为：5, 分区：0|值为：6, 分区：0|值为：7, 分区：0|值为：8, 分区：0|值为：9, 分区：0|值为：10]
    
    // 对RDD的分区进行重新分区，shuffle默认值为false,当shuffle=false时，不能增加分区数目,但不会报错，只是分区个数还是原来的
    JavaRDD<String> rdd2 = rdd.coalesce(3, true);
    
    mapPartationWithIndex(rdd2);
    //输出三个分区
    //[分区：0|值为：3, 分区：0|值为：6, 分区：0|值为：9, 分区：1|值为：1, 分区：1|值为：4, 分区：1|值为：7, 分区：1|值为：10, 分区：2|值为：2, 分区：2|值为：5, 分区：2|值为：8]
  }

  private void mapPartationWithIndex(JavaRDD<String> rdd) {
    JavaRDD<String> resultWithIndex = rdd
        .mapPartitionsWithIndex(new Function2<Integer, Iterator<String>, Iterator<String>>() {
          private static final long serialVersionUID = 8058567895317225883L;
          @Override
          public Iterator<String> call(Integer v1, Iterator<String> v2) throws Exception {
            List<String> result = new LinkedList<>();
            while (v2.hasNext()) {
              result.add("分区：" + v1 + "|值为：" + v2.next());
            }
            return result.iterator();
          }
        }, true);
    List<String> sList = resultWithIndex.collect();
    System.out.println(sList);// [分区：0|值为：1, 分区：0|值为：2, 分区：0|值为：3, 分区：0|值为：4,
                              // 分区：0|值为：5]
  }

  public static void main(String[] args) {
    new JavaRDDcoalesce().usingCoalesce();
    sc.close();
  }

}
