package com.xplus.commons.mvn.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.xplus.commons.mvn.api.FileMaker;
import com.xplus.commons.mvn.impl.entity.BEclipse;

/**
 * 创建eclipse的classpath和project文件
 * 
 * @author huzexiong
 *
 */
@Component(EclipseFileMakerImpl.BEAN_ID)
public class EclipseFileMakerImpl implements FileMaker<BEclipse> {

  public static final String BEAN_ID = "commons-mvn.eclipseFileMakerImpl";

  private final Logger logger = LoggerFactory.getLogger(EclipseFileMakerImpl.class);

  @Resource(name = ClassPathFileMakerImpl.BEAN_ID)
  private FileMaker classPathMaker;

  @Resource(name = ProjectFileMakerImpl.BEAN_ID)
  private FileMaker projectMaker;

  @Resource(name = GitIgnoreFileMakerImpl.BEAN_ID)
  private FileMaker gitIgnoreMaker;

  @Override
  public void make(BEclipse eclipse) {
    logger.debug("Ready for creating eclipse classpath and project files.");
    if (null == eclipse || ObjectUtils.isEmpty(eclipse.getPath())) {
      logger.debug("eclipse info not found, ignore files.");
      return;
    }
    if (null != eclipse.getClassPath()) {
      eclipse.getClassPath().setPath(eclipse.getPath());
      classPathMaker.make(eclipse.getClassPath());
    }
    if (null != eclipse.getProject()) {
      eclipse.getProject().setPath(eclipse.getPath());
      projectMaker.make(eclipse.getProject());
    }
    if (null != eclipse.getGitIgnore()) {
      eclipse.getGitIgnore().setPath(eclipse.getPath());
      gitIgnoreMaker.make(eclipse.getGitIgnore());
    }
  }
}
