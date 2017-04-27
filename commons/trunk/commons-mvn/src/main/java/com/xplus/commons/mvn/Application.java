package com.xplus.commons.mvn;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.xplus.commons.mvn.api.ProjectMaker;

/**
 * @author huzexiong
 *
 */
public class Application {

  private final Logger logger = LoggerFactory.getLogger(Application.class);

  /**
   * @param args
   */
  public static void main(String[] args) {
    new Application().run("zookeeper");
  }

  public void run(final String moduleName) {
//     System.setProperty("commons-ftl.file.forceOverWrite", "true");
    ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
        "classpath:applicationContext.xml");
    logger.info("Application get started...");

    ProjectMaker projectMaker = applicationContext.getBean(ProjectMaker.class);
    Map<String, Object> object = new HashMap<String, Object>();
    object.put("pom", makeModulePom(moduleName));
    object.put("log4j", makeModuleLog4j(moduleName));
    object.put("eclipse", makeEclipse(moduleName));

    projectMaker.make(object, String.format("C:/work/xplus/commons/trunk/commons-%s/", moduleName));
    applicationContext.close();
  }

  private Map<String, Object> makeModulePom(final String moduleName) {
    Map<String, Object> object = new HashMap<String, Object>();
    Map<String, Object> parent = new HashMap<String, Object>();
    parent.put("groupId", "com.xplus.commons");
    parent.put("artifactId", "commons");
    parent.put("version", "0.1-SNAPSHOT");
    object.put("parent", parent);
    object.put("artifactId", String.format("commons-%s", moduleName));
    object.put("name", String.format("Tools for %s", moduleName));
    object.put("description", String.format("%s", moduleName));
    return object;
  }

  private Map<String, Object> makeModuleLog4j(final String moduleName) {
    Map<String, Object> object = new HashMap<String, Object>();
    object.put("log4jPath", "D:/test");
    object.put("log4jName", String.format("commons-%s", moduleName));
    object.put("packageName", String.format("com.xplus.commons.%s", moduleName));
    return object;
  }
  
  private Map<String, Object> makeEclipse(final String moduleName) {
    Map<String, Object> object = new HashMap<String, Object>();
    object.put("j2seName", "JavaSE-1.7");
    object.put("projectName", String.format("commons-%s", moduleName));
    object.put("projectNameDesc", String.format("%s", moduleName));
    return object;
  }

}
