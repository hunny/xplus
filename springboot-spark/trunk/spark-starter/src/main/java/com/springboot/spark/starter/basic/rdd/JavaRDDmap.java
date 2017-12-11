package com.springboot.spark.starter.basic.rdd;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;

/**
 * 
 */
@SuppressWarnings("static-method")
public class JavaRDDmap implements Serializable {

  private static final long serialVersionUID = 5924321403879655951L;

  private final static SparkConf sparkConf = new SparkConf() //
      .setAppName("JavaRDDmap") //
      .setMaster("local") //
  ; //

  public final static JavaSparkContext sc = new JavaSparkContext(sparkConf);

  public void usingMap() {
    JavaRDD<String> rdd = sc.parallelize(Arrays.asList("1", "2", "3", "4", "5"));

    testMap(rdd);

    testFlatMap(rdd);

    testMapPartation(rdd);

    mapPartationWithIndex(rdd);
  }

  private void testMapPartation(JavaRDD<String> rdd) {
    // 类似与map，map作用于每个分区的每个元素，但mapPartitions作用于每个分区工
    // func的类型：Iterator[T] => Iterator[U]
    // 假设有N个元素，有M个分区，那么map的函数的将被调用N次,而mapPartitions被调用M次,
    // 当在映射的过程中不断的创建对象时就可以使用mapPartitions比map的效率要高很多，
    // 比如当向数据库写入数据时，如果使用map就需要为每个元素创建connection对象，
    // 但使用mapPartitions的话就需要为每个分区创建connetcion对象
    FlatMapFunction<Iterator<String>, Integer> flatPartData = new FlatMapFunction<Iterator<String>, Integer>() {
      private static final long serialVersionUID = 8442091678683509024L;
      @Override
      public Iterator<Integer> call(Iterator<String> t) throws Exception {
        List<Integer> list = new LinkedList<>();
        while (t.hasNext()) {
          Integer v = Integer.valueOf(t.next());
          if (v % 2 == 0) {
            list.add(v);
          }
        }
        return list.iterator();
      }
    };
    JavaRDD<Integer> flatMap = rdd.mapPartitions(flatPartData);
    List<Integer> list = flatMap.collect();
    System.out.println(list);// [2, 4]

    // 等效使用如下操作
    mapPartationWithFilter(rdd);
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

  private void mapPartationWithFilter(JavaRDD<String> rdd) {
    JavaRDD<Integer> flatMap;
    flatMap = rdd.mapPartitions(new FlatMapFunction<Iterator<String>, Integer>() {
      private static final long serialVersionUID = 6323674676484755670L;

      @Override
      public Iterator<Integer> call(Iterator<String> t) throws Exception {
        List<Integer> list = new LinkedList<>();
        while (t.hasNext()) {
          list.add(Integer.valueOf(t.next()));
        }
        return list.iterator();
      }
    }).filter(new Function<Integer, Boolean>() {
      private static final long serialVersionUID = -3874927526831444602L;

      @Override
      public Boolean call(Integer v1) throws Exception {
        return v1 % 2 == 0;
      }
    });
    List<Integer> list = flatMap.collect();
    System.out.println(list);// [2, 4]
  }

  private void testFlatMap(JavaRDD<String> rdd) {
    // A function that returns zero or more output records from each input
    // record.
    // 与map类似，但每个元素输入项都可以被映射到0个或多个的输出项，最终将结果”扁平化“后输出
    FlatMapFunction<String, String> flatData = new FlatMapFunction<String, String>() {
      private static final long serialVersionUID = -1498858863247516338L;

      @Override
      public Iterator<String> call(String t) throws Exception {
        Integer i = Integer.valueOf(t);
        String[] arr = new String[i];
        for (int n = 0; n < i; n++) {
          arr[n] = i + "V" + n;
        }
        return Arrays.asList(arr).iterator();
      }
    };
    JavaRDD<String> flatMapRDD = rdd.flatMap(flatData);
    // 返回包含在flatMapRDD中的所有元素列表，该函数仅在结果集比较小的情况下使用，因为此时结合集都会加载到内存中。
    List<String> list = flatMapRDD.collect();
    System.out.println(list);// [1V0, 2V0, 2V1, 3V0, 3V1, 3V2, 4V0, 4V1, 4V2,
                             // 4V3, 5V0, 5V1, 5V2, 5V3, 5V4]
  }

  private void testMap(JavaRDD<String> rdd) {
    // Base interface for functions whose return types do not create special
    // RDDs.
    Function<String, String> data = new Function<String, String>() {
      private static final long serialVersionUID = 779427867416861528L;

      @Override
      public String call(String v1) throws Exception {
        return v1 + "V";
      }
    };
    // 数据集中的每个元素经过用户自定义的函数转换形成一个新的RDD，新的RDD叫MappedRDD
    JavaRDD<String> mappedRDD = rdd.map(data);
    // 返回包含在mappedRDD中的所有元素列表，该函数仅在结果集比较小的情况下使用，因为此时结合集都会加载到内存中。
    List<String> list = mappedRDD.collect();
    System.out.println(list);// [1V, 2V, 3V, 4V, 5V]
  }

  public static void main(String[] args) {
    new JavaRDDmap().usingMap();
    sc.close();
  }

}
