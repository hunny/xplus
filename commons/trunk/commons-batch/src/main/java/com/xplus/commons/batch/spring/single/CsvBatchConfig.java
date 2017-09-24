package com.xplus.commons.batch.spring.single;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.validator.Validator;
import org.springframework.batch.support.DatabaseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 7. 配置。
 */
@Configuration
@EnableBatchProcessing//开启批处理的支持
public class CsvBatchConfig {

  @Autowired
  public JdbcTemplate jdbcTemplate;;
	
	@Bean
	public ItemReader<Person> read() {
		FlatFileItemReader<Person> reader = new FlatFileItemReader<Person>();
		reader.setResource(new ClassPathResource("batch/single/person.csv"));

		DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
		delimitedLineTokenizer.setNames(new String[] { //
				"name", //
				"age", //
				"nation", //
				"address" //
		});

		DefaultLineMapper<Person> defaultLineMapper = //
				new DefaultLineMapper<Person>();
		defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);

		BeanWrapperFieldSetMapper<Person> beanWrapperFieldSetMapper = //
				new BeanWrapperFieldSetMapper<Person>();
		beanWrapperFieldSetMapper.setTargetType(Person.class);
		defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);
		
		reader.setLineMapper(defaultLineMapper);
		return reader;
	}
	
	@Bean
	public ItemProcessor<Person, Person> processor() {
		CsvItemProcessor processor = new CsvItemProcessor();
		processor.setValidator(csvBeanValidator());
		return processor;
	}
	
	@Bean
	public ItemWriter<Person> writer(DataSource dataSource) {
		JdbcBatchItemWriter<Person> writer = new JdbcBatchItemWriter<Person>();
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Person>());
		StringBuilder sql = new StringBuilder();
		sql.append("insert into person ");
		sql.append("(uuid, version, name, age, nation, address) ");
		sql.append("values ");
		sql.append("(uuid(), 0, :name, :age, :nation, :address); ");
		writer.setSql(sql.toString());
		writer.setDataSource(dataSource);
		return writer;
	}
	
	@Bean
	public JobRepository jobRepository(DataSource dataSource, //
			PlatformTransactionManager transactionManager) throws Exception {
		JobRepositoryFactoryBean jobRepositoryFactoryBean = //
				new JobRepositoryFactoryBean();
		jobRepositoryFactoryBean.setDataSource(dataSource);
		jobRepositoryFactoryBean.setTransactionManager(transactionManager);
		jobRepositoryFactoryBean.setDatabaseType(DatabaseType.MYSQL.name());
		return jobRepositoryFactoryBean.getObject();
	}
	
	@Bean
	public SimpleJobLauncher jobLauncher(DataSource dataSource, //
			PlatformTransactionManager transactionManager) throws Exception {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(jobRepository(dataSource, transactionManager));
		return jobLauncher;
	}
	
	@Bean
	public Job importJob(JobBuilderFactory jobs, Step step) {
		return jobs.get("importJob") //
				.incrementer(new RunIdIncrementer()) //
				.flow(step) //
				.end() //
				.listener(csvJobListener()) //
				.build();
	}
	
	@Bean
	public Step step(StepBuilderFactory stepBuilderFactory, //
			ItemReader<Person> reader, //
			ItemWriter<Person> writer, //
			ItemProcessor<Person, Person> processor) {
		return stepBuilderFactory.get("step") //
				.<Person, Person>chunk(65000) //
				.reader(reader) //
				.processor(processor) //
				.writer(writer) //
				.build();
	}

	@Bean
	public CsvJobListener csvJobListener() {
		return new CsvJobListener(jdbcTemplate);
	}
	
	@Bean
	public Validator<Person> csvBeanValidator() {
		return new CsvBeanValidator<Person>();
	}
}
