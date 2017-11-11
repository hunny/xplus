package com.example.bootweb.angularjs.web;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/example/asset")
public class AssetController {

  private final Logger logger = LoggerFactory.getLogger(getClass());

//  @RequestMapping(value = "/show.html", //
//      method = RequestMethod.GET) //
//  public String showhtml(Model model, //
//      @RequestParam(value = "html", required = false, //
//      defaultValue="请传入一段html") String html, //
//      @RequestParam(value = "js", required = false) String js) {
//    logger.debug("收到请求html[{}], javascript[{}]。");
//    model.addAttribute("html", html);
//    return "showhtml";
//  }
  @RequestMapping(value = "/show.html", //
      method = RequestMethod.GET) //
  public void showhtml(HttpServletResponse response, //
      @RequestParam(value = "html", required = false, //
      defaultValue="请传入一段html") String html, //
      @RequestParam(value = "js", required = false) String js) throws IOException {
    logger.debug("收到请求html[{}], javascript[{}]。");
    html = StringEscapeUtils.unescapeHtml4(html);
    response.getWriter().write(html);
    response.getWriter().flush();
  }

}
