package com.example.bootweb.accessory.httpclient;

import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

/**
 * 描述：HttpClient客户端封装
 */
//@Configuration
public class HttpClientConfig {

  /**
   * FactoryBean生成的目标对象
   */
  private CloseableHttpClient client;

  @Autowired
  private ConnectionKeepAliveStrategy connectionKeepAliveStrategy;

  @Autowired
  private HttpRequestRetryHandler httpRequestRetryHandler;

  @Autowired
  private DefaultProxyRoutePlanner proxyRoutePlanner;

  @Autowired
  private PoolingHttpClientConnectionManager poolHttpcConnManager;

  @Autowired
  private RequestConfig config;

  // 销毁上下文时，销毁HttpClient实例
  public void destroy() throws Exception {
    /*
     * 调用httpClient.close()会先shut down connection
     * manager，然后再释放该HttpClient所占用的所有资源，
     * 关闭所有在使用或者空闲的connection包括底层socket。由于这里把它所使用的connection manager关闭了，
     * 所以在下次还要进行http请求的时候，要重新new一个connection manager来build一个HttpClient,
     * 也就是在需要关闭和新建Client的情况下，connection manager不能是单例的.
     */
    if (null != this.client) {
      this.client.close();
    }
  }

  @Bean(name = "httpClient")//, destroyMethod = "close()"
  @Scope(value = "singleton")
  public CloseableHttpClient client() throws Exception {
    /*
     * 建议此处使用HttpClients.custom的方式来创建HttpClientBuilder，而不要使用HttpClientBuilder.
     * create()方法来创建HttpClientBuilder
     * 从官方文档可以得出，HttpClientBuilder是非线程安全的，但是HttpClients确实Immutable的，immutable
     * 对象不仅能够保证对象的状态不被改变， 而且还可以不使用锁机制就能被其他线程共享
     */
    this.client = HttpClients.custom().setConnectionManager(poolHttpcConnManager)
        .setRetryHandler(httpRequestRetryHandler).setKeepAliveStrategy(connectionKeepAliveStrategy)
        .setRoutePlanner(proxyRoutePlanner).setDefaultRequestConfig(config).build();
    return this.client;
  }

}
