package com.xplus.commons.compiler.official;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class CompilerJavaFile {

  public static void main(String[] args) throws Exception {

    beforeCheck();

    File[] files = new File[] { //
        // src/main/resources/ok/test/Test.java
        new File(CompilerJavaFile.class.getResource("/").getFile() //
            + "ok/test/Test.java") //
    }; // input for first compilation task

    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

    Iterable<? extends JavaFileObject> compilationUnits = //
        fileManager.getJavaFileObjectsFromFiles(Arrays.asList(files));
    Boolean result1 = compiler.getTask(null, //
        fileManager, //
        null, null, null, //
        compilationUnits) //
        .call();
    if (null != result1 && result1.booleanValue()) {// 编译完成
      loadTestClass();// 测试编译的类。
    }

    fileManager.close();
  }

  /**
   * 测试案例执行之前，确保动态编译的类，在当前环境中不存在。
   * 
   * @throws Exception
   */
  protected static void beforeCheck() throws Exception {
    try {
      loadTestClass();
      throw new Exception("类已经存在了，测试失败，请删除已经编译的类文件。");
    } catch (ClassNotFoundException e) {
      System.out.println("经过检查，测试类不存在。");
    }
  }

  /**
   * 测试编译后的类
   * 
   * @throws Exception
   */
  protected static void loadTestClass() throws Exception {
    Class<?> clazz = Class.forName("ok.test.Test");
    Method show = clazz.getMethod("show");
    Object object = clazz.newInstance();
    boolean showResult = (boolean) show.invoke(object);
    System.err.println("showResult:" + showResult);
  }
}
