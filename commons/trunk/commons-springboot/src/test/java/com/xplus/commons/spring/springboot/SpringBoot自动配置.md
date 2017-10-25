# SpringBoot自动配置

## 主应用类
在Spring Boot应用中，通常将主应用类放置于应用的根包中，例如，`com.demo.example`。主应用类有main方法，并且使用了`@EnableAutoConfiguration`注解，并隐式定义了一个基础的包路径，Spring Boot会在该包路径来搜索类。
例如，如果正在编写一个JPA应用，被`@EnableAutoConfiguration`注解的类所在包将被用来搜索带有`@Entity`注解的实体类。

在主应用类上指定`@ComponentScan`，同样也隐式的指定了扫描时basePackage的路径。

在Application.java类声明了main方法，还使用了`@EnableAutoConfiguration`注解。

```
@RestController
@EnableAutoConfiguration
public class Application {

  @RequestMapping("/hi")
  String home() {
    return "Hello World!";
  }

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Application.class, args);
  }
}
```
- 说明：
  + `@RestController`和`@RequestMapping`注解是Spring MVC注解，它们不是Spring Boot的特定部分，具体查看Spring参考文档的MVC章节。
  + `@EnableAutoConfiguration`这个注解告诉Spring Boot根据添加的jar依赖猜测想如何配置Spring。由于`spring-boot-starter-web`添加了Tomcat和Spring MVC，所以auto-configuration将假定正在开发一个web应用并相应地对Spring进行设置。

## 配置类

在该类上可以使用`@Configuration`注解，用来对spring boot进行配置，也可以使用一个XML源来调用`SpringApplication.run()`进行配置。

标有`@Configuration`注解的类为配置类。可以不需要将所有的`@Configuration`放进一个单独的类。`@Import`注解可以用来导入其他配置类。也可以使用`@ComponentScan`注解自动收集所有的Spring组件，包括`@Configuration`类。

如果需要使用基于XML的配置，可以在注有`@Configuration`的类上使用附加的`@ImportResource`注解加载XML配置文件。

可以通过将`@EnableAutoConfiguration`或`@SpringBootApplication`注解添加到一个`@Configuration`类上来选择自动配置。自动配置的意思是Spring Boot尝试根据添加的jar依赖自动配置的Spring应用。

如果需要找出当前应用了哪些自动配置及应用的原因，可以使用`--debug`开关启动应用，这将会记录一个自动配置的报告并输出到控制台。

如果发现应用了不想要的特定自动配置类，可以使用`@EnableAutoConfiguration`注解的排除属性来禁用它们。

```
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.autoconfigure.jdbc.*;
import org.springframework.context.annotation.*;

@Configuration
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class MyConfiguration {
}
```

- 总结，上面提到了几个注解，用途分别如下：
  + `@Configuration`。标注一个类为配置类。
  + `@EnableAutoConfiguration`。开启自动配置。
  + `@SpringBootApplication`。等价于以默认属性使用`@Configuration`，`@EnableAutoConfiguration`和`@ComponentScan`。

如果启动类在根包下面，可以在该类上添加`@ComponentScan`注解而不需要添加任何参数，Spring Boot会在根包下面搜索注有`@Component`, `@Service`, `@Repository`, `@Controller`注解的所有类，并将他们注册为Spring Beans，否则，需要在`@ComponentScan`注解上定义basePackages或者其他属性。

这样Application.java可以定义为：

```
package com.demo.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {
  @RequestMapping("/hi")
  String home() {
    return "Hello World!";
  }

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Application.class, args);
  }
}
```

或者：

```
package com.demo.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class Application {
  @RequestMapping("/hi")
  String home() {
    return "Hello World!";
  }

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Application.class, args);
  }
}
```

## 命令行参数

启动类可以实现CommandLineRunner接口，通过run方法处理main方法传入的参数，并且能够使用`@Value`注解将命令行参数传入的值或者properties资源文件中定义的值注入到程序中。例如，创建一个HelloWorldService类：

```
package com.demo.example.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HelloWorldService {
  @Value("${name:World}")
  private String name;

  public String getMessage() {
    return "Hello " + this.name;
  }
}
```

并添加资源文件`application.properties`：

```
name: Java Test
```

修改Application类为如下：

```
package com.demo.example;

import com.demo.example.service.HelloWorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class Application implements CommandLineRunner {
  @Autowired
  private HelloWorldService helloWorldService;

  @RequestMapping("/hi")
  String home() {
    return "Hello World!";
  }

  @Override
  public void run(String... args) {
    System.out.println(this.helloWorldService.getMessage());
    if (args.length > 0 && args[0].equals("exitcode")) {
      throw new ExitException();
    }
  }

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Application.class, args);
  }
}
```

运行该类的main方法，则默认会输出：

```
Hello Java Test
```

再次运行main方法，并传入参数`--name=whatever`，则会输出：

```
Hello whatever
```

如果一些CommandLineRunner beans被定义必须以特定的次序调用，可以额外实现`org.springframework.core.Ordered`接口或使用`@Order`注解。

利用command-line runner的这个特性，再配合依赖注入，可以在应用程序启动时后首先引入一些依赖bean，例如data source、rpc服务或者其他模块等等，这些对象的初始化可以放在run方法中。不过，需要注意的是，在run方法中执行初始化动作的时候一旦遇到任何异常，都会使得应用程序停止运行，因此最好利用try/catch语句处理可能遇到的异常。

每个SpringApplication在退出时为了确保`ApplicationContext`被优雅的关闭，将会注册一个JVM的shutdown钩子。所有标准的Spring生命周期回调（比如，`DisposableBean`接口或`@PreDestroy`注解）都能使用。

此外，如果beans想在应用结束时返回一个特定的退出码，可以实现`org.springframework.boot.ExitCodeGenerator`接口，例如上面例子中的`ExitException`异常类：

```
package com.demo.example;

import org.springframework.boot.ExitCodeGenerator;

public class ExitException extends RuntimeException implements ExitCodeGenerator {
  @Override
  public int getExitCode() {
    return 10;
  }
}
```

## 自动配置

在启动类上使用`@EnableAutoConfiguration`注解，就会开启自动配置，简单点说就是它会根据定义在classpath下的类，自动的给生成一些Bean，并加载到Spring的Context中。

它的神秘之处，不在于它能做什么，而在于它会生成什么样的Bean对于开发人员是不可预知（或者说不容易预知）。

例如，上面例子中引入了对`spring-boot-starter-web`的依赖，则会开启Spring MVC自动配置，观察启动日志，可以发现应用启动了tomcat和spring mvc。

- Spring Boot为Spring MVC提供适用于多数应用的自动配置功能。在Spring默认基础上，自动配置添加了以下特性：
  + 引入`ContentNegotiatingViewResolver`和`BeanNameViewResolver` beans。
  + 对静态资源的支持，包括对WebJars的支持。
  + 自动注册`Converter`，`GenericConverter`，`Formatter` beans。
  + 对`HttpMessageConverters`的支持。
  + 自动注册`MessageCodeResolver`。
  + 对静态index.html的支持。
  + 对自定义Favicon的支持。
如果想全面控制Spring MVC，可以添加自己的`@Configuration`，并使用`@EnableWebMvc`对其注解。如果想保留Spring Boot MVC的特性，并只是添加其他的MVC配置(拦截器，formatters，视图控制器等)，可以添加自己的`WebMvcConfigurerAdapter`类型的`@Bean`（不使用`@EnableWebMvc`注解）。

再举个例子：要开发一个基于Spring JPA的应用，会涉及到下面三个Bean的配置，`DataSource`，`EntityManagerFactory`，`PlatformTransactionManager`。

```
@Configuration
@EnableJpaRepositories
@EnableTransactionManagement
public class Application {
  @Bean
  public DataSource dataSource() {
      ...
  }

  @Bean
  public EntityManagerFactory entityManagerFactory() {
      ..
      factory.setDataSource(dataSource());
      return factory.getObject();
  }

  @Bean
  public PlatformTransactionManager transactionManager() {
      JpaTransactionManager txManager = new JpaTransactionManager();
      txManager.setEntityManagerFactory(entityManagerFactory());
      return txManager;
  }
}
```

- 说明：
  + `@EnableJpaRepositories`会查找满足作为`Repository`条件（继承父类或者使用注解）的类。
  + `@EnableTransactionManagement`的作用：Enables Spring’s annotation-driven transaction management capability, similar to the support found in Spring’s <tx:*> XML namespace。
但是，如果使用了`@EnableAutoConfiguration`，那么上面三个Bean，都不需要配置。在classpath下面只引入了MySQL的驱动和SpringJpa。

```
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-jpa</artifactId>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <scope>compile</scope>
    </dependency>
```

- 在生产环境中，数据库连接可以使用DataSource池进行自动配置。下面是选取一个特定实现的算法：
  + 由于Tomcat数据源连接池的性能和并发，在tomcat可用时，我们总是优先使用它。
  + 如果HikariCP可用，我们将使用它。
  + 如果Commons DBCP可用，我们将使用它，但在生产环境不推荐使用它。
  + 最后，如果Commons DBCP2可用，我们将使用它。

如果使用`spring-boot-starter-jdbc`或`spring-boot-starter-data-jpa`，将会自动获取对tomcat-jdbc的依赖。

DataSource配置通过外部配置文件的`spring.datasource.*`属性控制。示例中，可能会在`application.properties`中声明下面的片段：

```
spring.datasource.url=jdbc:mysql://localhost/test
spring.datasource.username=dbuser
spring.datasource.password=dbpass
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
```

其他可选的配置可以查看DataSourceProperties。同时注意可以通过`spring.datasource.*`配置任何DataSource实现相关的特定属性：具体参考使用的连接池实现的文档。

既然Spring Boot能够从大多数数据库的url上推断出driver-class-name，那么就不需要再指定它了。对于一个将要创建的DataSource连接池，我们需要能够验证Driver是否可用，所以我们会在做任何事情之前检查它。比如，如果设置`spring.datasource.driverClassName=com.mysql.jdbc.Driver`，然后这个类就会被加载。
Spring的`JdbcTemplate`和`NamedParameterJdbcTemplate`类是被自动配置的，可以在自己的beans中通过`@Autowire`直接注入它们。

如果数据源是jndi，则定义：

```
spring.datasource.jndi-name=java:jboss/datasources/customers
```

## XML配置

如果不想使用注解进行配置，则可以使用xml配置文件，修改main方法如下：

```
  public static void main(String[] args) throws Exception {
    SpringApplication.run("classpath:/META-INF/application-context.xml", args);
  }
```

META-INF/application-context.xml文件如下：

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">

    <context:annotation-config/>
    <context:property-placeholder/>

    <bean id="helloService" class="com.demo.example.service.HelloWorldService"/>
    <bean id="application" class="com.demo.example.Application"/>

</beans>
```