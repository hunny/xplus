package com.xplus.commons.spring.basic.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

@Configuration
@ComponentScan("com.xplus.commons.spring.basic.resource")
@PropertySource("classpath:com/xplus/commons/spring/basic/resource/eltest.properties") //注入配置文件。
public class ElConfig {

	@Value("注入普通字符串。") //注入普通字符串。
	private String normal;
	
	@Value("#{systemProperties['os.name']}")// 注入操作系统属性。
	private String osName;
	
	@Value("#{ T(java.lang.Math).random() * 100.0 }")// 注入表达式结果。
	private double randomNumber;
	
	@Value("#{demoService.another}") //注入其它Bean属性。
	private String fromAnother;
	
	@Value("classpath:com/xplus/commons/spring/basic/resource/eltest.properties") //注入文件资源。
	private Resource testFile;
	
	@Value("http://www.baidu.com") // 注入网址资源。
	private Resource testUrl;
	
	@Value("${book.name}") // 注入配置文件。
	private String bookName;
	
	@Autowired // 注入配置文件。
	private Environment environment;
	
	@Bean //注入配置文件
	public static PropertySourcesPlaceholderConfigurer propertyConfigure() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	public void outputResources() {
		System.out.println("normal:" + normal);
		System.out.println("osName:" + osName);
		System.out.println("randomNumber:" + randomNumber);
		System.out.println("fromAnother:" + fromAnother);
		System.out.println("testFile:" + testFile);
		System.out.println("testUrl:" + testUrl);
//		System.out.println("testFile:" + IOUtils.toString(testFile.getInputStream()));
//		System.out.println("testUrl:" + IOUtils.toString(testUrl.getInputStream()));
		System.out.println("bookName:" + bookName);
		System.out.println("environment:" + environment.getProperty("book.author"));
	}
	
}
