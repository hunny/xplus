package com.xplus.commons.mvn.impl;

import java.io.File;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.xplus.commons.mvn.api.FileMaker;
import com.xplus.commons.mvn.impl.entity.BPropXml;
import com.xplus.commons.tpl.api.TemplateMaker;

/**
 * @author huzexiong
 *
 */
@Component(PropXmlFileMakerImpl.BEAN_ID)
public class PropXmlFileMakerImpl implements FileMaker<BPropXml> {

  private final Logger logger = LoggerFactory.getLogger(PropXmlFileMakerImpl.class);

  public static final String BEAN_ID = "commons-mvn.propXmlFileMakerImpl";
  public static final String XML_FTL = "xml.ftl";
  public static final String PROPERTIES_FTL = "properties.ftl";

  /**
   * 模板读写器
   */
  @Resource(name = "commons-tpl.freeMarkerTemplateMaker")
  private TemplateMaker templateMaker;

  /**
   * 模板的相对路径
   */
  @Value("${commons-mvn.tpl.path:META-INF/commons-mvn/ftl}")
  private String tplPath;

  @Override
  public void make(BPropXml t) {
    if (null == t || null == t.getPom()) {
      logger.debug("Pom info not found, ignore to generate files.");
      return;
    }
    makeMainPropXml(t);
    makeTestPropXml(t);
  }

  protected void makeMainPropXml(BPropXml t) {
    if (null == t.getJavaDir()) {
      logger.debug("Java dir info not found, ignore to generate files.");
      return;
    }
    logger.debug("Java dir File Maker Begin...");
    String artifactId = t.getPom().getArtifactId();
    t.setArtifactId(artifactId);
    String xmlPath = String.format("%s/%s/%s", //
        t.getJavaDir().getResourcePath(), //
        t.getJavaDir().getMetaInf(), //
        artifactId);
    if (!new File(xmlPath).mkdirs()) {
      logger.debug("make dirs [{}] failed.", xmlPath);
    }
    String tpl = String.format("%s/%s", tplPath, XML_FTL);
    String out = String.format("%s/%s", xmlPath, String.format("%s%s", artifactId, ".xml"));
    templateMaker.make(t, tpl, out);
    tpl = String.format("%s/%s", tplPath, PROPERTIES_FTL);
    out = String.format("%s/%s", //
        t.getJavaDir().getResourcePath(), //
        String.format("%s%s", artifactId, ".properties"));
    templateMaker.make(t, tpl, out);
  }

  protected void makeTestPropXml(BPropXml t) {
    if (null == t.getJavaTestDir()) {
      logger.debug("Java dir info not found, ignore to generate files.");
      return;
    }
    String artifactId = t.getPom().getArtifactId();
    String tpl = String.format("%s/%s", tplPath, XML_FTL);
    String out = String.format("%s/%s", //
        t.getJavaTestDir().getResourcePath(), //
        String.format("%s-%s", artifactId, "test.xml"));
    t.setArtifactId(String.format("%s-%s", artifactId, "test"));
    templateMaker.make(t, tpl, out);
    tpl = String.format("%s/%s", tplPath, PROPERTIES_FTL);
    out = String.format("%s/%s", //
        t.getJavaTestDir().getResourcePath(), //
        String.format("%s-%s", artifactId, "test.properties"));
    templateMaker.make(t, tpl, out);
    t.getPom().setArtifactId(artifactId);
  }

}
