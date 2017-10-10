# 分布式配置中心(Spring Cloud Config)

## 简介

在分布式系统中，由于服务数量巨多，为了方便服务配置文件统一管理，实时更新，所以需要分布式配置中心组件。在Spring Cloud中，有分布式配置中心组件spring cloud config ，它支持配置服务放在配置服务的内存中（即本地），也支持放在远程Git仓库中。在spring cloud config 组件中，分两个角色，一是config server，二是config client。

## 构建Config Server

创建一个spring-boot项目，取名为config-server,其pom.xml:

```
<?xml version="1.0"?>
<project
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"
  xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>com.xplus.server</groupId>
    <artifactId>xplus-server</artifactId>
    <version>0.1-SNAPSHOT</version>
  </parent>
  <groupId>com.xplus.server.config.server</groupId>
  <artifactId>config-server</artifactId>
  <name>config-server</name>
  <url>http://maven.apache.org</url>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    <java.version>1.8</java.version>
  </properties>
  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <scope>test</scope>
    </dependency>

    <!--spring boot -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-actuator</artifactId>
      <scope>runtime</scope>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-devtools</artifactId>
      <optional>true</optional>
      <scope>runtime</scope>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
      <scope>runtime</scope>
    </dependency>
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-config-server</artifactId>
    </dependency>
    <!--eureka server -->
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-eureka</artifactId>
      <scope>runtime</scope>
    </dependency>
    <!-- spring boot test -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
      </plugin>
    </plugins>
  </build>

</project>
```

在程序的入口Application类加上@EnableConfigServer注解开启配置服务器的功能，代码如下：

```
package com.xplus.server.config.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
```

需要在程序的配置文件application.properties文件配置以下

```
spring.application.name=config-server
server.port=8888

spring.cloud.config.server.git.uri=https://github.com/hunny/xplus/
spring.cloud.config.server.git.searchPaths=server/trunk/config-server/src/main/resources/config
spring.cloud.config.label=master
#spring.cloud.config.server.git.username=
#spring.cloud.config.server.git.password=
```

- spring.cloud.config.server.git.uri：配置git仓库地址
- spring.cloud.config.server.git.searchPaths：配置仓库路径
- spring.cloud.config.label：配置仓库的分支
- spring.cloud.config.server.git.username：访问git仓库的用户名
- spring.cloud.config.server.git.password：访问git仓库的用户密码
如果Git仓库为公开仓库，可以不填写用户名和密码，如果是私有仓库需要填写。

远程仓库https://github.com/hunny/xplus/ 中的目录server/trunk/config-server/src/main/resources/config有个文件config-client-dev.properties文件中有一个属性：

```
foo = foo version 3
```

启动程序：访问http://localhost:8888/foo/dev

```
{"name":"foo","profiles":["dev"],"label":null,"version":null,"state":null,"propertySources":[]}
```

证明配置服务中心可以从远程程序获取配置信息。

http请求地址和资源文件映射如下:

- /{application}/{profile}[/{label}]
- /{application}-{profile}.yml
- /{label}/{application}-{profile}.yml
- /{application}-{profile}.properties
- /{label}/{application}-{profile}.properties

## 构建一个config client

重新创建一个springboot项目，取名为config-client,其pom文件：

```
<?xml version="1.0"?>
<project
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"
  xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>com.xplus.server</groupId>
    <artifactId>xplus-server</artifactId>
    <version>0.1-SNAPSHOT</version>
  </parent>
  <groupId>com.xplus.server.config.client</groupId>
  <artifactId>config-client</artifactId>
  <name>config-client</name>
  <url>http://maven.apache.org</url>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    <java.version>1.8</java.version>
  </properties>
  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <scope>test</scope>
    </dependency>

    <!--spring boot -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-actuator</artifactId>
      <scope>runtime</scope>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-devtools</artifactId>
      <optional>true</optional>
      <scope>runtime</scope>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
      <scope>runtime</scope>
    </dependency>
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-config</artifactId>
    </dependency>
    <!-- spring boot test -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
      </plugin>
    </plugins>
  </build>

</project>
```

其配置文件bootstrap.properties：
```
spring.application.name=config-client
spring.cloud.config.label=master
spring.cloud.config.profile=dev
spring.cloud.config.uri= http://localhost:8888/
server.port=8881
```

- spring.cloud.config.label 指明远程仓库的分支
- spring.cloud.config.profile
  + dev开发环境配置文件
  + test测试环境
  + pro正式环境

- spring.cloud.config.uri= http://localhost:8888/ 指明配置服务中心的网址。

程序的入口类，写一个API接口“／hi”，返回从配置中心读取的foo变量的值，代码如下：

```
package com.xplus.server.config.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Value("${foo}")
  private String foo;

  @RequestMapping(value = "/hi")
  public String hi() {
    return foo;
  }

}
```

打开网址访问：http://localhost:8881/hi，网页显示：
```
foo version 3
```
这就说明，config-client从config-server获取了foo的属性，而config-server是从git仓库读取的

具体配置参见[Spring Cloud Config](http://projects.spring.io/spring-cloud/spring-cloud.html#_spring_cloud_config)。
