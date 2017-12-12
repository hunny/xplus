package com.springboot.spark.starter.basic.dataframe;

import java.io.Serializable;
import java.util.regex.Pattern;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class CreateDatasetUsingTextFile implements Serializable {

  private static final long serialVersionUID = 5924321403879655951L;
  private static final Pattern SPACE = Pattern.compile(" ");

  private final static SparkConf sparkConf = new SparkConf() //
      .setAppName(CreateDatasetUsingTextFile.class.getName()) //
      .setMaster("local") //
  ; //

  public final static JavaSparkContext sc = new JavaSparkContext(sparkConf);

  public void using() {

    SparkSession sparkSession = SparkSession.builder().sparkContext(sc.sc())
        .appName(CreateDatasetUsingTextFile.class.getName()).getOrCreate();

    Dataset<String> dataset = sparkSession.read()
        .textFile("src/main/java/com/springboot/spark/starter/basic/dataframe/machinelearning.txt");
    System.err.println("Output Result:");
    dataset.show();
    // +--------------------+
    // | value|
    // +--------------------+
    // |https://www.youtu...|
    // |https://www.youtu...|
    // |https://www.youtu...|
    // |https://www.leiph...|
    // +--------------------+
    JavaRDD<Foo> map = dataset.toJavaRDD().map(new Function<String, Foo>() {
      private static final long serialVersionUID = -510785798938184376L;

      @Override
      public Foo call(String v1) throws Exception {
        String[] values = SPACE.split(v1);
        return new Foo(values[0], v1);
      }
    });
    Dataset<Row> dataFrame = sparkSession.createDataFrame(map, Foo.class);
    dataFrame.show();
    // +--------------------+--------------------+
    // | key| value|
    // +--------------------+--------------------+
    // |https://www.youtu...|https://www.youtu...|
    // |https://www.youtu...|https://www.youtu...|
    // |https://www.youtu...|https://www.youtu...|
    // |https://www.leiph...|https://www.leiph...|
    // +--------------------+--------------------+

  }

  public static void main(String[] args) {
    new CreateDatasetUsingTextFile().using();
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
