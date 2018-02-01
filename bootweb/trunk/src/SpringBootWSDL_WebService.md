# SpringBoot WSDL

* [Spring WS - SOAP Web Service Consumer Provider WSDL Example](https://www.codenotfound.com/spring-ws-soap-web-service-consumer-provider-wsdl-example.html)

A detailed step-by-step tutorial on how to implement a Hello World web service starting from a WSDL and using Spring-WS and Spring Boot.
关于如何从WSDL开始并使用Spring-WS和Spring Boot实现Hello World Web服务的详细分步教程。

[Spring Web Services](http://projects.spring.io/spring-ws/) (Spring-WS) is a product of the Spring community focused on creating document-driven Web services. Spring-WS facilitates contract-first SOAP service development, allowing for a number of ways to manipulate XML payloads.
Spring Web Services（Spring-WS）是Spring社区致力于创建文档驱动的Web服务的产物。 Spring-WS推动了契约优先的SOAP服务开发，允许使用多种方式来操作XML有效载荷。

The following step by step tutorial illustrates a basic example in which we will configure, build and run a Hello World contract first client and endpoint using a WSDL, Spring-WS, Spring Boot and Maven.
以下逐步教程演示了一个基本示例，我们将使用WSDL，Spring-WS，Spring Boot和Maven来配置，构建和运行Hello World合约的第一个客户端和端点。

The tutorial code is organized in such a way that you can choose to only run the client (consumer) or endpoint (provider) part. In the below example we will setup both parts and then make an end-to-end test in which the client calls the endpoint.
教程代码的组织方式使您可以选择仅运行客户端（消费者）或端点（提供者）部分。 在下面的例子中，我们将设置两个部分，然后进行客户端调用端点的端到端测试。

* If you want to learn more about Spring WS - head on over to the [Spring-WS tutorials](https://www.codenotfound.com/spring-ws/) page.
* 如果您想了解有关Spring WS的更多信息，请参阅Spring-WS教程页面。

## General Project Setup

* Tools used（工具使用）:
  - Spring-WS 2.4.2
  - Spring Boot 1.5.9.RELEASE
  - Maven 3.3

As Spring Web Services is contract first only, we need to start from a contract definition. In this tutorial, we will use a Hello World service that is defined by below WSDL. The service takes as input a person’s first and last name and returns a greeting.
由于Spring Web Services是合同第一，我们需要从合同定义开始。 在本教程中，我们将使用由以下WSDL定义的Hello World服务。 该服务将输入一个人的姓名和返回问候。

```xml
<?xml version="1.0"?>
<wsdl:definitions name="HelloWorld"
  targetNamespace="http://codenotfound.com/services/helloworld"
  xmlns:tns="http://codenotfound.com/services/helloworld" xmlns:types="http://codenotfound.com/types/helloworld"
  xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/" xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">

  <wsdl:types>
    <xsd:schema targetNamespace="http://codenotfound.com/types/helloworld"
      xmlns:xsd="http://www.w3.org/2001/XMLSchema" elementFormDefault="qualified"
      attributeFormDefault="unqualified" version="1.0">

      <xsd:element name="person">
        <xsd:complexType>
          <xsd:sequence>
            <xsd:element name="firstName" type="xsd:string" />
            <xsd:element name="lastName" type="xsd:string" />
          </xsd:sequence>
        </xsd:complexType>
      </xsd:element>

      <xsd:element name="greeting">
        <xsd:complexType>
          <xsd:sequence>
            <xsd:element name="greeting" type="xsd:string" />
          </xsd:sequence>
        </xsd:complexType>
      </xsd:element>
    </xsd:schema>
  </wsdl:types>

  <wsdl:message name="SayHelloInput">
    <wsdl:part name="person" element="types:person" />
  </wsdl:message>

  <wsdl:message name="SayHelloOutput">
    <wsdl:part name="greeting" element="types:greeting" />
  </wsdl:message>

  <wsdl:portType name="HelloWorld_PortType">
    <wsdl:operation name="sayHello">
      <wsdl:input message="tns:SayHelloInput" />
      <wsdl:output message="tns:SayHelloOutput" />
    </wsdl:operation>
  </wsdl:portType>

  <wsdl:binding name="HelloWorld_SoapBinding" type="tns:HelloWorld_PortType">
    <soap:binding style="document"
      transport="http://schemas.xmlsoap.org/soap/http" />
    <wsdl:operation name="sayHello">
      <soap:operation
        soapAction="http://codenotfound.com/services/helloworld/sayHello" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
  </wsdl:binding>

  <wsdl:service name="HelloWorld_Service">
    <wsdl:documentation>Hello World service</wsdl:documentation>
    <wsdl:port name="HelloWorld_Port" binding="tns:HelloWorld_SoapBinding">
      <soap:address location="http://localhost:9090/codenotfound/ws/helloworld" />
    </wsdl:port>
  </wsdl:service>

</wsdl:definitions>
```

We will be building and running our example using [Apache Maven](https://maven.apache.org/). Shown below is the XML representation of our Maven project in a POM file. It contains the needed dependencies for compiling and running our example.
我们将使用Apache Maven构建和运行我们的示例。 下面显示的是我们的Maven项目在POM文件中的XML表示。 它包含编译和运行我们的示例所需的依赖关系。

In order to expose the Hello World service endpoint, we will use the [Spring Boot](https://projects.spring.io/spring-boot/) project that comes with an embedded Apache Tomcat server. To facilitate the management of the different Spring dependencies, [Spring Boot Starters](https://github.com/spring-projects/spring-boot/tree/master/spring-boot-project/spring-boot-starters) are used which are a set of convenient dependency descriptors that you can include in your application.
为了公开Hello World服务端点，我们将使用嵌入式Apache Tomcat服务器附带的Spring Boot项目。 为了便于管理不同的Spring依赖关系，使用了Spring Boot Starter，这是一套方便的依赖描述符，可以包含在你的应用程序中。

The `spring-boot-starter-web-services` dependency includes the needed dependencies for using Spring Web Services. The `spring-boot-starter-test` includes the dependencies for testing Spring Boot applications with libraries that include [JUnit](http://junit.org/junit4/), [Hamcrest](http://hamcrest.org/JavaHamcrest/) and [Mockito](http://site.mockito.org/).
`spring-boot-starter-web-services`依赖项包括使用Spring Web Services所需的依赖项。 `spring-boot-starter-test`包含用于测试Spring Boot应用程序的依赖关系，这些库包括JUnit，Hamcrest和Mockito。

To avoid having to manage the version compatibility of the different Spring dependencies, we will inherit the defaults from the `spring-boot-starter-parent` parent POM.
为了避免管理不同的Spring依赖关系的版本兼容性，我们将继承`spring-boot-starter-parent`父POM的默认值。

In the plugins section, we included the `spring-boot-maven-plugin` Maven plugin so that we can build a single, runnable “uber-jar”. This will also allow us to start the web service via a Maven command.
在插件部分，我们包含了`spring-boot-maven-plugin` Maven插件，以便我们可以构建一个可运行的“超级jar”。 这也将允许我们通过Maven命令启动Web服务。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>com.codenotfound</groupId>
  <artifactId>spring-ws-helloworld</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <packaging>jar</packaging>

  <name>spring-ws-helloworld</name>
  <description>Spring WS - SOAP Web Service Consumer Provider WSDL Example</description>
  <url>https://www.codenotfound.com/spring-ws-soap-web-service-consumer-provider-wsdl-example.html</url>

  <parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>1.5.9.RELEASE</version>
  </parent>

  <properties>
    <java.version>1.8</java.version>
    <maven-jaxb2-plugin.version>0.13.3</maven-jaxb2-plugin.version>
  </properties>

  <dependencies>
    <!-- spring-boot -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web-services</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <!-- spring-boot-maven-plugin -->
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
      </plugin>
      <!-- maven-jaxb2-plugin -->
      <plugin>
        <groupId>org.jvnet.jaxb2.maven2</groupId>
        <artifactId>maven-jaxb2-plugin</artifactId>
        <version>${maven-jaxb2-plugin.version}</version>
        <executions>
          <execution>
            <goals>
              <goal>generate</goal>
            </goals>
          </execution>
        </executions>
        <configuration>
          <schemaDirectory>${project.basedir}/src/main/resources/wsdl</schemaDirectory>
          <schemaIncludes>
            <include>*.wsdl</include>
          </schemaIncludes>
        </configuration>
      </plugin>
    </plugins>
  </build>
</project>
```

In order to directly use the 'person' and 'greeting' elements (defined in the 'types' section of the Hello World WSDL) in our Java code, we will use [JAXB](http://www.oracle.com/technetwork/articles/javase/index-140168.html) to generate the corresponding Java classes. The above POM file configures the [maven-jaxb2-plugin](https://github.com/highsource/maven-jaxb2-plugin) that will handle the generation.
为了在我们的Java代码中直接使用'person'和'greeting'元素（在Hello World WSDL的“类型”部分中定义），我们将使用JAXB生成相应的Java类。 上面的POM文件配置将处理代的maven-jaxb2插件。

The plugin will look into the defined '<schemaDirectory>' in order to find any WSDL files for which it needs to generate the Java classes. In order to trigger the generation via Maven, executed following command:
该插件将查找定义的“<schemaDirectory>”，以便查找需要为其生成Java类的任何WSDL文件。 为了通过Maven触发生成，执行以下命令：

```
mvn generate-sources
```

This results in a number of generated classes amongst which the Person and Greeting that we will use when implementing the client and provider of the Hello World service.
这导致了许多生成的类，其中包括Person和Greeting，我们将在实现Hello World服务的客户端和提供者时使用它们。

```
- target/generated-sources/xjc
-- com.codenotfound.types.helloworld
---- Greeting.java
---- ObjectFactory.java
---- package-info.java
---- Person.java
```

We also create a `SpringWsApplication` that contains a `main()` method that uses Spring Boot’s `SpringApplication.run()` method to bootstrap the application, starting Spring. For more information on Spring Boot, we refer to the [Spring Boot getting started guide](https://spring.io/guides/gs/spring-boot/).
我们还创建了一个`SpringWsApplication`，它包含一个使用Spring Boot的`SpringApplication.run()`方法来启动Spring的`main()`方法来引导应用程序。 有关Spring Boot的更多信息，请参阅Spring Boot入门指南。

```java
package com.codenotfound.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringWsApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringWsApplication.class, args);
  }
}
```

## Creating the Endpoint (Provider)

The server-side of Spring-WS is designed around a central class called `MessageDispatcher` that dispatches incoming XML messages to endpoints. For more detailed information check out the [Spring Web Services reference documentation on the MessageDispatcher](https://docs.spring.io/spring-ws/docs/2.4.2.RELEASE/reference/#_the_code_messagedispatcher_code).
Spring-WS的服务器端是围绕一个名为`MessageDispatcher`的中心类来设计的，它将传入的XML消息分发到端点。 有关更多详细信息，请查看MessageDispatcher上的Spring Web服务参考文档。

Spring Web Services supports multiple transport protocols. The most common is the HTTP transport, for which a custom `MessageDispatcherServlet` servlet is supplied. This is a standard `Servlet` which extends from the standard Spring Web DispatcherServlet ([=central dispatcher for HTTP request handlers/controllers](http://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/DispatcherServlet.html)), and wraps a MessageDispatcher.
Spring Web Services支持多种传输协议。 最常见的是HTTP传输，为其提供了一个定制的`MessageDispatcherServlet servlet`。 这是一个从标准Spring Web DispatcherServlet（= HTTP请求处理程序/控制器的中央调度程序）扩展而来的标准Servlet，并包装了一个`MessageDispatcher`。

> In other words: the MessageDispatcherServlet combines the attributes of the MessageDispatcher and DispatcherServlet and as a result allows the handling of XML messages over HTTP.
> 换句话说：MessageDispatcherServlet结合了MessageDispatcher和DispatcherServlet的属性，因此允许通过HTTP处理XML消息。

In the below `WebServiceConfig` configuration class we use a `ServletRegistrationBean` to register the `MessageDispatcherServlet`. Note that it is important to inject and set the `ApplicationContext` to the `MessageDispatcherServlet`, otherwise it will not automatically detect other Spring Web Services related beans (such as the lower `Wsdl11Definition`). By naming this bean 'messageDispatcherServlet', it does [not replace Spring Boot’s default DispatcherServlet bean](http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-switch-off-the-spring-mvc-dispatcherservlet).
在下面的`WebServiceConfig`配置类中，我们使用`ServletRegistrationBean`来注册`MessageDispatcherServlet`。 请注意，将`ApplicationContext`注入并设置`MessageDispatcherServlet`非常重要，否则它将不会自动检测其他Spring Web Services相关的Bean（如较低的`Wsdl11Definition`）。 通过命名这个bean为'messageDispatcherServlet'，它不会替代Spring Boot的默认DispatcherServlet bean。

The servlet mapping URI pattern on the `ServletRegistrationBean` is set to “/codenotfound/ws/*”. The web container will use this path to map incoming HTTP requests to the servlet.
`ServletRegistrationBean`上的Servlet映射URI模式设置为“/codenotfound/ws/*”。 Web容器将使用此路径将传入的HTTP请求映射到servlet。

The `DefaultWsdl11Definition` exposes a standard WSDL 1.1 using the specified Hello World WSDL file. The URL location at which this WSDL is available is determined by it’s `Bean` name in combination with the URI mapping of the `MessageDispatcherServlet`. For the example below this is: [host]=”http://localhost:9090”+[servlet mapping uri]=”/codenotfound/ws/”+[WsdlDefinition bean name]=”helloworld”+[WSDL postfix]=”.wsdl” or http://localhost:9090/codenotfound/ws/helloworld.wsdl.
`DefaultWsdl11Definition`使用指定的Hello World WSDL文件公开一个标准的WSDL 1.1。 此WSDL可用的URL位置由其`Bean`名称与`MessageDispatcherServlet`的URI映射组合来确定。 下面的例子是：[host] =“http//localhost:9090”+ [servlet mapping uri] =“/codenotfound/ws/”+ [WsdlDefinition bean name] =“helloworld”+ [WSDL postfix] = “.wsdl”或`http：// localhost:9090/codenotfound/ws/helloworld.wsdl`。

> To enable the support for @Endpoint annotation that we will use in the next section we need to annotate our configuration class with @EnableWs.
> 为了支持我们将在下一节中使用的@Endpoint注解，我们需要使用@EnableWs注释我们的配置类。

```java
package com.codenotfound.ws.endpoint;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.ws.wsdl.wsdl11.Wsdl11Definition;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

  @Bean
  public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
    MessageDispatcherServlet servlet = new MessageDispatcherServlet();
    servlet.setApplicationContext(applicationContext);

    return new ServletRegistrationBean(servlet, "/codenotfound/ws/*");
  }

  @Bean(name = "helloworld")
  public Wsdl11Definition defaultWsdl11Definition() {
    SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
    wsdl11Definition.setWsdl(new ClassPathResource("/wsdl/helloworld.wsdl"));

    return wsdl11Definition;
  }
}
```

> Note that we changed the default Spring Boot server HTTP port value from '8080' to '9090' by explicitly setting it in the application.yml properties file located under src/main/resources as shown below.
> 请注意，我们通过在src/main/resources下的application.yml属性文件中显式地设置默认的Spring Boot服务器HTTP端口值从'8080'改变为'9090'，如下所示。

Scroll down to # EMBEDDED SERVER CONFIGURATION in the following link in order to get a complete [overview of all the Spring Kafka properties](https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html) that can be set for auto configuration using the Spring Boot application properties file.
在下面的链接中向下滚动到＃EMBEDDED SERVER CONFIGURATION（嵌入式服务器配置），以获得可以使用Spring Boot应用程序属性文件为自动配置设置的所有Spring Kafka属性的完整概述。

```
server:
  port: 9090
```

Now that our `MessageDispatcherServlet` is defined it will try to match incoming XML messages on the defined URI with one of the available handling methods. So all we need to do is setup an `Endpoint` that contains a handling method that matches the incoming request. This service endpoint can be a simple POJO with a number of Spring WS annotations as shown below.
现在我们定义了`MessageDispatcherServlet`，它将尝试将定义的URI上的传入XML消息与其中一个可用的处理方法进行匹配。 所以我们需要做的就是设置一个包含匹配传入请求的处理方法的Endpoint。 这个服务端点可以是一个简单的POJO，带有一些Spring WS注释，如下所示。

The `HelloWorldEndpoint` POJO is annotated with the `@Endpoint` annotation which registers the class with Spring WS as a potential candidate for processing incoming SOAP messages. It contains a `sayHello()` method that receives a `Person` and returns a `Greeting`. Note that these are the Java classes that we generated earlier using JAXB (both are annotated with `@XmlRoolElement`).
`HelloWorldEndpoint` POJO使用`@Endpoint`注解进行注释，该注释将Spring WS注册为处理传入SOAP消息的候选对象。 它包含一个`sayHello()`方法，它接收一个`Person`并返回一个`Greeting`。 请注意，这些是我们先前使用JAXB生成的Java类（都使用`@XmlRoolElement`注释）。

To indicate what sort of messages a method can handle, it is annotated with the `@PayloadRoot` annotation that specifies a qualified name that is defined by a 'namespace' and a local name (='localPart'). Whenever a message comes in which has this qualified name for the payload root element, the method will be invoked.
为了说明一个方法可以处理什么类型的消息，使用`@PayloadRoot`注解来注释它，该注解指定一个由“名称空间”和本地名称（='localPart'）定义的限定名称。 每当有一个消息出现在有效载荷根元素的限定名称中时，该方法将被调用。

The `@ResponsePayload` annotation makes Spring WS map the returned value to the response payload which in our example is the JAXB `Greeting` object.
`@ResponsePayload`批注使Spring WS将返回值映射到响应有效内容，在我们的示例中为JAXB `Greeting`对象。

The `@RequestPayload` annotation on the `sayHello()` method parameter indicates that the incoming message will be mapped to the method’s request parameter. In our case, this is the JAXB `Person` object.
`sayHello()`方法参数上的`@RequestPayload`注释指示传入的消息将映射到方法的请求参数。 在我们的例子中，这是JAXB `Person`对象。

The implementation of the `sayHello` service simply logs the name of the received `Person` and then uses this name to construct a `Greeting` that is also logged and then returned.
`sayHello`服务的实现只是记录接收到的`Person`的名字，然后使用这个名字来构造一个也被记录然后返回的`Greeting`。

```java
package com.codenotfound.ws.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.codenotfound.types.helloworld.Greeting;
import com.codenotfound.types.helloworld.ObjectFactory;
import com.codenotfound.types.helloworld.Person;

@Endpoint
public class HelloWorldEndpoint {

  private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldEndpoint.class);

  @PayloadRoot(namespace = "http://codenotfound.com/types/helloworld", localPart = "person")
  @ResponsePayload
  public Greeting sayHello(@RequestPayload Person request) {
    LOGGER.info("Endpoint received person[firstName={},lastName={}]", request.getFirstName(),
        request.getLastName());

    String greeting = "Hello " + request.getFirstName() + " " + request.getLastName() + "!";

    ObjectFactory factory = new ObjectFactory();
    Greeting response = factory.createGreeting();
    response.setGreeting(greeting);

    LOGGER.info("Endpoint sending greeting='{}'", response.getGreeting());
    return response;
  }
}
```

## Creating the Client (Consumer)

The `WebServiceTemplate` is the core class for client-side Web service access in Spring-WS. It contains methods for sending requests and receiving response messages. Additionally, it can marshal objects to XML before sending them across a transport, and unmarshal any response XML into an object again.
`WebServiceTemplate`是Spring-WS中客户端Web服务访问的核心类。它包含发送请求和接收响应消息的方法。此外，它可以在将对象封送到XML之前将其发送到一个传输器，然后再将任何响应XML解组成一个对象。

As we will use JAXB to marshal our `Person` to a request XML and in turn unmarshal the response XML to our `Greeting` we need an instance of Spring’s `Jaxb2Marshaller`. This class requires a context path to operate, which you can set using the 'contextPath' property. The context path is a list of colon (:) separated Java package names that contain schema derived classes. In our example this is the package name of the generated `Person` and `Greeting` classes which is: 'com.codenotfound.types.helloworld'.
因为我们将使用JAXB将我们的`Person`编组为一个请求XML，然后将响应XML解组为我们的`Greeting`，我们需要一个Spring的`Jaxb2Marshaller`实例。这个类需要一个上下文路径来操作，你可以使用'contextPath'属性来设置。上下文路径是包含模式派生类的以冒号（:)分隔的Java包名称的列表。在我们的例子中，这是生成的`Person`和`Greeting`类的包名：“com.codenotfound.types.helloworld”。

The below `ClientConfig` configuration class specifies the `WebServiceTemplate` bean that uses the above `Jaxb2Marshaller` for marshaling and unmarshalling. We also set the default service URI using a `defaultUri` property loaded from the application.yml properties file shown below.
下面的`ClientConfig`配置类指定使用上述`Jaxb2Marshaller`进行封送处理和解组处理的`WebServiceTemplate` bean。我们还使用从下面显示的`application.yml`属性文件加载的`defaultUri`属性来设置默认的服务URI。

```
client:
  default-uri: http://localhost:9090/codenotfound/ws/helloworld
```

> Note that the 'helloworld' at the end of the default-uri can actually be omitted as previously we had specified `“/codenotfound/ws/*”` as URI of our endpoint servlet.
> 请注意，default-uri末尾的'helloworld'实际上可以省略，因为之前我们已经指定`“/codenotfound/ws/ *”`作为我们的端点servlet的URI。

> The below class is annotated with @Configuration which indicates that the class can be used by the Spring IoC container as a source of bean definitions.
> 下面的类用@Configuration标注，表明这个类可以被Spring IoC容器用作bean定义的源。

```java
package com.codenotfound.ws.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;

@Configuration
public class ClientConfig {

  @Value("${client.default-uri}")
  private String defaultUri;

  @Bean
  Jaxb2Marshaller jaxb2Marshaller() {
    Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
    jaxb2Marshaller.setContextPath("com.codenotfound.types.helloworld");

    return jaxb2Marshaller;
  }

  @Bean
  public WebServiceTemplate webServiceTemplate() {
    WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
    webServiceTemplate.setMarshaller(jaxb2Marshaller());
    webServiceTemplate.setUnmarshaller(jaxb2Marshaller());
    webServiceTemplate.setDefaultUri(defaultUri);
    return webServiceTemplate;
  }
}
```

The client code is specified in the `HelloWorldClient` class. The `sayHello()` method creates a Person object based on the 'firstname' and 'lastname' input parameters.
客户端代码在`HelloWorldClient`类中指定。 `sayHello()`方法基于'firstname'和'lastname'输入参数创建一个`Person`对象。

The auto-wired `WebServiceTemplate` is used to marshal and send a person XML request towards the Hello World service. The result is unmarshalled to a `Greeting` object which is logged.
自动连线的WebServiceTemplate用于编组并向Hello World服务发送一个XML请求。 结果被解组到一个记录的`Greeting`对象。

The `@Component` annotation will cause Spring to automatically import this bean into the container if automatic component scanning is enabled (adding the `@SpringBootApplication` annotation to the main `SpringWsApplication` class [is equivalent](http://docs.spring.io/autorepo/docs/spring-boot/current/reference/html/using-boot-using-springbootapplication-annotation.html) to using `@ComponentScan`).
如果启用了自动组件扫描（将`@SpringBootApplication`注释添加到主`SpringWsApplication`类相当于使用`@ComponentScan`），`@Component`注释将导致Spring自动将此Bean导入到容器中。

```java
package com.codenotfound.ws.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.codenotfound.types.helloworld.Greeting;
import com.codenotfound.types.helloworld.ObjectFactory;
import com.codenotfound.types.helloworld.Person;

@Component
public class HelloWorldClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldClient.class);

  @Autowired
  private WebServiceTemplate webServiceTemplate;

  public String sayHello(String firstName, String lastName) {
    ObjectFactory factory = new ObjectFactory();
    Person person = factory.createPerson();

    person.setFirstName(firstName);
    person.setLastName(lastName);

    LOGGER.info("Client sending person[firstName={},lastName={}]", person.getFirstName(),
        person.getLastName());

    Greeting greeting = (Greeting) webServiceTemplate.marshalSendAndReceive(person);

    LOGGER.info("Client received greeting='{}'", greeting.getGreeting());
    return greeting.getGreeting();
  }
}
```

## Testing the Client & Endpoint

We will create a basic unit test case in which the above client is used to send a request to the Hello World endpoint. We then verify if the response is equal to the expected Hello World greeting.
我们将创建一个基本的单元测试用例，其中上述客户端用于向Hello World端点发送请求。 然后我们验证响应是否等于预期的Hello World问候语。

The `@RunWith` and `@SpringBootTest` testing annotations, that were introduced with [Spring Boot 1.4](https://spring.io/blog/2016/04/15/testing-improvements-in-spring-boot-1-4#spring-boot-1-4-simplifications), are used to tell JUnit to run using Spring’s testing support and bootstrap with Spring Boot’s support.
Spring Boot 1.4中引入的`@RunWith`和`@SpringBootTest`测试注释用于告诉JUnit使用Spring的测试支持和Bootstrap支持来运行。

By setting the `DEFINED_PORT` web environment variable, a real HTTP server is started on the the 'server.port' property defined in the application.yml properties file.
通过设置`DEFINED_PORT` Web环境变量，将在application.yml属性文件中定义的“server.port”属性上启动一个真正的HTTP服务器。

```java
package com.codenotfound.ws;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.codenotfound.ws.client.HelloWorldClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class SpringWsApplicationTests {

  @Autowired
  private HelloWorldClient helloWorldClient;

  @Test
  public void testSayHello() {
    assertThat(helloWorldClient.sayHello("John", "Doe")).isEqualTo("Hello John Doe!");
  }
}
```

The above test case can be triggered by opening a command prompt in the projects root folder and executing following Maven command:
上面的测试用例可以通过在项目根文件夹中打开命令提示符并执行以下Maven命令来触发：

```
mvn test
```

The result should be a successful build during which the embedded Tomcat is started and a service call is made to the Hello World service:
结果应该是一个成功的构建，在这个构建过程中，启动嵌入式Tomcat并对Hello World服务进行服务调用：

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v1.5.9.RELEASE)

21:37:53.519 [main] INFO  c.c.ws.SpringWsApplicationTests - Starting SpringWsApplicationTests on cnf-pc with PID 3276 (started by CodeNotFound in c:\code\spring-ws\spring-ws-helloworld)
21:37:53.522 [main] INFO  c.c.ws.SpringWsApplicationTests - No active profile set, falling back to default profiles: default
21:37:55.819 [main] INFO  c.c.ws.SpringWsApplicationTests - Started SpringWsApplicationTests in 2.587 seconds (JVM running for 3.29)
21:37:55.849 [main] INFO  c.c.ws.client.HelloWorldClient - Client sending person[firstName=John,lastName=Doe]
21:37:56.077 [http-nio-9090-exec-1] INFO  c.c.ws.endpoint.HelloWorldEndpoint - Endpoint received person[firstName=John,lastName=Doe]
21:37:56.077 [http-nio-9090-exec-1] INFO  c.c.ws.endpoint.HelloWorldEndpoint - Endpoint sending greeting='Hello John Doe!'
21:37:56.092 [main] INFO  c.c.ws.client.HelloWorldClient - Client received greeting='Hello John Doe!'
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.954 sec - in com.codenotfound.ws.SpringWsApplicationTests

Results :

Tests run: 1, Failures: 0, Errors: 0, Skipped: 0

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 6.603 s
[INFO] Finished at: 2017-12-08T21:37:56+01:00
[INFO] Final Memory: 29M/282M
[INFO] ------------------------------------------------------------------------
```

If you just want to start Spring Boot so that the endpoint is up and running, execute following Maven command.
如果您只想启动Spring Boot以便端点启动并运行，请执行以下Maven命令。

```
mvn spring-boot:run
```

As mentioned earlier, the service WSDL is exposed on the following endpoint: http://localhost:9090/codenotfound/ws/helloworld.wsdl. This can be verified by opening it in a browser as shown below.
如前所述，服务WSDL公开在以下端点上：`http://localhost:9090/codenotfound/ws/helloworld.wsdl`。 这可以通过在浏览器中打开它来验证，如下所示。

```xml
<?xml version="1.0" encoding="UTF-8"?><wsdl:definitions xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/" name="HelloWorld" targetNamespace="http://codenotfound.com/services/helloworld" xmlns:tns="http://codenotfound.com/services/helloworld" xmlns:types="http://codenotfound.com/types/helloworld" xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/">

  <wsdl:types>
    <xsd:schema xmlns:xsd="http://www.w3.org/2001/XMLSchema" targetNamespace="http://codenotfound.com/types/helloworld" elementFormDefault="qualified" attributeFormDefault="unqualified" version="1.0">

      <xsd:element name="person">
        <xsd:complexType>
          <xsd:sequence>
            <xsd:element name="firstName" type="xsd:string"/>
            <xsd:element name="lastName" type="xsd:string"/>
          </xsd:sequence>
        </xsd:complexType>
      </xsd:element>

      <xsd:element name="greeting">
        <xsd:complexType>
          <xsd:sequence>
            <xsd:element name="greeting" type="xsd:string"/>
          </xsd:sequence>
        </xsd:complexType>
      </xsd:element>
    </xsd:schema>
  </wsdl:types>

  <wsdl:message name="SayHelloInput">
    <wsdl:part name="person" element="types:person"/>
  </wsdl:message>

  <wsdl:message name="SayHelloOutput">
    <wsdl:part name="greeting" element="types:greeting"/>
  </wsdl:message>

  <wsdl:portType name="HelloWorld_PortType">
    <wsdl:operation name="sayHello">
      <wsdl:input message="tns:SayHelloInput"/>
      <wsdl:output message="tns:SayHelloOutput"/>
    </wsdl:operation>
  </wsdl:portType>

  <wsdl:binding name="HelloWorld_SoapBinding" type="tns:HelloWorld_PortType">
    <soap:binding style="document" transport="http://schemas.xmlsoap.org/soap/http"/>
    <wsdl:operation name="sayHello">
      <soap:operation soapAction="http://codenotfound.com/services/helloworld/sayHello"/>
      <wsdl:input>
        <soap:body use="literal"/>
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal"/>
      </wsdl:output>
    </wsdl:operation>
  </wsdl:binding>

  <wsdl:service name="HelloWorld_Service">
    <wsdl:documentation>Hello World service</wsdl:documentation>
    <wsdl:port name="HelloWorld_Port" binding="tns:HelloWorld_SoapBinding">
      <soap:address location="http://localhost:9090/codenotfound/ws/helloworld"/>
    </wsdl:port>
  </wsdl:service>

</wsdl:definitions>
```





