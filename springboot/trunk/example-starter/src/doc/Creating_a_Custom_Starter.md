# Creating a Custom Starter

##  Overview
The core Spring Boot developers provide starters for most of the popular open source projects, but we are not limited to these.

We can also write our own custom starters. If we have an internal library for use within our organization, it would be a good practice to also write a starter for it if it’s going to be used in Spring Boot context.

These starters enable developers to avoid lengthy configuration and quickly jumpstart their development. However, with a lot of things happening in the background, it sometimes becomes difficult to understand how an annotation or just including a dependency in the pom.xml enables so many features.

In this article, we’ll demystify the Spring Boot magic to see what’s going on behind the scenes. Then we will use these concepts to create a starter for our own custom library.

## Demystifying Spring Boot’s Autoconfiguration

### Auto Configuration Classes

When Spring Boot starts up, it looks for a file named spring.factories in the classpath. This file is located in the META-INF directory. Let’s look at a snippet of this file from the [spring-boot-autoconfigure](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-autoconfigure/src/main/resources/META-INF/spring.factories) project:

```
# Auto Configure
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration,\
org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration,\
org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration,\
org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
```

This file maps a name to different configuration classes which Spring Boot will try to run. So, as per this snippet, Spring Boot will try to run all the configuration classes for RabbitMQ, Cassandra, MongoDB and Hibernate.

Whether or not these classes will actually run will depend on the presence of dependent classes on the classpath. For example, if the classes for MongoDB are found on the classpath, MongoAutoConfiguration will run and all the mongo related beans will be initialized.

This conditional initialization is enabled by the @ConditionalOnClass annotation. Let’s look at the code snippet from MongoAutoConfiguration class to see its usage:

```java
@Configuration
@ConditionalOnClass(MongoClient.class)
@EnableConfigurationProperties(MongoProperties.class)
@ConditionalOnMissingBean(type = "org.springframework.data.mongodb.MongoDbFactory")
public class MongoAutoConfiguration {
    // configuration code
}
```

Now how – if the MongoClient is available in the classpath – this configuration class will run populating the Spring bean factory with a MongoClient initialized with default config settings.

### Custom Properties from the `application.properties` File

Spring Boot initializes the beans using some pre-configured defaults. To override those defaults, we generally declare them in the application.properties file with some specific name. These properties are automatically picked up by the Spring Boot container.

Let’s see how that works.

In the code snippet for MongoAutoConfiguration, `@EnableConfigurationProperties` annotation is declared with the [MongoProperties](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/mongo/MongoProperties.java) class which acts as the container for custom properties:

```java
@ConfigurationProperties(prefix = "spring.data.mongodb")
public class MongoProperties {
 
    private String host;
 
    // other fields with standard getters and setters
}
```

The prefix plus the field name make the names of the properties in the application.properties file. So, to set the host for MongoDB, we only need to write the following in the property file:

```
spring.data.mongodb.host = localhost
```
Similarly, values for other fields in the class can be set using the property file.

## Creating a Custom Starter

* Based on the concepts in section 2, to create a custom starter we need to write the following components:
  - An auto-configure class for our library along with a properties class for custom configuration.
  - A starter pom to bring in the dependencies of the library and the autoconfigure project.

For demonstration, we have created a simple greeting library that will take in a greeting message for different times of day as configuration parameters and output the greeting message. We will also create a sample Spring Boot application to demonstrate the usage of our autoconfigure and starter modules.
  
### The Autoconfigure Module

We’ll call our auto configure module `greeter-spring-boot-autoconfigure`. This module will have two main classes i.e. `GreeterProperties` which will enable setting custom properties through `application.properties` file and `GreeterAutoConfiguartion` which will create the beans for greeter library.

Let’s look at the code for both the classes:

```java
@ConfigurationProperties(prefix = "example.starter.greeter")
public class GreeterProperties {
 
    private String userName;
    private String morningMessage;
    private String afternoonMessage;
    private String eveningMessage;
    private String nightMessage;
 
    // standard getters and setters
 
}
```

```java
@Configuration
@ConditionalOnClass(GreeterServer.class)
@EnableConfigurationProperties(GreeterProperties.class)
public class GreeterAutoConfiguration {
 
    @Autowired
    private GreeterProperties greeterProperties;
 
    @Bean
    @ConditionalOnMissingBean
    public GreetingConfig greeterConfig() {
 
        String userName = greeterProperties.getUserName() == null
          ? System.getProperty("user.name") 
          : greeterProperties.getUserName();
         
        // ..
 
        GreetingConfig greetingConfig = new GreetingConfig();
        greetingConfig.put(USER_NAME, userName);
        // ...
        return greetingConfig;
    }
 
    @Bean
    @ConditionalOnMissingBean
    public Greeter greeter(GreetingConfig greetingConfig) {
        return new Greeter(greetingConfig);
    }
}
```

We also need to add a spring.factories file in the src/main/resources/META-INF directory with the following content:

```
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
com.example.springboot.autoconfiguration.GreeterAutoConfiguration
```

On application startup, the GreeterAutoConfiguration class will run if the class Greeter is present in the classpath. If run successfully, it will populate the Spring application context with GreeterConfig and Greeter beans by reading the properties via GreeterProperties class.

The [`@ConditionalOnMissingBean`](http://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/autoconfigure/condition/ConditionalOnMissingBean.html) annotation will ensure that these beans will only be created if they don’t already exist. This enables developers to completely override the auto-configured beans by defining their own in one of the `@Configuration` classes.

### Creating `pom.xml`

Now let’s create the starter pom which will bring in the dependencies for the auto-configure module and the greeter library.

As per the naming convention, all the starters which are not managed by the core Spring Boot team should start with the library name followed by the suffix -spring-boot-starter. So we will call our starter as greeter-spring-boot-starter:

```xml
<project ...>
    <modelVersion>4.0.0</modelVersion>
 
    <groupId>com.example.springboot</groupId>
    <artifactId>starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
 
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <greeter.version>0.0.1-SNAPSHOT</greeter.version>
        <spring-boot.version>1.5.2.RELEASE</spring-boot.version>
    </properties>
 
    <dependencies>
 
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
            <version>${spring-boot.version}</version>
        </dependency>
 
        <dependency>
            <groupId>com.example.springboot</groupId>
            <artifactId>autoconfigure</artifactId>
            <version>${project.version}</version>
        </dependency>
 
        <dependency>
            <groupId>com.example.springboot</groupId>
            <artifactId>greeter</artifactId>
            <version>${greeter.version}</version>
        </dependency>
 
    </dependencies>
 
</project>
```

### Using the Starter

Let’s create greeter-spring-boot-sample-app which will use the starter. In the pom.xml we need to add it as a dependency:

```xml
<dependency>
    <groupId>com.example.springboot</groupId>
    <artifactId>starter</artifactId>
    <version>${greeter-starter.version}</version>
</dependency>
```

Spring Boot will automatically configure everything and we will have a Greeter bean ready to be injected and used.

Let’s also change some of the default values of the `GreeterProperties` by defining them in the `application.properties` file with the `example.starter.greeter` prefix:

```
example.starter.greeter.userName=World
example.starter.greeter.afternoonMessage=Woha\ Afternoon
```

Finally, let’s use the Greeter bean in our application:

```java
@SpringBootApplication
public class Application implements CommandLineRunner {
 
    @Autowired
    private GreeterService greeter;
 
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
 
    @Override
    public void run(String... args) throws Exception {
        String message = greeter.greet();
        System.out.println(message);
    }
}
```

## Conclusion
In this quick tutorial, we focused on rolling out a custom Spring Boot starter, and on how these starters, together with the autoconfigure mechanism – work in the background to eliminate a lot of manual configuration.