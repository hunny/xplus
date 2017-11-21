package com.springboot.spark.starter.service;

//import static org.apache.spark.sql.functions.col;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.RelationalGroupedDataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.types.StructType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.spark.starter.profile.SparkWordCount;

@Service
@SparkWordCount
public class WordCountSparkService {
  
  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  @Autowired
  private SparkSession sparkSession;

  public List<Count> count() {
    String input = "hello world hello hello hello";
    String[] _words = input.split(" ");
    List<Word> words = Arrays.stream(_words).map(Word::new).collect(Collectors.toList());
    Dataset<Row> dataFrame = sparkSession.createDataFrame(words, Word.class);
    dataFrame.show();
    StructType structType = dataFrame.schema();
    logger.info("StructType : '{}'", structType);
    RelationalGroupedDataset groupedDataset = dataFrame.groupBy(functions.col("word"));
    groupedDataset.count().show();
    List<Row> rows = groupedDataset.count().collectAsList();// JavaConversions.asScalaBuffer(words)).count();
    return rows.stream().map(new Function<Row, Count>() {
      @Override
      public Count apply(Row row) {
        return new Count(row.getString(0), row.getLong(1));
      }
    }).collect(Collectors.toList());
  }
}
