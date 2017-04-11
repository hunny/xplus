package com.xplus.commons.mvn.api;

import java.util.Map;

/**
 * 生成文件
 * 
 * @author huzexiong
 *
 */
public interface FileMaker {

  void make(Map<String, Object> object, String path);

}
