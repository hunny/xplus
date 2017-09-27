Quick SpringBoot

## 什么是SpringBoot

- 摘自官网

```
Takes an opinionated view of building production-ready Spring applications. Spring Boot favors convention over configuration and is designed to get you up and running as quickly as possible.
```

- 翻译

```
采纳了建立生产就绪Spring应用程序的观点。 Spring Boot优先于配置的惯例，旨在让您尽快启动和运行。
```

- Spring Boot致力于简洁，让开发者写更少的配置，程序能够更快的运行和启动。它是下一代javaweb框架，并且它是spring cloud（微服务）的基础。

## 搭建第一个Sping Boot 程序

- 通过在start.spring.io上建项目

- 通过idea构建
	+ 引入依赖包
	```
	  <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-dependencies</artifactId>
        <version>${spring-boot.version}</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
	```
	+ 或以parent的方式引入
	```	  
	  <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-dependencies</artifactId>
        <version>${spring-boot.version}</version>
      </parent>
	```
	+ 创建Application应用程序入口类
	```
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QuickSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuickSpringBootApplication.class, args);
	}

}
	```

[FeignClient 代码示例](http://blog.csdn.net/w_x_z_/article/details/71310035)
[SpringCloud 声明式REST客户端Feign](http://blog.csdn.net/w_x_z_/article/details/53327183)
[Feign正确的使用姿势和性能优化注意事项](http://www.jianshu.com/p/191d45210d16)
[史上最简单的 SpringCloud 教程 | 第一篇： 服务的注册与发现（Eureka）](http://blog.csdn.net/forezp/article/details/69696915)
[通过微服务进行分布式应用开发](http://blog.csdn.net/u012562943/article/details/52807358)
[SpringCloudLearning](https://github.com/forezp/SpringCloudLearning)

