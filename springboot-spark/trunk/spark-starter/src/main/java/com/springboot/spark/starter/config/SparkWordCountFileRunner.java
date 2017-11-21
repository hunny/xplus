package com.springboot.spark.starter.config;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.springboot.spark.starter.profile.SparkWordCountFile;

import scala.Tuple2;

@Component
@SparkWordCountFile
public class SparkWordCountFileRunner implements CommandLineRunner, Serializable {

  private static final long serialVersionUID = -3272208317248588053L;

  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  @Value("${app.name:jigsaw}")
  private String appName;

  @Value("${spark.home}")
  private String sparkHome;

  @Value("${master.uri:local}")
  private String masterUri;
  
  @Value("${word.count.file.num:2}")
  private Integer numOutputFiles;

  @SuppressWarnings("serial")
  @Override
  public void run(String... args) throws Exception {
    if (args.length < 2) {
      logger.info("Profile is SparkWordCountFile but argument not correct '{}', Note: args[0] is a file, args[1] is a folder.", Arrays.asList(args));
      return;
    }

    String inputFile = args[0];
    String outputFile = args[1];
    // Create a Java Spark Context.
    SparkConf conf = new SparkConf() //
        // The name of application. This will appear in the UI and in log data.
        .setAppName(appName) //
        .setSparkHome(sparkHome) //
        .setMaster(masterUri); //

    JavaSparkContext sc = new JavaSparkContext(conf);
    // Load our input data.
    JavaRDD<String> input = sc.textFile(inputFile);
    // Split up into words.
    JavaRDD<String> words = input.flatMap(new FlatMapFunction<String, String>() {
      public Iterator<String> call(String x) {
        return Arrays.asList(x.split(" ")).iterator();
      }
    });
    // Transform into word and count.
    JavaPairRDD<String, Integer> counts = words
        .mapToPair(new PairFunction<String, String, Integer>() {
          public Tuple2<String, Integer> call(String x) {
            return new Tuple2(x, 1);
          }
        }).reduceByKey(new Function2<Integer, Integer, Integer>() {
          public Integer call(Integer x, Integer y) {
            return x + y;
          }
        }, numOutputFiles);
    // Save the word count back out to a text file, causing evaluation.
    counts.sortByKey(true).saveAsTextFile(outputFile);
    sc.close();
    logger.info("Run completion, exit.");
    System.exit(0);
  }

}
