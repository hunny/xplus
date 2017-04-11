package com.xplus.commons.mvn.api;

import java.util.Map;

/**
 * @author huzexiong
 *
 */
public interface ProjectMaker {
  
  void make(Map<String, Object> object, String path);

}
