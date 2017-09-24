package com.xplus.commons.batch.spring.batchintro;

import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class Batch {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Job job( //
//			JobBuilderFactory jobBuilderFactory, //
//			StepBuilderFactory stepBuilderFactory, //
			Step1 step1, //
			Step2 step2) throws Exception {

		Step s1 = stepBuilderFactory.get("file-db") //
				.<Student, Student>chunk(100) //
				.reader(step1.fileReader(null)) //
				.writer(step1.jdbcWriter(null)) //
				.build();

		Step s2 = stepBuilderFactory.get("db-file") //
				.<Map<Integer, Integer>, Map<Integer, Integer>>chunk(100) //
				.reader(step2.jdbcReader(null)) //
				.writer(step2.fileWriter(null)) //
				.build();

		return jobBuilderFactory.get("etl") //
				.incrementer(new RunIdIncrementer()) //
				.start(s1) //
				.next(s2) //
				.build();

	}
}
