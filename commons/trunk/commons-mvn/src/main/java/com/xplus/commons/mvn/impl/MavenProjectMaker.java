package com.xplus.commons.mvn.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xplus.commons.mvn.api.DirMaker;
import com.xplus.commons.mvn.api.FileMaker;
import com.xplus.commons.mvn.api.ProjectMaker;

/**
 * 生成Maven工程
 * 
 * @author huzexiong
 *
 */
@Component
public class MavenProjectMaker implements ProjectMaker {

  @Autowired
  private DirMaker dirMaker;
  
  @Resource(name = JavaFileMaker.BEAN_ID)
  private FileMaker javaFileMaker;
  
  @Resource(name = EclipseFileMaker.BEAN_ID)
  private FileMaker eclipseFileMaker;
  
  
  @Override
  public void make(Map<String, Object> object, String path) {
    dirMaker.make(path);
    javaFileMaker.make(object, path);
    eclipseFileMaker.make(object, path);
  }

}
