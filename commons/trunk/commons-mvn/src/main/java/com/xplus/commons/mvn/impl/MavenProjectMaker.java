package com.xplus.commons.mvn.impl;

import java.util.Map;

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
  
  @Autowired
  private FileMaker fileMaker;
  
  @Override
  public void make(Map<String, Object> object, String path) {
    dirMaker.make(path);
    fileMaker.make(object, path);
  }

}
