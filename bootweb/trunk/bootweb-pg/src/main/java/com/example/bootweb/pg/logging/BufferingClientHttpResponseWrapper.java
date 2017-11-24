package com.example.bootweb.pg.logging;

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
