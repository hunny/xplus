package com.xplus.commons.mvn.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.xplus.commons.mvn.api.FileMaker;
import com.xplus.commons.tpl.api.TemplateMaker;

/**
 * 创建eclipse的classpath和project文件
 * 
 * @author huzexiong
 *
 */
@Component(EclipseFileMaker.BEAN_ID)
public class EclipseFileMaker implements FileMaker {

  public static final String BEAN_ID = "commons-mvn.eclipseFileMaker";

  private final Logger logger = LoggerFactory.getLogger(EclipseFileMaker.class);

  @Resource(name = "commons-tpl.freeMarkerTemplateMaker")
  private TemplateMaker templateMaker;

  /**
   * 模板的相对路径
   */
  @Value("${commons-mvn.tpl.path:META-INF/commons-mvn/ftl}")
  private String tplPath;

  @Override
  public void make(Map<String, Object> object, String path) {
    logger.debug("Ready for creating eclipse classpath and project files.");
    if (null == object || object.isEmpty() || ObjectUtils.isEmpty(object.get("eclipse"))) {
      logger.debug("eclipse info not found, ignore files.");
      return;
    }
    Map<String, Object> eclipse = (Map<String, Object>) object.get("eclipse");
    makeClasspath(eclipse, path);
    makeProject(eclipse, path);
  }

  protected void makeClasspath(Map<String, Object> object, String path) {
    logger.debug(".classpath File Maker Begin...");
    if (null == object || object.isEmpty() || ObjectUtils.isEmpty(object.get("j2seName"))) {
      logger.debug("classpath info not found, ignore files.");
      return;
    }
    templateMaker.make(object, String.format("%s/%s", tplPath, "classpath.ftl"),
        String.format("%s/%s", path, ".classpath"));
  }

  protected void makeProject(Map<String, Object> object, String path) {
    logger.debug(".project File Maker Begin...");
    if (null == object || object.isEmpty() || ObjectUtils.isEmpty(object.get("projectName"))) {
      logger.debug("project info not found, ignore files.");
      return;
    }
    templateMaker.make(object, String.format("%s/%s", tplPath, "project.ftl"),
        String.format("%s/%s", path, ".project"));
  }

  protected void makeGitignore(Map<String, Object> object, String path) {
    logger.debug(".gitignore File Maker Begin...");
    templateMaker.make(object, String.format("%s/%s", tplPath, "gitignore.ftl"),
        String.format("%s/%s", path, ".gitignore"));
  }
}
