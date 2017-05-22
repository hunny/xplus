package com.xplus.commons.topease.api.service.phantomjs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.xplus.commons.topease.api.service.Cookies;

public class Phantomjs implements Serializable {

  private static final long serialVersionUID = -3959578553876595803L;

  /** 请求的地址 */
  private String url;
  /** 请求的cookies值 */
  private List<Cookies> cookies = new ArrayList<Cookies>();
  /** 截屏存放路径 */
  private String screenCapturePath;
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }
  public List<Cookies> getCookies() {
    return cookies;
  }
  public void setCookies(List<Cookies> cookies) {
    this.cookies.clear();
    if (null != cookies && !cookies.isEmpty()) {
      this.cookies.addAll(cookies);
    }
  }
  public String getScreenCapturePath() {
    return screenCapturePath;
  }
  public void setScreenCapturePath(String screenCapturePath) {
    this.screenCapturePath = screenCapturePath;
  }

}
