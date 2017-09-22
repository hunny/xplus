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

