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

@SuppressWarnings("static-method")
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
    final ClientHttpResponse responseCopy = new BufferingClientHttpResponseWrapper(response);
    StringBuilder inputStringBuilder = new StringBuilder();
    BufferedReader bufferedReader = new BufferedReader(
        new InputStreamReader(responseCopy.getBody(), "UTF-8"));
    String line = bufferedReader.readLine();
    while (line != null) {
      inputStringBuilder.append(line);
      inputStringBuilder.append('\n');
      line = bufferedReader.readLine();
    }
//    responseCopy.close();
    LOGGER.debug(
        "==========================response begin=============================================");
    LOGGER.debug("Status code  : {}", responseCopy.getStatusCode());
    LOGGER.debug("Status text  : {}", responseCopy.getStatusText());
    LOGGER.debug("Headers      : {}", responseCopy.getHeaders());
    LOGGER.debug("Response body: {}", inputStringBuilder.toString());
    // LOGGER.debug("Response body: {}",
    // IOUtils.toString(responseCopy.getBody()));
    LOGGER.debug(
        "==========================response end===============================================");
    return responseCopy;
  }

}
