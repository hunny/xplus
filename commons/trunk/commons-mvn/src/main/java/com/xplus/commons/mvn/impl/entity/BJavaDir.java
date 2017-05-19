package com.xplus.commons.mvn.impl.entity;

import java.io.File;

/**
 * @author huzexiong
 */
public class BJavaDir extends BaseMaker {

  private static final long serialVersionUID = 7882356442942397786L;
  
  private String src = "src";
  private String dir = "main";
  private String java = "java";
  private String resources = "resources";
  private String metaInf = "META-INF";
  
  public String getSrc() {
    return src;
  }

  public void setSrc(String src) {
    this.src = src;
  }

  public String getDir() {
    return dir;
  }

  public void setDir(String dir) {
    this.dir = dir;
  }

  public String getJava() {
    return java;
  }

  public void setJava(String java) {
    this.java = java;
  }

  public String getResources() {
    return resources;
  }

  public void setResources(String resources) {
    this.resources = resources;
  }

  public String getMetaInf() {
    return metaInf;
  }

  public void setMetaInf(String metaInf) {
    this.metaInf = metaInf;
  }

  public String getDirPath() {
    return String.format("%s%s%s%s%s%s%s", getPath(), File.separator, getSrc(),
        File.separator, getDir(), File.separator, getJava());
  }
  
  public String getResourcePath() {
    return String.format("%s%s%s%s%s%s%s", getPath(), File.separator,
        getSrc(), File.separator, getDir(), File.separator, getResources());
  }
  
  public String getMetaInfPath() {
    return String.format("%s%s%s%s%s%s%s%s%s", getPath(), File.separator,
        getSrc(), File.separator, getDir(), File.separator, getResources(),
        File.separator, getMetaInf());
  }

}
