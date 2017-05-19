package com.xplus.commons.mvn.api;

/**
 * 生成文件
 * 
 * @author huzexiong
 *
 */
public interface FileMaker<T extends Maker> {

  void make(T t);

}
