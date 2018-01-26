package com.example.bootweb.accessory.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

public class InsertBuilder implements SqlBuilder {

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

  @Override
  public SqlResult build() {
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

    return new SqlResult(sql.toString(), //
        list);
  }

}
