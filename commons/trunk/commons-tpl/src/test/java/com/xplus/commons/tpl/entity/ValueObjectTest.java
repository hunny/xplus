package com.xplus.commons.tpl.entity;

import java.io.Serializable;

/**
 * @author huzexiong
 *
 */
public class ValueObjectTest implements Serializable {

  private static final long serialVersionUID = 4953178368580323878L;

  private String id;
  private String name;
  private String value;

  public ValueObjectTest() {
    this(null);
  }

  public ValueObjectTest(String id) {
    this(id, null);
  }

  public ValueObjectTest(String id, String name) {
    this(id, name, null);
  }

  public ValueObjectTest(String id, String name, String value) {
    this.id = id;
    this.name = name;
    this.value = value;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

}
