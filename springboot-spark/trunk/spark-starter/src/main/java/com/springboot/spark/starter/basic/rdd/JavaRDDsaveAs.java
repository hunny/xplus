package com.springboot.spark.starter.basic.rdd;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * 
 */
public class JavaRDDsaveAs implements Serializable {

  private static final long serialVersionUID = 5924321403879655951L;

  private final static SparkConf sparkConf = new SparkConf() //
      .setAppName(JavaRDDsaveAs.class.getName()) //
      .setMaster("local") //
  ; //

  public final static JavaSparkContext sc = new JavaSparkContext(sparkConf);

  public void using(String path) {
    JavaRDD<Integer> rdd1 = sc.parallelize(Arrays.asList(1, 2, 3, 4, 5));
    rdd1.saveAsTextFile(path + "/test_folder");
    rdd1.saveAsObjectFile(path + "/test_object_folder");
  }

  public static void main(String[] args) {
    if (args.length == 0) {
      System.err.println("Using path as first args, e.g D:/test/");
      return;
    }
    if (!new File(args[0]).isDirectory()) {
      System.err.println("Path must be a directory.");
      return;
    }
    new JavaRDDsaveAs().using(args[0]);
    sc.close();
  }

}
