# Logging RestTemplate

## Open debug logging.

### Configurate RestTemplate

* By default the [RestTemplate](http://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html) relies on standard JDK facilities to establish HTTP connections. You can switch to use a different HTTP library such as Apache HttpComponents

```java
  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    RestTemplate restTemplate = builder.build();
    return restTemplate;
  }
```

### Configurate logging

* `application.yml`

```
logging:
  level:
    org.springframework.web.client.RestTemplate: DEBUG
```

## Using Interceptor

### Wrapper Response

```java
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

/**
 * Simple implementation of {@link ClientHttpResponse} that reads the request's body into memory,
 * thus allowing for multiple invocations of {@link #getBody()}.
 */
public final class BufferingClientHttpResponseWrapper implements ClientHttpResponse {

  private final ClientHttpResponse response;

  private byte[] body;


  BufferingClientHttpResponseWrapper(ClientHttpResponse response) {
    this.response = response;
  }

  public HttpStatus getStatusCode() throws IOException {
    return this.response.getStatusCode();
  }

  public int getRawStatusCode() throws IOException {
    return this.response.getRawStatusCode();
  }

  public String getStatusText() throws IOException {
    return this.response.getStatusText();
  }

  public HttpHeaders getHeaders() {
    return this.response.getHeaders();
  }

  public InputStream getBody() throws IOException {
    if (this.body == null) {
      this.body = StreamUtils.copyToByteArray(this.response.getBody());
    }
    return new ByteArrayInputStream(this.body);
  }

  public void close() {
    this.response.close();
  }
}
```

### Implement Interceptor

```java
package com.example.bootweb.pg.logging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class LoggingRestTemplate implements ClientHttpRequestInterceptor {

  private final static Logger LOGGER = LoggerFactory.getLogger(LoggingRestTemplate.class);

  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body,
      ClientHttpRequestExecution execution) throws IOException {
    traceRequest(request, body);
    ClientHttpResponse response = execution.execute(request, body);
    return traceResponse(response);
  }

  private void traceRequest(HttpRequest request, byte[] body) throws IOException {
    if (!LOGGER.isDebugEnabled()) {
      return;
    }
    LOGGER.debug(
        "==========================request begin==============================================");
    LOGGER.debug("URI         : {}", request.getURI());
    LOGGER.debug("Method      : {}", request.getMethod());
    LOGGER.debug("Headers     : {}", request.getHeaders());
    LOGGER.debug("Request body: {}", new String(body, "UTF-8"));
    LOGGER.debug(
        "==========================request end================================================");
  }

  private ClientHttpResponse traceResponse(ClientHttpResponse response) throws IOException {
    if (!LOGGER.isDebugEnabled()) {
      return response;
    }
    final ClientHttpResponse responseWrapper = new BufferingClientHttpResponseWrapper(response);
    StringBuilder inputStringBuilder = new StringBuilder();
    BufferedReader bufferedReader = new BufferedReader(
        new InputStreamReader(responseWrapper.getBody(), "UTF-8"));
    String line = bufferedReader.readLine();
    while (line != null) {
      inputStringBuilder.append(line);
      inputStringBuilder.append('\n');
      line = bufferedReader.readLine();
    }
    LOGGER.debug(
        "==========================response begin=============================================");
    LOGGER.debug("Status code  : {}", responseWrapper.getStatusCode());
    LOGGER.debug("Status text  : {}", responseWrapper.getStatusText());
    LOGGER.debug("Headers      : {}", responseWrapper.getHeaders());
    LOGGER.debug("Response body: {}", inputStringBuilder.toString());
    LOGGER.debug(
        "==========================response end===============================================");
    return responseWrapper;
  }

}
```

### Configurate RestTemplate

```java
  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    RestTemplate restTemplate = builder.build();
    restTemplate.setInterceptors(Collections.singletonList(new LoggingRestTemplate()));
    return restTemplate;
  }
```

### Configurate logging

* Check the package of LoggingRestTemplate, for example in `application.yml`:

```
logging:
  level:
    com.example.bootweb.pg.logging: DEBUG
```

## Using httpcomponent

### Import httpcomponent dependency

```xml
    <dependency>
      <groupId>org.apache.httpcomponents</groupId>
      <artifactId>httpasyncclient</artifactId>
    </dependency>
```

### Configurate RestTemplate

```java
  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    RestTemplate restTemplate = builder.build();
    restTemplate.setRequestFactory(new HttpComponentsAsyncClientHttpRequestFactory());
    return restTemplate;
  }
```

### Configurate logging

* Check the package of LoggingRestTemplate, for example in `application.yml`:

```
logging:
  level:
    org.apache.http: DEBUG
```