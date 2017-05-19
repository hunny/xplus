package com.xplus.commons.mvn.impl.entity;

/**
 * ClassPath POJO 对象
 * 
 * @author huzexiong
 *
 */
public class BClassPath extends BaseMaker {

  private static final long serialVersionUID = 8951462368787888752L;
  
  /**
   * ClassPath中的J2SE Name
   * 
   * <p>
   * 例如：JavaSE-1.7
   * </p>
   */
  private String j2seName = "JavaSE-1.7";

  public String getJ2seName() {
    return j2seName;
  }

  public void setJ2seName(String j2seName) {
    this.j2seName = j2seName;
  }
  

}
