package com.springboot.spark.starter.basic.dataframe;

import java.io.Serializable;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class CreateDatasetUsingJsonFile implements Serializable {

  private static final long serialVersionUID = 5924321403879655951L;

  private final static SparkConf sparkConf = new SparkConf() //
      .setAppName(CreateDatasetUsingJsonFile.class.getName()) //
      .setMaster("local") //
  ; //

  public final static JavaSparkContext sc = new JavaSparkContext(sparkConf);

  public void using() {

    SparkSession sparkSession = SparkSession.builder().sparkContext(sc.sc())
        .appName(CreateDatasetUsingJsonFile.class.getName()).getOrCreate();
    Dataset<Row> dataset = sparkSession.read()
        .json("src/main/java/com/springboot/spark/starter/basic/dataframe/people.json");
    System.err.println("Output Result:");
    dataset.show();
    // +-------+-----+
    // | key|value|
    // +-------+-----+
    // |Michael| 1|
    // | Andy| 3|
    // | Justin| 2|
    // | Jack| 5|
    // +-------+-----+
  }

  public static void main(String[] args) {
    new CreateDatasetUsingJsonFile().using();
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
