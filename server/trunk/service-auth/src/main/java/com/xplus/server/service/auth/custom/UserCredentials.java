package com.xplus.server.service.auth.custom;

import java.io.Serializable;

/**
 * 身份认证
 */
public class UserCredentials implements Serializable {

  private static final long serialVersionUID = -928094736920779643L;

  private String username;
  
  private String password;
  
  public String getUsername() {
    return username;
  }
  
  public void setUsername(String username) {
    this.username = username;
  }
  
  public String getPassword() {
    return password;
  }
  
  public void setPassword(String password) {
    this.password = password;
  }

}
