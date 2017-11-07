# SpringBoot整合Swagger2

## 官网

http://swagger.io/

## 添加依赖

```
  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    <java.version>1.8</java.version>
    <swagger2.version>2.7.0</swagger2.version>
  </properties>
```

## Swagger配置类

> 用`@Configuration`注解该类，等同于XML中配置beans；用`@Bean`标注方法等价于XML中配置bean。

```
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class Swagger2Config {

  @Bean
  public Docket createRestApi() {
    return new Docket(DocumentationType.SWAGGER_2) //
        .apiInfo(apiInfo()) //
        .select() //
        .apis(RequestHandlerSelectors.basePackage("com.example.demo.web")) //
        .paths(PathSelectors.any()) //
        .build(); //
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder() //
        .title("springboot利用swagger构建api文档") //
        .description("简单优雅的restfun风格") //
        .termsOfServiceUrl("https://www.google.com") //
        .version("1.0") //
        .build();
  }
}
```

## 开启Swagger

Application加上注解`@EnableSwagger2` 表示开启Swagger

```
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
```

## 添加Restful 接口

## Swagger2文档

启动SpringBoot项目，访问 http://localhost:8080/swagger-ui.html

## Swagger注解

swagger通过注解表明该接口会生成文档，包括接口名、请求方法、参数、返回信息的等等。

| 名称 | 说明 |
| --- | --- |
| @Api | 修饰整个类，描述Controller的作用 |
| @ApiOperation | 描述一个类的一个方法，或者说一个接口 |
| @ApiParam | 单个参数描述 |
| @ApiModel | 用对象来接收参数 |
| @ApiProperty | 用对象接收参数时，描述对象的一个字段 |
| @ApiResponse | HTTP响应其中1个描述 |
| @ApiResponses | HTTP响应整体描述 |
| @ApiIgnore | 使用该注解忽略这个API |
| @ApiError  | 发生错误返回的信息 |
| @ApiImplicitParam | 一个请求参数 |
| @ApiImplicitParams | 多个请求参数 |
