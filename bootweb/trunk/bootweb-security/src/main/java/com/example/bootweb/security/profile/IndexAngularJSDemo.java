package com.example.bootweb.security.profile;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Profile;

/**
 * 演示使用SpringSecurity CSRF Index配合AngularJS。
 * 
 * 运行时配置<code>spring.profiles.active=IndexAngularJS</code>时使用。
 */
@Target({
    ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Profile("IndexAngularJS")
public @interface IndexAngularJSDemo {

}
