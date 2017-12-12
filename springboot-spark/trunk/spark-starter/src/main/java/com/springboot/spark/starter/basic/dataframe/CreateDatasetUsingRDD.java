package com.springboot.spark.starter.basic.dataframe;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class CreateDatasetUsingRDD implements Serializable {

  private static final long serialVersionUID = 5924321403879655951L;

  private final static SparkConf sparkConf = new SparkConf() //
      .setAppName(CreateDatasetUsingRDD.class.getName()) //
      .setMaster("local") //
  ; //

  public final static JavaSparkContext sc = new JavaSparkContext(sparkConf);

  public void using() {

    JavaRDD<Integer> rdd = sc.parallelize(Arrays.asList(1, 2, 3, 4, 4));
    JavaRDD<Foo> map = rdd.map(new Function<Integer, Foo>() {
      private static final long serialVersionUID = -4542297399325584941L;

      @Override
      public Foo call(Integer v1) throws Exception {
        return new Foo("K_" + v1, String.valueOf(v1));
      }
    });
    SparkSession sparkSession = SparkSession.builder().sparkContext(sc.sc())
        .appName(CreateDatasetUsingRDD.class.getName()).getOrCreate();
    Dataset<Row> dataset = sparkSession.createDataFrame(map, Foo.class);
    System.err.println("Output Result:");
    dataset.show();
    // +---+-----+
    // |key|value|
    // +---+-----+
    // |K_1| 1|
    // |K_2| 2|
    // |K_3| 3|
    // |K_4| 4|
    // |K_4| 4|
    // +---+-----+
  }

  public static void main(String[] args) {
    new CreateDatasetUsingRDD().using();
    sc.close();
  }

  public static class Foo implements Serializable {
    private static final long serialVersionUID = -8986913670248430476L;

    private String key;
    private String value;

    public Foo(String key, String value) {
      this.key = key;
      this.value = value;
    }

    public String getKey() {
      return key;
    }

    public void setKey(String key) {
      this.key = key;
    }

    public String getValue() {
      return value;
    }

    public void setValue(String value) {
      this.value = value;
    }

  }

}
