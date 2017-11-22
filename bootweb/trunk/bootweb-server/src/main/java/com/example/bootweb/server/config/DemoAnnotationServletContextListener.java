package com.example.bootweb.server.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.bootweb.server.profile.AnnotationServletProfile;

@WebListener
@AnnotationServletProfile
public class DemoAnnotationServletContextListener implements ServletContextListener {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  @Override
  public void contextInitialized(ServletContextEvent sce) {
    logger.info("ServletContextListener initialized.");
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    // TODO Auto-generated method stub

  }

}
