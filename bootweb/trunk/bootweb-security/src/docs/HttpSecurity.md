# HttpSecurity

[参考原文](https://springcloud.cc/spring-security-zhcn.html)

## 基础的网站安全java配置

创建我们的java配置。这个配置在你的应用程序中创建一个springSecurityFilterChain 的Servlet的过滤器 springSecurityFilterChain负责所有安全（例如 保护应用程序的URL，验证提交的用户名和密码，重定向到登陆的表单等等）。你可以在下面找到大部分java配置项的例子:

```java

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.inMemoryAuthentication()
				.withUser("user").password("password").roles("USER");
	}
}
```

没有太多的配置，但它确实有很多功能，你可以在下面找到功能摘要:

* 在你的应用程序中对每个URL进行验证
* 为你生成一个登陆表单
* 允许使用用户名 Username user 和密码 Password password 使用验证表单进行验证。
* 允许用户登出
* CSRF attack CSPF攻击防范
* Session Fixation Session保护
* 安全 Header 集成
  - HTTP Strict Transport Security 对安全要求严格的HTTP传输安全
  - X-Content-Type-Options X-Content-Type-Options集成
  - 缓存控制（稍后可以允许你缓存静态资源）
  - X-XSS-Protection X-XSS-Protection集成
  - X-Frame-Options 集成防止点击劫持 Clickjacking
* 和以下 Servlet API 方法集成
  - HttpServletRequest#getRemoteUser()
  - HttpServletRequest.html#getUserPrincipal()
  - HttpServletRequest.html#isUserInRole(java.lang.String)
  - HttpServletRequest.html#login(java.lang.String, java.lang.String)
  - HttpServletRequest.html#logout()

`WebSecurityConfig`只包含了关于如何验证我们的用户的信息。Spring Security怎么知道我们相对所有的用户进行验证？Spring Securityn怎么知道我们需要支持基于表单的验证？原因是`WebSecurityConfigurerAdapter`在c`onfigure(HttpSecurity http)`方法中提供了一个默认的配置，看起来和下面类似:

```java
protected void configure(HttpSecurity http) throws Exception {
	http
		.authorizeRequests()
			.anyRequest().authenticated()
			.and()
		.formLogin()
			.and()
		.httpBasic();
}
```

上面的默认配置:

* 确保我们应用中的所有请求都需要用户被认证
* 允许用户进行基于表单的认证
* 允许用户使用HTTP基于验证进行认证
你可以看到这个配置和下面的XML命名配置相似:

```xml
<http>
	<intercept-url pattern="/**" access="authenticated"/>
	<form-login />
	<http-basic />
</http>
```

java配置使用and()方法相当于XML标签的关闭。 这样允许我们继续配置父类节点。如果你阅读代码很合理，想配置请求验证， 并使用表单和HTTP基本身份验证进行登录。

然而，java配置有不同的默认URL和参数，当你自定义用户登录页是需要牢记这一点。让我们的URLRESTful，另外不要那么明显的观察出你在使用Spring Security，这样帮助我们避免信息泄露。比如：[information leaks](https://www.owasp.org/index.php/Information_Leak_(information_disclosure))。

## Java配置和表单登录

你可能会想知道系统提示您登录表单从哪里来的，因为我们都没有提供任何的HTML或JSP文件。由于Spring Security的默认配置并没有明确设定一个登录页面的URL，Spring Security自动生成一个，基于这个功能被启用，使用默认URL处理登录的提交内容，登录后跳转的URL等等。

自动生成的登录页面可以方便应用的快速启动和运行，大多数应用程序都需要提供自己的登录页面。要做到这一点，我们可以更新我们的配置，如下所示：

```java
protected void configure(HttpSecurity http) throws Exception {
	http
		.authorizeRequests()
			.anyRequest().authenticated()
			.and()
		.formLogin()
			.loginPage("/login") // Q.1
			.permitAll(); // Q.2       
}
```

Q.1. 指定登录页的路径
Q.2. 我们必须允许所有用户访问我们的登录页（例如为验证的用户），这个formLogin().permitAll()方法允许基于表单登录的所有的URL的所有用户的访问。

一个当前配置使用的JSP实现的页面如下：

> 下面这个登陆页是我们的当前配置，如果不符合我们的要求我们可以很容易的更新我们的配置。

```html
<c:url value="/login" var="loginUrl"/>
<form action="${loginUrl}" method="post">  // X.1     
	<c:if test="${param.error != null}">   // X.2     
		<p>
			Invalid username and password.
		</p>
	</c:if>
	<c:if test="${param.logout != null}">  // X.3     
		<p>
			You have been logged out.
		</p>
	</c:if>
	<p>
		<label for="username">Username</label>
		<input type="text" id="username" name="username"/>	// X.4
	</p>
	<p>
		<label for="password">Password</label>
		<input type="password" id="password" name="password"/>	// X.5
	</p>
	<input type="hidden"                        // X.6
		name="${_csrf.parameterName}"
		value="${_csrf.token}"/>
	<button type="submit" class="btn">Log in</button>
</form>
```

X.1. 一个POST请求到/login用来验证用户
X.2. 如果参数有error, 验证尝试失败
X.3. 如果请求蚕食logout存在则登出
X.4. 登录名参数必须被命名为username
X.5. 密码参数必须被命名为password
X.6. CSRF参数，了解更多查阅后续 包括[CSRF令牌](https://springcloud.cc/spring-security-zhcn.html#csrf-include-csrf-token)和[Cross Site Request Forgery (CSRF)](https://springcloud.cc/spring-security-zhcn.html#csrf)相关章节

## 验证请求

我们的例子中要求用户进行身份验证并且在我们应用程序的每个URL这样做。你可以通过给http.authorizeRequests()添加多个子节点来指定多个定制需求到我们的URL。例如：

```java
protected void configure(HttpSecurity http) throws Exception {
	http
		.authorizeRequests()                                                   // P.1             
			.antMatchers("/resources/**", "/signup", "/about").permitAll()     // P.2             
			.antMatchers("/admin/**").hasRole("ADMIN")                         // P.3             
			.antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')") // P.4           
			.anyRequest().authenticated()                                       // P.5            
			.and()
		// ...
		.formLogin();
}
```

P.1. http.authorizeRequests()方法有多个子节点，每个macher按照他们的声明顺序执行。
P.2. 我们指定任何用户都可以通过访问的多个URL模式。任何用户都可以访问URL以"/resources/", equals "/signup", 或者 "/about"开头的URL。
P.3. 以 "/admin/" 开头的URL只能由拥有 "ROLE_ADMIN"角色的用户访问。请注意我们使用 hasRole 方法，没有使用 "ROLE_" 前缀.
P.4. 任何以"/db/" 开头的URL需要用户同时具有 "ROLE_ADMIN" 和 "ROLE_DBA"。和上面一样我们的 hasRole 方法也没有使用 "ROLE_" 前缀.
P.5. 尚未匹配的任何URL要求用户进行身份验证

## 处理登出

当使用WebSecurityConfigurerAdapter, 注销功能会自动应用。默认是访问URL`/logout`将注销登陆的用户：

* 使HTTP Session 无效
* 清楚所有已经配置的 RememberMe 认证
* 清除SecurityContextHolder
* 跳转到 /login?logout

和登录功能类似，你也有不同的选项来定制你的注销功能：

```java
protected void configure(HttpSecurity http) throws Exception {
	http
		.logout()                                      // L.1                          
			.logoutUrl("/my/logout")                   // L.2                              
			.logoutSuccessUrl("/my/index")             // L.3                              
			.logoutSuccessHandler(logoutSuccessHandler)// L.4                              
			.invalidateHttpSession(true)               // L.5                              
			.addLogoutHandler(logoutHandler)           // L.6                              
			.deleteCookies(cookieNamesToClear)         // L.7                              
			.and()
		...
}
```

L.1. 提供注销支持，使用WebSecurityConfigurerAdapter会自动被应用。
L.2. 设置触发注销操作的URL (默认是/logout). 如果CSRF内启用（默认是启用的）的话这个请求的方式被限定为POST。 请查阅相关信息 JavaDoc相关信息.
L.3. 注销之后跳转的URL。默认是/login?logout。具体查看[JavaDoc](http://docs.spring.io/spring-security/site/docs/current/apidocs/org/springframework/security/config/annotation/web/configurers/LogoutConfigurer.html#logoutSuccessUrl(java.lang.String))文档.
L.4. 让你设置定制的 LogoutSuccessHandler。如果指定了这个选项那么logoutSuccessUrl()的设置会被忽略。请查阅[JavaDoc](http://docs.spring.io/spring-security/site/docs/current/apidocs/org/springframework/security/config/annotation/web/configurers/LogoutConfigurer.html#logoutSuccessHandler(org.springframework.security.web.authentication.logout.LogoutSuccessHandler))文档.
L.5. 指定是否在注销时让HttpSession无效。 默认设置为 true。 在内部配置SecurityContextLogoutHandler选项。 请查阅[JavaDoc](http://docs.spring.io/spring-security/site/docs/current/apidocs/org/springframework/security/config/annotation/web/configurers/LogoutConfigurer.html#invalidateHttpSession(boolean))文档.
L.6. 添加一个LogoutHandler.默认SecurityContextLogoutHandler会被添加为最后一个LogoutHandler。
L.7. 允许指定在注销成功时将移除的cookie。这是一个现实的添加一个CookieClearingLogoutHandler的快捷方式。

> 注销也可以通过XML命名空间进行配置，请参阅Spring Security XML命名空间相关文档获取更多细节[logout element](https://springcloud.cc/spring-security-zhcn.html#nsa-logout)。

一般来说，为了定制注销功能，你可以添加[LogoutHandler](http://docs.spring.io/spring-security/site/docs/current/apidocs/org/springframework/security/web/authentication/logout/LogoutHandler.html)和[LogoutSuccessHandler](http://docs.spring.io/spring-security/site/docs/current/apidocs/org/springframework/security/web/authentication/logout/LogoutSuccessHandler.html)的实现。 对于许多常见场景，当使用流食式API时，这些处理器会在幕后进行添加。

## LogoutHandler

一般来说，[LogoutHandler](http://docs.spring.io/spring-security/site/docs/current/apidocs/org/springframework/security/web/authentication/logout/LogoutHandler.html)的实现类可以参阅到注销处理中。他们被用来执行必要的清理，因而他们不应该抛出错误，我们提供您各种实现：

* [PersistentTokenBasedRememberMeServices](http://docs.spring.io/spring-security/site/docs/current/apidocs/org/springframework/security/web/authentication/rememberme/PersistentTokenBasedRememberMeServices.html)
* [TokenBasedRememberMeServices](http://docs.spring.io/spring-security/site/docs/current/apidocs/org/springframework/security/web/authentication/rememberme/TokenBasedRememberMeServices.html)
* [CookieClearingLogoutHandler](http://docs.spring.io/spring-security/site/docs/current/apidocs/org/springframework/security/web/authentication/logout/CookieClearingLogoutHandler.html)
* [CsrfLogoutHandler](http://docs.spring.io/spring-security/site/docs/current/apidocs/org/springframework/security/web/csrf/CsrfLogoutHandler.html)
* [SecurityContextLogoutHandler](http://docs.spring.io/spring-security/site/docs/current/apidocs/org/springframework/security/web/authentication/logout/SecurityContextLogoutHandler.html)
请查看[Remember-Me](https://springcloud.cc/spring-security-zhcn.html#remember-me-impls)的接口和实现 获取详情。

流式API提供了调用相应的`LogoutHandler`实现的快捷方式, 而不是直接提供`LogoutHandler` 的实现。例如：`deleteCookies()` 允许指定注销成功时要删除一个或者多个cookie。这是一个添加 `CookieClearingLogoutHandler`的快捷方式。

## LogoutSuccessHandler

`LogoutSuccessHandler`被`LogoutFilter`在成功注销后调用，用来进行重定向或者转发相应的目的地。注意这个接口与`LogoutHandler`几乎一样，但是可以抛出异常。

下面是提供的一些实现：

* [SimpleUrlLogoutSuccessHandler](http://docs.spring.io/spring-security/site/docs/current/apidocs/org/springframework/security/web/authentication/logout/SimpleUrlLogoutSuccessHandler.html)
* HttpStatusReturningLogoutSuccessHandler

和前面提到的一样，你不需要直接指定`SimpleUrlLogoutSuccessHandler`。而使用流式API通过设置`logoutSuccessUrl()`快捷的进行设置`SimpleUrlLogoutSuccessHandler`。注销成功 后将重定向到设置的URL地址。默认的地址是 `/login?logout`.

在REST API场景中`HttpStatusReturningLogoutSuccessHandler`会进行一些有趣的改变。`LogoutSuccessHandler`允许你设置一个返回给客户端的HTTP状态码（默认返回200）来替换重定向到URL这个动作。

## 进一步的注销相关的参考

* [处理注销](https://springcloud.cc/spring-security-zhcn.html#ns-logout)
* [测试注销](https://springcloud.cc/spring-security-zhcn.html#test-logout)
* [HttpServletRequest.logout()](https://springcloud.cc/spring-security-zhcn.html#servletapi-logout)
* [章节"Remember-Me接口和实现"](https://springcloud.cc/spring-security-zhcn.html#remember-me-impls)
* [注销的CSRF说明](https://springcloud.cc/spring-security-zhcn.html#csrf-logout)
* [点点注销（CAS协议）](https://springcloud.cc/spring-security-zhcn.html#cas-singlelogout)
* [注销的XML命名空间章节](https://springcloud.cc/spring-security-zhcn.html#nsa-logout)

## 验证
到现在为止我们只看了一下基本的验证配置，让我们看看一些稍微高级点的身份验证配置选项。

### 内存中的身份验证
我们已经看到了一个单用户配置到内存验证的示例，下面是配置多个用户的例子：

```java
@Autowired
public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	auth
		.inMemoryAuthentication()
			.withUser("user").password("password").roles("USER").and()
			.withUser("admin").password("password").roles("USER", "ADMIN");
}
```

### JDBC 验证
你可以找一些更新来支持JDBC的验证。下面的例子假设你已经在应用程序中定义好了DataSource， [jdbc-javaconfig](https://github.com/spring-projects/spring-security/tree/master/samples/javaconfig/jdbc) 示例提供了一个完整的基于JDBC的验证。

```java
@Autowired
private DataSource dataSource;

@Autowired
public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	auth
		.jdbcAuthentication()
			.dataSource(dataSource)
			.withDefaultSchema()
			.withUser("user").password("password").roles("USER").and()
			.withUser("admin").password("password").roles("USER", "ADMIN");
}
```

### LDAP 验证
你可以找一些更新来支持LDAP的身份验证， ldap-javaconfig 提供了一个完成的使用基于LDAP的身份验证的示例。

```java
@Autowired
private DataSource dataSource;

@Autowired
public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	auth
		.ldapAuthentication()
			.userDnPatterns("uid={0},ou=people")
			.groupSearchBase("ou=groups");
}
```

上面的例子中使用一下LDIF和前如何Apache DS LDAP示例。

```
dn: ou=groups,dc=springframework,dc=org
objectclass: top
objectclass: organizationalUnit
ou: groups

dn: ou=people,dc=springframework,dc=org
objectclass: top
objectclass: organizationalUnit
ou: people

dn: uid=admin,ou=people,dc=springframework,dc=org
objectclass: top
objectclass: person
objectclass: organizationalPerson
objectclass: inetOrgPerson
cn: Rod Johnson
sn: Johnson
uid: admin
userPassword: password

dn: uid=user,ou=people,dc=springframework,dc=org
objectclass: top
objectclass: person
objectclass: organizationalPerson
objectclass: inetOrgPerson
cn: Dianne Emu
sn: Emu
uid: user
userPassword: password

dn: cn=user,ou=groups,dc=springframework,dc=org
objectclass: top
objectclass: groupOfNames
cn: user
uniqueMember: uid=admin,ou=people,dc=springframework,dc=org
uniqueMember: uid=user,ou=people,dc=springframework,dc=org

dn: cn=admin,ou=groups,dc=springframework,dc=org
objectclass: top
objectclass: groupOfNames
cn: admin
uniqueMember: uid=admin,ou=people,dc=springframework,dc=org
```

### AuthenticationProvider
您可以通过一个自定义的`AuthenticationProvider`为bean定义自定义身份验证。 例如， 下面这个例子假设自定义身份验证`SpringAuthenticationProvider`实现了`AuthenticationProvider`:

> `AuthenticationManagerBuilder`如果还不密集这将仅被使用。

```java
@Bean
public SpringAuthenticationProvider springAuthenticationProvider() {
	return new SpringAuthenticationProvider();
}
```

### UserDetailsService
你可以通过一个自定义的`UserDetailsService`为bean定义自定义身份验证。 例如，下面这个例子假设自定义身份验证`SpringDataUserDetailsService`实现了`UserDetailsService`:

> `AuthenticationManagerBuilder`如果还不密集这将被仅被使用并且没有AuthenticationProviderBean申明。

```java
@Bean
public SpringDataUserDetailsService springDataUserDetailsService() {
	return new SpringDataUserDetailsService();
}
```

你也可以通过让passwordencoder为bean自定义密码如何编码。 例如，如果你使用BCrypt，你可以添加一个bean定义如下图所示：

```java
@Bean
public BCryptPasswordEncoder passwordEncoder() {
	return new BCryptPasswordEncoder();
}
```

## 多个HttpSecurity

我们可以配置多个HttpSecurity实例，就像我们可以有多个<http>块. 关键在于对`WebSecurityConfigurationAdapter`进行多次扩展。例如下面是一个对/api/开头的URL进行的不同的设置。

```java
@EnableWebSecurity
public class MultiHttpSecurityConfig {
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) { // G.1
		auth
			.inMemoryAuthentication()
				.withUser("user").password("password").roles("USER").and()
				.withUser("admin").password("password").roles("USER", "ADMIN");
	}

	@Configuration
	@Order(1)                                                        // G.2
	public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
		protected void configure(HttpSecurity http) throws Exception {
			http
				.antMatcher("/api/**")                               // G.3
				.authorizeRequests()
					.anyRequest().hasRole("ADMIN")
					.and()
				.httpBasic();
		}
	}

	@Configuration                                                   // G.4
	public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
				.authorizeRequests()
					.anyRequest().authenticated()
					.and()
				.formLogin();
		}
	}
}
```

G.1. 配置正常的验证。
G.2. 创建一个WebSecurityConfigurerAdapter，包含一个@Order注解，用来指定个哪一个WebSecurityConfigurerAdapter更优先。
G.3. http.antMatcher指出，这个HttpSecurity只应用到以/api/开头的URL上。
G.4. 创建另外一个WebSecurityConfigurerAdapter实例。用于不以/api/开头的URL，这个配置的顺序在ApiWebSecurityConfigurationAdapter之后，因为他没有指定@Order值为1(没有指定@Order默认会被放到最后).

## 方法安全

从2.0 开始Spring Security对服务层的方法的安全有了实质性的改善。他提供对JSR-250的注解安全支持像框架原生@Secured注解一样好。从3.0开始你也可以使用新的基于表达式的注解[expression-based annotations](https://springcloud.cc/spring-security-zhcn.html#el-access)。你可以应用安全到单独的bean，使用拦截方法元素去修饰Bean声明，或者你可以在整个服务层使用 AspectJ风格的切入点保护多个bean。

### EnableGlobalMethodSecurity

我们可以在任何使用`@Configuration`的实例上，使用`@EnableGlobalMethodSecurity`注解来启用基于注解的安全性。例如下面会启用Spring的`@Secured`注解。

```java
@EnableGlobalMethodSecurity(securedEnabled = true)
public class MethodSecurityConfig {
// ...
}
```

添加一个注解到一个方法（或者一个类或者接口）会限制对相应方法的访问。Spring Security的原生注解支持定义了一套用于该方法的属性。这些将被传递`AccessDecisionManager`到来做实际的决定：

```java
public interface BankService {
  @Secured("IS_AUTHENTICATED_ANONYMOUSLY")
  public Account readAccount(Long id);

  @Secured("IS_AUTHENTICATED_ANONYMOUSLY")
  public Account[] findAccounts();

  @Secured("ROLE_TELLER")
  public Account post(Account account, double amount);
}
```

使用如下代码启用JSR-250注解的支持

```java
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class MethodSecurityConfig {
// ...
}
```

这些都是基于标准的，并允许应有个你简单的基于角色的约束，但是没有Spring Security的本地注解的能力。要使用基于表达书的语法，你可以使用：

```java
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig {
// ...
}
```

和响应Java代码如下：

```java
public interface BankService {
  @PreAuthorize("isAnonymous()")
  public Account readAccount(Long id);

  @PreAuthorize("isAnonymous()")
  public Account[] findAccounts();

  @PreAuthorize("hasAuthority('ROLE_TELLER')")
  public Account post(Account account, double amount);
}
```

### GlobalMethodSecurityConfiguration

有时候你可能需要执行一些比`@EnableGlobalMethodSecurity`注解允许的更复杂的操作。对于这些情况，你可以扩展`GlobalMethodSecurityConfiguration`确保`@EnableGlobalMethodSecurity`注解出现在你的子类。例如如果你想提供一个定制的`MethodSecurityExpressionHandler`，你可以使用下面的配置：

```java
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
	@Override
	protected MethodSecurityExpressionHandler createExpressionHandler() {
		// ... create and return custom MethodSecurityExpressionHandler ...
		return expressionHandler;
	}
}
```

关于可以被重写的方法的更多信息，请参考GlobalMethodSecurityConfiguration的java文档。

### 已配置对象的后续处理

Spring Security的Java配置没有公开每个配置对象的每一个属性，这简化了广大用户的配置。毕竟如果要配置每一个属性，用户可以使用标准的Bean配置。

虽然有一些很好的理由不直接暴露所有属性，用户可能仍然需要更多高级配置，为了解决这个Spring Security引入了`ObjectPostProcessor`概念，用来替换java配置的对象实例。例如：如果你想在`filterSecurityPublishAuthorizationSuccess`里配置`FilterSecurityInterceptor`属性，你可以像下面一样：

```java
@Override
protected void configure(HttpSecurity http) throws Exception {
	http
		.authorizeRequests()
			.anyRequest().authenticated()
			.withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
				public <O extends FilterSecurityInterceptor> O postProcess(
						O fsi) {
					fsi.setPublishAuthorizationSuccess(true);
					return fsi;
				}
			});
}
```

## [安全命名空间配置](https://springcloud.cc/spring-security-zhcn.html#ns-config)

## 小结

简单回顾一下，Spring Security主要由以下几部分组成的:

* `SecurityContextHolder`, 提供几种访问 `SecurityContext`的方式。
* `SecurityContext`, 保存`Authentication`信息和请求对应的安全信息。
* `Authentication`, 展示Spring Security特定的主体。
* `GrantedAuthority`, 反应，在应用程序范围你，赋予主体的权限。
* `UserDetails`,通过你的应用DAO，提供必要的信息，构建`Authentication`对象。
* `UserDetailsService`, `创建一个UserDetails，传递一个` String类型的用户名(或者证书ID或其他).


