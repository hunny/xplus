package com.example.bootweb.markdown;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

  public static void main(String[] args) throws FileNotFoundException, IOException {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public MultipartConfigElement multipartConfigElement() {
    MultipartConfigFactory factory = new MultipartConfigFactory();
    //// 设置文件大小限制 ,超了，页面会抛出异常信息，这时候就需要进行异常信息的处理了;
    factory.setMaxFileSize("128KB"); // KB,MB
    /// 设置总上传数据总大小
    factory.setMaxRequestSize("256KB");
    // Sets the directory location wherefiles will be stored.
    // factory.setLocation("路径地址");
    return factory.createMultipartConfig();
  }

}
