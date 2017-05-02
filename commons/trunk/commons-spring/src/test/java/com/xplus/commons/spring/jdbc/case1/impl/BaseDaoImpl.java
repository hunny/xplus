package com.xplus.commons.spring.jdbc.case1.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.xplus.commons.spring.jdbc.case1.BaseDao;
import com.xplus.commons.spring.jdbc.case1.Pager;
import com.xplus.commons.spring.jdbc.case1.SqlContext;
import com.xplus.commons.spring.jdbc.case1.SqlUtils;

/**
 * @author huzexiong
 *
 */
public abstract class BaseDaoImpl<T> implements BaseDao<T> {

  /** 具体操作的实体类对象 */
  private Class<T> entityClass;

  /** spring jdbcTemplate 对象 */
  @Autowired
  protected JdbcTemplate jdbcTemplate;

  /**
   * 构造方法，获取运行时的具体实体对象
   */
  public BaseDaoImpl() {
    Type superclass = getClass().getGenericSuperclass();
    ParameterizedType type = (ParameterizedType) superclass;
    entityClass = (Class<T>) type.getActualTypeArguments()[0];
  }

  /**
   * 插入一条记录
   *
   * @param entity
   */
  @Override
  public Long insert(T entity) {
    final SqlContext sqlContext = SqlUtils.buildInsertSql(entity);
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(new PreparedStatementCreator() {
      public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        PreparedStatement ps = con.prepareStatement(sqlContext.getSql().toString(), new String[] {
            sqlContext.getPrimaryKey() });
        int index = 0;
        for (Object param : sqlContext.getParams()) {
          index++;
          ps.setObject(index, param);
        }
        return ps;
      }
    }, keyHolder);
    return keyHolder.getKey().longValue();
  }

  /**
   * 更新记录
   *
   * @param entity
   */
  @Override
  public int update(T entity) {
    SqlContext sqlContext = SqlUtils.buildUpdateSql(entity);
    return jdbcTemplate.update(sqlContext.getSql().toString(), sqlContext.getParams().toArray());
  }

  /**
   * 删除记录
   *
   * @param id
   */
  @Override
  public void delete(Long id) {
    String tableName = entityClass.getSimpleName();
    String primaryName = entityClass.getSimpleName();
    String sql = "DELETE FROM " + tableName + " WHERE " + primaryName + " = ?";
    jdbcTemplate.update(sql, id);
  }

  /**
   * 删除所有记录
   */
  @Override
  public void deleteAll() {
    String tableName = entityClass.getSimpleName();
    String sql = " TRUNCATE TABLE " + tableName;
    jdbcTemplate.execute(sql);
  }

  /**
   * 得到记录
   *
   * @param id
   * @return
   */
  @Override
  public T getById(Long id) {
    String tableName = entityClass.getSimpleName();
    String primaryName = entityClass.getSimpleName();
    String sql = "SELECT * FROM " + tableName + " WHERE " + primaryName + " = ?";
    return (T) jdbcTemplate.query(sql, new RowMapper() {
      @Override
      public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        return null;
      }
    }, id).get(0);
  }

  /**
   * 查询所有记录
   *
   * @return
   */
  @Override
  public List<T> findAll() {
    String sql = "SELECT * FROM " + entityClass.getSimpleName();
    return (List<T>) jdbcTemplate.query(sql, new RowMapper<List>() {
      @Override
      public List mapRow(ResultSet rs, int rowNum) throws SQLException {
        return null;
      }

    });
  }

  /**
   * 查询记录数
   *
   * @param entity
   * @return
   */
  public int queryCount(T entity) {
    String tableName = entityClass.getSimpleName();
    StringBuilder countSql = new StringBuilder("select count(*) from ");
    countSql.append(tableName);
    SqlContext sqlContext = SqlUtils.buildQueryCondition(entity);
    if (String.valueOf(sqlContext.getSql()).length() > 0) {
      countSql.append(" where ");
      countSql.append(sqlContext.getSql());
    }
    return jdbcTemplate.queryForObject(countSql.toString(), sqlContext.getParams().toArray(),
        new RowMapper<Integer>() {
          @Override
          public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
            return null;
          }
        });
  }

  /**
   * 查询分页列表
   *
   * @param entity
   * @return
   */
  public Pager queryPageList(T entity) {
    return null;
  }
}
