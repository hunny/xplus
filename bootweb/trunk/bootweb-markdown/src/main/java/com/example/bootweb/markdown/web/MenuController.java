package com.example.bootweb.markdown.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/menu", //
    produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MenuController {

  @GetMapping(value = { "/list" })
  public List<List<MenuItem>> list() {
    List<List<MenuItem>> result = new ArrayList<>();
    
    List<MenuItem> list = new ArrayList<>();
    list.add(new MenuItem("菜单名称"));
    list.add(new MenuItem("tool", "命令行创建工程"));
    list.add(new MenuItem("tool2", "测试示例2"));
    list.add(new MenuItem("tool3", "测试示例3"));
    list.add(new MenuItem("md/file/search", "MD文件显示"));
    result.add(list);
    
    List<MenuItem> list2 = new ArrayList<>();
    list2.add(new MenuItem("菜单分组"));
    result.add(list2);
    return result;
  }

}
