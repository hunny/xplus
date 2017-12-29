package com.xplus.commons.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import abc.test.BeanProxy;
import abc.test.User;

public class CompilerTest {
  JavaStringCompiler compiler;

  @Before
  public void setUp() throws Exception {
    compiler = new JavaStringCompiler();
  }

  static final String SINGLE_JAVA = "/* a single java class to one file */  "
      + "package abc.test;                                            "
      + "public class UserProxy extends User implements BeanProxy {     "
      + "    boolean dirty = false;                                     "
      + "    public void setId(String id) {                             "
      + "        super.setId(id);                                       "
      + "        setDirty(true);                                        "
      + "    }                                                          "
      + "    public void setName(String name) {                         "
      + "        super.setName(name);                                   "
      + "        setDirty(true);                                        "
      + "    }                                                          "
      + "    public void setCreated(long created) {                     "
      + "        super.setCreated(created);                             "
      + "        setDirty(true);                                        "
      + "    }                                                          "
      + "    public void setDirty(boolean dirty) {                      "
      + "        this.dirty = dirty;                                    "
      + "    }                                                          "
      + "    public boolean isDirty() {                                 "
      + "        return this.dirty;                                     "
      + "    }                                                          "
      + "}                                                              ";

  @Test
  public void testCompileSingleClass() throws Exception {
    Map<String, byte[]> results = compiler.compile("UserProxy.java", SINGLE_JAVA);
    assertEquals(1, results.size());
    assertTrue(results.containsKey("abc.test.UserProxy"));
    Class<?> clazz = compiler.loadClass("abc.test.UserProxy", results);
    // get method:
    Method setId = clazz.getMethod("setId", String.class);
    Method setName = clazz.getMethod("setName", String.class);
    Method setCreated = clazz.getMethod("setCreated", long.class);
    // try instance:
    Object obj = clazz.newInstance();
    // get as proxy:
    BeanProxy proxy = (BeanProxy) obj;
    assertFalse(proxy.isDirty());
    // set:
    setId.invoke(obj, "Hello world!");
    setName.invoke(obj, "Fly");
    setCreated.invoke(obj, 1234567890);
    // get as user:
    User user = (User) obj;
    assertEquals("Hello world!", user.getId());
    assertEquals("Fly", user.getName());
    assertEquals(1234567890, user.getCreated());
    assertTrue(proxy.isDirty());
  }

  static final String MULTIPLE_JAVA = "/* a single class to many files */   "
      + "package abc.test;                                            "
      + "import java.util.*;                                            "
      + "public class Multiple {                                        "
      + "    List<Bird> list = new ArrayList<Bird>();                   "
      + "    public void add(String name) {                             "
      + "        Bird bird = new Bird();                                "
      + "        bird.name = name;                                      "
      + "        this.list.add(bird);                                   "
      + "    }                                                          "
      + "    public Bird getFirstBird() {                               "
      + "        return this.list.get(0);                               "
      + "    }                                                          "
      + "    public static class StaticBird {                           "
      + "        public int weight = 100;                               "
      + "    }                                                          "
      + "    class NestedBird {                                         "
      + "        NestedBird() {                                         "
      + "            System.out.println(list.size() + \" birds...\");   "
      + "        }                                                      "
      + "    }                                                          "
      + "}                                                              "
      + "/* package level */                                            "
      + "class Bird {                                                   "
      + "    String name = null;                                        "
      + "}                                                              ";

  @Test
  public void testCompileMultipleClasses() throws Exception {
    Map<String, byte[]> results = compiler.compile("Multiple.java", MULTIPLE_JAVA);
    assertEquals(4, results.size());
    assertTrue(results.containsKey("abc.test.Multiple"));
    assertTrue(results.containsKey("abc.test.Multiple$StaticBird"));
    assertTrue(results.containsKey("abc.test.Multiple$NestedBird"));
    assertTrue(results.containsKey("abc.test.Bird"));
    Class<?> clzMul = compiler.loadClass("abc.test.Multiple", results);
    // try instance:
    Object obj = clzMul.newInstance();
    assertNotNull(obj);
  }
}
