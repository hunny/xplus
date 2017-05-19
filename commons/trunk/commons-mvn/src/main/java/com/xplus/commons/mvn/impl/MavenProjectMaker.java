package com.xplus.commons.mvn.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.xplus.commons.mvn.api.DirMaker;
import com.xplus.commons.mvn.api.FileMaker;
import com.xplus.commons.mvn.impl.entity.BClassPath;
import com.xplus.commons.mvn.impl.entity.BEclipse;
import com.xplus.commons.mvn.impl.entity.BGitIgnore;
import com.xplus.commons.mvn.impl.entity.BJavaDir;
import com.xplus.commons.mvn.impl.entity.BJavaTestDir;
import com.xplus.commons.mvn.impl.entity.BLog4j;
import com.xplus.commons.mvn.impl.entity.BPom;
import com.xplus.commons.mvn.impl.entity.BProject;
import com.xplus.commons.mvn.impl.entity.BPropXml;

/**
 * 生成Maven工程
 * 
 * @author huzexiong
 *
 */
@Component
public class MavenProjectMaker {

  @Resource(name = JavaDirMakerImpl.BEAN_ID)
  private DirMaker javaDirMaker;
  
  @Resource(name = JavaTestDirMakerImpl.BEAN_ID)
  private DirMaker javaTestDirMaker;
  
  @Resource(name = PomFileMakerImpl.BEAN_ID)
  private FileMaker pomFileMaker;
  
  @Resource(name = PropXmlFileMakerImpl.BEAN_ID)
  private FileMaker propXmlFileMaker;
  
  @Resource(name = Log4jFileMakerImpl.BEAN_ID)
  private FileMaker log4jFileMaker;
  
  @Resource(name = EclipseFileMakerImpl.BEAN_ID)
  private FileMaker eclipseFileMaker;
  
  public void make(BPom pom) {
    BJavaDir javaDir = new BJavaDir();
    javaDir.setPath(pom.getPath());
    BJavaTestDir javaTestDir = new BJavaTestDir();
    javaTestDir.setPath(pom.getPath());
    javaDirMaker.make(javaDir);
    javaTestDirMaker.make(javaTestDir);
    
    pomFileMaker.make(pom);
    
    BPropXml propXml = new BPropXml();
    propXml.setPath(pom.getPath());
    propXml.setPom(pom);
    propXml.setJavaDir(javaDir);
    propXml.setJavaTestDir(javaTestDir);
    propXmlFileMaker.make(propXml);
    
    BLog4j log4j = new BLog4j();
    log4j.setPath(pom.getPath());
    log4j.setLog4jPath("");
    log4j.setLog4jName(pom.getArtifactId());
    log4j.setPackageName(pom.getGroupId());
    log4j.setJavaDir(javaDir);
    log4j.setJavaTestDir(javaTestDir);
    log4jFileMaker.make(log4j);
    
    BGitIgnore gitIgnore = new BGitIgnore();
    gitIgnore.setPath(pom.getPath());
    
    BProject project = new BProject();
    project.setPath(pom.getPath());
    project.setName(pom.getArtifactId());
    project.setDescription("项目描述");
    
    BClassPath classPath = new BClassPath();
    classPath.setPath(pom.getPath());
    
    BEclipse eclipse = new BEclipse();
    eclipse.setPath(pom.getPath());
    eclipse.setProject(project);
    eclipse.setClassPath(classPath);
    eclipse.setGitIgnore(gitIgnore);
    eclipseFileMaker.make(eclipse);
  }

}
