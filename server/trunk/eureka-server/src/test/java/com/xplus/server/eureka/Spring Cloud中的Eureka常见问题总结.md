# Spring Cloud中Eureka常见问题总结

## 指定Eureka的Environment

```
eureka.environment: 指定环境
eureka.environment: eureka-client-{test,prod}.properties
```

The easiest way to configure Eureka client is by using property files. By default, Eureka client searches for the property file eureka-client.properties in the classpath. It further searches for environment specific overrides in the environment specific properties files. The environment is typically test or prod and is supplied by a -Deureka.environment java commandline switch to the eureka client (without the .properties suffix). Accordingly, the client also searches for eureka-client-{test,prod}.properties.

参考文档：[https://github.com/Netflix/eureka/wiki/Configuring-Eureka](https://github.com/Netflix/eureka/wiki/Configuring-Eureka)

## 指定Eureka的DataCenter

```
eureka.datacenter: 指定数据中心

```

If you are running in the cloud environment, you will need to pass in the java commandline property -Deureka.datacenter=cloud so that the Eureka Client/Server knows to initialize the information specific to AWS cloud.

参考文档：[https://github.com/Netflix/eureka/wiki/Configuring-Eureka](https://github.com/Netflix/eureka/wiki/Configuring-Eureka)，文中指出，配置-Deureka.datacenter=cloud，这样eureka将会知道是在AWS云上。

## 如何解决Eureka注册服务慢的问题

使用配置项：

```
eureka.instance.leaseRenewalIntervalInSeconds
```

参考文档：http://cloud.spring.io/spring-cloud-static/Camden.SR1/#_why_is_it_so_slow_to_register_a_service

- 原文：

```
Being an instance also involves a periodic heartbeat to the registry (via the client’s serviceUrl) with default duration 30 seconds. A service is not available for discovery by clients until the instance, the server and the client all have the same metadata in their local cache (so it could take 3 heartbeats). You can change the period using eureka.instance.leaseRenewalIntervalInSeconds and this will speed up the process of getting clients connected to other services. In production it’s probably better to stick with the default because there are some computations internally in the server that make assumptions about the lease renewal period.
```

- 翻译：

```
作为实例还涉及到与注册中心的周期性心跳，默认持续时间为30秒（通过serviceUrl）。在实例、服务器、客户端都在本地缓存中具有相同的元数据之前，服务不可用于客户端发现（所以可能需要3次心跳）。你可以使用eureka.instance.leaseRenewalIntervalInSeconds 配置，这将加快客户端连接到其他服务的过程。在生产中，最好坚持使用默认值，因为在服务器内部有一些计算，他们对续约做出假设。
```

## Eureka的自我保护模式

- 如果在Eureka Server的首页看到以下这段提示，则说明Eureka已经进入了保护模式。

```
EMERGENCY! EUREKA MAY BE INCORRECTLY CLAIMING INSTANCES ARE UP WHEN THEY'RE NOT. RENEWALS ARE LESSER THAN THRESHOLD AND HENCE THE INSTANCES ARE NOT BEING EXPIRED JUST TO BE SAFE.
```

保护模式主要用于一组客户端和Eureka Server之间存在网络分区场景下的保护。一旦进入保护模式，Eureka Server将会尝试保护其服务注册表中的信息，不再删除服务注册表中的数据（也就是不会注销任何微服务）。

详见：https://github.com/Netflix/eureka/wiki/Understanding-Eureka-Peer-to-Peer-Communication

## 如何解决Eureka Server不踢出已关停的节点的问题

在开发过程中，我们常常希望Eureka Server能够迅速有效地踢出已关停的节点，但是新手由于Eureka自我保护模式，以及心跳周期长的原因，常常会遇到Eureka Server不踢出已关停的节点的问题。解决方法如下：

(1) Eureka Server端：配置关闭自我保护，并按需配置Eureka Server清理无效节点的时间间隔。

```
eureka.server.enable-self-preservation			# 设为false，关闭自我保护
eureka.server.eviction-interval-timer-in-ms     # 清理间隔（单位毫秒，默认是60*1000）
```

(2) Eureka Client端：配置开启健康检查，并按需配置续约更新时间和到期时间。

```
eureka.client.healthcheck.enabled			# 开启健康检查（需要spring-boot-starter-actuator依赖）
eureka.instance.lease-renewal-interval-in-seconds		# 续约更新时间间隔（默认30秒）
eureka.instance.lease-expiration-duration-in-seconds 	# 续约到期时间（默认90秒）
```

- 示例：
服务器端配置：

```
eureka:
  server:
    enable-self-preservation: false
    eviction-interval-timer-in-ms: 4000
```

客户端配置：

```
eureka:
  client:
    healthcheck:
      enabled: true
  instance:
    lease-expiration-duration-in-seconds: 30 
    lease-renewal-interval-in-seconds: 10
```

注意：更改Eureka更新频率将打破服务器的自我保护功能，生产环境下不建议自定义这些配置。
详见：https://github.com/spring-cloud/spring-cloud-netflix/issues/373

## 自定义Eureka的Instance ID

在Spring Cloud中，服务的Instance ID的默认值是${spring.cloud.client.hostname}:${spring.application.name}:${spring.application.instance_id:${server.port}} ，也就是机器主机名:应用名称:应用端口 。因此在Eureka Server首页中看到的服务的信息类似如下：itmuch:microservice-provider-user:8000 。如果想要自定义这部分的信息怎么办？

- 示例：

```
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
  instance:
    preferIpAddress: true
    instance-id: ${spring.cloud.client.ipAddress}:${server.port}        # 将Instance ID设置成IP:端口的形式
```

## Eureka配置最佳实践参考

https://github.com/spring-cloud/spring-cloud-netflix/issues/203

注意点：```eureka.client.healthcheck.enabled=true``` 配置项必须设置在application.yml中

```
eureka.client.healthcheck.enabled=true 
```
只应该在application.yml中设置。如果设置在bootstrap.yml中将会导致一些不良的副作用，例如在Eureka中注册的应用名称是UNKNOWN等。

## 禁止Eureka服务注册中心注册自己

在默认设置下，Eureka服务注册中心也会将自己作为客户端来尝试注册它自己，所以我们需要禁用它的客户端注册行为。 
禁止方式如下：
```
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```
如果不禁止的话，会得到如下错误：

```
com.sun.jersey.api.client.ClientHandlerException: java.net.ConnectException: Connection refused: connect
2017-04-16 22:16:12.943  WARN 6864 --- [           main] c.n.d.s.t.d.RetryableEurekaHttpClient    : Request execution failure
2017-04-16 22:16:12.951 ERROR 6864 --- [           main] com.netflix.discovery.DiscoveryClient    : DiscoveryClient_UNKNOWN/DESKTOP-MQ8D0C9:8761 - was unable to refresh its cache! status = Cannot execute request on any known server
```

应该是因为当注册中心将自己作为客户端注册的时候，发现在server上的端口被自己占据了，然后就挂了。

如果要开启自动注册的话，可以启动两个server，互相注册 
```
A：eureka.client.serviceUrl.defaultZone=http://localhost:1112/eureka/ 
B：eureka.client.serviceUrl.defaultZone=http://localhost:1111/eureka/
```

## 显式指定端口server.port = 8761

启动服务注册中心的时候，如果没在application.properties中显示指定端口的话，我的机子上默认是8761端口，然后虽然启动没问题，但是访问local host:8761/的时候就访问不了，但是如果指定端口server.port = 8761，就一切正常了。不知道为什么，如果有人知道的话请指点一下。

## 界面上显示了红色粗体警告信息

启动两个client，过了一会，停了其中一个，访问注册中心时，界面上显示了红色粗体警告信息：
```
EMERGENCY! EUREKA MAY BE INCORRECTLY CLAIMING INSTANCES ARE UP WHEN THEY'RE NOT. RENEWALS ARE LESSER THAN THRESHOLD AND HENCE THE INSTANCES ARE NOT BEING EXPIRED JUST TO BE SAFE.
```
查阅了很多资料，终于了解了中间的问题。现将理解整理如下：

Eureka server和client之间每隔30秒会进行一次心跳通信，告诉server，client还活着。由此引出两个名词： 
Renews threshold：server期望在每分钟中收到的心跳次数 
Renews (last min)：上一分钟内收到的心跳次数。

前文说到禁止注册server自己为client，不管server是否禁止，阈值（threshold）是1。client个数为n，阈值为1+2*n（此为一个server且禁止自注册的情况） 
如果是多个server，且开启了自注册，那么就和client一样，是对于其他的server来说就是client，是要*2的

我开了两个server，自注册，相关数据如下 
| Lease expiration enabled | true |
| --- | --- |
| Renews threshold | 3 |
| Renews (last min) | 4 |

阈值：1+2*1 
renews： 
1）自注册 2 + 2*1 
2）非自注册：2*1

Eurake有一个配置参数eureka.server.renewalPercentThreshold，定义了renews 和renews threshold的比值，默认值为0.85。当server在15分钟内，比值低于percent，即少了15%的微服务心跳，server会进入自我保护状态，Self-Preservation。在此状态下，server不会删除注册信息，这就有可能导致在调用微服务时，实际上服务并不存在。 
这种保护状态实际上是考虑了client和server之间的心跳是因为网络问题，而非服务本身问题，不能简单的删除注册信息

stackoverflow上，有人给出的建议是： 
1、在生产上可以开自注册，部署两个server 
2、在本机器上测试的时候，可以把比值调低，比如0.49 
3、或者简单粗暴把自我保护模式关闭
```
eureka.server.enableSelfPreservation=false
```
参考资料： 
1、understanding spring cloud eureka server self-preservation and renew threshold(http://stackoverflow.com/questions/33921557/understanding-spring-cloud-eureka-server-self-preservation-and-renew-threshold) 
2、Understanding eureka client server communicationUnderstanding eureka client server communication(https://github.com/Netflix/eureka/wiki/Understanding-eureka-client-server-communication) 
3、Eureka never unregisters a service 
(http://stackoverflow.com/questions/32616329/eureka-never-unregisters-a-service)

## 多个eureka互相注册

参见[http://blog.csdn.net/zzp448561636/article/details/70198878](http://blog.csdn.net/zzp448561636/article/details/70198878)

前面说到多个eureka互相注册，那么是否相应的服务需要添加多个注册中心的地址呢？

```
eureka.client.serviceUrl.defaultZone=http://localhost:1111/eureka/,http://localhost:1112/eureka/
```
这样写多次当然是可以的。 
但是eureka之间的同步机制，可以简化一下这一配置。多个eureka实例需要两两互相注册，就可以实现集群中节点完全对等的效果，此时只要服务注册到其中一个注册中心，就会同步到其他注册中心。

如果不是两两注册，例如A->B->C->D->A，就不行。 

其次我们还需要开启自注册功能。 
否则话，eurake-service不会互相注册下去，computer-service只在ab上存在。 
测试截图如下： 
AB一致： 
CD一致：

## 参考资料

- [Spring Boot基础教程](http://blog.didispace.com/Spring-Boot%E5%9F%BA%E7%A1%80%E6%95%99%E7%A8%8B/)
- [Spring Cloud基础教程](http://blog.didispace.com/Spring-Cloud%E5%9F%BA%E7%A1%80%E6%95%99%E7%A8%8B/)
- [史上最简单的 SpringCloud 教程 | 终章](http://blog.csdn.net/forezp/article/details/70148833)
- [开源中国](http://git.oschina.net/didispace)
- [程序猿DD / SpringBoot-Learning](https://gitee.com/didispace/SpringBoot-Learning)
- [程序猿DD / SpringCloud-Learning](https://gitee.com/didispace/SpringCloud-Learning)
- [程序猿DD / spring-boot-starter-swagger](https://gitee.com/didispace/spring-boot-starter-swagger)
- [《Spring Boot参考指南》](http://blog.didispace.com/books/spring-boot-reference/)




