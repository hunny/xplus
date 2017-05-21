package com.xplus.commons.tpl.api;

/**
 * 使用模板生成
 * 
 * @author huzexiong
 *
 */
public interface TemplateMaker {
  
	/**
	 * 是否覆盖已经存在的文件
	 * @param forceOverWrite
	 */
	public void setForceOverWrite(boolean forceOverWrite);
	
  /**
   * 把对象T的值使用src的模板生成到dest文件中。
   * 
   * @param t
   * @param src
   * @param dest
   */
  void make(Object model, String src, String dest);

}
