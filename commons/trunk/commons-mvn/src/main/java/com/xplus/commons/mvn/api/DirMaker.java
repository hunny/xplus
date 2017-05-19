package com.xplus.commons.mvn.api;

/**
 * 生成目录
 * 
 * @author huzexiong
 *
 */
public interface DirMaker<T extends Maker> {
  
  void make(T t);

}
