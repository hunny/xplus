package com.springboot.spark.starter.runner;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.springboot.spark.starter.profile.SparkExampleComment;

import scala.Tuple2;

@Component
@SparkExampleComment
public class SparkExampleCommentRunner implements CommandLineRunner, Serializable {

  private static final long serialVersionUID = -3042825005283168208L;

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Value("${app.name:HunnyHu}")
  private String appName;

  @Value("${master.uri:local[2]}")
  private String masterUri;

  @SuppressWarnings("serial")
  @Override
  public void run(String... args) throws Exception {

    if (args.length < 2) {
      logger.info(
          "Profile is SparkWordCountFile but argument not correct '{}', Note: args[0] is a file, args[1] is a folder.",
          Arrays.asList(args));
      return;
    }

    String inputFile = args[0];
    String outputPath = args[1];

    // context ，用于读文件 ，类似于scala的sc
    // 格式为：
    // JavaSparkContext(master: String, appName: String, sparkHome: String,
    // jars: Array[String], environment: Map[String, String])
    JavaSparkContext ctx = new JavaSparkContext(masterUri, //
        appName, //
        System.getenv("SPARK_HOME"), //
        JavaSparkContext.jarOfClass(SparkExampleCommentRunner.class));

    // 也可以使用ctx获取环境变量，例如下面的语句
    logger.info("spark home:" + ctx.getSparkHome());

    // 一次一行，String类型 ,还有hadoopfile，sequenceFile什么的 ，可以直接用sc.textFile("path")
    JavaRDD<String> lines = ctx.textFile(inputFile, 1); // java.lang.String
                                                        // path,
    // int minSplits
    lines.cache(); // cache，暂时放在缓存中，一般用于哪些可能需要多次使用的RDD，据说这样会减少运行时间

    // collect方法，用于将RDD类型转化为java基本类型，如下
    logger.info("演示collect方法---------------");
    List<String> line = lines.collect();
    for (String val : line) {
      logger.info(val);
    }

    // 下面这些也是RDD的常用函数
    // lines.collect(); List<String>
    // lines.union(); javaRDD<String>
    // lines.top(1); List<String>
    // lines.count(); long
    // lines.countByValue();

    /**
     * filter test 定义一个返回bool类型的函数，spark运行filter的时候会过滤掉那些返回只为false的数据 String
     * s，中的变量s可以认为就是变量lines（lines可以理解为一系列的String类型数据）的每一条数据
     */
    logger.info("演示filter方法---------------");
    JavaRDD<String> contaninsE = lines.filter(new Function<String, Boolean>() {
      @Override
      public Boolean call(String s) throws Exception {

        return (s.contains("they"));
      }
    });
    logger.info("--------------next filter's  result------------------");
    line = contaninsE.collect();
    for (String val : line) {
      logger.info(val);
    }

    /**
     * sample test sample函数使用很简单，用于对数据进行抽样 参数为：withReplacement: Boolean,
     * fraction: Double, seed: Int
     * 
     */
    JavaRDD<String> sampletest = lines.sample(false, 0.1, 5);
    logger.info("-------------next sample-------------------");
    line = sampletest.collect();
    for (String val : line) {
      logger.info(val);
    }

    /**
     * 
     * new FlatMapFunction<String, String>两个string分别代表输入和输出类型
     * Override的call方法需要自己实现一个转换的方法，并返回一个Iterable的结构
     * flatmap属于一类非常常用的spark函数，简单的说作用就是将一条rdd数据使用你定义的函数给分解成多条rdd数据
     * 例如，当前状态下，lines这个rdd类型的变量中，每一条数据都是一行String，我们现在想把他拆分成1个个的词的话， 可以这样写 ：
     */
    JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
      @Override
      public Iterator<String> call(String s) {
        String[] words = s.split(" ");
        return Arrays.asList(words).iterator();
      }
    });

    /**
     * map 键值对 ，类似于MR的map方法 pairFunction<T,K,V>: T:输入类型；K,V：输出键值对 需要重写call方法实现转换
     */
    JavaPairRDD<String, Integer> ones = words
        .mapToPair(new PairFunction<String, String, Integer>() {
          @Override
          public Tuple2<String, Integer> call(String s) {
            return new Tuple2<String, Integer>(s, 1);
          }
        });

    // A two-argument function that takes arguments
    // of type T1 and T2 and returns an R.
    /**
     * reduceByKey方法，类似于MR的reduce
     * 要求被操作的数据（即下面实例中的ones）是KV键值对形式，该方法会按照key相同的进行聚合，在两两运算
     */
    JavaPairRDD<String, Integer> counts = ones
        .reduceByKey(new Function2<Integer, Integer, Integer>() {
          @Override
          public Integer call(Integer i1, Integer i2) { // reduce阶段，key相同的value怎么处理的问题
            return i1 + i2;
          }
        });

    // 备注：spark也有reduce方法，输入数据是RDD类型就可以，不需要键值对，
    // reduce方法会对输入进来的所有数据进行两两运算

    /**
     * sort，顾名思义，排序
     */
    JavaPairRDD<String, Integer> sort = counts.sortByKey();
    logger.info("----------next sort----------------------");

    /**
     * collect方法其实之前已经出现了多次，该方法用于将spark的RDD类型转化为我们熟知的java常见类型
     */
    List<Tuple2<String, Integer>> output = sort.collect();
    for (Tuple2<?, ?> tuple : output) {
      logger.info(tuple._1 + ": " + tuple._2());
    }

    /**
     * 保存函数，数据输出，spark为结果输出提供了很多接口
     */
    sort.saveAsTextFile(outputPath);

    // sort.saveAsNewAPIHadoopFile();
    // sort.saveAsHadoopFile();
    ctx.close();
    System.exit(0);
  }

}
