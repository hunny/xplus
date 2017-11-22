package com.springboot.spark.starter.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.springboot.spark.starter.profile.SparkTextSearch;

@Service
@SparkTextSearch
public class TextSearchSparkService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private JavaSparkContext sc;

  @Autowired
  private SparkSession sparkSession;
  
  @Value("${spark.text.search.file:}")
  private String textFile;

  public List<String> search() {
    logger.info("文本搜索：");
    if (StringUtils.isBlank(textFile)) {
      logger.info("无对应的文件，考虑设置文件  spark.text.search.file=");
      return Collections.emptyList();
    }
    
    final String path = textFile;
    // Creates a DataFrame having a single column named "line"
    JavaRDD<String> textFile = sc.textFile(path);
    JavaRDD<Row> rowRDD = textFile.map(RowFactory::create);
    List<StructField> fields = Arrays
        .asList(DataTypes.createStructField("line", DataTypes.StringType, true));
    StructType schema = DataTypes.createStructType(fields);
    Dataset<Row> df = sparkSession.createDataFrame(rowRDD, schema);
    df.show();
    Dataset<Row> errors = df.filter(functions.col("line").like("%ERROR%"));
    // Counts all the errors
    logger.info("Counts all the errors: {}", errors.count());
    
    // Counts errors mentioning MySQL
    long mysqlerrcount = errors.filter(functions.col("line").like("%MySQL%")).count();
    logger.info("Counts errors mentioning MySQL: {}", mysqlerrcount);
    
    // Fetches the MySQL errors as an array of strings
    List<Row> rows = errors.filter(functions.col("line").like("%MySQL%")).collectAsList();
    return rows.stream().map(new Function<Row, String>() {
      @Override
      public String apply(Row row) {
        return row.toString();
      }
    }).collect(Collectors.toList());
  }
}
