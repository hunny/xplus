# 高可用的分布式配置中心(Spring Cloud Config)

[分布式配置中心(Spring Cloud Config)]文章讲述了一个服务如何从配置中心读取文件，配置中心如何从远程git读取配置文件，当服务实例很多时，都从配置中心读取文件，这时可以考虑将配置中心做成一个微服务，将其集群化，从而达到高可用。

## 准备

继续使用上一篇文章的工程，使用eureka-server工程，用作服务注册中心。

## 改造config-server

在其pom.xml文件加上EurekaClient的起步依赖spring-cloud-starter-eureka，代码如下:

```
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-config-server</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka</artifactId>
        </dependency>
    </dependencies>
```

配置文件application.yml，指定服务注册地址为http://localhost:8889/eureka/，其他配置同上一篇文章，完整的配置如下：

```
spring.application.name=config-server
server.port=8888

spring.cloud.config.server.git.uri=https://github.com/hunny/xplus/
spring.cloud.config.server.git.searchPaths=server/trunk/config-server/src/main/resources/config
spring.cloud.config.label=master
#spring.cloud.config.server.git.username=
#spring.cloud.config.server.git.password=
eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka/
```

最后需要在程序的启动类Application加上@EnableEurekaClient的注解。

## 改造config-client

将其注册微到服务注册中心，作为Eureka客户端，需要pom文件加上起步依赖spring-cloud-starter-eureka，代码如下：

```
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
```

配置文件bootstrap.properties，注意是bootstrap。加上服务注册地址为http://localhost:8761/eureka/

```
server.port=8881
spring.application.name=config-client
spring.cloud.config.label=master
spring.cloud.config.profile=dev
spring.cloud.config.uri= http://localhost:8888/

eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka/
spring.cloud.config.discovery.enabled=true
spring.cloud.config.discovery.serviceId=config-server
```

- spring.cloud.config.discovery.enabled 是从配置中心读取文件。
- spring.cloud.config.discovery.serviceId 配置中心的servieId，即服务名。

这时发现，在读取配置文件不再写ip地址，而是服务名，这时如果配置服务部署多份，通过负载均衡，从而高可用。

依次启动eureka-servr,config-server,config-client 
访问网址：http://localhost:8761/

访问http://localhost:8881/hi，浏览器显示：

```
foo version 3
```





