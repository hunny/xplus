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


