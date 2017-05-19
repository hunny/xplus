/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2017，所有权利保留。
 * 
 * 项目名：	commons-mvn
 * 文件名：	ClassPathFileMaker.java
 * 模块说明：	
 * 修改历史：
 * 2017年5月19日 - huzexiong - 创建。
 */
package com.xplus.commons.mvn.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.xplus.commons.mvn.api.FileMaker;
import com.xplus.commons.mvn.impl.entity.BClassPath;
import com.xplus.commons.tpl.api.TemplateMaker;

/**
 * @author huzexiong
 *
 */
@Component(ClassPathFileMakerImpl.BEAN_ID)
public class ClassPathFileMakerImpl implements FileMaker<BClassPath> {
  
  private final Logger logger = LoggerFactory.getLogger(ClassPathFileMakerImpl.class);

  public static final String BEAN_ID = "commons-mvn.classPathFileMakerImpl";

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
  public void make(BClassPath t) {
    makeClasspath(t);
  }
  
  protected void makeClasspath(BClassPath t) {
    if (null == t || StringUtils.isEmpty(t.getJ2seName())) {
      logger.debug("classpath info not found, ignore files.");
      return;
    }
    String tpl = String.format("%s/%s", tplPath, "classpath.ftl");
    String outPath = String.format("%s/%s", t.getPath(), ".classpath");
    logger.debug(".classpath File Maker Begin [{}]...", tpl);
    templateMaker.make(t, tpl, outPath);
    logger.debug(".classpath File Maker Done[{}].", outPath);
  }


}
