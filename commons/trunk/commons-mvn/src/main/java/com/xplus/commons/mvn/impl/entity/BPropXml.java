package com.xplus.commons.mvn.impl.entity;

/**
 * @author huzexiong
 *
 */
public class BPropXml extends BaseMaker {

  private static final long serialVersionUID = -6514894423539079741L;

  private String artifactId = null;
  private BJavaDir javaDir;
  private BJavaTestDir javaTestDir;
  private BPom pom;
  
  public String getArtifactId() {
    return artifactId;
  }
  public void setArtifactId(String artifactId) {
    this.artifactId = artifactId;
  }
  public BJavaDir getJavaDir() {
    return javaDir;
  }
  public void setJavaDir(BJavaDir javaDir) {
    this.javaDir = javaDir;
  }
  public BJavaTestDir getJavaTestDir() {
    return javaTestDir;
  }
  public void setJavaTestDir(BJavaTestDir javaTestDir) {
    this.javaTestDir = javaTestDir;
  }
  public BPom getPom() {
    return pom;
  }
  public void setPom(BPom pom) {
    this.pom = pom;
  }
}
