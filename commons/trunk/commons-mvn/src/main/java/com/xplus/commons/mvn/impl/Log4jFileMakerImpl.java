package com.xplus.commons.mvn.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.xplus.commons.mvn.api.FileMaker;
import com.xplus.commons.mvn.impl.entity.BLog4j;
import com.xplus.commons.tpl.api.TemplateMaker;

/**
 * @author huzexiong
 */
@Component(Log4jFileMakerImpl.BEAN_ID)
public class Log4jFileMakerImpl implements FileMaker<BLog4j> {

  private final Logger logger = LoggerFactory.getLogger(Log4jFileMakerImpl.class);

  public static final String BEAN_ID = "commons-mvn.log4jFileMakerImpl";

  /**
   * 模板读写器
   */
  @Resource(name = "commons-tpl.freeMarkerTemplateMaker")
  private TemplateMaker templateMaker;

  /**
   * 模板的相对路径
   */
  @Value("${commons-mvn.tpl.path:META-INF/commons-mvn/ftl}")
  private String tplPath;

  @Override
  public void make(BLog4j t) {
    makeLog4j(t);
  }

  protected void makeLog4j(BLog4j t) {
    if (null == t //
        || StringUtils.isEmpty(t.getPath()) //
        || (null == t.getJavaDir() && null == t.getJavaTestDir())) {
      logger.debug("log4j info not found, ignore folder files.");
      return;
    }
    String tpl = String.format("%s/%s", tplPath, "log4j.ftl");

    if (null != t.getJavaDir()) {
      String out = String.format("%s/%s", t.getJavaDir().getResourcePath(), "log4j.properties");
      logger.debug("log4j.properties File Maker Begin [{}]...", tpl);
      templateMaker.make(t, tpl, out);
      logger.debug("log4j.properties File Maker Done [{}]...", out);
    }
    if (null != t.getJavaTestDir()) {
      String out = String.format("%s/%s", t.getJavaTestDir().getResourcePath(), "log4j.properties");
      logger.debug("log4j.properties File Maker Begin [{}]...", tpl);
      templateMaker.make(t, tpl, out);
      logger.debug("log4j.properties File Maker Done [{}]...", out);
    }
  }

}
