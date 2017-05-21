package com.xplus.commons.tpl.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.xplus.commons.tpl.api.TemplateMaker;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 使用FreeMarker来实现读取模板
 * 
 * @author huzexiong
 *
 */
public class FreeMarkerTemplateMaker implements TemplateMaker {

  private final Logger logger = LoggerFactory.getLogger(FreeMarkerTemplateMaker.class);

  @Value("${commons-ftl.file.forceOverWrite:false}")
  private boolean forceOverWrite = false;

  public boolean isForceOverWrite() {
		return forceOverWrite;
	}

	public void setForceOverWrite(boolean forceOverWrite) {
		this.forceOverWrite = forceOverWrite;
	}

	private Configuration configuration = null;

  public Configuration getConfiguration() {
    return configuration;
  }

  public void setConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }

  @Override
  public void make(Object model, String src, String dest) {
    Writer fileWriter = null;
    try {
      Template template = configuration.getTemplate(src);
      File file = new File(dest);
      if (file.exists() && !forceOverWrite) {
        throw new RuntimeException(String.format("File %s in path %s create has existed.",
            file.getName(), file.getAbsolutePath()));
      }
      fileWriter = new FileWriter(file);
      template.process(model, fileWriter);
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw new RuntimeException(e);
    } finally {
      try {
        if (null != fileWriter) {
          fileWriter.close();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

}
