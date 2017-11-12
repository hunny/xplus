package com.example.bootweb.angularjs.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.icu.text.MessageFormat;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.SimTocExtension;
import com.vladsch.flexmark.ext.typographic.TypographicExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;

@RestController
@RequestMapping(value = { "/md" })
public class MarkdownController {

  @Value("${bootweb.markdown.file-path:}")
  private String filePath;
  
  @RequestMapping(value = "list", //
      method = RequestMethod.GET, //
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public List<String> list() {
    List<String> list = new ArrayList<String>();
    list.add("Hello");
    // 查询当前classpath路径下的所有md文件，并全部返回
    return list;
  }

  @RequestMapping(value = "text", method = RequestMethod.GET)
  public Map<String, Object> text() throws IOException {
    InputStream inputStream = null;
    if (StringUtils.isBlank(filePath)) {// 默认文件。
      inputStream = this.getClass().getResourceAsStream("/AngularJS.md");
    } else {
      File targetFile = new File(filePath);
      if (!targetFile.exists()) {
        throw new IOException(// 
            MessageFormat.format("文件名称[{0}]不存在。", filePath));
      }
      if (!targetFile.isFile()) {
        throw new IOException(// 
            MessageFormat.format("文件名称[{0}]不是一个正常的文件。", filePath));
      }
      inputStream = new FileInputStream(targetFile);
    }
    MutableDataSet options = new MutableDataSet();
    options.set(HtmlRenderer.INDENT_SIZE, 2);
    options.set(HtmlRenderer.RENDER_HEADER_ID, true);
    // uncomment to set optional extensions
    options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(), //
        StrikethroughExtension.create(), //
        SimTocExtension.create(), //
        TypographicExtension.create()));

    // uncomment to convert soft-breaks to hard breaks
    options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");

    Parser parser = Parser.builder(options).build();
    HtmlRenderer renderer = HtmlRenderer.builder(options).build();

    Node document = parser.parseReader(new InputStreamReader(inputStream));
    Map<String, Object> result = new HashMap<String, Object>();
    result.put("html", renderer.render(document));
    return result;
  }

}
