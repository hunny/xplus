package com.xplus.commons.mvn.impl.entity;

/**
 * @author huzexiong
 *
 */
public class BProject extends BaseMaker {

  private static final long serialVersionUID = 6912091966639285294L;
  
  private String name = null;
  private String description = null;
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  

}
