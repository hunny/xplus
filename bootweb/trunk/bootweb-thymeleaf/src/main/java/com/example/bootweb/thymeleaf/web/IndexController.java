package com.example.bootweb.thymeleaf.web;

import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.example.bootweb.thymeleaf.web.view.Dashboard;
import com.example.bootweb.thymeleaf.web.view.Index;

@Controller
public class IndexController {

  private final Logger logger = LoggerFactory.getLogger(IndexController.class);
  
  @Value("${dashboard.number:4}")
  private Integer dashboardNumber;
  
  @Autowired
  private RequestMappingHandlerMapping handlerMapping;
  
  @RequestMapping(value = {"/index.html", "/index", "/"})
  public String index(Model model) {
    model.addAttribute("title", "Thymeleaf示例的标题");
    Index index = new Index();
    
    index.getDashboards().add(make("Git 入门到专家指南", //
        "https://progit.bootcss.com/", //
        "data:image/gif;base64,R0lGODlhAQABAIAAAHd3dwAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw==", //
        "Git中文版（第二版）是一本详细的Git指南，主要介绍了Git的使用基础和原理，让你从Git初学者成为Git专家。"));
    
    index.getDashboards().add(make("Metro 风格的 Bootstrap", //
        "http://www.bootcss.com/p/flat-ui/", //
        "data:image/gif;base64,R0lGODlhAQABAIAAAHd3dwAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw==", //
        "Flat UI 是基于Bootstrap做的Metro化改造。Flat UI包含了很多Bootstrap提供的组件，但是外观更加漂亮。"));
    
    index.getDashboards().add(make("菜鸟教程", //
        "http://www.runoob.com/", //
        "data:image/gif;base64,R0lGODlhAQABAIAAAHd3dwAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw==", //
        "大量入门级的菜鸟教程，包括前端、服务端入门级教程。"));
    
    index.getDashboards().add(make("Bootstrap框架", //
        "http://www.bootcss.com/", //
        "data:image/gif;base64,R0lGODlhAQABAIAAAHd3dwAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw==", //
        "简洁、直观、强悍的前端开发框架，让web开发更迅速、简单。"));
    
    index.setDashboards(new ArrayList<>(index.getDashboards()).subList(0, //
        index.getDashboards().size() >= dashboardNumber ? dashboardNumber : index.getDashboards().size()));
    
    index.getList().addAll(index.getDashboards());
    index.getList().add(make("Thymeleaf模板", //
        "http://www.thymeleaf.org/", //
        null, //
        "Thymeleaf is a modern server-side Java template engine for both web and standalone environments."));
    
    index.getList().add(make("git - 简易指南", //
        "http://www.bootcss.com/p/git-guide/", //
        null, //
        "助你开始使用 git 的简易指南，木有高深内容。"));
    
    index.getList().add(make("Layui", //
        "http://www.layui.com/", //
        null, //
        "由职业前端倾情打造，面向所有层次的前后端程序猿，零门槛开箱即用的前端UI解决方案。"));
    
    index.getList().add(make("WebJars", //
        "http://www.webjars.org/", //
        null, //
        "WebJars are client-side web libraries (e.g. jQuery & Bootstrap) packaged into JAR (Java Archive) files."));
    
    index.getList().add(make("Angularjs", //
        "https://angularjs.org/", //
        null, //
        "Angularjs官网。"));

    index.getList().add(make("angular.cn", //
        "https://www.angular.cn/", //
        null, //
        "除英文版之外，该中文版是第一个由官方正式发布的开发文档，跟官方网站保持同步更新的中文版。"));
    
    index.getList().add(make("JHipster", //
        "http://www.jhipster.tech/", //
        null, //
        "JHipster is a development platform to generate, develop and deploy Spring Boot + Angular Web applications and Spring microservices. 。"));
    
    index.getList().add(make("flexmark-java", //
        "https://github.com/vsch/flexmark-java", //
        null, //
        "flexmark-java is a Java implementation of CommonMark 0.28 spec parser using the blocks first, inlines after Markdown parsing architecture."));
    
    index.getList().add(make("深入学习微框架：Spring Boot", //
        "http://www.infoq.com/cn/articles/microframeworks1-spring-boot", //
        null, //
        "关于作者Daniel Woods是Netflix的高级软件工程师，负责开发持续交付和云部署工具。"));
    
    index.getList().add(make("Windows下配置Git服务器和客户端", //
        "http://www.cnblogs.com/lwme/archive/2012/12/25/configuring-git-server-and-client-on-windows.html", //
        null, //
        "Windows下配置Git服务器和客户端"));
    
    index.getList().add(make("Git本地服务器搭建及使用", //
        "http://www.cnblogs.com/linsanshu/p/5512038.html", //
        null, //
        "-"));
    
    index.getList().add(make("Github spring-projects", //
        "https://github.com/spring-projects", //
        null, //
        "-"));
    
    index.getList().add(make("Github spring-guides", //
        "https://github.com/spring-guides/", //
        null, //
        "-"));
    
    index.getList().add(make("Gradle快速入门", //
        "http://www.cnblogs.com/davenkin/p/gradle-learning-1.html", //
        null, //
        "-"));
    
    index.getList().add(make("比较好的文章，书籍等资源", //
        "https://github.com/qibaoguang/Study-Step-by-Step", //
        null, //
        "-"));
    
    index.getList().add(make("免费的编程中文书籍索引", //
        "https://github.com/justjavac/free-programming-books-zh_CN", //
        null, //
        "-"));
    
    index.getList().add(make("数据挖掘&机器学习", //
        "http://blog.csdn.net/qq1175421841/article/details/51222769", //
        null, //
        "-"));
    
    index.getList().add(make("Jersey guide", //
        "https://jersey.github.io/documentation/latest/getting-started.html", //
        null, //
        "-"));
    
    index.getList().add(make("Java 8新特性终极指南", //
        "http://www.importnew.com/11908.html", //
        null, //
        "-"));
    
    index.getList().add(make("springfox-demos", //
        "https://github.com/springfox/springfox-demos", //
        null, //
        "-"));
    
    index.getList().add(make("swaggerhub.com", //
        "https://app.swaggerhub.com/help/index", //
        null, //
        "-"));
    
    index.getList().add(make("Spring Boot RESTful API Documentation With Swagger 2", //
        "https://dzone.com/articles/spring-boot-restful-api-documentation-with-swagger", //
        null, //
        "-"));
    
    index.getList().add(make("baeldung.com - Setting Up Swagger 2 with a Spring REST API", //
        "http://www.baeldung.com/swagger-2-documentation-for-spring-rest-api", //
        null, //
        "-"));
    
    index.getList().add(make("《Spring Cloud微服务实战》作者", //
        "http://blog.didispace.com/", //
        null, //
        "-"));
    
    index.getList().add(make("AngularUI", //
        "https://angular-ui.github.io/", //
        null, //
        "AngularUI The companion suite(s) to the AngularJS framework."));
    
    index.getList().add(make("AngularUI", //
        "https://angular-ui.github.io/", //
        null, //
        "AngularUI The companion suite(s) to the AngularJS framework."));
    
    index.getList().add(make("Prism", //
        "http://prismjs.com/download.html", //
        null, //
        "Prism is a lightweight, extensible syntax highlighter"));
    
    index.getList().add(make("github-markdown-css", //
        "https://github.com/sindresorhus/github-markdown-css", //
        null, //
        "The minimal amount of CSS to replicate the GitHub Markdown style"));
    
    index.getList().add(make("markdown-css-themes", //
        "https://github.com/jasonm23/markdown-css-themes", //
        null, //
        "Markdown css theme collection"));
    
    model.addAttribute("data", index);
    
    model.addAttribute("handlerMethods", handlerMapping.getHandlerMethods());
    for (Map.Entry<RequestMappingInfo, HandlerMethod> map : handlerMapping.getHandlerMethods().entrySet()) {
      RequestMappingInfo info = map.getKey();
      logger.info("{}-{}", map.getKey(), map.getValue());
    }
    
//    StringBuilder builder = new StringBuilder("{");
//    builder.append(this.patternsCondition);
//    if (!this.methodsCondition.isEmpty()) {
//      builder.append(",methods=").append(this.methodsCondition);
//    }
//    if (!this.paramsCondition.isEmpty()) {
//      builder.append(",params=").append(this.paramsCondition);
//    }
//    if (!this.headersCondition.isEmpty()) {
//      builder.append(",headers=").append(this.headersCondition);
//    }
//    if (!this.consumesCondition.isEmpty()) {
//      builder.append(",consumes=").append(this.consumesCondition);
//    }
//    if (!this.producesCondition.isEmpty()) {
//      builder.append(",produces=").append(this.producesCondition);
//    }
//    if (!this.customConditionHolder.isEmpty()) {
//      builder.append(",custom=").append(this.customConditionHolder);
//    }
//    builder.append('}');
    
    return "index";
  }
  
  private Dashboard make(String text, String url, String src, String desc) {
    Dashboard dashboard = new Dashboard();
    dashboard.setText(text);
    dashboard.setSrc(src);
    dashboard.setUrl(url);
    dashboard.setDesc(desc);
    return dashboard;
  }
  
}
