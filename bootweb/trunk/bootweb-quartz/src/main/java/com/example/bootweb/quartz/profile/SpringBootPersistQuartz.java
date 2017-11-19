package com.example.bootweb.quartz.profile;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Profile;

/**
 * 演示Quartz持久化数据到H2数据库。
 * 运行时配置<code>spring.profiles.active=SpringBootPersistQuartz</code>时使用。
 */
@Target({
    ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Profile("SpringBootPersistQuartz")
public @interface SpringBootPersistQuartz {

}
