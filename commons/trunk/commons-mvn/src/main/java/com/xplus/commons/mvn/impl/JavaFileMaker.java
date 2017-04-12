package com.xplus.commons.mvn.impl;

import java.io.File;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.xplus.commons.mvn.api.FileMaker;
import com.xplus.commons.tpl.api.TemplateMaker;

/**
 * 生成Java工程文件
 * 
 * @author huzexiong
 *
 */
@Component
public class JavaFileMaker implements FileMaker {

  private final Logger logger = LoggerFactory.getLogger(JavaFileMaker.class);

  @Resource(name = "commons-tpl.freeMarkerTemplateMaker")
  private TemplateMaker templateMaker;

  /**
   * 模板的相对路径
   */
  @Value("${commons-mvn.tpl.path:META-INF/commons-mvn/ftl}")
  private String tplPath;

  @Override
  public void make(Map<String, Object> object, String path) {
    logger.debug("Java File Maker Begin...");
    Map<String, Object> pom = (Map<String, Object>) object.get("pom");
    makePom(pom, path);
    Map<String, Object> log4j = (Map<String, Object>) object.get("log4j");
    makeLog4j(log4j, path);
    makeMain(pom, path);
    makeTest(pom, path);
  }

  protected void makePom(Map<String, Object> object, String path) {
    logger.debug("pom.xml File Maker Begin...");
    if (null == object || object.isEmpty()) {
      logger.debug("pom info not found, ignore test folder files.");
      return;
    }
    templateMaker.make(object, String.format("%s/%s", tplPath, "pom.ftl"),
        String.format("%s/%s", path, "pom.xml"));
  }

  protected void makeLog4j(Map<String, Object> object, String path) {
    logger.debug("log4j.properties File Maker Begin...");
    if (null == object || object.isEmpty()) {
      logger.debug("log4j info not found, ignore test folder files.");
      return;
    }
    templateMaker.make(object, String.format("%s/%s", tplPath, "log4j.ftl"),
        String.format("%s/%s/%s", path, getSrcTestResources(), "log4j.properties"));
  }

  protected void makeMain(Map<String, Object> object, String path) {
    logger.debug("test folder File Maker Begin...");
    if (null == object || object.isEmpty()) {
      logger.debug("pom info not found, ignore test folder files.");
      return;
    }
    String artifactId = (String) object.get("artifactId");
    String xmlPath = String.format("%s/%s/%s/%s", path, getSrcMainResources(),
        JavaDirMaker.META_INF, artifactId);
    new File(xmlPath).mkdirs();
    templateMaker.make(object, String.format("%s/%s", tplPath, "test.xml.ftl"),
        String.format("%s/%s", xmlPath, String.format("%s-%s", artifactId, ".xml")));
    templateMaker.make(object, String.format("%s/%s", tplPath, "properties.ftl"), String.format(
        "%s/%s/%s", path, getSrcMainResources(), String.format("%s%s", artifactId, ".properties")));
  }

  protected void makeTest(Map<String, Object> object, String path) {
    logger.debug("test folder File Maker Begin...");
    if (null == object || object.isEmpty()) {
      logger.debug("pom info not found, ignore test folder files.");
      return;
    }
    String artifactId = (String) object.get("artifactId");
    templateMaker.make(object, String.format("%s/%s", tplPath, "test.xml.ftl"), String.format(
        "%s/%s/%s", path, getSrcTestResources(), String.format("%s-%s", artifactId, "test.xml")));
    templateMaker.make(object, String.format("%s/%s", tplPath, "properties.ftl"),
        String.format("%s/%s/%s", path, getSrcTestResources(),
            String.format("%s-%s", artifactId, "test.properties")));
  }

  protected String getSrcMainJava() {
    return String.format("%s/%s/%s", JavaDirMaker.SRC_FOLDER_NAME, JavaDirMaker.MAIN_FOLDER_NAME,
        JavaDirMaker.JAVA_FOLDER_NAME);
  }

  protected String getSrcMainResources() {
    return String.format("%s/%s/%s", JavaDirMaker.SRC_FOLDER_NAME, JavaDirMaker.MAIN_FOLDER_NAME,
        JavaDirMaker.RESOURCE_FOLDER_NAME);
  }

  protected String getSrcTestJava() {
    return String.format("%s/%s/%s", JavaDirMaker.SRC_FOLDER_NAME, JavaDirMaker.TEST_FOLDER_NAME,
        JavaDirMaker.JAVA_FOLDER_NAME);
  }

  protected String getSrcTestResources() {
    return String.format("%s/%s/%s", JavaDirMaker.SRC_FOLDER_NAME, JavaDirMaker.TEST_FOLDER_NAME,
        JavaDirMaker.RESOURCE_FOLDER_NAME);
  }

  protected void makeMeta(Map<String, Object> object, String path) {
    logger.debug("meta folder File Maker Begin...");
    if (null == object || object.isEmpty()) {
      logger.debug("pom info not found, ignore test folder files.");
      return;
    }
    String artifactId = (String) object.get("artifactId");
    String metaDir = String.format("%s/%s/%s%s", path, getSrcMainResources(), JavaDirMaker.META_INF,
        artifactId);
    templateMaker.make(object, String.format("%s/%s", tplPath, "meta.xml.ftl"),
        String.format("%s/%s.xml", metaDir, artifactId));
    templateMaker.make(object, String.format("%s/%s", tplPath, "properties.ftl"), String.format(
        "%s/%s/%s", path, getSrcMainResources(), String.format("%s%s", artifactId, ".properties")));
  }

}
