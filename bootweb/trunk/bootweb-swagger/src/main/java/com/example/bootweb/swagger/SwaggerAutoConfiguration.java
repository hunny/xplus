package com.example.bootweb.swagger;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public SwaggerProperty swaggerProperties() {
    return new SwaggerProperty();
  }

  @Bean
  @ConditionalOnMissingBean
  public Docket createRestApi(SwaggerProperty swaggerProperty) {
    ApiInfo apiInfo = new ApiInfoBuilder() //
        .title(swaggerProperty.getTitle()) //
        .description(swaggerProperty.getDescription()) //
        .version(swaggerProperty.getVersion()) //
        .license(swaggerProperty.getLicense()) //
        .licenseUrl(swaggerProperty.getLicenseUrl()) //
        .contact(new Contact( //
            swaggerProperty.getContact().getName(), //
            swaggerProperty.getContact().getUrl(), //
            swaggerProperty.getContact().getEmail() //
        )) //
        .termsOfServiceUrl(swaggerProperty.getTermsOfServiceUrl()) //
        .build();

    // base-path处理
    // 当没有配置任何path的时候，解析/**
    if (swaggerProperty.getBasePath().isEmpty()) {
      swaggerProperty.getBasePath().add("/**");
    }
    List<Predicate<String>> basePath = new ArrayList();
    for (String path : swaggerProperty.getBasePath()) {
      basePath.add(PathSelectors.ant(path));
    }

    // exclude-path处理
    List<Predicate<String>> excludePath = new ArrayList();
    for (String path : swaggerProperty.getExcludePath()) {
      excludePath.add(PathSelectors.ant(path));
    }

    return new Docket(DocumentationType.SWAGGER_2) //
        .apiInfo(apiInfo) //
        .select() //
        .apis(RequestHandlerSelectors.basePackage(swaggerProperty.getBasePackage())) //
        .paths(Predicates.and(Predicates.not(Predicates.or(excludePath)), //
            Predicates.or(basePath))) //
        .build();
  }

}
