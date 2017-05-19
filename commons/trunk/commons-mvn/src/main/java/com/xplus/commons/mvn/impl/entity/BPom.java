package com.xplus.commons.mvn.impl.entity;

/**
 * Pom参数变量实体
 * 
 * @author huzexiong
 *
 */
public class BPom extends BaseMaker {

  private static final long serialVersionUID = 376699099532232584L;
  
  private String groupId;
  private String artifactId;
  private String version;
  private String name;
  private String description;
  private BPom parent;
  public String getGroupId() {
    return groupId;
  }
  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }
  public String getArtifactId() {
    return artifactId;
  }
  public void setArtifactId(String aritfactId) {
    this.artifactId = aritfactId;
  }
  public String getVersion() {
    return version;
  }
  public void setVersion(String version) {
    this.version = version;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public BPom getParent() {
    return parent;
  }
  public void setParent(BPom parent) {
    this.parent = parent;
  }

}
