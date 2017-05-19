package com.xplus.commons.mvn.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.xplus.commons.mvn.api.FileMaker;
import com.xplus.commons.mvn.impl.entity.BPom;
import com.xplus.commons.tpl.api.TemplateMaker;

/**
 * @author huzexiong
 *
 */
@Component(PomFileMakerImpl.BEAN_ID)
public class PomFileMakerImpl implements FileMaker<BPom> {

  private final Logger logger = LoggerFactory.getLogger(PomFileMakerImpl.class);

  public static final String BEAN_ID = "commons-mvn.pomFileMakerImpl";

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
  public void make(BPom t) {
    makePom(t);
  }

  protected void makePom(BPom t) {
    logger.debug("pom.xml File Maker Begin...");
    if (null == t || StringUtils.isEmpty(t.getPath())) {
      logger.debug("pom info not found, ignore test folder files.");
      return;
    }
    String tpl = String.format("%s/%s", tplPath, "pom.ftl");
    String out = String.format("%s/%s", t.getPath(), "pom.xml");
    logger.debug("pom.xml File Maker Begin [{}]...", tpl);
    templateMaker.make(t, tpl, out);
    logger.debug("pom.xml File Maker Done [{}].", out);
  }

}
