package com.example.bootweb.jaxrs.jersy;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Resource implements Serializable {

  private static final long serialVersionUID = 4144444660085631303L;
  
  private Long id;
  private String description;
  private LocalDateTime createdTime;
  private LocalDateTime modifiedTime;

  public Resource(Long id, //
      String description, //
      LocalDateTime createdTime, //
      LocalDateTime modifiedTime) {
    super();
    this.id = id;
    this.description = description;
    this.createdTime = createdTime;
    this.modifiedTime = modifiedTime;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDateTime getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(LocalDateTime createdTime) {
    this.createdTime = createdTime;
  }

  public LocalDateTime getModifiedTime() {
    return modifiedTime;
  }

  public void setModifiedTime(LocalDateTime modifiedTime) {
    this.modifiedTime = modifiedTime;
  }

}
