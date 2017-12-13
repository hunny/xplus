package com.example.bootweb.markdown.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bootweb.markdown.service.FileService;
import com.example.bootweb.markdown.service.MarkdownService;
import com.ibm.icu.text.MessageFormat;

@RestController
@RequestMapping(value = { "/md" })
public class MarkdownController {

  @Autowired
  private MarkdownService markdownService;
  
  @Autowired
  private FileService fileService;

  @RequestMapping(value = "html", method = RequestMethod.GET)
  public Map<String, Object> text(@RequestParam(name = "filePath", required = false) String filePath)
      throws IOException {
    InputStream inputStream = null;
    Map<String, Object> result = new HashMap<String, Object>();
    if (StringUtils.isBlank(filePath)) {// 默认文件。
      result.put("html", "没有发现文件。");
      return result;
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
    result.put("html", markdownService.markdownToHtml(inputStream));
    return result;
  }
  
  @RequestMapping(value = "/file/list", method = RequestMethod.GET)
  public List<FileBean> list(@RequestParam(name = "path", required = false) String path) {
    if (StringUtils.isBlank(path)) {
      return Collections.emptyList();
    }
    List<FileBean> result = new ArrayList<>();
    List<String> list = fileService.list(path, true, ".md");
    for (String str : list) {
      File file = new File(str);
      String p = file.getAbsolutePath().replaceAll("\\\\", "/");
      if (!p.startsWith("/")) {
        p = "/" + p;
      }
      result.add(new FileBean(file.getName(), p));
    }
    return result;
  }

}
