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
