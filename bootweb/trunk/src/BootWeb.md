[TOC]

# BootWeb

## 使用Maven生成工程

### 生成父工程bootweb-parent

```
mvn archetype:generate -DgroupId=com.example.bootweb -DartifactId=bootweb-parent -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
```

生成工程后修改：
```
<packaging>pom</packaging>
```

### 生成SpringBoot服务工程bootweb-server

```
mvn archetype:generate -DgroupId=com.example.bootweb -DartifactId=bootweb-server -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
```

### 父工程bootweb-parent中引入SpringBoot管理

去掉`<dependencies>`，添加如下配置：

```
  <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-dependencies</artifactId>
        <version>${spring-boot.version}</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>

  <build>
    <pluginManagement>
      <plugins>
        <plugin>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-maven-plugin</artifactId>
          <version>${spring-boot.version}</version>
        </plugin>
      </plugins>
    </pluginManagement>
  </build>
```

### 服务工程bootweb-server中引入SpringBoot依赖

```
  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
      <scope>runtime</scope>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
    </dependency>
  </dependencies>
```

新建代码：

```
package com.example.bootweb.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
  
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
  
}
```

```
package com.example.bootweb.server.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

  @RequestMapping("about")
  public String about() {
    return "Hello World!";
  }
  
}
```

### 测试运行

运行Application，输入http://localhost:8080/about，测试SpringBoot是否正常运行。

## SpringBoot资源映射

### 静态资源位置

Spring Boot默认配置的`/**`映射到`/static`（或`/public` ，`/resources`，`/META-INF/resources`），`/webjars/**`会映射到`classpath:/META-INF/resources/webjars/`。
注意：上面的`/static`等目录都是在`classpath:`下面。
如果想增加如`/mystatic/**`映射到`classpath:/mystatic/`，可以让配置类继承`WebMvcConfigurerAdapter`，然后重写如下方法：

```
@Override
public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/mystatic/**")
            .addResourceLocations("classpath:/mystatic/");
}
```

这种方式会在默认的基础上增加`/mystatic/**`映射到`classpath:/mystatic/`，不会影响默认的方式，可以同时使用。

静态资源映射还有一个配置选项，为了简单这里用`.properties`方式书写：

```
spring.mvc.static-path-pattern=/** # Path pattern used for static resources.
```

这个配置会影响默认的`/**`，例如修改为`/static/**`后，只能映射如`/static/js/sample.js`这样的请求（修改前是`/js/sample.js`）。这个配置只能写一个值，不像大多数可以配置多个用逗号隔开的。

### 引入前端依赖

* 引入bootstrap

[下载地址](http://v3.bootcss.com/getting-started/#download)

* 引入jquery

[下载地址](http://jquery.com/download/)

* 引入angularjs

[下载地址](https://code.angularjs.org/)

* 或者直接使用[webjars](http://www.webjars.org/)

```
      <dependency>
        <groupId>org.webjars</groupId>
        <artifactId>bootstrap</artifactId>
        <version>${webjars-bootstrap.version}</version>
      </dependency>
      <dependency>
        <groupId>org.webjars</groupId>
        <artifactId>jquery</artifactId>
        <version>${webjars-jquery.version}</version>
      </dependency>
```

引用方式：

```
<link rel='stylesheet'
  href='/webjars/bootstrap/3.3.7/css/bootstrap.min.css'>
```

```
  <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
  <script src="/webjars/jquery/3.2.1/jquery.min.js"></script>
  <!-- Include all compiled plugins (below), or include individual files as needed -->
  <script src="/webjars/bootstrap/3.3.7/js/bootstrap.min.js"></script>
```

#### 忽略引入版本

* 追加如下依赖

```
<dependencies>
    <dependency>
        <groupId>org.webjars</groupId>
        <artifactId>webjars-locator</artifactId>
        <version>0.30</version>
    </dependency>
</dependencies>
```

* 引入文件变更

```
<link rel='stylesheet' href='/webjars/bootstrap/css/bootstrap.min.css'>
<script src="/webjars/jquery/jquery.min.js"></script>
<script src="/webjars/bootstrap/js/bootstrap.min.js"></script>
```

## 创建CDN静态资源工程

### 创建bootweb-cdn工程

使用maven创建：

```
mvn archetype:generate -DgroupId=com.example.bootweb -DartifactId=bootweb-cdn -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false -Dversion=0.0.1-SNAPSHOT
```

把bootweb-server工程中的asserts文件复制到bootweb-cdn工程下，删除bootweb-server目录下的asserts，同时在bootweb-server下引入:

```
    <dependency>
      <groupId>${project.groupId}</groupId>
      <artifactId>bootweb-cdn</artifactId>
      <version>${project.version}</version>
    </dependency>
```

## 使用thymeleaf

### 使用maven创建工程

```
mvn archetype:generate -DgroupId=com.example.bootweb -DartifactId=bootweb-thymeleaf -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false -Dversion=0.0.1-SNAPSHOT
```

### 引入依赖

```
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>
```

### 建新文件

* 在resources的templates目录下，新建index.html

```
<!DOCTYPE html>
<html lang="zh-CN" xmlns="http://www.w3.org/1999/xhtml"
  xmlns:th="http://www.thymeleaf.org">
<head>
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
<!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
<title th:text="${title}">Thymeleaf示例</title>

<!-- Bootstrap -->
<link rel='stylesheet' href='/webjars/bootstrap/css/bootstrap.min.css' />

<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<link href="/assets/css/ie10-viewport-bug-workaround.css"
  rel="stylesheet" />

<!-- Custom styles for this template -->
<link href="/assets/css/dashboard.css" rel="stylesheet" />

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
      <script src="/assets/lib/html5shiv/3.7.3/html5shiv.min.js"></script>
      <script src="/assets/lib/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
  <nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container-fluid">
      <div class="navbar-header">
        <button type="button" class="navbar-toggle collapsed"
          data-toggle="collapse" data-target="#navbar"
          aria-expanded="false" aria-controls="navbar">
          <span class="sr-only">Toggle navigation</span> <span
            class="icon-bar"></span> <span class="icon-bar"></span> <span
            class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="#">Project name</a>
      </div>
      <div id="navbar" class="navbar-collapse collapse">
        <ul class="nav navbar-nav navbar-right">
          <li><a href="#">Dashboard</a></li>
          <li><a href="#">Settings</a></li>
          <li><a href="#">Profile</a></li>
          <li><a href="#">Help</a></li>
        </ul>
        <form class="navbar-form navbar-right">
          <input type="text" class="form-control"
            placeholder="Search..." />
        </form>
      </div>
    </div>
  </nav>

  <div class="container-fluid">
    <div class="row">
      <div class="col-sm-3 col-md-2 sidebar">
        <ul class="nav nav-sidebar">
          <li class="active"><a href="#">Overview <span
              class="sr-only">(current)</span></a></li>
          <li><a href="#">Reports</a></li>
          <li><a href="#">Analytics</a></li>
          <li><a href="#">Export</a></li>
        </ul>
        <ul class="nav nav-sidebar">
          <li><a href="">Nav item</a></li>
          <li><a href="">Nav item again</a></li>
          <li><a href="">One more nav</a></li>
          <li><a href="">Another nav item</a></li>
          <li><a href="">More navigation</a></li>
        </ul>
        <ul class="nav nav-sidebar">
          <li><a href="">Nav item again</a></li>
          <li><a href="">One more nav</a></li>
          <li><a href="">Another nav item</a></li>
        </ul>
      </div>
      <div
        class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <h1 class="page-header">一级菜单</h1>

        <div class="row placeholders">
          <div th:each="dashboard : ${data.dashboards}"
            class="col-xs-6 col-sm-3 placeholder">
            <img th:src="${dashboard.src}" width="200" height="200"
              class="img-responsive" th:alt="${dashboard.desc}" />
            <h4>
              <a th:href="${dashboard.url}" target="_blank"
                th:text="${dashboard.text}"></a>
            </h4>
            <span class="text-muted" th:text="${dashboard.desc}"></span>
          </div>
        </div>

        <h2 class="sub-header">二级菜单</h2>
        <div class="panel panel-default">
          <!-- Default panel contents -->
          <div class="panel-heading">面板头部</div>
          <div class="panel-body">
            <p>
              <a href="http://v3.bootcss.com/css/" target="_blank">全局
                CSS 样式</a>：设置全局 CSS 样式；基本的 HTML 元素均可以通过 class
              设置样式并得到增强效果；还有先进的栅格系统。
            </p>
            <p>
              <a href="http://v3.bootcss.com/components/"
                target="_blank">组件</a>：无数可复用的组件，包括字体图标、下拉菜单、导航、警告框、弹出框等更多功能。
            </p>
          </div>
          <div class="table-responsive">
            <table class="table table-bordered">
              <thead>
                <tr>
                  <th>序号</th>
                  <th>标题</th>
                  <th>说明</th>
                </tr>
              </thead>
              <tbody>
                <tr th:each="dashboard, stat : ${data.dashboards}">
                  <td th:text="${stat.index + 1}">&nbsp;</td>
                  <td><a th:href="${dashboard.url}" target="_blank" th:text="${dashboard.text}"></a></td>
                  <td th:text="${dashboard.desc}">&nbsp;</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- Bootstrap core JavaScript
  ================================================== -->
  <!-- Placed at the end of the document so the pages load faster -->
  <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
  <script src="/webjars/jquery/jquery.min.js"></script>
  <!-- Include all compiled plugins (below), or include individual files as needed -->
  <script src="/webjars/bootstrap/js/bootstrap.min.js"></script>
</body>
</html>
```

其中需要注意头部：

```
<html lang="zh-CN" xmlns="http://www.w3.org/1999/xhtml"
  xmlns:th="http://www.thymeleaf.org">
```

* 新建Application的SpringBoot启动程序：

```
package com.example.bootweb.thymeleaf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
  
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
  
}
```

* 新建Controller文件：

```
package com.example.bootweb.thymeleaf.web;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.bootweb.thymeleaf.web.view.Dashboard;
import com.example.bootweb.thymeleaf.web.view.Index;

@Controller
public class IndexController {

  @Value("${dashboard.number:4}")
  private Integer dashboardNumber;
  
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
    
    model.addAttribute("data", index);
    
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

```

## 创建bootweb-markdown工程

命令行创建：

```
mvn archetype:generate -DgroupId=com.example.bootweb -DartifactId=bootweb-markdown -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
```


