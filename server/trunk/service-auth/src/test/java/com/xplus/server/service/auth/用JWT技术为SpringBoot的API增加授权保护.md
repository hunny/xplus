# 用JWT技术为SpringBoot的API增加授权保护

## 开发一个简单的API

spring提供了一个[网页](http://start.spring.io)可以便捷的生成springboot程序。选择工具、版本，点击Generate Project按钮后，下载文件到本地。

## 创建示例REST

```
package com.xplus.server.service.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @RequestMapping(value = {
      "/hi", "hello" })
  public String hello() {
    return "Hello World!";
  }

}

```

通过http://localhost:8080/hi或http://localhost:8080/hello访问

## 添加pom.xml文件依赖

```
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## 添加示例的请求

我们引入一些新的方法并返回一些简单的结果，后面我们将对这些内容进行访问控制，这里用到了上面的结果集处理类。这里多放两个方法，后面我们来测试权限和角色的验证用。

```

  @RequestMapping(value = {
      "/hi", "hello" }, //
      produces = "application/json;charset=UTF-8")
  public String hello() {
    return "Hello!";
  }

  @RequestMapping(value = {
      "/world", "/wd" }, //
      produces = "application/json;charset=UTF-8")
  public String world() {
    return "World";
  }

  // 路由映射到/users
  @RequestMapping(value = "/users", //
      produces = "application/json;charset=UTF-8")
  public String users() {
    List<String> users = new ArrayList<String>() {
      private static final long serialVersionUID = 9047104758343011245L;
      {
        add("Jack");
        add("Tom");
        add("Jerry");
      }
    };
    return StringUtils.join(users, ',');
  }
```

## 使用JWT保护你的Spring Boot应用

开始介绍正题，这里我们会对/users进行访问控制，先通过申请一个JWT(JSON Web Token读jot)，然后通过这个访问/users，才能拿到数据。

- 关于JWT，参见以下内容，这些不在本文讨论范围内:
  + [RFC7519](https://tools.ietf.org/html/rfc7519)
  + [JWT](https://jwt.io/)

JWT很大程度上还是个新技术，通过使用HMAC(Hash-based Message Authentication Code)计算信息摘要，也可以用RSA公私钥中的私钥进行签名。这个根据业务场景进行选择。

## 添加Spring Security

根据上文我们说过我们要对/users进行访问控制，让用户在/login进行登录并获得Token。这里我们需要将spring-boot-starter-security加入pom.xml。加入后，我们的Spring Boot项目将需要提供身份验证，相关的pom.xml如下:

```
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-security</artifactId>
    </dependency>

    <dependency>
      <groupId>io.jsonwebtoken</groupId>
      <artifactId>jjwt</artifactId>
      <version>0.9.0</version>
    </dependency>
```

至此我们之前所有的路由都需要身份验证。我们将引入一个安全设置类WebSecurityConfig，这个类需要从WebSecurityConfigurerAdapter类继承。

```
@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // 设置 HTTP 验证规则
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 关闭csrf验证
        http.csrf().disable()
                // 对请求进行认证
                .authorizeRequests()
                // 所有 / 的所有请求 都放行
                .antMatchers("/").permitAll()
                // 所有 /login 的POST请求 都放行
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                // 权限检查
                .antMatchers("/hello").hasAuthority("AUTH_WRITE")
                // 角色检查
                .antMatchers("/world").hasRole("ADMIN")
                // 所有请求需要身份认证
                .anyRequest().authenticated()
            .and()
                // 添加一个过滤器 所有访问 /login 的请求交给 JWTLoginFilter 来处理 这个类处理所有的JWT相关内容
                .addFilterBefore(new JWTLoginFilter("/login", authenticationManager()),
                        UsernamePasswordAuthenticationFilter.class)
                // 添加一个过滤器验证其他请求的Token是否合法
                .addFilterBefore(new JWTAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 使用自定义身份验证组件
        auth.authenticationProvider(new CustomAuthenticationProvider());

    }
}
```

新建两个基本类，一个负责存储用户名密码，另一个是一个权限类型，负责存储权限和角色。

```
public class UserCredentials implements Serializable {

  private static final long serialVersionUID = -928094736920779643L;

  private String username;
  
  private String password;
  
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

```
public class GrantedAuthorityImpl implements GrantedAuthority {

  private static final long serialVersionUID = -3642792395006458943L;

  private String authority;

  public GrantedAuthorityImpl(String authority) {
    this.authority = authority;
  }

  public void setAuthority(String authority) {
    this.authority = authority;
  }

  @Override
  public String getAuthority() {
    return this.authority;
  }

}
```

在上面的安全设置类中，我们设置所有人都能访问/和POST方式访问/login，其他的任何路由都需要进行认证。然后将所有访问/login的请求，都交给JWTLoginFilter过滤器来处理。稍后我们会创建这个过滤器和其他这里需要的JWTAuthenticationFilter和CustomAuthenticationProvider两个类。

建立一个JWT生成，和验签的类

```
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenAuthenticationUtils {

  private static final long EXPIRATIONTIME = 432_000_000; // 5天
  private static final String SECRET = "P@ssw02d"; // JWT密码
  private static final String TOKEN_PREFIX = "Bearer"; // Token前缀
  private static final String HEADER_STRING = "Authorization";// 存放Token的Header
                                                              // Key

  // JWT生成方法
  public static void addAuthentication(HttpServletResponse response, String username) {

    // 生成JWT
    String JWT = Jwts.builder()
        // 保存权限（角色）
        .claim("authorities", "ROLE_ADMIN,AUTH_WRITE")
        // 用户名写入标题
        .setSubject(username)
        // 有效期设置
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
        // 签名设置
        .signWith(SignatureAlgorithm.HS512, SECRET).compact();

    // 将 JWT 写入 body
    try {
      response.setContentType("application/json");
      response.setStatus(HttpServletResponse.SC_OK);
      response.getOutputStream().println(JWT);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // JWT验证方法
  public static Authentication getAuthentication(HttpServletRequest request) {
    // 从Header中拿到token
    String token = request.getHeader(HEADER_STRING);

    if (token != null) {
      // 解析 Token
      Claims claims = Jwts.parser()
          // 验签
          .setSigningKey(SECRET)
          // 去掉 Bearer
          .parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody();

      // 拿用户名
      String user = claims.getSubject();

      // 得到 权限（角色）
      List<GrantedAuthority> authorities = AuthorityUtils
          .commaSeparatedStringToAuthorityList((String) claims.get("authorities"));

      // 返回验证令牌
      return user != null ? new UsernamePasswordAuthenticationToken(user, null, authorities) : null;
    }
    return null;
  }
}
```

这个类就两个static方法，一个负责生成JWT，一个负责认证JWT最后生成验证令牌。

## 自定义验证组件

这个类就是提供密码验证功能，在实际使用时换成自己相应的验证逻辑，从数据库中取出、比对、赋予用户相应权限。

```
//自定义身份认证验证组件
public class CustomAuthenticationProvider implements AuthenticationProvider {

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    // 获取认证的用户名 & 密码
    String name = authentication.getName();
    String password = authentication.getCredentials().toString();

    // 认证逻辑
    if (name.equals("admin") && password.equals("123456")) {

      // 这里设置权限和角色
      ArrayList<GrantedAuthority> authorities = new ArrayList<>();
      authorities.add(new GrantedAuthorityImpl("ROLE_ADMIN"));
      authorities.add(new GrantedAuthorityImpl("AUTH_WRITE"));
      // 生成令牌
      Authentication auth = new UsernamePasswordAuthenticationToken(name, password, authorities);
      return auth;
    } else {
      throw new BadCredentialsException("密码错误~");
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }

}
```

- 实现JWTLoginFilter 这个Filter比较简单，除了构造函数需要重写三个方法。
  + attemptAuthentication - 登录时需要验证时候调用。
  + successfulAuthentication - 验证成功后调用。
  + unsuccessfulAuthentication - 验证失败后调用，返回403。

```
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xplus.server.service.auth.jwt.TokenAuthenticationUtils;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

  public JWTLoginFilter(String url, AuthenticationManager authManager) {
    super(new AntPathRequestMatcher(url));
    setAuthenticationManager(authManager);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) //
      throws AuthenticationException, IOException, ServletException {
    // JSON反序列化成 AccountCredentials
    UserCredentials creds = new ObjectMapper().readValue(request.getInputStream(),
        UserCredentials.class);

    // 返回一个验证令牌
    return getAuthenticationManager().authenticate(
        new UsernamePasswordAuthenticationToken(creds.getUsername(), creds.getPassword()));
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res,
      FilterChain chain, Authentication auth) throws IOException, ServletException {
    TokenAuthenticationUtils.addAuthentication(res, auth.getName());
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, AuthenticationException failed)
      throws IOException, ServletException {

    response.setContentType("application/json");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.getOutputStream().print("Authentication Failed.");
    ;
  }

}
```

再完成最后一个类JWTAuthenticationFilter，这也是个拦截器，它拦截所有需要JWT的请求，然后调用TokenAuthenticationService类的静态方法去做JWT验证。

```
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.xplus.server.service.auth.jwt.TokenAuthenticationUtils;

public class JWTAuthenticationFilter extends GenericFilterBean {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {
    Authentication authentication = TokenAuthenticationUtils
        .getAuthentication((HttpServletRequest) request);

    SecurityContextHolder.getContext().setAuthentication(authentication);
    filterChain.doFilter(request, response);
  }

}
```

## 回顾流程

整个Spring Security结合JWT基本就差不多了，下面我们来测试下，并说下整体流程。

- 开始测试，先运行整个项目，这里介绍下过程：
  + 先程序启动 - main函数
  + 注册验证组件 - WebSecurityConfig 类 configure(AuthenticationManagerBuilder auth)方法，这里我们注册了自定义验证组件
  + 设置验证规则 - WebSecurityConfig 类 configure(HttpSecurity http)方法，这里设置了各种路由访问规则
  + 初始化过滤组件 - JWTLoginFilter 和 JWTAuthenticationFilter 类会初始化

## 验证测试

* 首先测试获取Token：
	- POST访问http://localhost:8080/login，并传入{"username":"admin", "password":"123456"}
	- 使用curl请求如下：
	```
	curl -H "Content-Type: application/json" -X POST -d '{"username":"admin","password":"123456"}' http://127.0.0.1:8080/login
	```
	```
	POST http://localhost:8081/login
	POST data:
	{"username":"admin", "password":"123456"}
	[no cookies]
	Request Headers:
	Connection: keep-alive
	Content-Type: application/json
	Accept: application/json
	Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJhdXRob3JpdGllcyI6IlJPTEVfQURNSU4sQVVUSF9XUklURSIsInN1YiI6ImFkbWluIiwiZXhwIjoxNTA5MTk4NDYzfQ.Ix2fPXeUQu5SMP-0CC5sWZLKvFsRfTaa8dPkNt24UCPfgxOBex1-oIcaKW4Ad1_lQooHQt9a-609_pyws93b6A
	Content-Length: 41
	Host: localhost:8081
	User-Agent: Apache-HttpClient/4.5.3 (Java/1.8.0_92)
	```
- 整个过程如下：
  + 拿到传入JSON，解析用户名密码 - JWTLoginFilter 类 attemptAuthentication 方法
  + 自定义身份认证验证组件，进行身份认证 - CustomAuthenticationProvider 类 authenticate 方法
  + 验证成功 - JWTLoginFilter 类 successfulAuthentication 方法
  + 生成JWT - TokenAuthenticationUtils 类 addAuthentication方法

* 测试一个访问资源的：

	```
	curl -H "Content-Type: application/json" -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJhdXRob3JpdGllcyI6IlJPTEVfQURNSU4sQVVUSF9XUklURSIsInN1YiI6ImFkbWluIiwiZXhwIjoxNDkzNzgyMjQwfQ.HNfV1CU2CdAnBTH682C5-KOfr2P71xr9PYLaLpDVhOw8KWWSJ0lBo0BCq4LoNwsK_Y3-W3avgbJb0jW9FNYDRQ" http://127.0.0.1:8080/users
	```

	```
	GET http://localhost:8081/users
	GET data:
	[no cookies]
	Request Headers:
	Connection: keep-alive
	Content-Type: application/json
	Accept: application/json
	Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJhdXRob3JpdGllcyI6IlJPTEVfQURNSU4sQVVUSF9XUklURSIsInN1YiI6ImFkbWluIiwiZXhwIjoxNTA5MTk4NDYzfQ.Ix2fPXeUQu5SMP-0CC5sWZLKvFsRfTaa8dPkNt24UCPfgxOBex1-oIcaKW4Ad1_lQooHQt9a-609_pyws93b6A
	Content-Length: 0
	Host: localhost:8081
	User-Agent: Apache-HttpClient/4.5.3 (Java/1.8.0_92)
	```

说明我们的Token生效可以正常访问。
- 处理流程：
  + 接到请求进行拦截 - JWTAuthenticationFilter 中的方法
  + 验证JWT - TokenAuthenticationService 类 getAuthentication 方法
  + 访问Controller

## 总结
本文主要介绍了，如何用Spring Security结合JWT保护你的Spring Boot应用。如何使用Role和Authority，其实在Spring Security中，对于GrantedAuthority接口实现类来说是不区分是Role还是Authority，二者区别就是如果是hasAuthority判断，就是判断整个字符串，判断hasRole时，系统自动加上ROLE_到判断的Role字符串上，也就是说hasRole("CREATE")和hasAuthority('ROLE_CREATE')是相同的。利用这些可以搭建完整的RBAC体系。