package com.xplus.commons.mvn.impl.entity;

/**
 * @author huzexiong
 *
 */
public class BEclipse extends BaseMaker {

  private static final long serialVersionUID = -219466902829366829L;
  
  private BClassPath classPath;
  private BProject project;
  private BGitIgnore gitIgnore;
  
  public BClassPath getClassPath() {
    return classPath;
  }
  public void setClassPath(BClassPath classPath) {
    this.classPath = classPath;
  }
  public BProject getProject() {
    return project;
  }
  public void setProject(BProject project) {
    this.project = project;
  }
  public BGitIgnore getGitIgnore() {
    return gitIgnore;
  }
  public void setGitIgnore(BGitIgnore gitIgnore) {
    this.gitIgnore = gitIgnore;
  }

}
