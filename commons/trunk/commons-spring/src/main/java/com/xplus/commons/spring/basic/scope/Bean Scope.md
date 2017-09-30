#Bean的Scope

- Scope 描述的是Spring容器如何新建Bean的实例的。Spring的Scope有以下几种，通过@Scope注解来实现。
	+ Singleton: 一个Spring容器中只有一个Bean的实例，此为Spring的默认配置，全容器共享一个实例。
	+ Prototype: 每次调用新建一个Bean的实例。
	+ Request: Web项目中，给每一个http request新建一个Bean实例。
	+ Session: Web项目中，给每一个http session新建一个Bean实例。
	+ Global Session: 这个只在protal应用中有用，给每一个global http session新建一个Bean实例。
	+ StepScope: 在Spring Batch中存在。