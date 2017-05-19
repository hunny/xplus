/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	commons-mvn
 * 文件名：	Maker.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月19日 - huzexiong - 创建。
 */
package com.xplus.commons.mvn.api;

import java.io.Serializable;

/**
 * 文件生成的对象基类
 * 
 * @author huzexiong
 *
 */
public interface Maker extends Serializable {

  String getPath();
  
  void setPath(String path);
  
}
