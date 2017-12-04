package com.springboot.spark.starter.runner.official;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.springboot.spark.starter.runner.official.profile.SparkPiProfile;

@Component
@SparkPiProfile
public class SparkPiRunner implements CommandLineRunner, Serializable {

  private static final long serialVersionUID = -1240166366466641399L;

  @Autowired
  private JavaSparkContext spark;
  
  @Override
  public void run(String... args) throws Exception {
    
    int slices = (args.length == 1) ? Integer.parseInt(args[0]) : 2;
    int n = 100000 * slices;
    List<Integer> l = new ArrayList<>(n);
    for (int i = 0; i < n; i++) {
      l.add(i);
    }

    JavaRDD<Integer> dataSet = spark.parallelize(l, slices);

    int count = dataSet.map(integer -> {
      double x = Math.random() * 2 - 1;
      double y = Math.random() * 2 - 1;
      return (x * x + y * y <= 1) ? 1 : 0;
    }).reduce((integer, integer2) -> integer + integer2);

    System.out.println("Pi is roughly " + 4.0 * count / n);
    spark.stop();
    System.exit(0);
  }

}
