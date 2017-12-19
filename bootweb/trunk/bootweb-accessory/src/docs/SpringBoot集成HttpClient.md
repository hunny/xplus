# 集成HttpClient

官方文档：HttpClient implementations are expected to be thread safe. It is recommended that the same instance of this class is reused for multiple request executions，翻译过来的意思就是：HttpClient的实现是线程安全的，可以重用相同的实例来执行多次请求。遇到这种描述的话，我们就应该想到，需要对HttpClient来进行封装了。由于是使用的spring boot，所以下面来结合spring boot来封装HttpClient。

## 一、Request retry handler(请求重试处理)

为了使自定义异常机制生效，需要实现HttpRequestRetryHandler接口，代码如下：

### 重试机制

```java
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
```

### 重试机制配置

```java
import org.apache.http.client.HttpRequestRetryHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * HttpClient的重试处理配置
 */
@Configuration
public class HttpRetryConfig {

  @Value("${httpclient.config.retryTime}") // 此处建议采用@ConfigurationProperties(prefix="httpclient.config")方式，方便复用
  private int retryTime;

  @Bean
  public HttpRequestRetryHandler httpRequestRetryHandler() {
    return new HttpRetryHandler(retryTime);
  }
}
```

## 二、Pooling connection manager(连接池管理)

PoolingHttpClientConnectionManager用来管理客户端的连接池，并且可以为多个线程的请求提供服务，代码如下：

### 连接池配置

```java
import java.util.concurrent.TimeUnit;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Pooling connection manager(连接池管理)
 * 
 * PoolingHttpClientConnectionManager用来管理客户端的连接池，并且可以为多个线程的请求提供服务
 *
 */
@Configuration
public class ConnectPoolConfig {

  /**
   * 连接池最大连接数
   */
  @Value("${httpclient.config.connMaxTotal}")
  private int connMaxTotal = 20;

  /** 
   *  
   */
  @Value("${httpclient.config.maxPerRoute}")
  private int maxPerRoute = 20;

  /**
   * 连接存活时间，单位为s
   */
  @Value("${httpclient.config.timeToLive}")
  private int timeToLive = 60;

  @Bean
  public PoolingHttpClientConnectionManager poolingClientConnectionManager() {
    PoolingHttpClientConnectionManager poolHttpcConnManager = new PoolingHttpClientConnectionManager(
        timeToLive, TimeUnit.SECONDS);
    // 最大连接数
    poolHttpcConnManager.setMaxTotal(this.connMaxTotal);
    // 路由基数
    poolHttpcConnManager.setDefaultMaxPerRoute(this.maxPerRoute);
    return poolHttpcConnManager;
  }
}
```

注意：当HttpClient实例不再需要并且即将超出范围时，重要的是关闭其连接管理器，以确保管理器保持活动的所有连接都被关闭，并释放由这些连接分配的系统资源

### 连接池说明

上面PoolingHttpClientConnectionManager类的构造函数如下：

```java
public PoolingHttpClientConnectionManager(final long timeToLive, final TimeUnit tunit) {  
    this(getDefaultRegistry(), null, null ,null, timeToLive, tunit);  
}

private static Registry<ConnectionSocketFactory> getDefaultRegistry() {  
    return RegistryBuilder.<ConnectionSocketFactory>create()  
            .register("http", PlainConnectionSocketFactory.getSocketFactory())  
            .register("https", SSLConnectionSocketFactory.getSocketFactory())  
            .build();  
}
```

在PoolingHttpClientConnectionManager的配置中有两个最大连接数量，分别控制着总的最大连接数量和每个route的最大连接数量。如果没有显式设置，默认每个route只允许最多2个connection，总的connection数量不超过20。这个值对于很多并发度高的应用来说是不够的，必须根据实际的情况设置合适的值，思路和线程池的大小设置方式是类似的，如果所有的连接请求都是到同一个url，那可以把MaxPerRoute的值设置成和MaxTotal一致，这样就能更高效地复用连接。

> 特别注意：想要复用一个connection就必须要让它占有的系统资源得到正确释放，释放方法如下：
> 如果是使用outputStream就要保证整个entity都被write out。
> 如果是inputStream，则再最后要记得调用inputStream.close()。或者使用EntityUtils.consume(entity)或EntityUtils.consumeQuietly(entity)来让entity被完全耗尽（后者不抛异常）来做这一工作。
> EntityUtils中有个toString方法也很方便的（调用这个方法最后也会自动把inputStream close掉的，但是在实际的测试过程中，会导致连接没有释放的现象），不过只有在可以确定收到的entity不是特别大的情况下才能使用。
> 如果没有让整个entity被fully consumed，则该连接是不能被复用的，很快就会因为在连接池中取不到可用的连接超时或者阻塞在这里（因为该连接的状态将会一直是leased的，即正在被使用的状态）。所以如果想要复用connection，一定一定要记得把entity fully consume掉，只要检测到stream的eof，是会自动调用ConnectionHolder的releaseConnection方法进行处理的

## 三、Connection keep alive strategy(保持连接策略)

HTTP规范没有指定持久连接可能和应该保持存活多久。一些HTTP服务器使用非标准的Keep-Alive标头来向客户端通信它们打算在服务器端保持连接的时间段（以秒为单位）。
HttpClient可以使用这些信息。如果响应中不存在Keep-Alive头，HttpClient会假定连接可以无限期地保持活动。然而，一般使用的许多HTTP服务器都配置为在一段不活动状态之后删除持久连接，以便节省系统资源，而不会通知客户端。如果默认策略过于乐观，则可能需要提供自定义的保持活动策略，代码如下：

### 保持连接策略

```java
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 描述：连接保持策略
 */
@Configuration
public class KeepAliveConfig {

  @Value("${httpclient.config.keepAliveTime}")
  private int keepAliveTime = 30;

  @Bean("connectionKeepAliveStrategy")
  public ConnectionKeepAliveStrategy connectionKeepAliveStrategy() {
    return new ConnectionKeepAliveStrategy() {

      public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
        // Honor 'keep-alive' header
        HeaderElementIterator it = new BasicHeaderElementIterator(
            response.headerIterator(HTTP.CONN_KEEP_ALIVE));
        while (it.hasNext()) {
          HeaderElement he = it.nextElement();
          String param = he.getName();
          String value = he.getValue();
          if (value != null && param.equalsIgnoreCase("timeout")) {
            try {
              return Long.parseLong(value) * 1000;
            } catch (NumberFormatException ignore) {
            }
          }
        }
        return 30 * 1000;
      }
    };
  }
}
```

注意：长连接并不使用于所有的情况，尤其现在的系统，大都是部署在多台服务器上，且具有负载均衡的功能，如果我们在访问的时候，一直保持长连接，一旦那台服务器挂了，就会影响客户端，同时也不能充分的利用服务端的负载均衡的特性，反而短连接更有利一些，这些需要根据具体的需求来定，而不是一言概括。

## 四、HttpClient proxy configuration(代理配置)

用来配置代理，代码如下：

```java
import org.apache.http.HttpHost;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 描述：HttpClient代理
 */
@Configuration
public class ProxyRouteConfig {

  // 代理的host地址
  @Value("${httpclient.config.proxyhost}")
  private String proxyHost;

  // 代理的端口号
  @Value("${httpclient.config.proxyPort}")
  private int proxyPort = 80;

  @Bean
  public DefaultProxyRoutePlanner defaultProxyRoutePlanner() {
    HttpHost proxy = new HttpHost(this.proxyHost, this.proxyPort);
    return new DefaultProxyRoutePlanner(proxy);
  }

}
```

 HttpClient不仅支持简单的直连、复杂的路由策略以及代理。
 HttpRoutePlanner是基于http上下文情况下，客户端到服务器的路由计算策略，一般没有代理的话，就不用设置这个东西。
 这里有一个很关键的概念—Route：在HttpClient中，一个Route指 运行环境机器->目标机器host的一条线路，也就是如果目标url的host是同一个，那么它们的route也是一样的。

 ## 五、RequestConfig

 用来设置请求的各种配置，代码如下：

 ```java
 import org.apache.http.client.config.RequestConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 用来设置请求的各种配置
 */
@Configuration
public class HttpRequestConfig {

  @Value("${httpclient.config.connectTimeout}")
  private int connectTimeout = 2000;

  @Value("${httpclient.config.connectRequestTimeout}")
  private int connectRequestTimeout = 2000;

  @Value("${httpclient.config.socketTimeout}")
  private int socketTimeout = 2000;

  @Bean
  public RequestConfig config() {
    return RequestConfig.custom().setConnectionRequestTimeout(this.connectRequestTimeout)
        .setConnectTimeout(this.connectTimeout).setSocketTimeout(this.socketTimeout).build();
  }

}
 ```

### 请求配置说明

* RequestConfig是对request的一些配置。里面比较重要的有三个超时时间，默认的情况下这三个超时时间都为0（如果不设置request的Config，会在execute的过程中使用HttpClientParamConfig的getRequestConfig中用默认参数进行设置），这也就意味着无限等待，很容易导致所有的请求阻塞在这个地方无限期等待。这三个超时时间为：
	- a、connectionRequestTimeout—从连接池中取连接的超时时间
		+ 这个时间定义的是从ConnectionManager管理的连接池中取出连接的超时时间， 如果连接池中没有可用的连接，则request会被阻塞，最长等待connectionRequestTimeout的时间，如果还没有被服务，则抛出ConnectionPoolTimeoutException异常，不继续等待。
	- b、connectTimeout—连接超时时间
		+ 这个时间定义了通过网络与服务器建立连接的超时时间，也就是取得了连接池中的某个连接之后到接通目标url的连接等待时间。发生超时，会抛出ConnectionTimeoutException异常。
	- c、socketTimeout—请求超时时间
		+ 这个时间定义了socket读数据的超时时间，也就是连接到服务器之后到从服务器获取响应数据需要等待的时间，或者说是连接上一个url之后到获取response的返回等待时间。发生超时，会抛出SocketTimeoutException异常。

## 六、实例化HttpClient

通过实现FactoryBean来实例化HttpClient，代码如下：

```java
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 描述：HttpClient客户端封装
 */
@Service("httpClient")
public class HttpClientManagerFactoryBen
    implements FactoryBean<CloseableHttpClient>, InitializingBean, DisposableBean {

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
  @Override
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

  @Override // 初始化实例
  public void afterPropertiesSet() throws Exception {
    /*
     * 建议此处使用HttpClients.custom的方式来创建HttpClientBuilder，而不要使用HttpClientBuilder.
     * create()方法来创建HttpClientBuilder
     * 从官方文档可以得出，HttpClientBuilder是非线程安全的，但是HttpClients确实Immutable的，immutable
     * 对象不仅能够保证对象的状态不被改变， 而且还可以不使用锁机制就能被其他线程共享
     */
    this.client = HttpClients.custom().setConnectionManager(poolHttpcConnManager)
        .setRetryHandler(httpRequestRetryHandler).setKeepAliveStrategy(connectionKeepAliveStrategy)
        .setRoutePlanner(proxyRoutePlanner).setDefaultRequestConfig(config).build();
  }

  // 返回实例的类型
  @Override
  public CloseableHttpClient getObject() throws Exception {
    return this.client;
  }

  @Override
  public Class<?> getObjectType() {
    return (this.client == null ? CloseableHttpClient.class : this.client.getClass());
  }

  // 构建的实例为单例
  @Override
  public boolean isSingleton() {
    return true;
  }

}
```

## 七、增加配置文件

```
# 代理的host  
httpclient.config.proxyhost=127.0.0.1
# 代理端口  
httpclient.config.proxyPort=8080
# 连接超时或异常重试次数  
httpclient.config.retryTime=3
# 长连接保持时间，单位为s  
httpclient.config.keepAliveTime=30
# 连接池最大连接数  
httpclient.config.connMaxTotal=20
httpclient.config.maxPerRoute=20
# 连接超时时间，单位ms  
httpclient.config.connectTimeout=2000
# 请求超时时间  
httpclient.config.connectRequestTimeout=2000
# sock超时时间  
httpclient.config.socketTimeout=2000
# 连接存活时间，单位s  
httpclient.config.timeToLive=60
```

## 八、测试

测试代码如下：

```java
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HttpClientManagerFactoryBenTest {
  
  // 注入HttpClient实例
  @Resource(name = "httpClient")
  private CloseableHttpClient client;

  @Test
  public void test() throws Exception {
    int count = 10;
    final CountDownLatch latch = new CountDownLatch(count);
    ExecutorService service = Executors.newFixedThreadPool(2);
    for (int i = 0; i < count; i++) {
      service.submit(new Runnable() {
        @Override
        public void run() {
          System.out.println("the current thread is:" + Thread.currentThread().getName());
          HttpEntity entity = null;
          try {
            HttpGet get = new HttpGet("http://localhost:8080/about");
            // 通过httpclient的execute提交 请求 ，并用CloseableHttpResponse接受返回信息
            CloseableHttpResponse response = client.execute(get);
            System.out.println("client object:" + client);
            entity = response.getEntity();
            System.out.println(
                "============" + EntityUtils.toString(entity, Consts.UTF_8) + "=============");
            EntityUtils.consumeQuietly(entity);// 释放连接
          } catch (ClientProtocolException e) {
            e.printStackTrace();
          } catch (ParseException e) {
            e.printStackTrace();
          } catch (IOException e) {
            e.printStackTrace();
          } finally {
            if (null != entity) {// 释放连接
              EntityUtils.consumeQuietly(entity);
            }
            latch.countDown();
          }
        }
      });
    }
    try {
      latch.await();
      System.out.println("execute over.");
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception(e);
    }
  }
}
```

通过上面的几个步骤，就基本上完成了对HttpClient的封装，如果需要更细致的话，可以按照上面的思路，逐步完善，将HttpClient封装成HttpClientTemplate，因为CloseableHttpClient内部使用了回调机制，和JdbcTemplate，或者是RedisTemplate类似，直到可以以spring boot starter的方式提供服务。
