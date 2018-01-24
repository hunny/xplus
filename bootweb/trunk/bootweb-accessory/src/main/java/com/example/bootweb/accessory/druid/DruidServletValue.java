package com.example.bootweb.accessory.druid;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "spring.druid.servlet")
@Component
@ConditionDruidServlet
public class DruidServletValue {

  private String username = "admin";
  private String password;
  private String urlMappings = "/druid/*";
  private String allow = "127.0.0.1";
  private String deny = "";
  private String resetEnable = "false";
  private String exclusions = "*.js,*.gif,*.jpg,*.png,*.css,*.ico" + "," + urlMappings;

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

  public String getUrlMappings() {
    return urlMappings;
  }

  public void setUrlMappings(String urlMappings) {
    this.urlMappings = urlMappings;
  }

  public String getAllow() {
    return allow;
  }

  public void setAllow(String allow) {
    this.allow = allow;
  }

  public String getDeny() {
    return deny;
  }

  public void setDeny(String deny) {
    this.deny = deny;
  }

  public String getResetEnable() {
    return resetEnable;
  }

  public void setResetEnable(String resetEnable) {
    this.resetEnable = resetEnable;
  }

  public String getExclusions() {
    return exclusions;
  }

  public void setExclusions(String exclusions) {
    this.exclusions = exclusions;
  }

}
