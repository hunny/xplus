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
    new Application().run();
  }

  public void run() {
     System.setProperty("commons-ftl.file.forceOverWrite", "true");
    ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
        "classpath:applicationContext.xml");
    logger.info("Application get started...");

    ProjectMaker projectMaker = applicationContext.getBean(ProjectMaker.class);
    Map<String, Object> object = new HashMap<String, Object>();
    object.put("pom", makeModulePom());
    object.put("log4j", makeModuleLog4j());
    object.put("eclipse", makeEclipse());

    projectMaker.make(object, "D:/test/");
    applicationContext.close();
  }

  private Map<String, Object> makeModulePom() {
    Map<String, Object> object = new HashMap<String, Object>();
    Map<String, Object> parent = new HashMap<String, Object>();
    parent.put("groupId", "com.xplus.commons");
    parent.put("artifactId", "commons");
    parent.put("version", "0.1-SNAPSHOT");
    object.put("parent", parent);
    object.put("artifactId", "commons-tpl");
    object.put("name", "Maven Tools");
    object.put("description", "Maven Tools.");
    return object;
  }

  private Map<String, Object> makeModuleLog4j() {
    Map<String, Object> object = new HashMap<String, Object>();
    object.put("log4jPath", "D:/test");
    object.put("log4jName", "commons-tpl");
    object.put("packageName", "com.xplus.commons.tpl");
    return object;
  }
  
  private Map<String, Object> makeEclipse() {
    Map<String, Object> object = new HashMap<String, Object>();
    object.put("j2seName", "JavaSE-1.7");
    object.put("projectName", "commons-mvn");
    object.put("projectNameDesc", "工程描述");
    return object;
  }

}
