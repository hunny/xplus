package com.example.bootweb.translate.profile;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Profile;

/**
 * 使用Google Translate API基本演示。
 * 运行时配置<code>spring.profiles.active=TranslateBasicDemo</code>时使用。
 */
@Target({
    ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Profile("TranslateBasicDemo")
public @interface TranslateBasicDemo {
  // Do Nothing
}
