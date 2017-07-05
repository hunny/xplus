package com.xplus.commons.cmdb.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author huzexiong
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
  
  /**
   * 访问地址：http://localhost:8080/swagger-ui.html
   */
  @Bean
  public Docket productApi() {
    return new Docket(DocumentationType.SWAGGER_2) //
        .select() //
        .apis(RequestHandlerSelectors.basePackage("com.xplus.commons.cmdb.web")) //
        .paths(PathSelectors.regex("/stack.*")) //
        .build() //
        .apiInfo(metaData()); //

  }

  private ApiInfo metaData() {
    ApiInfo apiInfo = new ApiInfo("Spring Boot REST API", //
        "Spring Boot REST API for Online Store", //
        "1.0", //
        "Terms of service", //
        new Contact("Hu Zexiong", "http://localhost:8080/stack/about/",
            "huxiong888@163.com"), //
        "Apache License Version 2.0", //
        "https://www.apache.org/licenses/LICENSE-2.0");
    return apiInfo;
  }
}
