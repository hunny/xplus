package com.xplus.commons.batch.spring.batchintro;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.RowMapper;

@Configuration
public class Step2 {

	@Bean
	public ItemReader<Map<Integer, Integer>> jdbcReader(DataSource dataSource) {
		// return new JdbcCursorItemReaderBuilder<Map<Integer,
		// Integer>>().dataSource(dataSource).name("jdbc-reader")
		// .sql("select COUNT(age) c, age a from STUDENT group by age")
		// .rowMapper((rs, i) -> Collections.singletonMap(rs.getInt("a"),
		// rs.getInt("c"))).build();
		JdbcCursorItemReader<Map<Integer, Integer>> reader = //
				new JdbcCursorItemReader<Map<Integer, Integer>>();
		reader.setName("jdbc-reader");
		reader.setDataSource(dataSource);
		reader.setSql("select COUNT(age) c, age a from STUDENT group by age");
		reader.setRowMapper(new RowMapper<Map<Integer, Integer>>() {
			@Override
			public Map<Integer, Integer> mapRow(ResultSet rs, int rowNum) throws SQLException {
				return Collections.singletonMap(rs.getInt("a"), //
						rs.getInt("c"));
			}
		});
		return reader;
	}

	@Bean
	public ItemWriter<Map<Integer, Integer>> fileWriter(@Value("${output}") Resource resource) {
		// return new FlatFileItemWriterBuilder<Map<Integer,
		// Integer>>().name("file-writer").resource(resource)
		// .lineAggregator(new DelimitedLineAggregator<Map<Integer, Integer>>() {
		// {
		// setDelimiter(",");
		// setFieldExtractor(integerIntegerMap -> {
		// Map.Entry<Integer, Integer> next =
		// integerIntegerMap.entrySet().iterator().next();
		// return new Object[] { next.getKey(), next.getValue() };
		// });
		// }
		// }).build();
		FlatFileItemWriter<Map<Integer, Integer>> writer = //
				new FlatFileItemWriter<Map<Integer, Integer>>();
		writer.setName("file-writer");
		writer.setResource(resource);
		writer.setLineAggregator(new DelimitedLineAggregator<Map<Integer, Integer>>() {
			{
				setDelimiter(",");
//				setFieldExtractor(integerIntegerMap -> {
//					Map.Entry<Integer, Integer> next = integerIntegerMap.entrySet().iterator().next();
//					return new Object[] { next.getKey(), next.getValue() };
//				});
				setFieldExtractor(new FieldExtractor<Map<Integer, Integer>>() {
					@Override
					public Object[] extract(Map<Integer, Integer> item) {
						Map.Entry<Integer, Integer> next = item.entrySet().iterator().next();
						return new Object[] { next.getKey(), next.getValue() };
					}
				});
			}
		});
		return writer;
	}
}
