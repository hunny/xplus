package com.example.bootweb.accessory.dao;

import java.util.ArrayList;
import java.util.List;

public class SqlResult {

  private String sql;
  private final List<Object> params = new ArrayList<>();

  public SqlResult() {
    // Do Nothing.
  }

  public SqlResult(String sql, List<Object> params) {
    this.sql = sql;
    if (null != params) {
      this.params.addAll(params);
    }
  }

  public String getSql() {
    return sql;
  }

  public void setSql(String sql) {
    this.sql = sql;
  }

  public List<Object> getParams() {
    return params;
  }

  public Object[] toArray() {
    return params.toArray(new Object[] {});
  }

  public void setParams(List<Object> params) {
    this.params.clear();
    if (null != params) {
      this.params.addAll(params);
    }
  }

  @Override
  public String toString() {
    return "SqlResult [sql=" + sql + ", \nparams=" + params + "]";
  }

}
