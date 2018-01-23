package com.example.bootweb.accessory.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

public class InsertBuilder {

  private StringBuffer sql = new StringBuffer();
  private String table = null;
  private Map<String, Object> values = new HashMap<>();
  private List<Object> list = new ArrayList<>();

  public static InsertBuilder newBuilder() {
    return new InsertBuilder();
  }

  public InsertBuilder table(String table) {
    this.table = table;
    return this;
  }

  public InsertBuilder addValue(String field, Object value) {
    values.put(field, value);
    return this;
  }

  public Insert build() {
    Assert.notNull(table, "table");
    Assert.notEmpty(values, "values");
    
    StringBuilder fields = new StringBuilder();
    StringBuilder holders = new StringBuilder();
    for (Map.Entry<String, Object> map : values.entrySet()) {
      fields.append(map.getKey().toUpperCase());
      fields.append(",");
      holders.append("?,");
      list.add(map.getValue());
    }

    sql.append("INSERT INTO ");
    sql.append(this.table);
    sql.append(" (");
    sql.append(fields.toString().replaceFirst(",$", ""));
    sql.append(") VALUES (");
    sql.append(holders.toString().replaceFirst(",$", ""));
    sql.append(")");
    return new Insert(sql.toString(), //
        list.toArray(new Object[] {}));
  }

  public static class Insert {

    private String sql;
    private Object[] values;

    public Insert(String sql, Object[] values) {
      super();
      this.sql = sql;
      this.values = values;
    }

    public String getSql() {
      return sql;
    }

    public void setSql(String sql) {
      this.sql = sql;
    }

    public Object[] getValues() {
      return values;
    }

    public void setValues(Object[] values) {
      this.values = values;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("Insert [sql=");
      builder.append(sql);
      builder.append(", values=");
      builder.append(Arrays.toString(values));
      builder.append("]");
      return builder.toString();
    }

  }

}
