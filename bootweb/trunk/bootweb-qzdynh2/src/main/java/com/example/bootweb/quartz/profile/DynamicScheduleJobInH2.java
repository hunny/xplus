package com.example.bootweb.quartz.profile;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Profile;

/**
 * 使用H2数据库配置表达式动态调用任务。
 * 运行时配置<code>spring.profiles.active=DynamicScheduleJobInH2</code>时使用。
 */
@Target({
    ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Profile("DynamicScheduleJobInH2")
public @interface DynamicScheduleJobInH2 {
  // Do Nothing
}
