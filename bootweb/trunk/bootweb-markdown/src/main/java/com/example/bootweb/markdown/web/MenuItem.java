package com.example.bootweb.markdown.web;

public class MenuItem {

  private String href;
  private String name;
  private boolean active = false;
  private boolean group = false;

  public MenuItem(String name) {
    this.name = name;
    this.group = true;
  }
  public MenuItem(String href, String name) {
    this.href = href;
    this.name = name;
  }

  public MenuItem(String href, String name, boolean active) {
    this.href = href;
    this.name = name;
    this.active = active;
  }

  public String getHref() {
    return href;
  }

  public void setHref(String href) {
    this.href = href;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public boolean isGroup() {
    return group;
  }

  public void setGroup(boolean group) {
    this.group = group;
  }

}
