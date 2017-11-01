# Hystrix Dashboard

## Hystrix简介

### [雪崩效应](http://blog.csdn.net/w_x_z_/article/details/53444199)

在微服务架构中通常会有多个服务层调用，大量的微服务通过网络进行通信，从而支撑起整个系统。各个微服务之间也难免存在大量的依赖关系。然而任何服务都不是100%可用的，网络往往也是脆弱的，所以难免有些请求会失败。基础服务的故障导致级联故障，进而造成了整个系统的不可用，这种现象被称为服务雪崩效应。服务雪崩效应描述的是一种因服务提供者的不可用导致服务消费者的不可用，并将不可用逐渐放大的过程。

### Netflix Hystrix 断路器

Netflix的Hystrix类库实现了断路器模式，在microservice架构中有多个层的服务调用。一个低水平的服务群中一个服务挂掉会给用户导致级联失效。调用一个特定的服务达到一定阈值(在Hystrix里默认是5秒内20个失败)，断路由开启并且调用没有成功的。开发人员能够提供错误原因和开启一个断路由回调。

Netflix已经创建了一个名为Hystrix的库,实现了断路器的模式。在microservice架构通常有多个层的服务调用。低水平的服务的服务失败会导致级联故障一直给到用户。当调用一个特定的服务达到一定阈值(默认5秒失败20次),打开断路器。在错误的情况下和一个开启的断路回滚应可以由开发人员提供。有一个断路器阻止级联失败并且允许关闭服务一段时间进行愈合。回滚会被其他hystrix保护调用，静态数据或健全的空值。

在微服务架构中为例保证程序的可用性，防止程序出错导致网络阻塞，出现了断路器模型。断路器的状况反应了一个程序的可用性和健壮性，它是一个重要指标。Hystrix Dashboard是作为断路器状态的一个组件，提供了数据监控和友好的图形化界面。

### Hystrix Dashboard简介

在微服务架构中为例保证程序的可用性，防止程序出错导致网络阻塞，出现了断路器模型。断路器的状况反应了一个程序的可用性和健壮性，它是一个重要指标。Hystrix Dashboard是作为断路器状态的一个组件，提供了数据监控和友好的图形化界面。

### 实现Hystrix Dashboard服务

* 在工程eureka-client中引入maven依赖：

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-hystrix-dashboard</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-hystrix</artifactId>
</dependency>
```

* 在程序的入口ServiceHiApplication类，加上以下注解：
	- 加上`@EnableHystrix注解开启断路器`注解开启断路器。
	- 在程序中声明断路点`@HystrixCommand`。
	- 加上`@EnableHystrixDashboard`注解，开启HystrixDashboard。

* 开启eureka-server和eureka-client。

* 访问地址：
	- 打开地址访问：[Hystrix Dashboard](http://localhost:8763/hystrix)。
		+ 在界面依次输入：http://locahost:8763/hystrix.stream 、2000 、miya；点确定。
		+ 在另一个窗口输入： http://localhost:8763/hi?name=OK。重新刷新hystrix.stream网页，你会看到图形化界面：
	- 打开地址访问：[http://localhost:8762/hystrix.stream](http://localhost:8762/hystrix.stream)，可以看到一些具体的数据

### 参考资料

* [spring cloud: Hystrix断路器(熔断器)](http://blog.csdn.net/zhuchuangang/article/details/51289593)
* [Spring Cloud 入门教程(八)： 断路器指标数据监控Hystrix Dashboard 和 Turbine](http://www.cnblogs.com/chry/p/7286601.html)

