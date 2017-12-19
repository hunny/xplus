package com.example.bootweb.accessory.httpclient;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.protocol.HttpContext;

/**
 * HttpClient的重试处理机制
 */
public class HttpRetryHandler implements HttpRequestRetryHandler {

  private int retryCount;
  
  public HttpRetryHandler(int retryCount) {
    this.retryCount = retryCount;
  }
  
  @Override
  public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
    // Do not retry if over max retry count,如果重试次数超过了retryTime,则不再重试请求
    if (executionCount >= retryCount) {
      return false;
    }
    // 服务端断掉客户端的连接异常
    if (exception instanceof NoHttpResponseException) {
      return true;
    }
    // time out 超时重试
    if (exception instanceof InterruptedIOException) {
      return true;
    }
    // Unknown host
    if (exception instanceof UnknownHostException) {
      return false;
    }
    // Connection refused
    if (exception instanceof ConnectTimeoutException) {
      return false;
    }
    // SSL handshake exception
    if (exception instanceof SSLException) {
      return false;
    }
    HttpClientContext clientContext = HttpClientContext.adapt(context);
    HttpRequest request = clientContext.getRequest();
    if (!(request instanceof HttpEntityEnclosingRequest)) {
      return true;
    }
    return false;
  }

}
