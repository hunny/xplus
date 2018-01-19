package com.xplus.commons.compiler.javassist;

import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

public class JavassistTest {

  @BeforeClass
  public static void setUp() throws Exception {
    ClassPool pool = ClassPool.getDefault();
    pool.makeClass("test.Point");
    pool.get("test.Point").toClass();
  }

  @Test
  public void testBasic() throws Exception {
    ClassPool pool = ClassPool.getDefault();
    pool.makeClass("test.Rectangle");
    CtClass cc = pool.get("test.Rectangle");
    cc.setSuperclass(pool.get("test.Point"));
    cc.writeFile();
    Assert.assertEquals("相同", cc.toClass().getSuperclass().getName(), "test.Point");
  }

  /**
   * 一个简单使用测试。
   */
  @Test
  public void testCreateClass() throws Exception {
    ClassPool pool = ClassPool.getDefault();
    pool.makeClass("my.test.Point");
    CtClass cc = pool.get("my.test.Point");
    Class<?> c = cc.toClass();
    Assert.assertEquals("相同", "my.test.Point", c.getName());
  }

  @Test
  public void testModifyMethod() throws Exception {
    ClassPool cp = ClassPool.getDefault();
    CtClass cc = cp.get("com.xplus.commons.compiler.javassist.Hello");
    CtMethod m = cc.getDeclaredMethod("say");
    m.insertBefore("{ return \"Hello.say()\"; }");
    Class<?> c = cc.toClass();
    Hello h = (Hello) c.newInstance();
    Assert.assertEquals("相同", "Hello.say()", h.say());
    Assert.assertEquals("相同", "Hello.say()", new Hello().say());
  }

//  @Test
  public void testCreateMethod() throws Exception {
    ClassPool cp = ClassPool.getDefault();
//    cp.makeClass("my.test.PointOk");
//    CtClass cc = cp.get("my.test.PointOk");
    CtClass cc = cp.get("com.xplus.commons.compiler.javassist.Hello");
    cc.defrost();
    CtMethod m = CtNewMethod.make("public int xmove(int dx) { return dx + 10; }", cc);
    cc.addMethod(m);
    Class mClass = cc.toClass();
    cc.writeFile();
    for (Method me : mClass.getDeclaredMethods()) { // test print, ok
      System.out.println(me.getName());
    }
  }

}
