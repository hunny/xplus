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

  @RequestMapping(value = { "/index.html", "/index", "/" })
  public String index(Model model) {
    model.addAttribute("title", "Thymeleaf示例的标题");
    Index index = new Index();

    index.getDashboards().add(make("Git 入门到专家指南", //
        "https://progit.bootcss.com/", //
        "data:image/gif;base64,R0lGODlhAQABAIAAAHd3dwAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw==", //
        "Git中文版（第二版）是一本详细的Git指南，主要介绍了Git的使用基础和原理，让你从Git初学者成为Git专家。"));

    index.getDashboards().add(make("Git 通俗易懂", //
        "https://www.liaoxuefeng.com/wiki/0013739516305929606dd18361248578c67b8067c8c017b000", //
        "data:image/gif;base64,R0lGODlhAQABAIAAAHd3dwAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw==", //
        "Git中文版浅显易懂的Git教程。"));

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
        "Prism is a lightweight, extensible syntax highlighter. http://prismjs.com/extending.html#api"));

    index.getList().add(make("github-markdown-css", //
        "https://github.com/sindresorhus/github-markdown-css", //
        null, //
        "The minimal amount of CSS to replicate the GitHub Markdown style"));

    index.getList().add(make("markdown-css-themes", //
        "https://github.com/jasonm23/markdown-css-themes", //
        null, //
        "Markdown css theme collection"));

    index.getList().add(make("JHipster", //
        "http://www.jhipster.tech/", //
        null, //
        "Goal is to generate for a complete and modern Web app or microservice architecture."));
    
    index.getList().add(make("Spring Cloud中文网-官方文档中文版", //
        "https://springcloud.cc/", //
        null, //
        "Spring Cloud 微服务架构集大成者，云计算最佳业务实践。"));
    
    index.getList().add(make("Spring Cloud 官网", //
        "http://projects.spring.io/spring-cloud/", //
        null, //
        "Spring Cloud 微服务架构集大成者，云计算最佳业务实践。"));

    index.getList().add(make("Spring Cloud 官网", //
        "http://projects.spring.io/spring-cloud/", //
        null, //
        "Spring Cloud 微服务架构集大成者，云计算最佳业务实践。"));
    
    index.getList().add(make("Apache Spark官方示例", //
        "https://github.com/apache/spark", //
        null, //
        "Apache Spark官方示例。"));

    index.getList().add(make("Spark官方文档-Spark编程指南", //
        "http://coredumper.cn/index.php/2017/10/16/spark-programming-guide-3/", //
        null, //
        "Spark官方文档-Spark编程指南。"));

    index.getList().add(make("Spark SQL, DataFrames以及 Datasets 编程指南", //
        "http://ifeve.com/spark-sql-dataframes/", //
        null, //
        "《Spark 官方文档》Spark SQL, DataFrames 以及 Datasets 编程指南"));

    index.getList().add(make("Spark快速入门", //
        "http://ifeve.com/spark-quick-start/", //
        null, //
        "《Spark 官方文档》Spark快速入门"));

    index.getList().add(make("Spark Streaming编程指南", //
        "http://ifeve.com/spark-streaming-2/", //
        null, //
        "《Spark官方文档》Spark Streaming编程指南"));

    index.getList().add(make("Gitee Most Valuable Projects", //
        "https://gitee.com/explore/recommend", //
        null, //
        "GVP (Gitee Most Valuable Projects) - 码云最有价值开源项目计划是码云综合评定出的优秀开源项目的展示平台"));

    index.getList().add(make("Apache Spark 2.2.0中文文档", //
        "https://gitee.com/explore/recommend", //
        null, //
        "Apache Spark 2.2.0中文文档"));

    index.getList().add(make("Apache Spark 官方文档中文版", //
        "https://github.com/apachecn/spark-doc-zh", //
        null, //
        "Apache Spark 官方文档中文版"));

    index.getList().add(make("Git（win／Linux／Mac）图形化界面", //
        "https://www.cnblogs.com/tyxa/p/6135896.html", //
        null, //
        "Git各大平台（win／Linux／Mac）图形化界面客户端大汇总"));

    index.getList().add(make("VSCode（Visual Studio Code）", //
        "https://github.com/apachecn/spark-doc-zh/blob/master/help/vscode-windows-usage.md", //
        null, //
        "VSCode（Visual Studio Code）Windows 平台入门使用指南"));
    
    index.getList().add(make("码云 Gitee.com", //
        "https://gitee.com/", //
        null, //
        "码云 Gitee.com, http://git.oschina.net源码托管。"));

    index.getList().add(make("iBase4J的SpringBoot版本", //
        "https://gitee.com/iBase4J/iBase4J-SpringBoot", //
        null, //
        "iBase4J是Java语言的分布式系统架构。 使用Spring整合开源框架。"));

    index.getList().add(make("Spring Cloud可参考中文资料", //
        "https://gitee.com/didispace/SpringCloud-Learning", //
        null, //
        "Spring Cloud构建微服务架构的可参考中文资料。"));

    index.getList().add(make("Apache Spark 2.2.0 官方文档中文版", //
        "http://blog.csdn.net/u012185296/article/details/76855770", //
        null, //
        "Apache Spark 2.2.0 官方文档中文版（翻译完成 98%. 除 MLib 外） | ApacheCN。"));

    index.getList().add(make("机器学习博文", //
        "http://www.cnblogs.com/LeftNotEasy/tag/%E6%9C%BA%E5%99%A8%E5%AD%A6%E4%B9%A0/", //
        null, //
        "关注于 机器学习、数据挖掘、并行计算、数学。"));

    index.getList().add(make("最小二乘估计(Least Squares Estimator)的公式的推导", //
        "http://www.qiujiawei.com/linear-algebra-15/", //
        null, //
        "图形学、机器学习，以及各种有趣的数学。"));

    index.getList().add(make("Videos answering a \"what is _?\"", //
        "https://www.youtube.com/channel/UCYO_jab_esuFRV4b17AJtAw", //
        null, //
        "Videos answering a \"what is _?\" style question about Math."));
    
    index.getList().add(make("youtube-dl", //
        "https://github.com/rg3/youtube-dl", //
        null, //
        "Videos downloader."));
    
    index.getList().add(make("spring-cloud-examples", //
        "https://github.com/ityouknow/spring-cloud-examples", //
        null, //
        "spring-cloud-examples, 大话Spring Cloud."));
    
    index.getList().add(make("Spring Cloud都做了些什么？", //
        "http://www.ityouknow.com/springcloud/2017/11/02/framework-and-springcloud.html", //
        null, //
        "从架构演进的角度聊聊Spring Cloud都做了些什么？"));
    
    index.getList().add(make("Spring Boot相关的学习资料", //
        "http://www.ityouknow.com/springboot/2015/12/30/springboot-collect.html", //
        null, //
        "收集Spring Boot相关的学习资料"));
    
    index.getList().add(make("Spring Cloud相关的学习资料", //
        "http://www.ityouknow.com/springcloud/2016/12/30/springcloud-collect.html", //
        null, //
        "收集Spring Cloud相关的学习资料"));
    
    index.getList().add(make("使用Jenkins部署Spring Boot", //
        "http://www.ityouknow.com/springboot/2017/11/11/springboot-jenkins.html", //
        null, //
        "使用Jenkins部署Spring Boot"));
    
    index.getList().add(make("不错的博文", //
        "http://www.ityouknow.com/blog", //
        null, //
        "不错的博文"));

    index.getList().add(make("10条", //
        "http://www.10tiao.com/", //
        null, //
        "开发者公众号大全和技术文章"));

    model.addAttribute("data", index);

    model.addAttribute("handlerMethods", handlerMapping.getHandlerMethods());
    for (Map.Entry<RequestMappingInfo, HandlerMethod> map : handlerMapping.getHandlerMethods().entrySet()) {
      logger.info("{}-{}", map.getKey(), map.getValue());
    }

    return "index";
  }

  @SuppressWarnings("static-method")
  private Dashboard make(String text, String url, String src, String desc) {
    Dashboard dashboard = new Dashboard();
    dashboard.setText(text);
    dashboard.setSrc(src);
    dashboard.setUrl(url);
    dashboard.setDesc(desc);
    return dashboard;
  }

}
