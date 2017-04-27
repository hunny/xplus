package com.xplus.commons.http.api;

/**
 * Http请求接口
 * 
 * @author huzexiong
 *
 */
public interface HttpText {
  
  void http(String url, HttpTextHandler handler, String chartset);
  
}
