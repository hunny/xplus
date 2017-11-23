package com.example.bootweb.pg.jdbcdao;

import java.io.Serializable;
import java.math.BigInteger;

public class UuidBean implements Serializable {

  private static final long serialVersionUID = -8211929914843235510L;

  private BigInteger id;
  private String name;

  public BigInteger getId() {
    return id;
  }

  public void setId(BigInteger id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
