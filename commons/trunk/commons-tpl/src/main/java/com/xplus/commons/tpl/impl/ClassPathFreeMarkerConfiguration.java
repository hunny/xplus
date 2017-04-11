package com.xplus.commons.tpl.impl;

import freemarker.template.Configuration;
import freemarker.template.Version;

/**
 * 依赖当前类路径生成的配置
 * 
 * @author huzexiong
 *
 */
public class ClassPathFreeMarkerConfiguration extends Configuration {

  public ClassPathFreeMarkerConfiguration(Version version, String basePackagePath) {
    super(version);
    this.setClassForTemplateLoading(this.getClass(), basePackagePath);
  }

}
