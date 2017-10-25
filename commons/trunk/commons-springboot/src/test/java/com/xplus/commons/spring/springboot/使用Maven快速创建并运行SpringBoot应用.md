[TOC]

# 使用Maven快速创建并运行SpringBoot应用

创建一个Spring Boot的项目，其中一种方式是需要创建一个Maven的POM文件并继承 `spring-boot-starter-parent` 项目，然后添加一些 Spring Boot子项目的依赖，使用`spring-boot-maven-plugin`插件来创建可执行的 jar 包。

## 创建工程

### 使用maven创建一个工程：

在需要创建工程的目录中，输入如下命令：

```
$ mvn archetype:generate -DgroupId=com.javachen.example -DartifactId=spring-boot-example -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveModel=false
```

会在当前操作目录下创建文件夹名称为`spring-boot-example`的maven工程。其pom.xml如下：

```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.demo.example</groupId>
  <artifactId>spring-boot-example</artifactId>
  <packaging>jar</packaging>
  <version>1.0-SNAPSHOT</version>
  <name>spring-boot-example</name>
  <url>http://maven.apache.org</url>
  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
</project>

```

### 引入SprigBoot依赖

修改pom.xml，引入SpringBoot依赖：

```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.demo.example</groupId>
  <artifactId>spring-boot-example</artifactId>
  <packaging>jar</packaging>
  <version>1.0-SNAPSHOT</version>
  <name>spring-boot-example</name>
  <url>http://maven.apache.org</url>

  <!-- Inherit defaults from Spring Boot -->
  <parent>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-parent</artifactId>
      <version>1.5.4.RELEASE</version>
  </parent>

  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
    </dependency>

    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
  </dependencies>


  <!-- Package as an executable jar -->
  <build>
      <plugins>
          <plugin>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-maven-plugin</artifactId>
          </plugin>
      </plugins>
  </build>

  <!-- (you don't need this if you are using a .RELEASE version) -->
  <repositories>
      <repository>
          <id>spring-snapshots</id>
          <url>http://repo.spring.io/snapshot</url>
          <snapshots><enabled>true</enabled></snapshots>
      </repository>
      <repository>
          <id>spring-milestones</id>
          <url>http://repo.spring.io/milestone</url>
      </repository>
  </repositories>
  <pluginRepositories>
      <pluginRepository>
          <id>spring-snapshots</id>
          <url>http://repo.spring.io/snapshot</url>
      </pluginRepository>
      <pluginRepository>
          <id>spring-milestones</id>
          <url>http://repo.spring.io/milestone</url>
      </pluginRepository>
  </pluginRepositories>

</project>
```

如果不想继承`spring-boot-starter-parent`父项目，可以使用依赖管理来引入对 Spring Boot 的依赖。

```
<dependencyManagement>
     <dependencies>
        <dependency>
            <!-- Import dependency management from Spring Boot -->
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-dependencies</artifactId>
            <version>1.5.4.RELEASE</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

还可以通过下面方式修改 Maven 使用的 Java 版本：

```
<properties>
    <java.version>1.8</java.version>
</properties>
```

## Spring Boot CLI 使用方法

SpringBootCLI是一个命令行工具，可用于快速搭建基于Spring的原型。它支持运行Groovy脚本。[下载地址](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#getting-started-installing-the-cli)。

安装完成，可以检查版本：

```
$ spring --version
Spring Boot v1.5.4.RELEASE
```

spring 命令用法：

```
$ spring
usage: spring [--help] [--version]
       <command> [<args>]

Available commands are:

  run [options] <files> [--] [args]
    Run a spring groovy script

  ... more command help is shown here
```

以很快创建一个简单的web应用来测试是否安装成功。创建HelloWorld.groovy文件，内容如下：

```
@RestController
class Application {

    @RequestMapping("/hi")
    String home() {
        "Hello World!"
    }
}
```

然后，运行下面命令：

```
$ spring run HelloWorld.groovy
```

使用浏览器打开 http://localhost:8080/hi 你会看到下面的输出：

```
Hello World!
```

还可以传递一些命令行的参数，使用`--`分隔命令行参数，下面命令修改服务启动端口为 9000：

```
$ spring run HelloWorld.groovy --server.port=9000
```

设置 JVM 参数之后，在运行：

```
$ JAVA_OPTS=-Xmx1024m 
$ spring run HelloWorld.groovy
```

## 部署 Spring Boot 应用

通过下面命令查看项目依赖：

```
$ mvn dependency:tree
```

在src/main/java/com/demo/example下面修改App类：

```

```

- 说明：
  + @RestController和@RequestMapping注解是Spring MVC注解（它们不是Spring Boot的特定部分）。具体查看Spring参考文档的MVC章节。
  + @EnableAutoConfiguration。这个注解告诉Spring Boot根据添加的jar依赖猜测你想如何配置Spring。由于spring-boot-starter-web添加了Tomcat和Spring MVC，所以auto-configuration将假定你正在开发一个web应用并相应地对Spring进行设置。
  + main方法通过调用run，将业务委托给了Spring Boot的SpringApplication类。SpringApplication将引导我们的应用，启动Spring，相应地启动被自动配置的Tomcat web服务器。

如果pom.xml使用了`spring-boot-starter-parent`，则我们可以运行`mvn spring-boot:run`命令启动应用：

```
$ mvn spring-boot:run
```

输出如下日志：

```
C:\bootdemo\spring-boot-example>mvn spring-boot:run
[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Building spring-boot-example 1.0-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO]
[INFO] >>> spring-boot-maven-plugin:1.5.4.RELEASE:run (default-cli) > test-compile @ spring-boot-example >>>
[INFO]
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ spring-boot-example ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] skip non existing resourceDirectory C:\bootdemo\spring-boot-example\src\main\resources
[INFO] skip non existing resourceDirectory C:\bootdemo\spring-boot-example\src\main\resources
[INFO]
[INFO] --- maven-compiler-plugin:3.1:compile (default-compile) @ spring-boot-example ---
[INFO] Changes detected - recompiling the module!
[INFO] Compiling 1 source file to C:\bootdemo\spring-boot-example\target\classes
[INFO]
[INFO] --- maven-resources-plugin:2.6:testResources (default-testResources) @ spring-boot-example ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] skip non existing resourceDirectory C:\bootdemo\spring-boot-example\src\test\resources
[INFO]
[INFO] --- maven-compiler-plugin:3.1:testCompile (default-testCompile) @ spring-boot-example ---
[INFO] Changes detected - recompiling the module!
[INFO] Compiling 1 source file to C:\bootdemo\spring-boot-example\target\test-classes
[INFO]
[INFO] <<< spring-boot-maven-plugin:1.5.4.RELEASE:run (default-cli) < test-compile @ spring-boot-example <<<
[INFO]
[INFO] --- spring-boot-maven-plugin:1.5.4.RELEASE:run (default-cli) @ spring-boot-example ---

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v1.5.4.RELEASE)

2017-10-25 16:49:30.139  INFO 10128 --- [           main] com.demo.example.App                     : Starting App on user-PC with PID 10128 (C:\bootdemo\sp
ring-boot-example\target\classes started by user in C:\bootdemo\spring-boot-example)
2017-10-25 16:49:30.143  INFO 10128 --- [           main] com.demo.example.App                     : No active profile set, falling back to default profiles: de
fault
2017-10-25 16:49:30.188  INFO 10128 --- [           main] ationConfigEmbeddedWebApplicationContext : Refreshing org.springframework.boot.context.embedded.Annota
tionConfigEmbeddedWebApplicationContext@2c6124e0: startup date [Wed Oct 25 16:49:30 CST 2017]; root of context hierarchy
2017-10-25 16:49:31.269  INFO 10128 --- [           main] s.b.c.e.t.TomcatEmbeddedServletContainer : Tomcat initialized with port(s): 8080 (http)
2017-10-25 16:49:31.281  INFO 10128 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2017-10-25 16:49:31.282  INFO 10128 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet Engine: Apache Tomcat/8.5.15
2017-10-25 16:49:31.363  INFO 10128 --- [ost-startStop-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2017-10-25 16:49:31.363  INFO 10128 --- [ost-startStop-1] o.s.web.context.ContextLoader            : Root WebApplicationContext: initialization completed in 117
8 ms
2017-10-25 16:49:31.461  INFO 10128 --- [ost-startStop-1] o.s.b.w.servlet.ServletRegistrationBean  : Mapping servlet: 'dispatcherServlet' to [/]
2017-10-25 16:49:31.466  INFO 10128 --- [ost-startStop-1] o.s.b.w.servlet.FilterRegistrationBean   : Mapping filter: 'characterEncodingFilter' to: [/*]
2017-10-25 16:49:31.467  INFO 10128 --- [ost-startStop-1] o.s.b.w.servlet.FilterRegistrationBean   : Mapping filter: 'hiddenHttpMethodFilter' to: [/*]
2017-10-25 16:49:31.467  INFO 10128 --- [ost-startStop-1] o.s.b.w.servlet.FilterRegistrationBean   : Mapping filter: 'httpPutFormContentFilter' to: [/*]
2017-10-25 16:49:31.470  INFO 10128 --- [ost-startStop-1] o.s.b.w.servlet.FilterRegistrationBean   : Mapping filter: 'requestContextFilter' to: [/*]
2017-10-25 16:49:31.710  INFO 10128 --- [           main] s.w.s.m.m.a.RequestMappingHandlerAdapter : Looking for @ControllerAdvice: org.springframework.boot.con
text.embedded.AnnotationConfigEmbeddedWebApplicationContext@2c6124e0: startup date [Wed Oct 25 16:49:30 CST 2017]; root of context hierarchy
2017-10-25 16:49:31.772  INFO 10128 --- [           main] s.w.s.m.m.a.RequestMappingHandlerMapping : Mapped "{[/hi]}" onto public java.lang.String com.demo.exam
ple.App.home()
2017-10-25 16:49:31.776  INFO 10128 --- [           main] s.w.s.m.m.a.RequestMappingHandlerMapping : Mapped "{[/error]}" onto public org.springframework.http.Re
sponseEntity<java.util.Map<java.lang.String, java.lang.Object>> org.springframework.boot.autoconfigure.web.BasicErrorController.error(javax.servlet.http.HttpSer
vletRequest)
2017-10-25 16:49:31.777  INFO 10128 --- [           main] s.w.s.m.m.a.RequestMappingHandlerMapping : Mapped "{[/error],produces=[text/html]}" onto public org.sp
ringframework.web.servlet.ModelAndView org.springframework.boot.autoconfigure.web.BasicErrorController.errorHtml(javax.servlet.http.HttpServletRequest,javax.ser
vlet.http.HttpServletResponse)
2017-10-25 16:49:31.804  INFO 10128 --- [           main] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped URL path [/webjars/**] onto handler of type [class o
rg.springframework.web.servlet.resource.ResourceHttpRequestHandler]
2017-10-25 16:49:31.804  INFO 10128 --- [           main] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped URL path [/**] onto handler of type [class org.sprin
gframework.web.servlet.resource.ResourceHttpRequestHandler]
2017-10-25 16:49:31.841  INFO 10128 --- [           main] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped URL path [/**/favicon.ico] onto handler of type [cla
ss org.springframework.web.servlet.resource.ResourceHttpRequestHandler]
2017-10-25 16:49:31.948  INFO 10128 --- [           main] o.s.j.e.a.AnnotationMBeanExporter        : Registering beans for JMX exposure on startup
2017-10-25 16:49:32.012  INFO 10128 --- [           main] s.b.c.e.t.TomcatEmbeddedServletContainer : Tomcat started on port(s): 8080 (http)
2017-10-25 16:49:32.017  INFO 10128 --- [           main] com.demo.example.App                     : Started App in 2.161 seconds (JVM running for 5.023)
```

然后，使用浏览器打开 http://localhost:8080/ 你会看到下面的输出：

```
Hello World!
```

点击ctrl-c，可以关闭应用程序。

添加-Ddebug参数可以查看加载了哪些配置类：

```
$ mvn spring-boot:run -Ddebug
```

运行日志如下：

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v1.5.4.RELEASE)

2017-10-25 16:52:48.085  INFO 9484 --- [           main] com.demo.example.App                     : Starting App on user-PC with PID 9484 (C:\bootdemo\spri
ng-boot-example\target\classes started by user in C:\bootdemo\spring-boot-example)
2017-10-25 16:52:48.085  INFO 9484 --- [           main] com.demo.example.App                     : No active profile set, falling back to default profiles: def
ault
2017-10-25 16:52:48.087 DEBUG 9484 --- [           main] o.s.boot.SpringApplication               : Loading source class com.demo.example.App
2017-10-25 16:52:48.126  INFO 9484 --- [           main] ationConfigEmbeddedWebApplicationContext : Refreshing org.springframework.boot.context.embedded.Annotat
ionConfigEmbeddedWebApplicationContext@7d46fe65: startup date [Wed Oct 25 16:52:48 CST 2017]; root of context hierarchy
2017-10-25 16:52:48.130 DEBUG 9484 --- [           main] ationConfigEmbeddedWebApplicationContext : Bean factory for org.springframework.boot.context.embedded.A
nnotationConfigEmbeddedWebApplicationContext@7d46fe65: org.springframework.beans.factory.support.DefaultListableBeanFactory@5ef84f36: defining beans [org.spring
framework.context.annotation.internalConfigurationAnnotationProcessor,org.springframework.context.annotation.internalAutowiredAnnotationProcessor,org.springfram
ework.context.annotation.internalRequiredAnnotationProcessor,org.springframework.context.annotation.internalCommonAnnotationProcessor,org.springframework.contex
t.event.internalEventListenerProcessor,org.springframework.context.event.internalEventListenerFactory,app]; root of factory hierarchy
2017-10-25 16:52:48.783 DEBUG 9484 --- [           main] ationConfigEmbeddedWebApplicationContext : Unable to locate MessageSource with name 'messageSource': us
ing default [org.springframework.context.support.DelegatingMessageSource@1afeaa34]
2017-10-25 16:52:48.784 DEBUG 9484 --- [           main] ationConfigEmbeddedWebApplicationContext : Unable to locate ApplicationEventMulticaster with name 'appl
icationEventMulticaster': using default [org.springframework.context.event.SimpleApplicationEventMulticaster@7bbc6db0]
2017-10-25 16:52:49.087 DEBUG 9484 --- [           main] .t.TomcatEmbeddedServletContainerFactory : Code archive: C:\Users\user\.m2\repository\org\springfr
amework\boot\spring-boot\1.5.4.RELEASE\spring-boot-1.5.4.RELEASE.jar
2017-10-25 16:52:49.088 DEBUG 9484 --- [           main] .t.TomcatEmbeddedServletContainerFactory : Code archive: C:\Users\user\.m2\repository\org\springfr
amework\boot\spring-boot\1.5.4.RELEASE\spring-boot-1.5.4.RELEASE.jar
2017-10-25 16:52:49.088 DEBUG 9484 --- [           main] .t.TomcatEmbeddedServletContainerFactory : None of the document roots [src/main/webapp, public, static]
 point to a directory and will be ignored.
2017-10-25 16:52:49.121  INFO 9484 --- [           main] s.b.c.e.t.TomcatEmbeddedServletContainer : Tomcat initialized with port(s): 8080 (http)
2017-10-25 16:52:49.131  INFO 9484 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2017-10-25 16:52:49.134  INFO 9484 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet Engine: Apache Tomcat/8.5.15
2017-10-25 16:52:49.201  INFO 9484 --- [ost-startStop-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2017-10-25 16:52:49.202  INFO 9484 --- [ost-startStop-1] o.s.web.context.ContextLoader            : Root WebApplicationContext: initialization completed in 1080
 ms
2017-10-25 16:52:49.258 DEBUG 9484 --- [ost-startStop-1] o.s.b.w.s.ServletContextInitializerBeans : Added existing Servlet initializer bean 'dispatcherServletRe
gistration'; order=2147483647, resource=class path resource [org/springframework/boot/autoconfigure/web/DispatcherServletAutoConfiguration$DispatcherServletRegi
strationConfiguration.class]
2017-10-25 16:52:49.300 DEBUG 9484 --- [ost-startStop-1] o.s.b.w.s.ServletContextInitializerBeans : Created Filter initializer for bean 'characterEncodingFilter
'; order=-2147483648, resource=class path resource [org/springframework/boot/autoconfigure/web/HttpEncodingAutoConfiguration.class]
2017-10-25 16:52:49.301 DEBUG 9484 --- [ost-startStop-1] o.s.b.w.s.ServletContextInitializerBeans : Created Filter initializer for bean 'hiddenHttpMethodFilter'
; order=-10000, resource=class path resource [org/springframework/boot/autoconfigure/web/WebMvcAutoConfiguration.class]
2017-10-25 16:52:49.301 DEBUG 9484 --- [ost-startStop-1] o.s.b.w.s.ServletContextInitializerBeans : Created Filter initializer for bean 'httpPutFormContentFilte
r'; order=-9900, resource=class path resource [org/springframework/boot/autoconfigure/web/WebMvcAutoConfiguration.class]
2017-10-25 16:52:49.304 DEBUG 9484 --- [ost-startStop-1] o.s.b.w.s.ServletContextInitializerBeans : Created Filter initializer for bean 'requestContextFilter';
order=-105, resource=class path resource [org/springframework/boot/autoconfigure/web/WebMvcAutoConfiguration$WebMvcAutoConfigurationAdapter.class]
2017-10-25 16:52:49.311  INFO 9484 --- [ost-startStop-1] o.s.b.w.servlet.ServletRegistrationBean  : Mapping servlet: 'dispatcherServlet' to [/]
2017-10-25 16:52:49.316  INFO 9484 --- [ost-startStop-1] o.s.b.w.servlet.FilterRegistrationBean   : Mapping filter: 'characterEncodingFilter' to: [/*]
2017-10-25 16:52:49.317  INFO 9484 --- [ost-startStop-1] o.s.b.w.servlet.FilterRegistrationBean   : Mapping filter: 'hiddenHttpMethodFilter' to: [/*]
2017-10-25 16:52:49.318  INFO 9484 --- [ost-startStop-1] o.s.b.w.servlet.FilterRegistrationBean   : Mapping filter: 'httpPutFormContentFilter' to: [/*]
2017-10-25 16:52:49.321  INFO 9484 --- [ost-startStop-1] o.s.b.w.servlet.FilterRegistrationBean   : Mapping filter: 'requestContextFilter' to: [/*]
2017-10-25 16:52:49.340 DEBUG 9484 --- [ost-startStop-1] o.s.b.w.f.OrderedRequestContextFilter    : Initializing filter 'requestContextFilter'
2017-10-25 16:52:49.341 DEBUG 9484 --- [ost-startStop-1] o.s.b.w.f.OrderedRequestContextFilter    : Filter 'requestContextFilter' configured successfully
2017-10-25 16:52:49.341 DEBUG 9484 --- [ost-startStop-1] .s.b.w.f.OrderedHttpPutFormContentFilter : Initializing filter 'httpPutFormContentFilter'
2017-10-25 16:52:49.344 DEBUG 9484 --- [ost-startStop-1] .s.b.w.f.OrderedHttpPutFormContentFilter : Filter 'httpPutFormContentFilter' configured successfully
2017-10-25 16:52:49.347 DEBUG 9484 --- [ost-startStop-1] o.s.b.w.f.OrderedHiddenHttpMethodFilter  : Initializing filter 'hiddenHttpMethodFilter'
2017-10-25 16:52:49.350 DEBUG 9484 --- [ost-startStop-1] o.s.b.w.f.OrderedHiddenHttpMethodFilter  : Filter 'hiddenHttpMethodFilter' configured successfully
2017-10-25 16:52:49.353 DEBUG 9484 --- [ost-startStop-1] o.s.b.w.f.OrderedCharacterEncodingFilter : Initializing filter 'characterEncodingFilter'
2017-10-25 16:52:49.355 DEBUG 9484 --- [ost-startStop-1] o.s.b.w.f.OrderedCharacterEncodingFilter : Filter 'characterEncodingFilter' configured successfully
2017-10-25 16:52:49.547  INFO 9484 --- [           main] s.w.s.m.m.a.RequestMappingHandlerAdapter : Looking for @ControllerAdvice: org.springframework.boot.cont
ext.embedded.AnnotationConfigEmbeddedWebApplicationContext@7d46fe65: startup date [Wed Oct 25 16:52:48 CST 2017]; root of context hierarchy
2017-10-25 16:52:49.597  INFO 9484 --- [           main] s.w.s.m.m.a.RequestMappingHandlerMapping : Mapped "{[/hi]}" onto public java.lang.String com.demo.examp
le.App.home()
2017-10-25 16:52:49.603  INFO 9484 --- [           main] s.w.s.m.m.a.RequestMappingHandlerMapping : Mapped "{[/error]}" onto public org.springframework.http.Res
ponseEntity<java.util.Map<java.lang.String, java.lang.Object>> org.springframework.boot.autoconfigure.web.BasicErrorController.error(javax.servlet.http.HttpServ
letRequest)
2017-10-25 16:52:49.603  INFO 9484 --- [           main] s.w.s.m.m.a.RequestMappingHandlerMapping : Mapped "{[/error],produces=[text/html]}" onto public org.spr
ingframework.web.servlet.ModelAndView org.springframework.boot.autoconfigure.web.BasicErrorController.errorHtml(javax.servlet.http.HttpServletRequest,javax.serv
let.http.HttpServletResponse)
2017-10-25 16:52:49.627  INFO 9484 --- [           main] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped URL path [/webjars/**] onto handler of type [class or
g.springframework.web.servlet.resource.ResourceHttpRequestHandler]
2017-10-25 16:52:49.628  INFO 9484 --- [           main] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped URL path [/**] onto handler of type [class org.spring
framework.web.servlet.resource.ResourceHttpRequestHandler]
2017-10-25 16:52:49.656  INFO 9484 --- [           main] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped URL path [/**/favicon.ico] onto handler of type [clas
s org.springframework.web.servlet.resource.ResourceHttpRequestHandler]
2017-10-25 16:52:49.742  INFO 9484 --- [           main] o.s.j.e.a.AnnotationMBeanExporter        : Registering beans for JMX exposure on startup
2017-10-25 16:52:49.747 DEBUG 9484 --- [           main] ationConfigEmbeddedWebApplicationContext : Unable to locate LifecycleProcessor with name 'lifecycleProc
essor': using default [org.springframework.context.support.DefaultLifecycleProcessor@2a95c77]
2017-10-25 16:52:49.759 DEBUG 9484 --- [           main] utoConfigurationReportLoggingInitializer :


=========================
AUTO-CONFIGURATION REPORT
=========================


Positive matches:
-----------------

   DispatcherServletAutoConfiguration matched:
      - @ConditionalOnClass found required class 'org.springframework.web.servlet.DispatcherServlet'; @ConditionalOnMissingClass did not find unwanted class (On
ClassCondition)
      - @ConditionalOnWebApplication (required) found StandardServletEnvironment (OnWebApplicationCondition)

   DispatcherServletAutoConfiguration.DispatcherServletConfiguration matched:
      - @ConditionalOnClass found required class 'javax.servlet.ServletRegistration'; @ConditionalOnMissingClass did not find unwanted class (OnClassCondition)
      - Default DispatcherServlet did not find dispatcher servlet beans (DispatcherServletAutoConfiguration.DefaultDispatcherServletCondition)

   DispatcherServletAutoConfiguration.DispatcherServletRegistrationConfiguration matched:
      - @ConditionalOnClass found required class 'javax.servlet.ServletRegistration'; @ConditionalOnMissingClass did not find unwanted class (OnClassCondition)
      - DispatcherServlet Registration did not find servlet registration bean (DispatcherServletAutoConfiguration.DispatcherServletRegistrationCondition)

   DispatcherServletAutoConfiguration.DispatcherServletRegistrationConfiguration#dispatcherServletRegistration matched:
      - @ConditionalOnBean (names: dispatcherServlet; types: org.springframework.web.servlet.DispatcherServlet; SearchStrategy: all) found beans 'dispatcherServ
let', 'dispatcherServlet' (OnBeanCondition)

   EmbeddedServletContainerAutoConfiguration matched:
      - @ConditionalOnWebApplication (required) found StandardServletEnvironment (OnWebApplicationCondition)

   EmbeddedServletContainerAutoConfiguration.EmbeddedTomcat matched:
      - @ConditionalOnClass found required classes 'javax.servlet.Servlet', 'org.apache.catalina.startup.Tomcat'; @ConditionalOnMissingClass did not find unwant
ed class (OnClassCondition)
      - @ConditionalOnMissingBean (types: org.springframework.boot.context.embedded.EmbeddedServletContainerFactory; SearchStrategy: current) did not find any b
eans (OnBeanCondition)

   ErrorMvcAutoConfiguration matched:
      - @ConditionalOnClass found required classes 'javax.servlet.Servlet', 'org.springframework.web.servlet.DispatcherServlet'; @ConditionalOnMissingClass did
not find unwanted class (OnClassCondition)
      - @ConditionalOnWebApplication (required) found StandardServletEnvironment (OnWebApplicationCondition)

   ErrorMvcAutoConfiguration#basicErrorController matched:
      - @ConditionalOnMissingBean (types: org.springframework.boot.autoconfigure.web.ErrorController; SearchStrategy: current) did not find any beans (OnBeanCon
dition)

   ErrorMvcAutoConfiguration#errorAttributes matched:
      - @ConditionalOnMissingBean (types: org.springframework.boot.autoconfigure.web.ErrorAttributes; SearchStrategy: current) did not find any beans (OnBeanCon
dition)

   ErrorMvcAutoConfiguration.DefaultErrorViewResolverConfiguration#conventionErrorViewResolver matched:
      - @ConditionalOnBean (types: org.springframework.web.servlet.DispatcherServlet; SearchStrategy: all) found bean 'dispatcherServlet'; @ConditionalOnMissing
Bean (types: org.springframework.boot.autoconfigure.web.DefaultErrorViewResolver; SearchStrategy: all) did not find any beans (OnBeanCondition)

   ErrorMvcAutoConfiguration.WhitelabelErrorViewConfiguration matched:
      - @ConditionalOnProperty (server.error.whitelabel.enabled) matched (OnPropertyCondition)
      - ErrorTemplate Missing did not find error template view (ErrorMvcAutoConfiguration.ErrorTemplateMissingCondition)

   ErrorMvcAutoConfiguration.WhitelabelErrorViewConfiguration#beanNameViewResolver matched:
      - @ConditionalOnMissingBean (types: org.springframework.web.servlet.view.BeanNameViewResolver; SearchStrategy: all) did not find any beans (OnBeanConditio
n)

   ErrorMvcAutoConfiguration.WhitelabelErrorViewConfiguration#defaultErrorView matched:
      - @ConditionalOnMissingBean (names: error; SearchStrategy: all) did not find any beans (OnBeanCondition)

   GenericCacheConfiguration matched:
      - Cache org.springframework.boot.autoconfigure.cache.GenericCacheConfiguration automatic cache type (CacheCondition)

   HttpEncodingAutoConfiguration matched:
      - @ConditionalOnClass found required class 'org.springframework.web.filter.CharacterEncodingFilter'; @ConditionalOnMissingClass did not find unwanted clas
s (OnClassCondition)
      - @ConditionalOnWebApplication (required) found StandardServletEnvironment (OnWebApplicationCondition)
      - @ConditionalOnProperty (spring.http.encoding.enabled) matched (OnPropertyCondition)

   HttpEncodingAutoConfiguration#characterEncodingFilter matched:
      - @ConditionalOnMissingBean (types: org.springframework.web.filter.CharacterEncodingFilter; SearchStrategy: all) did not find any beans (OnBeanCondition)

   HttpMessageConvertersAutoConfiguration matched:
      - @ConditionalOnClass found required class 'org.springframework.http.converter.HttpMessageConverter'; @ConditionalOnMissingClass did not find unwanted cla
ss (OnClassCondition)

   HttpMessageConvertersAutoConfiguration#messageConverters matched:
      - @ConditionalOnMissingBean (types: org.springframework.boot.autoconfigure.web.HttpMessageConverters; SearchStrategy: all) did not find any beans (OnBeanC
ondition)

   HttpMessageConvertersAutoConfiguration.StringHttpMessageConverterConfiguration matched:
      - @ConditionalOnClass found required class 'org.springframework.http.converter.StringHttpMessageConverter'; @ConditionalOnMissingClass did not find unwant
ed class (OnClassCondition)

   HttpMessageConvertersAutoConfiguration.StringHttpMessageConverterConfiguration#stringHttpMessageConverter matched:
      - @ConditionalOnMissingBean (types: org.springframework.http.converter.StringHttpMessageConverter; SearchStrategy: all) did not find any beans (OnBeanCond
ition)

   JacksonAutoConfiguration matched:
      - @ConditionalOnClass found required class 'com.fasterxml.jackson.databind.ObjectMapper'; @ConditionalOnMissingClass did not find unwanted class (OnClassC
ondition)

   JacksonAutoConfiguration.Jackson2ObjectMapperBuilderCustomizerConfiguration matched:
      - @ConditionalOnClass found required classes 'com.fasterxml.jackson.databind.ObjectMapper', 'org.springframework.http.converter.json.Jackson2ObjectMapperB
uilder'; @ConditionalOnMissingClass did not find unwanted class (OnClassCondition)

   JacksonAutoConfiguration.JacksonObjectMapperBuilderConfiguration matched:
      - @ConditionalOnClass found required classes 'com.fasterxml.jackson.databind.ObjectMapper', 'org.springframework.http.converter.json.Jackson2ObjectMapperB
uilder'; @ConditionalOnMissingClass did not find unwanted class (OnClassCondition)

   JacksonAutoConfiguration.JacksonObjectMapperBuilderConfiguration#jacksonObjectMapperBuilder matched:
      - @ConditionalOnMissingBean (types: org.springframework.http.converter.json.Jackson2ObjectMapperBuilder; SearchStrategy: all) did not find any beans (OnBe
anCondition)

   JacksonAutoConfiguration.JacksonObjectMapperConfiguration matched:
      - @ConditionalOnClass found required classes 'com.fasterxml.jackson.databind.ObjectMapper', 'org.springframework.http.converter.json.Jackson2ObjectMapperB
uilder'; @ConditionalOnMissingClass did not find unwanted class (OnClassCondition)

   JacksonAutoConfiguration.JacksonObjectMapperConfiguration#jacksonObjectMapper matched:
      - @ConditionalOnMissingBean (types: com.fasterxml.jackson.databind.ObjectMapper; SearchStrategy: all) did not find any beans (OnBeanCondition)

   JacksonHttpMessageConvertersConfiguration.MappingJackson2HttpMessageConverterConfiguration matched:
      - @ConditionalOnClass found required class 'com.fasterxml.jackson.databind.ObjectMapper'; @ConditionalOnMissingClass did not find unwanted class (OnClassC
ondition)
      - @ConditionalOnProperty (spring.http.converters.preferred-json-mapper=jackson) matched (OnPropertyCondition)
      - @ConditionalOnBean (types: com.fasterxml.jackson.databind.ObjectMapper; SearchStrategy: all) found bean 'jacksonObjectMapper' (OnBeanCondition)

   JacksonHttpMessageConvertersConfiguration.MappingJackson2HttpMessageConverterConfiguration#mappingJackson2HttpMessageConverter matched:
      - @ConditionalOnMissingBean (types: org.springframework.http.converter.json.MappingJackson2HttpMessageConverter; SearchStrategy: all) did not find any bea
ns (OnBeanCondition)

   JmxAutoConfiguration matched:
      - @ConditionalOnClass found required class 'org.springframework.jmx.export.MBeanExporter'; @ConditionalOnMissingClass did not find unwanted class (OnClass
Condition)
      - @ConditionalOnProperty (spring.jmx.enabled=true) matched (OnPropertyCondition)

   JmxAutoConfiguration#mbeanExporter matched:
      - @ConditionalOnMissingBean (types: org.springframework.jmx.export.MBeanExporter; SearchStrategy: current) did not find any beans (OnBeanCondition)

   JmxAutoConfiguration#mbeanServer matched:
      - @ConditionalOnMissingBean (types: javax.management.MBeanServer; SearchStrategy: all) did not find any beans (OnBeanCondition)

   JmxAutoConfiguration#objectNamingStrategy matched:
      - @ConditionalOnMissingBean (types: org.springframework.jmx.export.naming.ObjectNamingStrategy; SearchStrategy: current) did not find any beans (OnBeanCon
dition)

   MultipartAutoConfiguration matched:
      - @ConditionalOnClass found required classes 'javax.servlet.Servlet', 'org.springframework.web.multipart.support.StandardServletMultipartResolver', 'javax
.servlet.MultipartConfigElement'; @ConditionalOnMissingClass did not find unwanted class (OnClassCondition)
      - @ConditionalOnProperty (spring.http.multipart.enabled) matched (OnPropertyCondition)

   MultipartAutoConfiguration#multipartConfigElement matched:
      - @ConditionalOnMissingBean (types: javax.servlet.MultipartConfigElement; SearchStrategy: all) did not find any beans (OnBeanCondition)

   MultipartAutoConfiguration#multipartResolver matched:
      - @ConditionalOnMissingBean (types: org.springframework.web.multipart.MultipartResolver; SearchStrategy: all) did not find any beans (OnBeanCondition)

   NoOpCacheConfiguration matched:
      - Cache org.springframework.boot.autoconfigure.cache.NoOpCacheConfiguration automatic cache type (CacheCondition)

   PropertyPlaceholderAutoConfiguration#propertySourcesPlaceholderConfigurer matched:
      - @ConditionalOnMissingBean (types: org.springframework.context.support.PropertySourcesPlaceholderConfigurer; SearchStrategy: current) did not find any be
ans (OnBeanCondition)

   RedisCacheConfiguration matched:
      - Cache org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration automatic cache type (CacheCondition)

   ServerPropertiesAutoConfiguration matched:
      - @ConditionalOnWebApplication (required) found StandardServletEnvironment (OnWebApplicationCondition)

   ServerPropertiesAutoConfiguration#serverProperties matched:
      - @ConditionalOnMissingBean (types: org.springframework.boot.autoconfigure.web.ServerProperties; SearchStrategy: current) did not find any beans (OnBeanCo
ndition)

   SimpleCacheConfiguration matched:
      - Cache org.springframework.boot.autoconfigure.cache.SimpleCacheConfiguration automatic cache type (CacheCondition)

   ValidationAutoConfiguration matched:
      - @ConditionalOnClass found required class 'javax.validation.executable.ExecutableValidator'; @ConditionalOnMissingClass did not find unwanted class (OnCl
assCondition)
      - @ConditionalOnResource found location classpath:META-INF/services/javax.validation.spi.ValidationProvider (OnResourceCondition)

   ValidationAutoConfiguration#defaultValidator matched:
      - @ConditionalOnMissingBean (types: javax.validation.Validator; SearchStrategy: all) did not find any beans (OnBeanCondition)

   ValidationAutoConfiguration#methodValidationPostProcessor matched:
      - @ConditionalOnMissingBean (types: org.springframework.validation.beanvalidation.MethodValidationPostProcessor; SearchStrategy: all) did not find any bea
ns (OnBeanCondition)

   WebClientAutoConfiguration.RestTemplateConfiguration matched:
      - @ConditionalOnClass found required class 'org.springframework.web.client.RestTemplate'; @ConditionalOnMissingClass did not find unwanted class (OnClassC
ondition)

   WebClientAutoConfiguration.RestTemplateConfiguration#restTemplateBuilder matched:
      - @ConditionalOnMissingBean (types: org.springframework.boot.web.client.RestTemplateBuilder; SearchStrategy: all) did not find any beans (OnBeanCondition)


   WebMvcAutoConfiguration matched:
      - @ConditionalOnClass found required classes 'javax.servlet.Servlet', 'org.springframework.web.servlet.DispatcherServlet', 'org.springframework.web.servle
t.config.annotation.WebMvcConfigurerAdapter'; @ConditionalOnMissingClass did not find unwanted class (OnClassCondition)
      - @ConditionalOnWebApplication (required) found StandardServletEnvironment (OnWebApplicationCondition)
      - @ConditionalOnMissingBean (types: org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport; SearchStrategy: all) did not find any be
ans (OnBeanCondition)

   WebMvcAutoConfiguration#hiddenHttpMethodFilter matched:
      - @ConditionalOnMissingBean (types: org.springframework.web.filter.HiddenHttpMethodFilter; SearchStrategy: all) did not find any beans (OnBeanCondition)

   WebMvcAutoConfiguration#httpPutFormContentFilter matched:
      - @ConditionalOnProperty (spring.mvc.formcontent.putfilter.enabled) matched (OnPropertyCondition)
      - @ConditionalOnMissingBean (types: org.springframework.web.filter.HttpPutFormContentFilter; SearchStrategy: all) did not find any beans (OnBeanCondition)


   WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter#defaultViewResolver matched:
      - @ConditionalOnMissingBean (types: org.springframework.web.servlet.view.InternalResourceViewResolver; SearchStrategy: all) did not find any beans (OnBean
Condition)

   WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter#requestContextFilter matched:
      - @ConditionalOnMissingBean (types: org.springframework.web.context.request.RequestContextListener,org.springframework.web.filter.RequestContextFilter; Se
archStrategy: all) did not find any beans (OnBeanCondition)

   WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter#viewResolver matched:
      - @ConditionalOnBean (types: org.springframework.web.servlet.ViewResolver; SearchStrategy: all) found beans 'defaultViewResolver', 'beanNameViewResolver',
 'mvcViewResolver'; @ConditionalOnMissingBean (names: viewResolver; types: org.springframework.web.servlet.view.ContentNegotiatingViewResolver; SearchStrategy:
all) did not find any beans (OnBeanCondition)

   WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter.FaviconConfiguration matched:
      - @ConditionalOnProperty (spring.mvc.favicon.enabled) matched (OnPropertyCondition)

   WebSocketAutoConfiguration matched:
      - @ConditionalOnClass found required classes 'javax.servlet.Servlet', 'javax.websocket.server.ServerContainer'; @ConditionalOnMissingClass did not find un
wanted class (OnClassCondition)
      - @ConditionalOnWebApplication (required) found StandardServletEnvironment (OnWebApplicationCondition)

   WebSocketAutoConfiguration.TomcatWebSocketConfiguration matched:
      - @ConditionalOnClass found required classes 'org.apache.catalina.startup.Tomcat', 'org.apache.tomcat.websocket.server.WsSci'; @ConditionalOnMissingClass
did not find unwanted class (OnClassCondition)

   WebSocketAutoConfiguration.TomcatWebSocketConfiguration#websocketContainerCustomizer matched:
      - @ConditionalOnJava (1.7 or newer) found 1.8 (OnJavaCondition)
      - @ConditionalOnMissingBean (names: websocketContainerCustomizer; SearchStrategy: all) did not find any beans (OnBeanCondition)


Negative matches:
-----------------

   ActiveMQAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'javax.jms.ConnectionFactory', 'org.apache.activemq.ActiveMQConnectionFactory' (OnClassCondition)

   AopAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'org.aspectj.lang.annotation.Aspect', 'org.aspectj.lang.reflect.Advice' (OnClassCondition)

   ArtemisAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'javax.jms.ConnectionFactory', 'org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory' (
OnClassCondition)

   BatchAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'org.springframework.batch.core.launch.JobLauncher', 'org.springframework.jdbc.core.JdbcOperations'
 (OnClassCondition)

   CacheAutoConfiguration:
      Did not match:
         - @ConditionalOnBean (types: org.springframework.cache.interceptor.CacheAspectSupport; SearchStrategy: all) did not find any beans (OnBeanCondition)
      Matched:
         - @ConditionalOnClass found required class 'org.springframework.cache.CacheManager'; @ConditionalOnMissingClass did not find unwanted class (OnClassCon
dition)

   CacheAutoConfiguration.CacheManagerJpaDependencyConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean' (OnClassCondition)
         - Ancestor org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration did not match (ConditionEvaluationReport.AncestorsMatchedCondition)

   CaffeineCacheConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'com.github.benmanes.caffeine.cache.Caffeine', 'org.springframework.cache.caffeine.CaffeineCacheMan
ager' (OnClassCondition)

   CassandraAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'com.datastax.driver.core.Cluster' (OnClassCondition)

   CassandraDataAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'com.datastax.driver.core.Cluster', 'org.springframework.data.cassandra.core.CassandraAdminOperatio
ns' (OnClassCondition)

   CassandraRepositoriesAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'com.datastax.driver.core.Session', 'org.springframework.data.cassandra.repository.CassandraReposit
ory' (OnClassCondition)

   CloudAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.cloud.config.java.CloudScanConfiguration' (OnClassCondition)

   CouchbaseAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'com.couchbase.client.java.CouchbaseBucket', 'com.couchbase.client.java.Cluster' (OnClassCondition)


   CouchbaseCacheConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'com.couchbase.client.java.Bucket', 'com.couchbase.client.spring.cache.CouchbaseCacheManager' (OnCl
assCondition)

   CouchbaseDataAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'com.couchbase.client.java.Bucket', 'org.springframework.data.couchbase.repository.CouchbaseReposit
ory' (OnClassCondition)

   CouchbaseRepositoriesAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'com.couchbase.client.java.Bucket', 'org.springframework.data.couchbase.repository.CouchbaseReposit
ory' (OnClassCondition)

   DataSourceAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType' (OnClassCondition)

   DataSourceTransactionManagerAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'org.springframework.jdbc.core.JdbcTemplate', 'org.springframework.transaction.PlatformTransactionM
anager' (OnClassCondition)

   DeviceDelegatingViewResolverAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.mobile.device.view.LiteDeviceDelegatingViewResolver' (OnClassCondition)

   DeviceResolverAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'org.springframework.mobile.device.DeviceResolverHandlerInterceptor', 'org.springframework.mobile.d
evice.DeviceHandlerMethodArgumentResolver' (OnClassCondition)

   DispatcherServletAutoConfiguration.DispatcherServletConfiguration#multipartResolver:
      Did not match:
         - @ConditionalOnBean (types: org.springframework.web.multipart.MultipartResolver; SearchStrategy: all) did not find any beans (OnBeanCondition)

   EhCacheCacheConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'net.sf.ehcache.Cache', 'org.springframework.cache.ehcache.EhCacheCacheManager' (OnClassCondition)

   ElasticsearchAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'org.elasticsearch.client.Client', 'org.springframework.data.elasticsearch.client.TransportClientFa
ctoryBean', 'org.springframework.data.elasticsearch.client.NodeClientFactoryBean' (OnClassCondition)

   ElasticsearchDataAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'org.elasticsearch.client.Client', 'org.springframework.data.elasticsearch.core.ElasticsearchTempla
te' (OnClassCondition)

   ElasticsearchRepositoriesAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'org.elasticsearch.client.Client', 'org.springframework.data.elasticsearch.repository.Elasticsearch
Repository' (OnClassCondition)

   EmbeddedLdapAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'com.unboundid.ldap.listener.InMemoryDirectoryServer' (OnClassCondition)

   EmbeddedMongoAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'com.mongodb.Mongo', 'de.flapdoodle.embed.mongo.MongodStarter' (OnClassCondition)

   EmbeddedServletContainerAutoConfiguration.EmbeddedJetty:
      Did not match:
         - @ConditionalOnClass did not find required classes 'org.eclipse.jetty.server.Server', 'org.eclipse.jetty.util.Loader', 'org.eclipse.jetty.webapp.WebAp
pContext' (OnClassCondition)

   EmbeddedServletContainerAutoConfiguration.EmbeddedUndertow:
      Did not match:
         - @ConditionalOnClass did not find required classes 'io.undertow.Undertow', 'org.xnio.SslClientAuthMode' (OnClassCondition)

   FacebookAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'org.springframework.social.config.annotation.SocialConfigurerAdapter', 'org.springframework.social
.facebook.connect.FacebookConnectionFactory' (OnClassCondition)

   FallbackWebSecurityAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.security.config.annotation.web.configuration.EnableWebSecurity' (OnClassConditio
n)

   FlywayAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.flywaydb.core.Flyway' (OnClassCondition)

   FreeMarkerAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'freemarker.template.Configuration', 'org.springframework.ui.freemarker.FreeMarkerConfigurationFact
ory' (OnClassCondition)

   GroovyTemplateAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'groovy.text.markup.MarkupTemplateEngine' (OnClassCondition)

   GsonAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'com.google.gson.Gson' (OnClassCondition)

   GsonHttpMessageConvertersConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'com.google.gson.Gson' (OnClassCondition)

   GuavaCacheConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'com.google.common.cache.CacheBuilder', 'org.springframework.cache.guava.GuavaCacheManager' (OnClas
sCondition)

   H2ConsoleAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.h2.server.web.WebServlet' (OnClassCondition)

   HazelcastAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'com.hazelcast.core.HazelcastInstance' (OnClassCondition)

   HazelcastCacheConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'com.hazelcast.core.HazelcastInstance', 'com.hazelcast.spring.cache.HazelcastCacheManager' (OnClass
Condition)

   HazelcastJpaDependencyAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'com.hazelcast.core.HazelcastInstance', 'org.springframework.orm.jpa.LocalContainerEntityManagerFac
toryBean' (OnClassCondition)

   HibernateJpaAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean', 'javax.persistence.EntityMana
ger' (OnClassCondition)

   HypermediaAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'org.springframework.hateoas.Resource', 'org.springframework.plugin.core.Plugin' (OnClassCondition)


   InfinispanCacheConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.infinispan.spring.provider.SpringEmbeddedCacheManager' (OnClassCondition)

   IntegrationAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.integration.config.EnableIntegration' (OnClassCondition)

   JCacheCacheConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'javax.cache.Caching', 'org.springframework.cache.jcache.JCacheCacheManager' (OnClassCondition)

   JacksonAutoConfiguration.JodaDateTimeJacksonConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'org.joda.time.DateTime', 'com.fasterxml.jackson.datatype.joda.ser.DateTimeSerializer', 'com.faster
xml.jackson.datatype.joda.cfg.JacksonJodaDateFormat' (OnClassCondition)

   JacksonAutoConfiguration.ParameterNamesModuleConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'com.fasterxml.jackson.module.paramnames.ParameterNamesModule' (OnClassCondition)

   JacksonHttpMessageConvertersConfiguration.MappingJackson2XmlHttpMessageConverterConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'com.fasterxml.jackson.dataformat.xml.XmlMapper' (OnClassCondition)

   JdbcTemplateAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.jdbc.core.JdbcTemplate' (OnClassCondition)

   JerseyAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.glassfish.jersey.server.spring.SpringComponentProvider' (OnClassCondition)

   JestAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'io.searchbox.client.JestClient' (OnClassCondition)

   JmsAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'javax.jms.Message', 'org.springframework.jms.core.JmsTemplate' (OnClassCondition)

   JndiConnectionFactoryAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.jms.core.JmsTemplate' (OnClassCondition)

   JndiDataSourceAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType' (OnClassCondition)

   JooqAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.jooq.DSLContext' (OnClassCondition)

   JpaRepositoriesAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.data.jpa.repository.JpaRepository' (OnClassCondition)

   JtaAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'javax.transaction.Transaction' (OnClassCondition)

   KafkaAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.kafka.core.KafkaTemplate' (OnClassCondition)

   LdapAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.ldap.core.ContextSource' (OnClassCondition)

   LdapDataAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.data.ldap.repository.LdapRepository' (OnClassCondition)

   LdapRepositoriesAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.data.ldap.repository.LdapRepository' (OnClassCondition)

   LinkedInAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'org.springframework.social.config.annotation.SocialConfigurerAdapter', 'org.springframework.social
.linkedin.connect.LinkedInConnectionFactory' (OnClassCondition)

   LiquibaseAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'liquibase.integration.spring.SpringLiquibase' (OnClassCondition)

   MailSenderAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'javax.mail.internet.MimeMessage' (OnClassCondition)

   MailSenderValidatorAutoConfiguration:
      Did not match:
         - @ConditionalOnProperty (spring.mail.test-connection) did not find property 'test-connection' (OnPropertyCondition)

   MessageSourceAutoConfiguration:
      Did not match:
         - ResourceBundle did not find bundle with basename messages (MessageSourceAutoConfiguration.ResourceBundleCondition)

   MongoAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'com.mongodb.MongoClient' (OnClassCondition)

   MongoDataAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'com.mongodb.Mongo', 'org.springframework.data.mongodb.core.MongoTemplate' (OnClassCondition)

   MongoRepositoriesAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'com.mongodb.Mongo', 'org.springframework.data.mongodb.repository.MongoRepository' (OnClassConditio
n)

   MustacheAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'com.samskivert.mustache.Mustache' (OnClassCondition)

   Neo4jDataAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'org.neo4j.ogm.session.SessionFactory', 'org.springframework.transaction.PlatformTransactionManager
' (OnClassCondition)

   Neo4jRepositoriesAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'org.neo4j.ogm.session.Neo4jSession', 'org.springframework.data.neo4j.repository.GraphRepository' (
OnClassCondition)

   OAuth2AutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.security.oauth2.common.OAuth2AccessToken' (OnClassCondition)

   PersistenceExceptionTranslationAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor' (OnClassCondition)

   ProjectInfoAutoConfiguration#buildProperties:
      Did not match:
         - @ConditionalOnResource did not find resource '${spring.info.build.location:classpath:META-INF/build-info.properties}' (OnResourceCondition)

   ProjectInfoAutoConfiguration#gitProperties:
      Did not match:
         - GitResource did not find git info at classpath:git.properties (ProjectInfoAutoConfiguration.GitResourceAvailableCondition)

   RabbitAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'org.springframework.amqp.rabbit.core.RabbitTemplate', 'com.rabbitmq.client.Channel' (OnClassCondit
ion)

   ReactorAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'reactor.spring.context.config.EnableReactor', 'reactor.Environment' (OnClassCondition)

   RedisAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'org.springframework.data.redis.connection.jedis.JedisConnection', 'org.springframework.data.redis.
core.RedisOperations', 'redis.clients.jedis.Jedis' (OnClassCondition)

   RedisRepositoriesAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'redis.clients.jedis.Jedis', 'org.springframework.data.redis.repository.configuration.EnableRedisRe
positories' (OnClassCondition)

   RepositoryRestMvcAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration' (OnClassCondition)

   SecurityAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'org.springframework.security.authentication.AuthenticationManager', 'org.springframework.security.
config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter' (OnClassCondition)

   SecurityFilterAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer', 'org.springfr
amework.security.config.http.SessionCreationPolicy' (OnClassCondition)

   SendGridAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'com.sendgrid.SendGrid' (OnClassCondition)

   SessionAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.session.Session' (OnClassCondition)

   SitePreferenceAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'org.springframework.mobile.device.site.SitePreferenceHandlerInterceptor', 'org.springframework.mob
ile.device.site.SitePreferenceHandlerMethodArgumentResolver' (OnClassCondition)

   SocialWebAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'org.springframework.social.connect.web.ConnectController', 'org.springframework.social.config.anno
tation.SocialConfigurerAdapter' (OnClassCondition)

   SolrAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'org.apache.solr.client.solrj.impl.HttpSolrClient', 'org.apache.solr.client.solrj.impl.CloudSolrCli
ent' (OnClassCondition)

   SolrRepositoriesAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'org.apache.solr.client.solrj.SolrClient', 'org.springframework.data.solr.repository.SolrRepository
' (OnClassCondition)

   SpringApplicationAdminJmxAutoConfiguration:
      Did not match:
         - @ConditionalOnProperty (spring.application.admin.enabled=true) did not find property 'enabled' (OnPropertyCondition)

   SpringDataWebAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.data.web.PageableHandlerMethodArgumentResolver' (OnClassCondition)

   ThymeleafAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.thymeleaf.spring4.SpringTemplateEngine' (OnClassCondition)

   TransactionAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.transaction.PlatformTransactionManager' (OnClassCondition)

   TwitterAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'org.springframework.social.config.annotation.SocialConfigurerAdapter', 'org.springframework.social
.twitter.connect.TwitterConnectionFactory' (OnClassCondition)

   WebMvcAutoConfiguration.ResourceChainCustomizerConfiguration:
      Did not match:
         - @ConditionalOnEnabledResourceChain did not find class org.webjars.WebJarAssetLocator (OnEnabledResourceChainCondition)

   WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter#beanNameViewResolver:
      Did not match:
         - @ConditionalOnMissingBean (types: org.springframework.web.servlet.view.BeanNameViewResolver; SearchStrategy: all) found bean 'beanNameViewResolver' (
OnBeanCondition)

   WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter#dateFormatter:
      Did not match:
         - @ConditionalOnProperty (spring.mvc.date-format) did not find property 'date-format' (OnPropertyCondition)

   WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter#localeResolver:
      Did not match:
         - @ConditionalOnProperty (spring.mvc.locale) did not find property 'locale' (OnPropertyCondition)

   WebServicesAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.ws.transport.http.MessageDispatcherServlet' (OnClassCondition)

   WebSocketAutoConfiguration.JettyWebSocketConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer' (OnClassCondit
ion)

   WebSocketAutoConfiguration.UndertowWebSocketConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'io.undertow.websockets.jsr.Bootstrap' (OnClassCondition)

   WebSocketMessagingAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer' (OnClassCondition
)

   XADataSourceAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'javax.transaction.TransactionManager', 'org.springframework.jdbc.datasource.embedded.EmbeddedDatab
aseType' (OnClassCondition)


Exclusions:
-----------

    None


Unconditional classes:
----------------------

    org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration

    org.springframework.boot.autoconfigure.web.WebClientAutoConfiguration

    org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration

    org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration



2017-10-25 16:52:50.067  INFO 9484 --- [           main] s.b.c.e.t.TomcatEmbeddedServletContainer : Tomcat started on port(s): 8080 (http)
2017-10-25 16:52:50.071  INFO 9484 --- [           main] com.demo.example.App                     : Started App in 2.32 seconds (JVM running for 4.417)
```

主要查看`Positive matches`下的类，例如：

```
=========================
AUTO-CONFIGURATION REPORT
=========================


Positive matches:
-----------------

   DispatcherServletAutoConfiguration matched:
      - @ConditionalOnClass found required class 'org.springframework.web.servlet.DispatcherServlet'; @ConditionalOnMissingClass did not find unwanted class (On
ClassCondition)
      - @ConditionalOnWebApplication (required) found StandardServletEnvironment (OnWebApplicationCondition)

   DispatcherServletAutoConfiguration.DispatcherServletConfiguration matched:
      - @ConditionalOnClass found required class 'javax.servlet.ServletRegistration'; @ConditionalOnMissingClass did not find unwanted class (OnClassCondition)
      - Default DispatcherServlet did not find dispatcher servlet beans (DispatcherServletAutoConfiguration.DefaultDispatcherServletCondition)

   ......
```

加载了太多的配置类，会影响程序性能，故可以自定义导入需要的配置类：

```
package com.demo.example;

import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.HttpEncodingAutoConfiguration;
import org.springframework.boot.autoconfigure.web.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerPropertiesAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        DispatcherServletAutoConfiguration.class,
        EmbeddedServletContainerAutoConfiguration.class,
        ErrorMvcAutoConfiguration.class,
        HttpEncodingAutoConfiguration.class,
        HttpMessageConvertersAutoConfiguration.class,
        JacksonAutoConfiguration.class,
        JmxAutoConfiguration.class,
        MultipartAutoConfiguration.class,
        ServerPropertiesAutoConfiguration.class,
        PropertyPlaceholderAutoConfiguration.class,
        ThymeleafAutoConfiguration.class,
        WebMvcAutoConfiguration.class,
        WebSocketAutoConfiguration.class,
})
public class SampleWebUiApplication {

}

```

如果你的pom.xml中添加了`spring-boot-maven-plugin`插件，你可以运行`mvn package`命令在`target`目录生成一个可执行的 jar 文件：

```
C:\bootdemo\spring-boot-example>mvn clean package
[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Building spring-boot-example 1.0-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO]
[INFO] --- maven-clean-plugin:2.6.1:clean (default-clean) @ spring-boot-example ---
[INFO] Deleting C:\bootdemo\spring-boot-example\target
[INFO]
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ spring-boot-example ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] skip non existing resourceDirectory C:\bootdemo\spring-boot-example\src\main\resources
[INFO] skip non existing resourceDirectory C:\bootdemo\spring-boot-example\src\main\resources
[INFO]
[INFO] --- maven-compiler-plugin:3.1:compile (default-compile) @ spring-boot-example ---
[INFO] Changes detected - recompiling the module!
[INFO] Compiling 2 source files to C:\bootdemo\spring-boot-example\target\classes
[INFO]
[INFO] --- maven-resources-plugin:2.6:testResources (default-testResources) @ spring-boot-example ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] skip non existing resourceDirectory C:\bootdemo\spring-boot-example\src\test\resources
[INFO]
[INFO] --- maven-compiler-plugin:3.1:testCompile (default-testCompile) @ spring-boot-example ---
[INFO] Changes detected - recompiling the module!
[INFO] Compiling 1 source file to C:\bootdemo\spring-boot-example\target\test-classes
[INFO]
[INFO] --- maven-surefire-plugin:2.18.1:test (default-test) @ spring-boot-example ---
[INFO] Surefire report directory: C:\bootdemo\spring-boot-example\target\surefire-reports
Downloading: http://testm2repo/nexus/content/groups/public/org/apache/maven/surefire/surefire-junit3/2.18.1/surefire-junit3-2.18.1.pom
Downloaded: http://testm2repo/nexus/content/groups/public/org/apache/maven/surefire/surefire-junit3/2.18.1/surefire-junit3-2.18.1.pom (2 KB at 0.2 KB/sec)
Downloading: http://testm2repo/nexus/content/groups/public/org/apache/maven/surefire/surefire-junit3/2.18.1/surefire-junit3-2.18.1.jar
Downloaded: http://testm2repo/nexus/content/groups/public/org/apache/maven/surefire/surefire-junit3/2.18.1/surefire-junit3-2.18.1.jar (25 KB at 0.9 KB/sec)

-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running com.demo.example.AppTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.001 sec - in com.demo.example.AppTest

Results :

Tests run: 1, Failures: 0, Errors: 0, Skipped: 0

[INFO]
[INFO] --- maven-jar-plugin:2.6:jar (default-jar) @ spring-boot-example ---
[INFO] Building jar: C:\bootdemo\spring-boot-example\target\spring-boot-example-1.0-SNAPSHOT.jar
[INFO]
[INFO] --- spring-boot-maven-plugin:1.5.4.RELEASE:repackage (default) @ spring-boot-example ---
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 38.664 s
[INFO] Finished at: 2017-10-25T17:05:27+08:00
[INFO] Final Memory: 32M/281M
[INFO] ------------------------------------------------------------------------
```

然后，可以运行项目命令执行生成的jar文件：

```
C:\bootdemo\spring-boot-example>java -jar target/spring-boot-example-1.0-SNAPSHOT.jar
```

如果 Maven 运行过程出现内存溢出，则可以添加下面参数：

```
$ export JAVA_OPTS=-Xmx1024m -XX:MaxPermSize=128M -Djava.security.egd=file:/dev/./urandom
```
或
```
C:\bootdemo\spring-boot-example>set JAVA_OPTS=-Xmx1024m -XX:MaxPermSize=128M -Djava.security.egd=file:/dev/./urandom
```

你也可以启动远程调试：

```
$ java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=n \
       -jar target/spring-boot-example-1.0-SNAPSHOT.jar
```

