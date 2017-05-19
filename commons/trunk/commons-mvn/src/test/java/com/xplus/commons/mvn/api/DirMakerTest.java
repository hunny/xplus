package com.xplus.commons.mvn.api;

import org.junit.Test;

import com.xplus.commons.mvn.impl.JavaDirMakerImpl;
import com.xplus.commons.mvn.impl.entity.BJavaDir;

/**
 * @author huzexiong
 *
 */
public class DirMakerTest {

  @Test
  public void testMaker() {
    DirMaker dirMaker = new JavaDirMakerImpl();
    BJavaDir javaDir = new BJavaDir();
    javaDir.setPath("C:/work/xplus/commons/trunk/commons-mvn");
    dirMaker.make(javaDir);
  }
  
}
