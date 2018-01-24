package com.example.bootweb.accessory.druid;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * 是否启用及启用DruidServet的条件。
 * <p>
 * 1. 配置<code>spring.druid.servlet.password</code>密码值。
 * <p>
 * 2. 启用了{@link DruidConfig}。
 * 
 */
@Target({
    ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ConditionalOnProperty(//
    prefix = "spring.druid.servlet", //
    name = "password", //
    matchIfMissing = false //
) //
@ConditionalOnBean(DruidConfigValue.class)
public @interface ConditionDruidServlet {
  // Do Nothing.
}
