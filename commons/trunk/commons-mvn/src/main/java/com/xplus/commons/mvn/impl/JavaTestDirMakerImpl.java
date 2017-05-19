package com.xplus.commons.mvn.impl;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.xplus.commons.mvn.api.DirMaker;
import com.xplus.commons.mvn.impl.entity.BJavaTestDir;

/**
 * 生成Java工程的文件目录结构
 * 
 * @author huzexiong
 *
 */
@Component(JavaTestDirMakerImpl.BEAN_ID)
public class JavaTestDirMakerImpl implements DirMaker<BJavaTestDir> {

  public static final String BEAN_ID = "commons-mvn.javaTestDirMakerImpl";

  private static final Logger logger = LoggerFactory.getLogger(JavaTestDirMakerImpl.class);

  @Override
  public void make(BJavaTestDir dir) {
    makDirs(dir);
  }

  protected boolean makDirs(BJavaTestDir dir) {
    logger.debug("Ready for generating maven test folders.");
    return new File(dir.getDirPath()).mkdirs() //
        && new File(dir.getResourcePath()).mkdirs() //
        && new File(dir.getMetaInfPath()).mkdirs();
  }

}
