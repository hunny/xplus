package com.example.bootweb.accessory.api;

/**
 * @param <P>
 *          请求参数
 * @param <R>
 *          结果处理
 */
public interface HttpBuilder<P, R> {

  public static final String METHOD_GET = "GET";
  public static final String METHOD_POST = "GET";

  /**
   * 请求地址
   * 
   * @param uri
   * @return
   */
  HttpBuilder<P, R> uri(String uri);

  /**
   * 请求参数
   * 
   * @param params
   * @return
   */
  HttpBuilder<P, R> params(P params);

  /**
   * 请求参数
   * 
   * @param params
   * @return
   */
  HttpBuilder<P, R> header(P params);
  
  /**
   * 请求方式
   * @param method
   */
  HttpBuilder<P, R> method(String method);

  /**
   * 结果解析器
   * 
   * @param parser
   */
  HttpBuilder<P, R> parser(Parser parser);

  R build();
}
