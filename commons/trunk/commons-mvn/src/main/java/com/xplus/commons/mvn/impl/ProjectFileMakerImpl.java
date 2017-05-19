package com.xplus.commons.mvn.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.xplus.commons.mvn.api.FileMaker;
import com.xplus.commons.mvn.impl.entity.BProject;
import com.xplus.commons.tpl.api.TemplateMaker;

/**
 * @author huzexiong
 *
 */
@Component(ProjectFileMakerImpl.BEAN_ID)
public class ProjectFileMakerImpl implements FileMaker<BProject> {

  public static final String BEAN_ID = "commons-mvn.projectFileMakerImpl";

  private final Logger logger = LoggerFactory.getLogger(ProjectFileMakerImpl.class);

  @Resource(name = "commons-tpl.freeMarkerTemplateMaker")
  private TemplateMaker templateMaker;

  /**
   * 模板的相对路径
   */
  @Value("${commons-mvn.tpl.path:META-INF/commons-mvn/ftl}")
  private String tplPath;

  @Override
  public void make(BProject t) {
    makeProject(t);
  }

  protected void makeProject(BProject t) {
    if (null == t || ObjectUtils.isEmpty(t.getName())) {
      logger.debug("project info not found, ignore files.");
      return;
    }
    String tpl = String.format("%s/%s", tplPath, "project.ftl");
    String out = String.format("%s/%s", t.getPath(), ".project");
    logger.debug("Project[{}] .project File Maker Begin[{}]...", t.getName(), tpl);
    templateMaker.make(t, tpl, out);
    logger.debug(".project File Maker Done[{}].", out);
  }

}
