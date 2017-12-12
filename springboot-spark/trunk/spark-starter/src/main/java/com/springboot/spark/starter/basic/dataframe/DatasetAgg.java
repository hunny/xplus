package com.springboot.spark.starter.basic.dataframe;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

@SuppressWarnings("static-method")
public class DatasetAgg implements Serializable {

  private static final long serialVersionUID = 5924321403879655951L;

  private final static SparkConf sparkConf = new SparkConf() //
      .setAppName(DatasetAgg.class.getName()) //
      .setMaster("local") //
  ; //

  public final static JavaSparkContext sc = new JavaSparkContext(sparkConf);

  public void using() {

    SparkSession sparkSession = SparkSession.builder().sparkContext(sc.sc())
        .appName(DatasetAgg.class.getName()).getOrCreate();
    Dataset<Row> dataset = sparkSession.read()
        .json("src/main/java/com/springboot/spark/starter/basic/dataframe/people.json");
    // Persist this Dataset with the default storage level (`MEMORY_AND_DISK`).
    dataset.cache();
    // Prints the physical plan to the console for debugging purposes.
    System.err.println("Prints the physical plan to the console for debugging purposes.");
    dataset.explain();
    agg(dataset);
  }

  private void agg(Dataset<Row> dataset) {
    Map<String, String> exprs = new HashMap<>();
    exprs.put("key", "count");
    exprs.put("value", "sum");
    Dataset<Row> agg = dataset.agg(exprs);
    System.err.println("Original Output:");
    dataset.show();
    // Original Output:
    // +-------+-----+
    // | key|value|
    // +-------+-----+
    // |Michael| 1|
    // | Andy| 3|
    // | Justin| 2|
    // | Jack| 5|
    // +-------+-----+
    System.err.println("agg Output:");
    agg.show();
    // agg Output:
    // +----------+----------+
    // |sum(value)|count(key)|
    // +----------+----------+
    // | 11| 4|
    // +----------+----------+
  }

  public static void main(String[] args) {
    new DatasetAgg().using();
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
