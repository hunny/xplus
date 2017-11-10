package com.example.bootweb.angularjs.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = { "/md" })
public class MarkdownController {

  @RequestMapping(value = "list", //
      method = RequestMethod.GET, //
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public List<String> list() {
    List<String> list = new ArrayList<String>();
    list.add("Hello");
    // 查询当前classpath路径下的所有md文件，并全部返回
    return list;
  }

  @RequestMapping(value = "html", method = RequestMethod.GET)
  public String html() {
    return "html";
  }

}
