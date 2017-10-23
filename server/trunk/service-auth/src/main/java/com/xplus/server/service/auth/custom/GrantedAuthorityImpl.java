package com.xplus.server.service.auth.custom;

import org.springframework.security.core.GrantedAuthority;

/**
 * 权限。
 *
 */
public class GrantedAuthorityImpl implements GrantedAuthority {

  private static final long serialVersionUID = -3642792395006458943L;

  private String authority;

  public GrantedAuthorityImpl(String authority) {
    this.authority = authority;
  }

  public void setAuthority(String authority) {
    this.authority = authority;
  }

  @Override
  public String getAuthority() {
    return this.authority;
  }

}
