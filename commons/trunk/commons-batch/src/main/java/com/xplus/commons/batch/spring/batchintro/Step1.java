package com.xplus.commons.batch.spring.batchintro;

import javax.sql.DataSource;

import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
//import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
//import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class Step1 {
	@Bean
	public FlatFileItemReader<Student> fileReader(@Value("${input}") Resource in) throws Exception {

		// return new FlatFileItemReaderBuilder<Student>().name("file-reader") //
		// .resource(in) //
		// .targetType(Student.class) //
		// .linesToSkip(1) //
		// .delimited() //
		// .delimiter(",") //
		// .names(new String[] { "firstName", "lastName", "email", "age" }).build();
		FlatFileItemReader<Student> reader = new FlatFileItemReader<Student>();
		reader.setName("file-reader");
		reader.setResource(in);
		reader.setLineMapper(new DefaultLineMapper<Student>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] { "first_name", "last_name", "email", "age" });
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<Student>() {
					{
						setTargetType(Student.class);
					}
				});
			}
		});
		return reader;
	}

	@Bean
	public JdbcBatchItemWriter<Student> jdbcWriter(DataSource dataSource) {
		// return new JdbcBatchItemWriterBuilder<Student>().dataSource(ds)
		// .sql("insert into STUDENT( FIRST_NAME, LAST_NAME, EMAIL, AGE) values
		// (:firstName, :lastName, :email, :age)")
		// .beanMapped().build();
		JdbcBatchItemWriter<Student> writer = new JdbcBatchItemWriter<Student>();
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Student>());
		writer
				.setSql("INSERT INTO STUDENT( FIRST_NAME, LAST_NAME, EMAIL, AGE) VALUES (:firstName, :lastName, :email, :age)");
		writer.setDataSource(dataSource);
		return writer;
	}
}
