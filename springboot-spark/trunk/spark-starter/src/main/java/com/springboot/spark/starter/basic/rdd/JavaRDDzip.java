package com.springboot.spark.starter.basic.rdd;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction2;
import org.apache.spark.api.java.function.Function2;

/**
 * 
 */
@SuppressWarnings("static-method")
public class JavaRDDzip implements Serializable {

  private static final long serialVersionUID = 5924321403879655951L;

  private final static SparkConf sparkConf = new SparkConf() //
      .setAppName(JavaRDDzip.class.getName()) //
      .setMaster("local[2]") //
  ; //

  public final static JavaSparkContext sc = new JavaSparkContext(sparkConf);

  public void using() {
    zip();
    zipPartition();
    zipWithIndex();
    zipWithUniqueId();
  }

  private void zip() {
    JavaRDD<Integer> rdd1 = sc.parallelize(Arrays.asList(1, 2, 3, 4, 5));
    JavaRDD<Integer> rdd2 = sc.parallelize(Arrays.asList(6, 7, 8, 9, 10));
    // zip函数用于将两个RDD组合成Key/Value形式的RDD,这里默认两个RDD的partition数量以及元素数量都相同，否则会抛出异常。
    JavaPairRDD<Integer, Integer> rdd3 = rdd1.zip(rdd2);
    System.out.println("zip 大小：" + rdd3.count() + ", 数据：" + rdd3.collect());
    // 大小：5, 数据：[(1,6), (2,7), (3,8), (4,9), (5,10)]
  }

  private void zipPartition() {
    JavaRDD<Integer> rdd1 = sc.parallelize(Arrays.asList(1, 2, 3, 4, 5), 2);
    JavaRDD<String> rdd3 = rdd1
        .mapPartitionsWithIndex(new Function2<Integer, Iterator<Integer>, Iterator<String>>() {
          private static final long serialVersionUID = -8086247568863010902L;

          @Override
          public Iterator<String> call(Integer v1, Iterator<Integer> v2) throws Exception {
            List<String> result = new LinkedList<>();
            while (v2.hasNext()) {
              result.add("part_" + v1 + "|value_" + v2.next());
            }
            return result.iterator();
          }
        }, false);
    System.out.println(rdd3.collect());
    JavaRDD<String> rdd2 = sc.parallelize(Arrays.asList("A", "B", "C", "D", "E"), 2);
    JavaRDD<String> rdd4 = rdd2
        .mapPartitionsWithIndex(new Function2<Integer, Iterator<String>, Iterator<String>>() {
          private static final long serialVersionUID = -241605037175411549L;

          @Override
          public Iterator<String> call(Integer v1, Iterator<String> v2) throws Exception {
            List<String> result = new LinkedList<>();
            while (v2.hasNext()) {
              result.add("part_" + v1 + "|value_" + v2.next());
            }
            return result.iterator();
          }
        }, false);
    System.out.println(rdd4.collect());

    // zipPartitions函数将多个RDD按照partition组合成为新的RDD，该函数需要组合的RDD具有相同的分区数，但对于每个分区内的元素数量没有要求。
    // 这两个区别就是参数preservesPartitioning，是否保留父RDD的partitioner分区信息
    JavaRDD<String> zipPartitions = rdd1.zipPartitions(rdd2,
        new FlatMapFunction2<Iterator<Integer>, Iterator<String>, String>() {
          private static final long serialVersionUID = -5122575583415503967L;

          @Override
          public Iterator<String> call(Iterator<Integer> t1, Iterator<String> t2) throws Exception {
            List<String> result = new LinkedList<>();
            while (t1.hasNext() && t2.hasNext()) {
              result.add("merge_" + t1.next() + "|" + t2.next());
            }
            return result.iterator();
          }

        });
    System.out
        .println("zipPartitions 大小：" + zipPartitions.count() + ", 数据：" + zipPartitions.collect());
    // 大小：5, 数据：[merge_1|A, merge_2|B, merge_3|C, merge_4|D, merge_5|E]
  }

  private void zipWithIndex() {
    JavaRDD<Integer> rdd1 = sc.parallelize(Arrays.asList(32, 32, 43, 21, 10, 54), 2);
    // 该函数将RDD中的元素和这个元素在RDD中的ID（索引号）组合成键/值对。
    JavaPairRDD<Integer, Long> zipWithIndex = rdd1.zipWithIndex();
    System.out
        .println("zipWithIndex 大小：" + zipWithIndex.count() + ", 数据：" + zipWithIndex.collect());
    // zipWithIndex 大小：6, 数据：[(32,0), (32,1), (43,2), (21,3), (10,4), (54,5)]
  }

  private void zipWithUniqueId() {
    JavaRDD<String> rdd2 = sc.parallelize(Arrays.asList("A", "B", "C", "D", "E", "F"), 2);
    // 显示分区
    System.err.println(rdd2
    .mapPartitionsWithIndex(new Function2<Integer, Iterator<String>, Iterator<String>>() {
      private static final long serialVersionUID = -241605037175411549L;

      @Override
      public Iterator<String> call(Integer v1, Iterator<String> v2) throws Exception {
        List<String> result = new LinkedList<>();
        while (v2.hasNext()) {
          result.add("part_" + v1 + "|value_" + v2.next());
        }
        return result.iterator();
      }
    }, false).collect());
    //[part_0|value_A, part_0|value_B, part_0|value_C, part_1|value_D, part_1|value_E, part_1|value_F]
    
    // 该函数将RDD中元素和一个唯一ID组合成键/值对，该唯一ID生成算法如下：
    // 每个分区中第一个元素的唯一ID值为：该分区索引号，
    // 每个分区中第N个元素的唯一ID值为：(前一个元素的唯一ID值) + (该RDD总的分区数)
    JavaPairRDD<String, Long> zipWithUniqueId = rdd2.zipWithUniqueId();
    System.out.println(
        "zipWithUniqueId 大小：" + zipWithUniqueId.count() + ", 数据：" + zipWithUniqueId.collect());
    // zipWithUniqueId 大小：6, 数据：[(A,0), (B,2), (C,4), (D,1), (E,3), (F,5)]
  }

  public static void main(String[] args) {
    new JavaRDDzip().using();
    sc.close();
  }

}
