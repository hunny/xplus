[TOC]
# SpringBoot配置DataSource

标签（空格分隔）： 未分类

---

## 使用Spring Boot默认数据源

### 引入POM依赖

在pom文件中直接依赖官方提供的`spring-boot-start-jdbc`模块或者`spring-boot-start-data-jpa`模块。
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
```

### 数据库配置

Spring Boot支持.properties格式和.yml格式配置文件，根据个人习惯可以随意选择（笔者推荐yml格式，可读性更强）。在classpath路径下创建application.properties文件或者application.yml文件。两种类型配置分别如下：

- .yml配置文件
```
spring:
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost/test
    username: root
    password: 123456
```
__注意： .yml类型文件，属性名后面冒号和值之间必须有一个空格，如username: root是正确格式, 但是useranme:root格式就是错误的。__
- .properties文件配置
```
spring.datasource.url=jdbc:mysql://localhost/test
spring.datasource.username=root
spring.datasource.password=123456
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
```

## 使用其它第三方数据源
如果不想使用默认的tomcat-jdbc数据源，也可以根据需要选择其它性能优秀的数据源，如Druid、c3p0等等。本文中以Druid为例。

### 引入POM依赖
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.apache.tomcat</groupId>
            <artifactId>tomcat-jdbc</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<!-- 引入Druid依赖 -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>1.0.29</version>
</dependency>
```

### 数据库配置

- .yml配置文件
```
spring:
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost/test
    username: root
    password: 123456
```
- .properties文件配置
```
spring.datasource.url=jdbc:mysql://localhost/test
spring.datasource.username=root
spring.datasource.password=123456
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
```

### 定义数据源
```
@ComponentScan
@Configuration
public class ApplicationConfig {

    @Autowired
    private Environment env;

    @Bean
    public DataSource getDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        return dataSource;
    }   
}
```
或者
```
@ComponentScan
@Configuration
@ConfigurationProperties(prefix="spring.datasource")
public class ApplicationConfig {
    private String url;
    private String username;
    private String password;
    @Bean
    public DataSource getDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);// 用户名
        dataSource.setPassword(password);// 密码
        return dataSource;
    }
    public String getUrl() {
     return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
```
