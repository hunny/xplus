package com.xplus.commons.tpl.impl;

import java.io.File;
import java.io.IOException;

import freemarker.template.Configuration;
import freemarker.template.Version;

/**
 * 依赖当前类路径生成的配置
 * 
 * @author huzexiong
 *
 */
public class FilePathFreeMarkerConfiguration extends Configuration {

  public FilePathFreeMarkerConfiguration(Version version, String directoryForTemplateLoading) {
    super(version);
    try {
      this.setDirectoryForTemplateLoading(new File(directoryForTemplateLoading));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
