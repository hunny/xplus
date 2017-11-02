package com.xplus.commons.mvn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.xplus.commons.mvn.impl.MavenProjectMaker;
import com.xplus.commons.mvn.impl.entity.BPom;

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
    new Application().run("spark");
  }

  public void run(final String moduleName) {
    System.setProperty("commons-ftl.file.forceOverWrite", "true");
    ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
        "classpath:applicationContext.xml");
    logger.info("Application get started...");

    String path = String.format("C:/work/xplus/commons/trunk/commons-%s/", moduleName);
    
    BPom parent = makeParent(path);
    BPom pom = makePom(path, moduleName, parent);
    MavenProjectMaker projectMaker = applicationContext.getBean(MavenProjectMaker.class);
    projectMaker.make(pom);
    applicationContext.close();
  }
  
  private BPom makeParent(String path) {
    BPom parent = new BPom();
    parent.setParent(null);
    parent.setPath(path);
    parent.setGroupId("com.xplus.commons");
    parent.setArtifactId("commons");
    parent.setVersion("0.1-SNAPSHOT");
    return parent;
  }
  
  private BPom makePom(String path, String artifactId, BPom parent) {
    BPom pom = new BPom();
    pom.setParent(parent);
    pom.setPath(path);
    pom.setGroupId(null == parent ? "com.xplus.commons" : parent.getGroupId());
    pom.setArtifactId((null == parent ? artifactId : String.format("%s-%s", parent.getArtifactId(), artifactId)));
    pom.setName(String.format("Tools for %s", artifactId));
    pom.setVersion(null == parent ? "0.1-SNAPSHOT" : parent.getVersion());
    pom.setDescription(String.format("%s", artifactId));
    return pom;
  }

}
