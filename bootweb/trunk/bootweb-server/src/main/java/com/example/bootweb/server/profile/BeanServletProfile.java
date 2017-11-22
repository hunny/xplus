package com.example.bootweb.server.profile;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Profile;

/**
 * 使用SpringBoot Bean的形式加载使用Servlet. 使用方式：spring.profiles.active=BeanServlet
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Profile("BeanServlet")
public @interface BeanServletProfile {
  // Do Nothing.
}
