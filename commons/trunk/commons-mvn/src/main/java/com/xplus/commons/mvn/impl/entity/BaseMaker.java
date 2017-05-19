package com.xplus.commons.mvn.impl.entity;

import com.xplus.commons.mvn.api.Maker;

/**
 * @author huzexiong
 *
 */
public abstract class BaseMaker implements Maker {

  private static final long serialVersionUID = -879201775509440216L;
  
  private String path;

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }
  

}
