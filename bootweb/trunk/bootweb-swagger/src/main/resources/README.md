* Add maven dependency

```
<dependency>
  <groupId>com.example.bootweb.swagger</groupId>
  <artifactId>spring-boot-starter-swagger</artifactId>
  <version>0.0.1-SHAPSHOT</version>
</dependency>
```

* Add swagger api annotation

```
@EnableSwagger2Api
```

* For example: 

```
@SpringBootApplication
@EnableSwagger2Api
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
```

* Enjoy it

```
http://localhost:8080/swagger-ui.html
```
