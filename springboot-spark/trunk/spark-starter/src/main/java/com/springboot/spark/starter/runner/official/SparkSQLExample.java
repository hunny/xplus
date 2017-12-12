package com.springboot.spark.starter.runner.official;

import java.io.Serializable;
//$example off:create_ds$
//$example on:programmatic_schema$
import java.util.ArrayList;
//$example off:programmatic_schema$
//$example on:create_ds$
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//$example on:schema_inferring$
//$example on:programmatic_schema$
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
//$example off:programmatic_schema$
//$example on:create_ds$
import org.apache.spark.api.java.function.MapFunction;
//$example off:programmatic_schema$
import org.apache.spark.sql.AnalysisException;
//$example on:create_df$
//$example on:run_sql$
//$example on:programmatic_schema$
import org.apache.spark.sql.Dataset;
//$example off:programmatic_schema$
//$example off:create_df$
//$example off:run_sql$
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
//$example off:create_ds$
//$example off:schema_inferring$
import org.apache.spark.sql.RowFactory;
//$example on:init_session$
import org.apache.spark.sql.SparkSession;
//$example on:untyped_ops$
//col("...") is preferable to df.col("...")
import org.apache.spark.sql.functions;
//$example off:init_session$
//$example on:programmatic_schema$
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.springboot.spark.starter.runner.official.profile.SparkSQLProfile;
//$example off:untyped_ops$

@Component
@SparkSQLProfile
public class SparkSQLExample implements CommandLineRunner, Serializable {
  private static final long serialVersionUID = 2669686500658811822L;

  // $example on:create_ds$
  public static class Person implements Serializable {
    private static final long serialVersionUID = -7088354690506778713L;
    private String name;
    private int age;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getAge() {
      return age;
    }

    public void setAge(int age) {
      this.age = age;
    }
  }
  // $example off:create_ds$

  @Autowired
  private SparkSession spark;
  
  @Override
  public void run(String... args) throws Exception {
    // $example on:init_session$
//    SparkSession spark = SparkSession.builder() //
//        .appName("Java Spark SQL basic example") //
//        .getOrCreate();
    // $example off:init_session$

    runBasicDataFrameExample(spark);
    runDatasetCreationExample(spark);
    runInferSchemaExample(spark);
    runProgrammaticSchemaExample(spark);

    spark.stop();
  }

  private static void runBasicDataFrameExample(SparkSession spark) throws AnalysisException {
    // $example on:create_df$
    Dataset<Row> df = spark.read().json("src/main/resources/testdata/people.json");

    // Displays the content of the DataFrame to stdout
    df.show();
    // +----+-------+
    // | age| name|
    // +----+-------+
    // |null|Michael|
    // | 30| Andy|
    // | 19| Justin|
    // +----+-------+
    // $example off:create_df$

    // $example on:untyped_ops$
    // Print the schema in a tree format
    df.printSchema();
    // root
    // |-- age: long (nullable = true)
    // |-- name: string (nullable = true)

    // Select only the "name" column
    df.select("name").show();
    // +-------+
    // | name|
    // +-------+
    // |Michael|
    // | Andy|
    // | Justin|
    // +-------+

    // Select everybody, but increment the age by 1
    df.select(functions.col("name"), functions.col("age").plus(1)).show();
    // +-------+---------+
    // | name|(age + 1)|
    // +-------+---------+
    // |Michael| null|
    // | Andy| 31|
    // | Justin| 20|
    // +-------+---------+

    // Select people older than 21
    df.filter(functions.col("age").gt(21)).show();
    // +---+----+
    // |age|name|
    // +---+----+
    // | 30|Andy|
    // +---+----+

    // Count people by age
    df.groupBy("age").count().show();
    // +----+-----+
    // | age|count|
    // +----+-----+
    // | 19| 1|
    // |null| 1|
    // | 30| 1|
    // +----+-----+
    // $example off:untyped_ops$

    // $example on:run_sql$
    // Register the DataFrame as a SQL temporary view
    df.createOrReplaceTempView("people");

    Dataset<Row> sqlDF = spark.sql("SELECT * FROM people");
    sqlDF.show();
    // +----+-------+
    // | age| name|
    // +----+-------+
    // |null|Michael|
    // | 30| Andy|
    // | 19| Justin|
    // +----+-------+
    // $example off:run_sql$

    // $example on:global_temp_view$
    // Register the DataFrame as a global temporary view
    df.createGlobalTempView("people");

    // Global temporary view is tied to a system preserved database
    // `global_temp`
    spark.sql("SELECT * FROM global_temp.people").show();
    // +----+-------+
    // | age| name|
    // +----+-------+
    // |null|Michael|
    // | 30| Andy|
    // | 19| Justin|
    // +----+-------+

    // Global temporary view is cross-session
    spark.newSession().sql("SELECT * FROM global_temp.people").show();
    // +----+-------+
    // | age| name|
    // +----+-------+
    // |null|Michael|
    // | 30| Andy|
    // | 19| Justin|
    // +----+-------+
    // $example off:global_temp_view$
  }

  private static void runDatasetCreationExample(SparkSession spark) {
    // $example on:create_ds$
    // Create an instance of a Bean class
    Person person = new Person();
    person.setName("Andy");
    person.setAge(32);

    // Encoders are created for Java beans
    Encoder<Person> personEncoder = Encoders.bean(Person.class);
    Dataset<Person> javaBeanDS = spark.createDataset(Collections.singletonList(person),
        personEncoder);
    javaBeanDS.show();
    // +---+----+
    // |age|name|
    // +---+----+
    // | 32|Andy|
    // +---+----+

    // Encoders for most common types are provided in class Encoders
    Encoder<Integer> integerEncoder = Encoders.INT();
    Dataset<Integer> primitiveDS = spark.createDataset(Arrays.asList(1, 2, 3), integerEncoder);
    Dataset<Integer> transformedDS = primitiveDS
        .map((MapFunction<Integer, Integer>) value -> value + 1, integerEncoder);
    transformedDS.collect(); // Returns [2, 3, 4]

    // DataFrames can be converted to a Dataset by providing a class. Mapping
    // based on name
    String path = "src/main/resources/testdata/people.json";
    Dataset<Person> peopleDS = spark.read().json(path).as(personEncoder);
    peopleDS.show();
    // +----+-------+
    // | age| name|
    // +----+-------+
    // |null|Michael|
    // | 30| Andy|
    // | 19| Justin|
    // +----+-------+
    // $example off:create_ds$
  }

  private static void runInferSchemaExample(SparkSession spark) {
    // $example on:schema_inferring$
    // Create an RDD of Person objects from a text file
    JavaRDD<Person> peopleRDD = spark.read().textFile("src/main/resources/testdata/people.txt")
        .javaRDD().map(line -> {
          String[] parts = line.split(",");
          Person person = new Person();
          person.setName(parts[0]);
          person.setAge(Integer.parseInt(parts[1].trim()));
          return person;
        });

    // Apply a schema to an RDD of JavaBeans to get a DataFrame
    Dataset<Row> peopleDF = spark.createDataFrame(peopleRDD, Person.class);
    // Register the DataFrame as a temporary view
    peopleDF.createOrReplaceTempView("people");

    // SQL statements can be run by using the sql methods provided by spark
    Dataset<Row> teenagersDF = spark.sql("SELECT name FROM people WHERE age BETWEEN 13 AND 19");

    // The columns of a row in the result can be accessed by field index
    Encoder<String> stringEncoder = Encoders.STRING();
    Dataset<String> teenagerNamesByIndexDF = teenagersDF
        .map((MapFunction<Row, String>) row -> "Name: " + row.getString(0), stringEncoder);
    teenagerNamesByIndexDF.show();
    // +------------+
    // | value|
    // +------------+
    // |Name: Justin|
    // +------------+

    // or by field name
    Dataset<String> teenagerNamesByFieldDF = teenagersDF.map(
        (MapFunction<Row, String>) row -> "Name: " + row.<String> getAs("name"), stringEncoder);
    teenagerNamesByFieldDF.show();
    // +------------+
    // | value|
    // +------------+
    // |Name: Justin|
    // +------------+
    // $example off:schema_inferring$
  }

  private static void runProgrammaticSchemaExample(SparkSession spark) {
    // $example on:programmatic_schema$
    // Create an RDD
    JavaRDD<String> peopleRDD = spark.sparkContext()
        .textFile("src/main/resources/testdata/people.txt", 1).toJavaRDD();

    // The schema is encoded in a string
    String schemaString = "name age";

    // Generate the schema based on the string of schema
    List<StructField> fields = new ArrayList<>();
    for (String fieldName : schemaString.split(" ")) {
      StructField field = DataTypes.createStructField(fieldName, DataTypes.StringType, true);
      fields.add(field);
    }
    StructType schema = DataTypes.createStructType(fields);

    // Convert records of the RDD (people) to Rows
    JavaRDD<Row> rowRDD = peopleRDD.map((Function<String, Row>) record -> {
      String[] attributes = record.split(",");
      return RowFactory.create(attributes[0], attributes[1].trim());
    });

    // Apply the schema to the RDD
    Dataset<Row> peopleDataFrame = spark.createDataFrame(rowRDD, schema);

    // Creates a temporary view using the DataFrame
    peopleDataFrame.createOrReplaceTempView("people");

    // SQL can be run over a temporary view created using DataFrames
    Dataset<Row> results = spark.sql("SELECT name FROM people");

    // The results of SQL queries are DataFrames and support all the normal RDD
    // operations
    // The columns of a row in the result can be accessed by field index or by
    // field name
    Dataset<String> namesDS = results
        .map((MapFunction<Row, String>) row -> "Name: " + row.getString(0), Encoders.STRING());
    namesDS.show();
    // +-------------+
    // | value|
    // +-------------+
    // |Name: Michael|
    // | Name: Andy|
    // | Name: Justin|
    // +-------------+
    // $example off:programmatic_schema$
  }
}
