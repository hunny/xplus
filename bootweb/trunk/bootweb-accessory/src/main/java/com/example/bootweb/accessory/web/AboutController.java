package com.example.bootweb.accessory.web;

import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bootweb.accessory.api.Httpable;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AboutController {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  @Autowired
  private Httpable<String> httpable;

  @GetMapping("/about")
  public ResponseEntity<String> getAbout() {
    
    logger.info("Receive about request.");
    
    return new ResponseEntity<String>("Template Project", HttpStatus.OK);
  }
  
  @GetMapping("/tianyancha/list/{name}")
  public ResponseEntity<String> list(@PathVariable String name) throws Exception {
//    String url = "https://www.baidu.com";
//    String url = "http://localhost:8081/about";
    
    String baseUrl = "https://m.tianyancha.com";
    String url = baseUrl + "/search?key=" + URLEncoder.encode(name, "UTF-8") + "&checkFrom=searchBox";
    String html = httpable.get(url);
    Document document = Jsoup.parse(html);
    // How to use select with https://jsoup.org/cookbook/extracting-data/selector-syntax
    // http://www.simplenetworks.io/blog/2015/5/26/managing-csrf-tokens-in-apache-httpclient-44x
    Elements elements = document.select("a.query_name");
    String href = null;
    for (Element element : elements) {
      if (name.equals(element.text())) {
        href = baseUrl + element.attr("href");
        break;
      }
    }
    if (null != href) {
      logger.info("查询到公司地址：{}", href);
      html = httpable.get(url);
      document = Jsoup.parse(html);
      logger.info("公司信息：{}", document.toString());
//      logger.info("邮箱信息：{}", document.select("div:has(span.icon-email417)").first().toString());
    }
    return new ResponseEntity<String>(elements.toString(), HttpStatus.OK);
  }
  
}
