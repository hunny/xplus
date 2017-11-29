package com.example.bootweb.markdown.web;

import java.io.Serializable;

public class FileBean implements Serializable {

  private static final long serialVersionUID = -6480308237115290581L;

  private String name;
  private String path;

  public FileBean(String name, String path) {
    this.name = name;
    this.path = path;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

}
