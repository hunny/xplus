package com.xplus.commons.pattern.creational.builder.case1;

import java.io.Serializable;

/**
 * Product（产品角色）
 */
public class ProgrammerProduct implements Serializable {

  private static final long serialVersionUID = 4176715714983515507L;

  private boolean cplus;
  private boolean python;
  private boolean java;
  private boolean c;
  private boolean kotlin;

  public boolean isCplus() {
    return cplus;
  }

  public void setCplus(boolean cplus) {
    this.cplus = cplus;
  }

  public boolean isPython() {
    return python;
  }

  public void setPython(boolean python) {
    this.python = python;
  }

  public boolean isJava() {
    return java;
  }

  public void setJava(boolean java) {
    this.java = java;
  }

  public boolean isC() {
    return c;
  }

  public void setC(boolean c) {
    this.c = c;
  }

  public boolean isKotlin() {
    return kotlin;
  }

  public void setKotlin(boolean kotlin) {
    this.kotlin = kotlin;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Programmer [cplus=");
    builder.append(cplus);
    builder.append(", python=");
    builder.append(python);
    builder.append(", java=");
    builder.append(java);
    builder.append(", c=");
    builder.append(c);
    builder.append(", kotlin=");
    builder.append(kotlin);
    builder.append("]");
    return builder.toString();
  }

}
