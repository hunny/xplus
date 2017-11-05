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
mvn archetype:generate -DgroupId=com.example.bootweb.server -DartifactId=bootweb-server -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
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



