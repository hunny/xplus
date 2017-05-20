package com.xplus.commons.topease;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 应用程序主入口
 * 
 * @author huzexiong
 *
 */
public class Application {

	public static void main(String[] args) throws Exception {
		loadFromXml(args);
	}

	public static void loadFromXml(String[] args) throws Exception {
		System.setProperty("java.awt.headless", "false");
		ConfigurableApplicationContext context = SpringApplication
				.run(new Object[] { "classpath:/META-INF/commons-topease/commons-topease.xml" }, args);
		SwingApp appFrame = context.getBean(SwingApp.class);
		appFrame.run(args);
	}

	public static void loadFromBuilder(String[] args) throws Exception {
		ConfigurableApplicationContext context = new SpringApplicationBuilder(Application.class).headless(false).run(args);
		SwingApp appFrame = context.getBean(SwingApp.class);
		appFrame.run(args);	}

}
