package com.springboot.spark.starter.runner.official;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.springboot.spark.starter.runner.official.profile.SQLDataSourceProfile;

@Component
@SQLDataSourceProfile
public class SQLDataSourceRunner implements CommandLineRunner, Serializable {

  private static final long serialVersionUID = -1186586366947489351L;
  
  public static class Square implements Serializable {
    
    private static final long serialVersionUID = 3604247260359580567L;
    
    private int value;
    private int square;

    public int getValue() {
      return value;
    }

    public void setValue(int value) {
      this.value = value;
    }

    public int getSquare() {
      return square;
    }

    public void setSquare(int square) {
      this.square = square;
    }
  }

  public static class Cube implements Serializable {

    private static final long serialVersionUID = 4398206188318346369L;
    
    private int value;
    private int cube;

    public int getValue() {
      return value;
    }

    public void setValue(int value) {
      this.value = value;
    }

    public int getCube() {
      return cube;
    }

    public void setCube(int cube) {
      this.cube = cube;
    }
  }

  public void runBasicDataSourceExample(SparkSession spark) {
    Dataset<Row> usersDF = spark.read().load("src/main/resources/testdata/users.parquet");
    usersDF.show();
    System.out.println("from src/main/resources/testdata/users.parquet");
    usersDF.select("name", "favorite_color").write().mode(SaveMode.Overwrite).save("tmp/namesAndFavColors.parquet");
    Dataset<Row> peopleDF = spark.read().format("json")
        .load("src/main/resources/testdata/people.json");
    peopleDF.show();
    System.out.println("from src/main/resources/testdata/people.json");
    peopleDF.select("name", "age").write().mode(SaveMode.Overwrite).format("parquet").save("tmp/namesAndAges.parquet");
    Dataset<Row> peopleDFCsv = spark.read().format("csv").option("sep", ";")
        .option("inferSchema", "true").option("header", "true")
        .load("src/main/resources/testdata/people.csv");
    peopleDFCsv.show();
    System.out.println("from src/main/resources/testdata/people.csv");
    Dataset<Row> sqlDF = spark
        .sql("SELECT * FROM parquet.`src/main/resources/testdata/users.parquet`");
    sqlDF.show();
    System.out.println("from SELECT * FROM parquet.`src/main/resources/testdata/users.parquet`");
    peopleDF.write().mode(SaveMode.Overwrite).bucketBy(42, "name").sortBy("age").saveAsTable("people_bucketed");
    peopleDF.show();
    usersDF.write().mode(SaveMode.Overwrite).partitionBy("favorite_color").format("parquet")
        .save("tmp/namesPartByColor.parquet");
    peopleDF.show();
    peopleDF.write().mode(SaveMode.Overwrite).partitionBy("age").bucketBy(42, "name")
        .saveAsTable("people_partitioned_bucketed");
    peopleDF.show();
    spark.sql("DROP TABLE IF EXISTS people_bucketed");
    spark.sql("DROP TABLE IF EXISTS people_partitioned_bucketed");
  }

  public void runBasicParquetExample(SparkSession spark) {
    Dataset<Row> peopleDF = spark.read().json("src/main/resources/testdata/people.json");

    peopleDF.show();
    peopleDF.show();
    peopleDF.show();
    // DataFrames can be saved as Parquet files, maintaining the schema
    // information
    peopleDF.write().mode(SaveMode.Overwrite).parquet("people.parquet");

    // Read in the Parquet file created above.
    // Parquet files are self-describing so the schema is preserved
    // The result of loading a parquet file is also a DataFrame
    Dataset<Row> parquetFileDF = spark.read().parquet("people.parquet");
    parquetFileDF.show();
    // Parquet files can also be used to create a temporary view and then used
    // in SQL statements
    parquetFileDF.createOrReplaceTempView("parquetFile");
    parquetFileDF.show();
    Dataset<Row> namesDF = spark.sql("SELECT name FROM parquetFile WHERE age BETWEEN 13 AND 19");
    namesDF.show();
    Dataset<String> namesDS = namesDF
        .map((MapFunction<Row, String>) row -> "Name: " + row.getString(0), Encoders.STRING());
    namesDS.show();
    // +------------+
    // | value|
    // +------------+
    // |Name: Justin|
    // +------------+
  }

  public void runParquetSchemaMergingExample(SparkSession spark) {
    List<Square> squares = new ArrayList<>();
    for (int value = 1; value <= 5; value++) {
      Square square = new Square();
      square.setValue(value);
      square.setSquare(value * value);
      squares.add(square);
    }

    // Create a simple DataFrame, store into a partition directory
    Dataset<Row> squaresDF = spark.createDataFrame(squares, Square.class);
    squaresDF.write().mode(SaveMode.Overwrite).parquet("data/test_table/key=1");
    squaresDF.show();
    List<Cube> cubes = new ArrayList<>();
    for (int value = 6; value <= 10; value++) {
      Cube cube = new Cube();
      cube.setValue(value);
      cube.setCube(value * value * value);
      cubes.add(cube);
    }

    // Create another DataFrame in a new partition directory,
    // adding a new column and dropping an existing column
    Dataset<Row> cubesDF = spark.createDataFrame(cubes, Cube.class);
    cubesDF.show();
    cubesDF.write().mode(SaveMode.Overwrite).parquet("data/test_table/key=2");
    cubesDF.show();
    
    // Read the partitioned table
    Dataset<Row> mergedDF = spark.read().option("mergeSchema", true).parquet("data/test_table");
    mergedDF.printSchema();
    mergedDF.show();
    // The final schema consists of all 3 columns in the Parquet files together
    // with the partitioning column appeared in the partition directory paths
    // root
    // |-- value: int (nullable = true)
    // |-- square: int (nullable = true)
    // |-- cube: int (nullable = true)
    // |-- key: int (nullable = true)
  }

  public void runJsonDatasetExample(SparkSession spark) {
    // A JSON dataset is pointed to by path.
    // The path can be either a single text file or a directory storing text
    // files
    Dataset<Row> people = spark.read().json("src/main/resources/testdata/people.json");

    // The inferred schema can be visualized using the printSchema() method
    people.printSchema();
    people.show();
    // root
    // |-- age: long (nullable = true)
    // |-- name: string (nullable = true)

    // Creates a temporary view using the DataFrame
    people.createOrReplaceTempView("people");

    // SQL statements can be run by using the sql methods provided by spark
    Dataset<Row> namesDF = spark.sql("SELECT name FROM people WHERE age BETWEEN 13 AND 19");
    namesDF.show();
    // +------+
    // | name|
    // +------+
    // |Justin|
    // +------+

    // Alternatively, a DataFrame can be created for a JSON dataset represented
    // by
    // a Dataset<String> storing one JSON object per string.
    List<String> jsonData = Arrays
        .asList("{\"name\":\"Yin\",\"address\":{\"city\":\"Columbus\",\"state\":\"Ohio\"}}");
    Dataset<String> anotherPeopleDataset = spark.createDataset(jsonData, Encoders.STRING());
    anotherPeopleDataset.show();
    
    Dataset<Row> anotherPeople = spark.read().json(anotherPeopleDataset);
    anotherPeople.show();
    // +---------------+----+
    // | address|name|
    // +---------------+----+
    // |[Columbus,Ohio]| Yin|
    // +---------------+----+
  }

  public void runJdbcDatasetExample(SparkSession spark) {
    // Note: JDBC loading and saving can be achieved via either the load/save or
    // jdbc methods
    // Loading data from a JDBC source
    Dataset<Row> jdbcDF = spark.read().format("jdbc") // 
        .option("url", "jdbc:postgresql://172.17.10.52:6545/hdb") // 
        .option("driver", "org.postgresql.Driver") // 
        .option("dbtable", "LOGSTASH_TEST_QFDPOS") // database:hdb, schema:dev, 
        // table:LOGSTASH_TEST_QFDPOS, 
        // not (dev.LOGSTASH_TEST_QFDPOS)
        .option("user", "dev") // 
        .option("password", "hawq5432") // 
        .load();
    jdbcDF.limit(5);
    jdbcDF.show(10);
    
    Properties connectionProperties = new Properties();
    connectionProperties.put("user", "dev");
    connectionProperties.put("password", "hawq5432");
    Dataset<Row> jdbcDF2 = spark.read().jdbc("jdbc:postgresql://172.17.10.52:6545/hdb", "LOGSTASH_TEST_QFDPOS",
        connectionProperties);
    jdbcDF2.show(10);
    
    Dataset<Row> jdbcDF3 = spark.read() //
        .format("jdbc") // 
        .option("url", "jdbc:mysql://localhost:3306/points?useUnicode=yes&characterEncoding=utf-8&useSSL=no") // 
        .option("driver", "com.mysql.jdbc.Driver") // 
        .option("dbtable", "transaction")
        .option("user", "points") // 
        .option("password", "points") // 
        .load();//
    jdbcDF3.show();
    
    // Saving data to a JDBC source
    jdbcDF3.write() //
        .format("jdbc") //
        .option("url", "jdbc:mysql://localhost:3306/mbr?useUnicode=yes&characterEncoding=utf-8&useSSL=no") //
//        .option("driver", "com.mysql.jdbc.Driver") // 
        .option("dbtable", "transaction2") //
        .option("user", "mbr") //
        .option("password", "mbr") //
        .mode(SaveMode.Overwrite)
        .save();//

    Properties connectionProperties4 = new Properties();
    connectionProperties4.put("user", "mbr");
    connectionProperties4.put("password", "mbr");
    Dataset<Row> jdbcDF4 = spark.read().jdbc("jdbc:mysql://localhost:3306/mbr?useUnicode=yes&characterEncoding=utf-8&useSSL=no", "transaction2",
        connectionProperties4);
    jdbcDF4.show(10);
    
    // Specifying create table column data types on write
    jdbcDF4.write()//
        .mode(SaveMode.Overwrite) //
//        .option("createTableColumnTypes", "name CHAR(64), comments VARCHAR(1024)") //
        .jdbc("jdbc:mysql://localhost:3306/mbr?useUnicode=yes&characterEncoding=utf-8&useSSL=no", "transaction3", connectionProperties4);
  }

  @Override
  public void run(String... args) throws Exception {
    SparkSession spark = SparkSession.builder().appName("Java Spark SQL data sources example")
        .config("spark.some.config.option", "some-value").getOrCreate();
    runBasicDataSourceExample(spark);
    runBasicParquetExample(spark);
    runParquetSchemaMergingExample(spark);
    runJsonDatasetExample(spark);
//    runJdbcDatasetExample(spark);
    spark.stop();
    System.exit(0);
  }
}
