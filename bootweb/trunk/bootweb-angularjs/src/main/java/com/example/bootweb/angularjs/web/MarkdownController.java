package com.example.bootweb.angularjs.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bootweb.angularjs.service.MarkdownService;
import com.ibm.icu.text.MessageFormat;

@RestController
@RequestMapping(value = { "/md" })
public class MarkdownController {

  @Value("${bootweb.markdown.file-path:}")
  private String filePath;
  
  @Autowired
  private MarkdownService markdownService;
  
  @RequestMapping(value = "list", //
      method = RequestMethod.GET, //
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public List<String> list(@RequestParam(name = "path", required = false) String path) {
    boolean absolute = true;
    if (StringUtils.isBlank(path)) {
      absolute = false;
      path = this.getClass().getResource("/").getPath();
    }
    // 查询当前classpath路径下的所有md文件，并全部返回
    return markdownService.list(path, absolute);
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
    Map<String, Object> result = new HashMap<String, Object>();
    result.put("html", markdownService.render(inputStream));
    return result;
  }

}
