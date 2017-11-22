package com.example.bootweb.server.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.bootweb.server.profile.AnnotationServletProfile;

@WebFilter
@AnnotationServletProfile
public class DemoAnnotationFilter implements Filter {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    logger.info("Filter init.");
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    logger.info("Filter doFilter.");
    chain.doFilter(request, response);

  }

  @Override
  public void destroy() {
    logger.info("Filter destroy.");
  }

}
