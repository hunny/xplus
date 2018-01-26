package com.example.bootweb.accessory.dao.case1;

import java.util.List;

/**
 * 实体T，主键K
 * 
 * @author huzexiong
 *
 */
public interface BaseDao<K, T> {
  /**
   * 插入一条记录
   *
   * @param entity
   */
  K insert(T entity);
  
  /**
   * 插入一条记录
   *
   * @param entity
   */
  Long insertAutoId(T entity);

  /**
   * 更新记录
   *
   * @param entity
   */
  int update(T entity);

  /**
   * 删除记录
   *
   * @param id
   */
  boolean delete(Long id);

  /**
   * 删除所有记录
   */
  void deleteAll();

  /**
   * 得到记录
   *
   * @param id
   * @return
   */
  T getById(Long id);

  /**
   * 查询所有记录
   *
   * @return
   */
  List<T> findAll();

}
