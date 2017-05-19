package com.xplus.commons.mvn.impl;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.xplus.commons.mvn.api.DirMaker;
import com.xplus.commons.mvn.impl.entity.BJavaDir;

/**
 * 生成Java工程的文件目录结构
 * 
 * @author huzexiong
 *
 */
@Component(JavaDirMakerImpl.BEAN_ID)
public class JavaDirMakerImpl implements DirMaker<BJavaDir> {

  public static final String BEAN_ID = "commons-mvn.javaDirMakerImpl";

  private static final Logger logger = LoggerFactory.getLogger(JavaDirMakerImpl.class);

  @Override
  public void make(BJavaDir dir) {
    makDirs(dir);
  }

  protected boolean makDirs(BJavaDir dir) {
    logger.debug("Ready for generating maven java folders.");
    return new File(dir.getDirPath()).mkdirs() //
        && new File(dir.getResourcePath()).mkdirs() //
        && new File(dir.getMetaInfPath()).mkdirs();
  }

}
