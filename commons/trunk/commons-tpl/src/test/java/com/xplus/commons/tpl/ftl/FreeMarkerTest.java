package com.xplus.commons.tpl.ftl;

import java.io.File;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.xplus.commons.tpl.entity.ValueObjectTest;
import com.xplus.commons.tpl.impl.FreeMarkerTemplateMaker;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

/**
 * @author huzexiong
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "classpath:commons-tpl-test.xml", "classpath:/META-INF/commons-tpl/commons-ftl.xml" })
public class FreeMarkerTest {

  @Resource(name = "commons-tpl.freemarker.configuration")
  private Configuration configuration;

  @Resource(name = "commons-tpl.freeMarkerTemplateMaker")
  private FreeMarkerTemplateMaker freeMarkerTemplateMaker;

  @Test
  public void testStringFreeMarker() {
    try {
      String key = "test";
      StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
      stringTemplateLoader.putTemplate(key, "${name}，你好！${msg}");
      configuration.setTemplateLoader(stringTemplateLoader);

      Map<String, Object> root = new HashMap<String, Object>();
      root.put("name", "FreeMarker!");
      root.put("msg", "您已经完成了第一个FreeMarker的示例。");
      Template t = configuration.getTemplate(key);
      t.process(root, new OutputStreamWriter(System.out));
      System.out.println();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testFileFreeMarker() throws Exception {

    configuration.setClassForTemplateLoading(this.getClass(), "/");
    // Some other recommended settings:
    configuration.setDefaultEncoding("UTF-8");
    configuration.setLocale(Locale.US);
    configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

    Map<String, Object> input = new HashMap<String, Object>();

    input.put("title", "Vogella example");

    input.put("exampleObject", new ValueObjectTest("Java object", "me"));

    List<ValueObjectTest> systems = new ArrayList<ValueObjectTest>();
    systems.add(new ValueObjectTest("Android", "Google"));
    systems.add(new ValueObjectTest("iOS States", "Apple"));
    systems.add(new ValueObjectTest("Ubuntu", "Canonical"));
    systems.add(new ValueObjectTest("Windows7", "Microsoft"));
    input.put("systems", systems);

    Template template = configuration.getTemplate("tpls/helloworld.ftl");
    Writer consoleWriter = new OutputStreamWriter(System.out);

    template.process(input, consoleWriter);
    File file = new File("output.html");
    Writer fileWriter = new FileWriter(file);
    try {
      template.process(input, fileWriter);
    } finally {
      fileWriter.close();
    }
  }

  @Test
  public void testFreeMarkerTemplateMaker() {
    Map<String, Object> input = new HashMap<String, Object>();
    input.put("title", "Vogella example");
    input.put("exampleObject", new ValueObjectTest("Java object", "me"));
    List<ValueObjectTest> systems = new ArrayList<ValueObjectTest>();
    systems.add(new ValueObjectTest("Android", "Google"));
    systems.add(new ValueObjectTest("iOS States", "Apple"));
    systems.add(new ValueObjectTest("Ubuntu", "Canonical"));
    systems.add(new ValueObjectTest("Windows7", "Microsoft"));
    input.put("systems", systems);
    freeMarkerTemplateMaker.make(input, "tpls/helloworld.ftl", "classPathOutput.html");
  }

}
