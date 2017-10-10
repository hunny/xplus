# SpringCloud系列

## HelloWorld系列

* [一、服务的注册与发现（Eureka）](../../eureka-server/src/test/java/com/xplus/server/eureka/服务的注册与发现（Eureka）.md)
	- Eureka是一个服务注册和发现模块。
	- 服务注册中心：eureka-server，端口为8761。
	- 服务提供者：eureka-client，端口为8762，向服务注册中心注册。

* [二、负载均衡客户端（Ribbon）和服务消费者（rest）](../../balancer-ribbon/src/test/java/com/xplus/server/balancer/ribbon/服务消费者（rest+ribbon）.md)
	- Ribbon是一个负载均衡客户端，能很好地控制HTTP和TCP的行为。
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
	- Zuul是Netflix出品的一个基于JVM路由和服务端的负载均衡器，主要功能是路由转发和服务过滤，它默认和Ribbon结合实现了负载均衡的功能，zuul有以下功能：
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

* [六、分布式配置中心(Spring Cloud Config)](../../config-server/src/test/java/com/xplus/server/config/server/分布式配置中心(Spring Cloud Config).md)
	- 配置服务端: config-server，端口8888，提供配置获取服务。
	- 配置客户端: config-client，端口8881，需要从配置服务端获取配置信息。
