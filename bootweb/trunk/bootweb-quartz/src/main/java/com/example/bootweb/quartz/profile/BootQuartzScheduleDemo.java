package com.example.bootweb.quartz.profile;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Profile;

/**
 * 使用SpringBoot+Quartz调用任务。
 * 运行时配置<code>spring.profiles.active=BootQuartzScheduleDemo</code>时使用。
 */
@Target({
    ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Profile("BootQuartzScheduleDemo")
public @interface BootQuartzScheduleDemo {
  // Do Nothing
}
