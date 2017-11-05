package com.example.bootweb.markdown.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

  @RequestMapping(value = {"/index.html", "/index", "/"})
  public String index(Model model) {
    model.addAttribute("title", "Markdown示例的标题");
    return "index";
  }
  
}
