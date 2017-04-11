package com.xplus.commons.mvn.impl.entity;

import java.io.Serializable;

/**
 * Maven 实体
 * 
 * @author huzexiong
 *
 */
public class BOMaven implements Serializable {

  private static final long serialVersionUID = 5237112517210951970L;

  private String groupId;
  private String artifactId;
  private String version;
  private String packaging;
  private BOMaven parent;

  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public String getArtifactId() {
    return artifactId;
  }

  public void setArtifactId(String artifactId) {
    this.artifactId = artifactId;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getPackaging() {
    return packaging;
  }

  public void setPackaging(String packaging) {
    this.packaging = packaging;
  }

  public BOMaven getParent() {
    return parent;
  }

  public void setParent(BOMaven parent) {
    this.parent = parent;
  }

}
