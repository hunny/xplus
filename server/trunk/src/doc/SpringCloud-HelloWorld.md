# SpringCloud系列

## HelloWorld系列

* [Spring Cloud中文网](https://springcloud.cc/)

* [Spring Cloud Dalston版本中文网](https://springcloud.cc/spring-cloud-dalston.html)

* [一、服务的注册与发现（Eureka）](../../eureka-server/src/test/java/com/xplus/server/eureka/服务的注册与发现（Eureka）.md)
	- Eureka是一个服务注册和云端服务发现模块，一个基于 REST 的服务，用于定位服务，以实现云端中间层服务发现和故障转移。。
	- 服务注册中心：eureka-server，端口为8761。
	- 服务提供者：eureka-client，端口为8762，向服务注册中心注册。

* [二、负载均衡客户端（Ribbon）和服务消费者（rest）](../../balancer-ribbon/src/test/java/com/xplus/server/balancer/ribbon/服务消费者（rest+ribbon）.md)
	- Ribbon是一个负载均衡客户端，能很好地控制HTTP和TCP的行为，提供云端负载均衡，有多种负载均衡策略可供选择，可配合服务发现和断路器使用。
	- 服务注册中心：eureka-server，端口为8761。
	- 服务消费者：eureka-client运行两个实例，端口分别为8762和8763，分别向服务注册中心注册，用于向消费者balancer-ribbon反馈数据。
	- 负载均衡客户端：balancer-ribbon，端口为8764，向服务注册中心注册，获取来自eureka-client的数据。

* [三、声明式的伪Http客户端服务消费者（Feign）](../../service-feign/src/test/java/com/xplus/server/service/feign/服务消费者（Feign）.md)
	- Feign采用的是基于接口的注解，使用声明式的伪Http客户端，默认集成了Ribbon，并和Eureka结合，实现负载均衡的效果。
	- 服务注册中心：eureka-server，端口为8761。
	- 服务消费者Feign：service-feign，端口8765，向服务注册中心注册，获取来自eureka-client的数据。
	- 服务消费者：eureka-client运行两个实例，端口分别为8762和8763，分别向服务注册中心注册，用于向消费者service-feign反馈数据。

* [四、断路器（Hystrix）及Hystrix仪表盘](../../service-hystrix/src/test/java/com/xplus/server/service/hystrix/断路器（Hystrix）.md)
	- 当对特定的服务的调用的不可用达到一个阀值（Hystric 是5秒20次） 断路器将会被打开，可用于避免连锁故障，使用fallback方法直接返回一个固定值。
	- 服务注册中心：eureka-server，端口为8761。
	- 服务消费者：eureka-client，端口为8762，向服务注册中心注册，用于向有断路器的消费者service-hystrix反馈数据。
	- 有断路器的服务消费者：service-hystrix，端口8766，向服务注册中心注册，获取来自eureka-client的数据，当消费者eureka-client不可用时，service-hystrix中的断路器会被触发。

* [五、路由网关、服务过滤和安全验证等(zuul)](../../service-zuul/src/test/java/com/xplus/server/service/zuul/路由网关(zuul).md)
	- Zuul是Netflix出品的一个基于JVM路由和服务端的负载均衡器，主要功能是路由转发和服务过滤，它默认和Ribbon结合实现了负载均衡的功能，Zuul 是在云平台上提供动态路由,监控,弹性,安全等边缘服务的框架。Zuul 相当于是设备和 Netflix 流应用的 Web 网站后端所有请求的前门。zuul有以下功能：
		+ Authentication（认证）
		+ Insights（洞悉）
		+ Stress Testing（压力测试）
		+ Canary Testing（金丝雀测试）
		+ Dynamic Routing（动态路由）
		+ Service Migration（服务迁移）
		+ Load Shedding（负载削减）
		+ Security（安全）
		+ Static Response handling（静态响应处理）
		+ Active/Active traffic management（主动/主动交换管理）
	- Spring Cloud创建了一个嵌入式Zuul代理来缓和急需一个UI应用程序来代理调用一个或多个后端服务的通用需求，这个功能对于代理前端需要访问的后端服务非常有用，避免了所有后端服务需要关心管理CORS和认证的问题。
	- 服务注册中心：eureka-server，端口为8761。
	- 路由网关消费者：service-zuul，端口为8769，向服务注册中心注册，根据路由网关配置，可转发请求到balancer-ribbon和service-feign服务中。
	- 服务消费者：eureka-client，端口为8762，向服务注册中心注册，用于向服务消费者balancer-ribbon和service-feign提供响应数据。
	- 服务消费者：balancer-ribbon，端口为8764，向服务注册中心注册，从服务消费者eureka-client获取数据。
	- 服务消费者：service-feign，端口为8765，向服务注册中心注册，从服务消费者eureka-client获取数据。

* [六、分布式配置中心(Spring Cloud Config)](../../config-server/src/test/java/com/xplus/server/config/server/分布式配置中心(SpringCloudConfig).md)
	- 配置管理工具包，可以把配置放到远程服务器，集中化管理集群配置，目前支持本地存储、Git以及Subversion。
	- 分布式配置中心服务端: config-server，端口8888，提供配置获取服务。
	- 分布式配置中心客户端: config-client，端口8881，需要从配置服务端获取配置信息。

* [七、高可用的分布式配置中心(Spring Cloud Config)](../../config-server/src/test/java/com/xplus/server/config/server/高可用的分布式配置中心(SpringCloudConfig).md)
	- 构建微服务集群化高可用分布式配置中心。
	- 服务注册中心：eureka-server，端口为8761。
	- 分布式配置中心服务端: config-server，端口8888，向服务注册中心注册，提供配置获取服务。
	- 分布式配置中心客户端: config-client，端口8881，向服务注册中心注册，需要从配置服务端获取配置信息。

* [八、消息总线(SpringCloudBus)](../../cloud-bus/src/test/java/com/xplus/server/cloud/bus/消息总线(SpringCloudBus).md)
	- Spring Cloud Bus（Spring事件、消息总线）将分布式的节点用轻量的消息代理连接起来，用于在集群（例如，配置变化事件）中传播状态变化，可与Spring Cloud Config联合实现热部署。
	- 服务注册中心：eureka-server，端口为8761。
	- 分布式配置中心服务端: config-server，端口8888，向服务注册中心注册，提供配置获取服务。
	- 带消息总线的分布式配置中心客户端: cloud-bus，默认端口8881，向服务注册中心注册，需要至少运行两个实例，需要从配置服务端获取配置信息。
	- [配置中心svn示例](../../cloud-bus/src/test/java/com/xplus/server/cloud/bus/配置中心svn示例和refresh.md)。
	- [安装RabbitMQ](/cloud-bus/src/test/java/com/xplus/server/cloud/bus/InstallRabbitMQ.md)。

* [九、服务链路追踪(Spring Cloud Sleuth)](../../sleuth-zipkin/src/test/java/com/xplus/server/sleuth/zipkin/服务链路追踪(SpringCloudSleuth).md)
	- Spring Cloud Sleuth 主要是分布式系统中的日志收集工具包，封装了Dapper和log-based追踪以及Zipkin和HTrace操作，为SpringCloud应用实现了一种分布式追踪解决方案。
	- 收集追踪服务端：sleuth-zipkin，端口9411，提供日志数据收集服务。
	- 被追踪服务端：sleuth-svrhi，端口8988，与sleuth-svrhello相互调用，产生追踪信息。
	- 被追踪服务端：sleuth-svrhello，端口8989，与sleuth-svrhi相互调用，产生追踪信息。

* [十、高可用的服务注册中心](../../eureka-cloud/src/test/java/com/xplus/server/eureka/cloud/高可用的服务注册中心.md)
    - 主要进行集群化Eureka Server。
	- 服务注册中心：eureka-cloud，通过spring.profiles.active启动两个服务，并相互注册。
	- 服务提供者：eureka-client，端口为8762，向服务注册中心注册。


