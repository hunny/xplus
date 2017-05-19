package com.xplus.commons.mvn.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.xplus.commons.mvn.api.FileMaker;
import com.xplus.commons.mvn.impl.entity.BGitIgnore;
import com.xplus.commons.tpl.api.TemplateMaker;

/**
 * @author huzexiong
 *
 */
@Component(GitIgnoreFileMakerImpl.BEAN_ID)
public class GitIgnoreFileMakerImpl implements FileMaker<BGitIgnore> {

  public static final String BEAN_ID = "commons-mvn.gitIgnoreFileMakerImpl";

  private final Logger logger = LoggerFactory.getLogger(GitIgnoreFileMakerImpl.class);

  @Resource(name = "commons-tpl.freeMarkerTemplateMaker")
  private TemplateMaker templateMaker;

  /**
   * 模板的相对路径
   */
  @Value("${commons-mvn.tpl.path:META-INF/commons-mvn/ftl}")
  private String tplPath;
  
  @Override
  public void make(BGitIgnore t) {
    makeGitignore(t);
  }
  
  protected void makeGitignore(BGitIgnore t) {
    logger.debug(".gitignore File Maker Begin...");
    String tpl = String.format("%s/%s", tplPath, "gitignore.ftl");
    String out = String.format("%s/%s", t.getPath(), ".gitignore");
    templateMaker.make(t, tpl, out);
  }

}
