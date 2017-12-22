package com.example.bootweb.accessory.api;

/**
 * 可浏览的信息接口
 */
public interface Http<T> {
  
  /**
   * 使用请求地址获取可浏览的对象。
   * @param url
   * @return
   */
  T get(String url);
  
}
