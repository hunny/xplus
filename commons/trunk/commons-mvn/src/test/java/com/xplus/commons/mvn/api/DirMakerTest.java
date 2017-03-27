/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	commons-mvn
 * 文件名：	DirMakerTest.java
 * 模块说明：	
 * 修改历史：
 * 2017年3月27日 - huzexiong - 创建。
 */
package com.xplus.commons.mvn.api;

import org.junit.Test;

import com.xplus.commons.mvn.impl.JavaDirMaker;

/**
 * @author huzexiong
 *
 */
public class DirMakerTest {

  @Test
  public void testMaker() {
    DirMaker dirMaker = new JavaDirMaker();
    dirMaker.make("C:/work/xplus/commons/trunk/commons-mvn");
  }
  
}
