package com.xplus.commons.topease.api.service;

import java.io.Serializable;

public class Cookies implements Serializable {

  private static final long serialVersionUID = -7482724104397413346L;

  private String name;
  private String value;
  private String domain;
  private String path;
  private boolean httponly = true;
  private boolean secure = false;
  private long expires;

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

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public boolean isHttponly() {
    return httponly;
  }

  public void setHttponly(boolean httponly) {
    this.httponly = httponly;
  }

  public boolean isSecure() {
    return secure;
  }

  public void setSecure(boolean secure) {
    this.secure = secure;
  }

  public long getExpires() {
    return expires;
  }

  public void setExpires(long expires) {
    this.expires = expires;
  }

}
