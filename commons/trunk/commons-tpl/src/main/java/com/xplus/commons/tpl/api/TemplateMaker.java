package com.xplus.commons.tpl.api;

import java.util.Map;

/**
 * 使用模板生成
 * 
 * @author huzexiong
 *
 */
public interface TemplateMaker {
  
  /**
   * 把对象T的值使用src的模板生成到dest文件中。
   * 
   * @param t
   * @param src
   * @param dest
   */
  void make(Map<String, Object> t, String src, String dest);

}
