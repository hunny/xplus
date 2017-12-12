package com.springboot.spark.starter.basic.dataframe;

import java.io.Serializable;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

@SuppressWarnings("static-method")
public class DatasetSql implements Serializable {

  private static final long serialVersionUID = 5924321403879655951L;

  private final static SparkConf sparkConf = new SparkConf() //
      .setAppName(DatasetSql.class.getName()) //
      .setMaster("local") //
  ; //

  public final static JavaSparkContext sc = new JavaSparkContext(sparkConf);

  public void using() {

    SparkSession sparkSession = SparkSession.builder().sparkContext(sc.sc())
        .appName(DatasetSql.class.getName()).getOrCreate();
    Dataset<Row> dataset = sparkSession.read()
        .json("src/main/java/com/springboot/spark/starter/basic/dataframe/people.json");
    // Persist this Dataset with the default storage level (`MEMORY_AND_DISK`).
    dataset.cache();
    sql(dataset);
  }

  private void sql(Dataset<Row> dataset) {
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

    Dataset<Row> select = dataset.select("key");
    System.err.println("select Output:");
    select.show();
    // select Output:
    // +-------+
    // | key|
    // +-------+
    // |Michael|
    // | Andy|
    // | Justin|
    // | Jack|
    // +-------+

    select = dataset.select(new Column("key"), new Column("value").plus(Integer.valueOf(10)));
    System.err.println("select Output:");
    select.show();
    // +-------+------------+
    // | key|(value + 10)|
    // +-------+------------+
    // |Michael| 11|
    // | Andy| 13|
    // | Justin| 12|
    // | Jack| 15|
    // +-------+------------+
    
    Dataset<Row> filter = select.filter(new Column("(value + 10)").gt(Integer.valueOf(12)));
    System.err.println("filter Output:");
    filter.printSchema();
    // root
    // |-- key: string (nullable = true)
    // |-- (value + 10): long (nullable = true)
    filter.show();
    // +----+------------+
    // | key|(value + 10)|
    // +----+------------+
    // |Andy| 13|
    // |Jack| 15|
    // +----+------------+
  }

  public static void main(String[] args) {
    new DatasetSql().using();
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
