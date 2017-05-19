/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	commons-mvn
 * 文件名：	Log4j.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月19日 - huzexiong - 创建。
 */
package com.xplus.commons.mvn.impl.entity;

import org.springframework.util.StringUtils;

/**
 * @author huzexiong
 *
 */
public class BLog4j extends BaseMaker {

  private static final long serialVersionUID = 4963412124963034050L;

  private String log4jPath = null;
  private String log4jName = null;
  private String packageName = null;
  private BJavaDir javaDir = null;
  private BJavaTestDir javaTestDir = null;

  public String getLog4jPath() {
    return log4jPath;
  }

  public void setLog4jPath(String log4jPath) {
    this.log4jPath = log4jPath;
  }

  public String getLog4jName() {
    return log4jName;
  }

  public void setLog4jName(String log4jName) {
    this.log4jName = log4jName;
  }

  public String getPackageName() {
    return packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public BJavaDir getJavaDir() {
    return javaDir;
  }

  public void setJavaDir(BJavaDir javaDir) {
    this.javaDir = javaDir;
  }

  public BJavaTestDir getJavaTestDir() {
    return javaTestDir;
  }

  public void setJavaTestDir(BJavaTestDir javaTestDir) {
    this.javaTestDir = javaTestDir;
  }
  
  public String getLoggerPath() {
    if (StringUtils.isEmpty(getLog4jPath())) {
      return getLog4jName();
    }
    return String.format("%s/%s", getLog4jPath(), getLog4jName());
  }

}
