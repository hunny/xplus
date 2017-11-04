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

  @RequestMapping("index")
  public String index() {
    return "Hello World!";
  }
  
}
```

运行Application，输入http://localhost:8080/index，测试SpringBoot是否正常运行。



