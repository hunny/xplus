package com.xplus.commons.mvn.impl;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.xplus.commons.mvn.api.DirMaker;

/**
 * 生成Java工程的文件目录结构
 * 
 * @author huzexiong
 *
 */
@Component
public class JavaDirMaker implements DirMaker {

  private static final Logger logger = LoggerFactory.getLogger(JavaDirMaker.class);

  public static final String SRC_FOLDER_NAME = "src";
  public static final String MAIN_FOLDER_NAME = "main";
  public static final String TEST_FOLDER_NAME = "test";
  public static final String JAVA_FOLDER_NAME = "java";
  public static final String RESOURCE_FOLDER_NAME = "resources";
  public static final String META_INF = "META-INF";

  private String srcFolderName = SRC_FOLDER_NAME;
  private String mainFolderName = MAIN_FOLDER_NAME;
  private String testFolderName = TEST_FOLDER_NAME;
  private String javaFolderName = JAVA_FOLDER_NAME;
  private String resourceFolderName = RESOURCE_FOLDER_NAME;

  @Override
  public void make(String path) {
    makeMainDir(path);
    makeTestDir(path);
  }

  protected boolean makeMainDir(String path) {
    return makDirs(path, getMainFolderName());
  }

  protected boolean makeTestDir(String path) {
    return makDirs(path, getTestFolderName());
  }

  protected boolean makDirs(String path, String folder) {
    String javaNamePath = String.format("%s%s%s%s%s%s%s", path, File.separator, getSrcFolderName(),
        File.separator, folder, File.separator, getJavaFolderName());
    logger.debug("Making {} java dir: {}", folder, javaNamePath);
    String resourceNamePath = String.format("%s%s%s%s%s%s%s", path, File.separator,
        getSrcFolderName(), File.separator, folder, File.separator, getResourceFolderName());
    logger.debug("Making {} resource dir: {}", folder, resourceNamePath);
    String metaInfPath = String.format("%s%s%s%s%s%s%s%s%s", path, File.separator,
        getSrcFolderName(), File.separator, folder, File.separator, getResourceFolderName(),
        File.separator, META_INF);
    logger.debug("Making {} resource meta dir: {}", folder, metaInfPath);
    return new File(javaNamePath).mkdirs() && new File(resourceNamePath).mkdirs()
        && new File(metaInfPath).mkdirs();
  }

  public String getSrcFolderName() {
    return srcFolderName;
  }

  public void setSrcFolderName(String srcFolderName) {
    this.srcFolderName = srcFolderName;
  }

  public String getMainFolderName() {
    return mainFolderName;
  }

  public void setMainFolderName(String mainFolderName) {
    this.mainFolderName = mainFolderName;
  }

  public String getTestFolderName() {
    return testFolderName;
  }

  public void setTestFolderName(String testFolderName) {
    this.testFolderName = testFolderName;
  }

  public String getJavaFolderName() {
    return javaFolderName;
  }

  public void setJavaFolderName(String javaFolderName) {
    this.javaFolderName = javaFolderName;
  }

  public String getResourceFolderName() {
    return resourceFolderName;
  }

  public void setResourceFolderName(String resourceFolderName) {
    this.resourceFolderName = resourceFolderName;
  }

}
