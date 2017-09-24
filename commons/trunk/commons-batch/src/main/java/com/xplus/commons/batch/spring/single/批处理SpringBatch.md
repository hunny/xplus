# 批处理Spring Batch

[Spring Boot](https://docs.spring.io/spring-boot/docs/)，Spring Batch是用来处理大量数据操作的一个框架，主要用来读取大量数据，然后进行一定处理后输出成指定的形式。

## Spring Batch 组成

| 名称 | 用途 |
| --- | --- |
| JobRepository | 用来注册Job的容器 |
| JobLauncher | 用来启动Job的接口 |
| Job | 要实际执行的任务，包含一个或多个Step |
| Step | Step步骤包含ItemReader、ItemProcessor和ItemWriter |
| ItemReader | 用来读取数据的接口 |
| ItemProcessor | 用来处理数据的接口 |
| ItemWriter | 用来输出数据的接口 |

Spring Batch的主要组成部分只需要注册为Spring的Bean即可。若想开启批处理的支持还需要在配置类上使用 ```@EnableBatchProcessing```。
一个示例程序如下：

```
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.support.DatabaseType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author hunnyhu
 *
 */
@Configuration
@EnableBatchProcessing
public class BatchConfig {

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
	public JobLauncher jobLauncher(DataSource dataSource, //
			PlatformTransactionManager transactionManager) throws Exception {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(jobRepository(dataSource, //
				transactionManager));
		return jobLauncher;
	}
	
	@Bean
	public Job importJob(JobBuilderFactory jobFactory, Step step) {
		return jobFactory.get("importJob") //
//		.incrementer(new RunIdIncrementter())
		.flow(step) //
		.end() //
		.build();
	}
	
	@Bean
	public Step step1(StepBuilderFactory stepBuilderFactory, //
			ItemReader<PersonEntity> reader, //
			ItemWriter<PersonEntity> writer, //
			ItemProcessor<PersonEntity, PersonEntity> processor) {
		return stepBuilderFactory.get("step1") //
				.<PersonEntity, PersonEntity>chunk(65000) //
				.reader(reader) //
				.processor(processor) //
				.writer(writer)
				.build();
	}
	
	@Bean
	public ItemReader<PersonEntity> reader() {
		// 新建ItemReader接口的实现类返回
		return null;
	}
	
	@Bean
	public ItemProcessor<PersonEntity, PersonEntity> processor() {
		// 新建ItemProcessor接口的实现类返回
		return null;
	}
	
	@Bean
	public ItemWriter<PersonEntity> writer(DataSource dataSource) {
		// 新建ItemWriter接口的实现类返回
		return null;
	}

}
```

### Job监听

监听Job执行情况，需要定义一个类实现JobExecutionListener，并在定义Job的Bean上绑定该监听器。

示例代码如下：

```
public class MyJobListener implements JobExecutionListener {

	@Override
	public void beforeJob(JobExecution jobExecution) {
		// Job 开始前
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		// Job 完成后
	}

}
```

### 数据读取

### 数据处理及校验

### 数据输出

### 计划任务

### 参数后置绑定

### Spring Boot的支持

- 源码位置

```
org.springframework.boot.autoconfigure.batch
```

- Spring Boot提供如下属性来定制Spring Batch

```
spring.batch.job.name=job1,job2 #启动时要执行的Job.
spring.batch.job.enabled=true #是否自动执行定义的Job.
spring.batch.initializer.enabled=true #是否初始化Spring Batch的数据库，默认为是.
spring.batch.schema=
spring.batch.table-prefix=BATCH #设置Spring Batch的数据库表的前缀.

```




