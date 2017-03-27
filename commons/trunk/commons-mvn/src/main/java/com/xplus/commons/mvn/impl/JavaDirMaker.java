/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	commons-mvn
 * 文件名：	JavaDirMaker.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月27日 - huzexiong - 创建。
 */
package com.xplus.commons.mvn.impl;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xplus.commons.mvn.api.DirMaker;

/**
 * @author huzexiong
 *
 */
public class JavaDirMaker implements DirMaker {

  private static final Logger logger = LoggerFactory.getLogger(JavaDirMaker.class);

  private String srcFolderName = "src";
  private String mainFolderName = "main";
  private String testFolderName = "test";
  private String javaFolderName = "java";
  private String resourceFolderName = "resources";

  private static void debug(String format, Object... arguments) {
    if (logger.isDebugEnabled()) {
      logger.debug(format, arguments);
    }
  }

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
    String javaNamePath = String.format("%s%s%s%s%s%s%s", path, File.separator, getSrcFolderName(), File.separator, folder, File.separator,
        getJavaFolderName());
    debug("Making {} java dir: {}", folder, javaNamePath);
    String resourceNamePath = String.format("%s%s%s%s%s%s%s", path, File.separator, getSrcFolderName(), File.separator, folder,
        File.separator, getResourceFolderName());
    debug("Making {} resource dir: {}", folder, resourceNamePath);
    String metaInfPath = String.format("%s%s%s%s%s%s%s%s%s", path, File.separator, getSrcFolderName(), File.separator, folder, File.separator,
        getResourceFolderName(), File.separator, "META-INF");
    debug("Making {} resource meta dir: {}", folder, metaInfPath);
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
