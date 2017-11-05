package com.example.bootweb.thymeleaf.web.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Index implements Serializable {

  private static final long serialVersionUID = -1660365702989595366L;

  private final List<Dashboard> dashboards = new ArrayList<>();
  private final List<Dashboard> list = new ArrayList<>();
  
  public List<Dashboard> getDashboards() {
    return dashboards;
  }

  public void setDashboards(List<Dashboard> dashboards) {
    this.dashboards.clear();
    if (null != dashboards) {
      this.dashboards.addAll(dashboards);
    }
  }

  public List<Dashboard> getList() {
    return list;
  }

  public void setList(List<Dashboard> list) {
    this.list.clear();
    if (null != list) {
      this.list.addAll(list);
    }
  }
  
}
