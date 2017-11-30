# Spring Boot Actuator

If you want enhanced control over your endpoints, including tuning sensitivity and security, you should familiarize yourself with Spring Boot Actuator and its tools.

如果您希望加强对端点的控制，包括调试敏感信息和安全性，您应该熟悉SpringBoot执行器及其工具。

Spring Boot Actuator is a sub-project of Spring Boot. It provides several production-grade services to your application out of the box. Once Actuator is configured in your Spring Boot application, you can interact and monitor your application by invoking different HTTP endpoints exposed by Spring Boot Actuator such as application health, bean details, version details, configurations, logger details, etc.

SpringBoot执行器是SpringBoot的一个子工程。它为您的应用程序提供几个生产级服务。一旦在SpringBoot应用程序中配置了执行器，您就可以通过调用由SpringBoot执行器公开的不同HTTP端点来交互和监视应用程序，如应用程序健康、bean详细信息、版本详细信息、配置、日志记录细节等。

Spring Boot includes a number of built-in endpoints, and you can also add your own or even configure existing endpoints to be exposed on any custom endpoints of your choice. It is obvious that all the endpoints cannot be exposed publicly, considering that there are many sensitive endpoints like beans, env, etc. Hence, Spring Boot also sets sensitive defaults to true for many endpoints that require a username/password when they are accessed over HTTP (or simply disabled if web security is not enabled). Health and info are not sensitive by default.

SpringBoot包含许多内置端点，您还可以添加自己的配置，甚至可以配置现有的端点以暴露在您选择的任意自定义端点上。显然，所有的端点都不能公开暴露，考虑到有许多敏感的端点，比如bean、环境等，因此，当许多端点在HTTP访问时也需要用户名/密码时，SpringBoot也设置了敏感的默认值（或者如果没有启用Web安全性，则简单地禁用）。默认情况下，健康检查和公开信息不敏感。

## How to Enable Spring Boot Actuator

This is easy. You only need to include the following maven dependency in your existing pom.xml file:

这很容易。只需要在现有Maven pom.xml文件中添加依赖：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

## Different Actuator Endpoints

Once above maven dependency is included in the POM file, 16 different actuator REST endpoints, such as actuator, beans, dump, info, loggers, and metrics are exposed.

For a complete list of actuator REST endpoints, with examples, you can take a look [here](http://www.devglan.com/spring-boot/spring-boot-actuator-rest-endpoints-example).

If you are using Spring MVC on top of this, then four additional endpoints — docs, heapdump, jolokia, and logfile can be used.

## Customizing Actuator Endpoints

Spring Boot allows customizing endpoints by using Spring properties. Simply mention the properties you want to customize in your application.properties. You can customize an endpoint in three ways. You can enable or disable an endpoint, customize its sensitivity, and also its id.

The following is an example that changes the sensitivity and id of the metrics endpoint and also enables shutdown.

```
endpoints.metrics.id=springmetrics
endpoints.metrics.sensitive=false
endpoints.metrics.enabled=true
```

Apart from this, you can also customize the endpoints globally. The following example marks all endpoints as sensitive except info.

```
endpoints.sensitive=true
endpoints.info.sensitive=false
```

If you're interested, [here are the code and configurations](http://www.devglan.com/spring-boot/spring-boot-actuator-rest-endpoints-example/#/customizing-actuator-endpoints) for customization of actuator endpoints.

## Custom Endpoint Implementation

Apart from the above default endpoints exposed by spring boot, We can write our own endpoints by implementing the interface Endpoint. This is useful when you want to expose application details which are an added feature to your application. For example, in this example I have created a custom endpoint to display the server details for the application.

The implementation class would looks like this:

```java
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.stereotype.Component;

@Component
public class ServerEndpoint implements Endpoint<List<String>> {
  
  public String getId() {
    return "server";
  }

  public List<String> invoke() {
    List<String> serverDetails = new ArrayList<String>();
    try {
      serverDetails.add("Server IP Address : " + InetAddress.getLocalHost().getHostAddress());
      serverDetails.add("Server OS : " + System.getProperty("os.name").toLowerCase());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return serverDetails;
  }

  public boolean isEnabled() {
    return true;
  }

  public boolean isSensitive() {
    return false;
  }
}
```

Here’s what the output might look like:

```
  ["Server IP Address : 192.168.1.164","Server OS : Mac OS X"]
```

## Securing Actuator Endpoints

As we saw, there are only two endpoints, health and info, that are by default not sensitive. But other endpoints, like loggers and beans, that are sensitive and hence require authorization to access. To access these sensitive endpoints, you can either disable the sensitivity or secure it using Spring Security.

To secure the actuator endpoints, include following maven dependency in your pom.xml file.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

In Spring Boot, including the above dependencies will by default provide inbuilt form-based authentication with the userid as the user and a randomly generated password. The following entries are then required to enable basic security to your sensitive endpoints.

```
management.security.enabled=true
security.basic.enabled=true
security.user.name=admin
security.user.password=admin
```

To access the actuator-restricted endpoints, you have to have the ACTUATOR role. It is a default configuration.

Apart from this, you can also secure actuator REST endpoints using AuthenticationManagerBuilder  by extending the WebSecurityConfigurerAdapter class provided by Spring. [Here is the complete implementation](http://www.devglan.com/spring-security/securing-spring-boot-actuator-endpoints-with-spring-security) using AuthenticationManagerBuilder.

## Custom Metric Data

There is actually support here for ‘gauge’ – single value data, and ‘counter’ – incrementing/decrementing data types of metrics. Let’s make use of some of this support to implement our own custom data into the /metrics endpoint.

For example – we’ll customize the login flow to record a successful and failed log in the attempt:

```java
public interface LoginService {
    public boolean login(String userName, char[] password);
}
```

And the actual implementation, where we increase the relevant counters:

```java
@Service
public class LoginServiceImpl implements LoginService {
 
    private CounterService counterService;
     
    @Autowired
    public LoginServiceImpl(CounterService counterService) {
        this.counterService = counterService;
    }
     
    public boolean login(String userName, char[] password) {
        boolean success;
        if (userName.equals("admin") && "secret".toCharArray().equals(password)) {
            counterService.increment("counter.login.success");
            success = true;
        }
        else {
            counterService.increment("counter.login.failure");
            success = false;
        }
        return success;
    }
}
```

Here’s what the output might look like:

```
{
    ...
    "counter.login.success" : 105,
    "counter.login.failure" : 12,
    ...
}
```

## Creating a Custom Actuator Endpoint to List All EndPoints

The best thing about Spring is that it always encourages developers to come up with their own configurations and implementations — and this is the case with actuator endpoints, too.

To customize the endpoint and define your own endpoint, simply implement the interface Endpoint and override its methods. That's it, you're finished exposing your own endpoints.

The following is a simple code snippet that defines a custom endpoint in Spring Actuator. It can be accessed at /showendpoints.

```java
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.stereotype.Component;
import java.util.List;
@Component
public class ListEndPoints extends AbstractEndpoint<List<Endpoint>> {
    private List<Endpoint> endpoints;
    public ListEndPoints(List<Endpoint> endpoints) {
        super("showendpoints");
        this.endpoints = endpoints;
    }
    @Override
    public List<Endpoint> invoke() {
        return this.endpoints;
    }
}
```

## Create A New Endpoint

Besides using the existing endpoints provided by Spring Boot – you can also create an entirely new endpoint.

First – you’ll need to have the new endpoint implementation implement the Endpoint<T> interface:

```java
@Component
public class CustomEndpoint implements Endpoint<List<String>> {
     
    public String getId() {
        return "customEndpoint";
    }
 
    public boolean isEnabled() {
        return true;
    }
 
    public boolean isSensitive() {
        return true;
    }
 
    public List<String> invoke() {
        // Custom logic to build the output
        List<String> messages = new ArrayList<String>();
        messages.add("This is message 1");
        messages.add("This is message 2");
        return messages;
    }
}
```

The way to access this new endpoint is by its id, at /customEndpoint.

Output:

```
[ "This is message 1", "This is message 2" ]
```

## Further Customization

For security purposes, you might choose to expose the actuator endpoints over a non-standard port – the management.port property can easily be used to configure that.

You can also change the management.address property to restrict where the endpoints can be accessed from over the network:

```
management.port=8081
management.address=127.0.0.1
management.security.enabled=false
```

All the built-in endpoints except /info are sensitive by default. If the application is using Spring Security – you can secure these endpoints by defining the default security properties – username, password, and role – in the application.properties file:

```
security.user.name=admin
security.user.password=secret
management.security.role=SUPERUSER
```

## Health endpoint
The /health endpoint is used to check the health/status of the running application. It’s usually used by basic monitoring software to alert you if the production goes down.

By default only health information is shown to unauthorized access over HTTP:

```
{
   "status" : "UP"
}
```

This health information is collected from all the beans implementing HealthIndicator interface configured in your application context.

Some information returned by `HealthIndicator` is sensitive in nature – but you can configure `endpoints.health.sensitive=false` to expose the other information like disk space, data source etc.

## Custom Health Endpoint
You can also roll your own custom health indicator. One can extend the HealthIndicator interface and provide their own implementation. CustomHealthCheck is implementing the method health() which is declared in the interface HealthIndicator. When you create a custom class of this type and override the method health(), the default functionality will be overwritten by your custom logic.

```java
@Component
public class CustomHealthCheck implements HealthIndicator {
    public Health health() {
        int errorCode = 0;
        if (errorCode != 1) {
            return Health.down().withDetail("Error Code", errorCode).build();
        }
        return Health.up().build();
    }
}
```

The output will be:

```
    {
      "status": "DOWN",
      "customHealthCheck": {
        "status": "DOWN",
        "Error Code": 0
      },
      "diskSpace": {
        "status": "UP",
        "free": 19593824409432,
        "threshold": 15485791
      }
    }
```

## 参考

* [Spring Boot Actuator: A Complete Guide](https://dzone.com/articles/spring-boot-actuator-a-complete-guide)
* [An-introduction-to-spring-boot-actuator](https://aboullaite.me/an-introduction-to-spring-boot-actuator/)