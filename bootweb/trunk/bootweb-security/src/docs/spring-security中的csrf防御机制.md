# spring-security中的csrf防御机制

## 什么是csrf？

csrf又称跨域请求伪造，攻击方通过伪造用户请求访问受信任站点。CSRF这种攻击方式在2000年已经被国外的安全人员提出，但在国内，直到06年才开始被关注，08年，国内外的多个大型社区和交互网站分别爆出CSRF漏洞，如：NYTimes.com（纽约时报）、Metafilter（一个大型的BLOG网站），YouTube和百度HI......而现在，互联网上的许多站点仍对此毫无防备，以至于安全业界称CSRF为“沉睡的巨人”。
举个例子，用户通过表单发送请求到银行网站，银行网站获取请求参数后对用户账户做出更改。在用户没有退出银行网站情况下，访问了攻击网站，攻击网站中有一段跨域访问的代码，可能自动触发也可能点击提交按钮，访问的url正是银行网站接受表单的url。因为都来自于用户的浏览器端，银行将请求看作是用户发起的，所以对请求进行了处理，造成的结果就是用户的银行账户被攻击网站修改。
解决方法基本上都是增加攻击网站无法获取到的一些表单信息，比如增加图片验证码，可以杜绝csrf攻击，但是除了登陆注册之外，其他的地方都不适合放验证码，因为降低了网站易用性

* 相关介绍：
 -[http://baike.baidu.com/view/1609487.htm?fr=aladdin](http://baike.baidu.com/view/1609487.htm?fr=aladdin)
 -[http://www.cnblogs.com/hyddd/archive/2009/04/09/1432744.html](http://www.cnblogs.com/hyddd/archive/2009/04/09/1432744.html)
 
## spring-security中csrf防御原理

在web应用中增加前置过滤器，对需要验证的请求验证是否包含csrf的token信息，如果不包含，则报错。这样攻击网站无法获取到token信息，则跨域提交的信息都无法通过过滤器的校验。
看一下CsrfFilter的源码就很好理解了

```java
// 先从tokenRepository中加载token  
   CsrfToken csrfToken = tokenRepository.loadToken(request);  
   final boolean missingToken = csrfToken == null;  
   // 如果为空，则tokenRepository生成新的token，并保存到tokenRepository中  
   if(missingToken) {  
       CsrfToken generatedToken = tokenRepository.generateToken(request);  
       // 默认的SaveOnAccessCsrfToken方法，记录tokenRepository，  
       // tokenRepository，response，获取token时先将token同步保存到tokenRepository中  
       csrfToken = new SaveOnAccessCsrfToken(tokenRepository, request, response, generatedToken);  
   }  
   // 将token写入request的attribute中，方便页面上使用  
   request.setAttribute(CsrfToken.class.getName(), csrfToken);  
   request.setAttribute(csrfToken.getParameterName(), csrfToken);  
  
   // 如果不需要csrf验证的请求，则直接下传请求（requireCsrfProtectionMatcher是默认的对象，对符合^(GET|HEAD|TRACE|OPTIONS)$的请求不验证）  
   if(!requireCsrfProtectionMatcher.matches(request)) {  
       filterChain.doFilter(request, response);  
       return;  
   }  
  
   // 从用户请求中获取token信息  
   String actualToken = request.getHeader(csrfToken.getHeaderName());  
   if(actualToken == null) {  
       actualToken = request.getParameter(csrfToken.getParameterName());  
   }  
   // 验证，如果相同，则下传请求，如果不同，则抛出异常  
   if(!csrfToken.getToken().equals(actualToken)) {  
       if(logger.isDebugEnabled()) {  
           logger.debug("Invalid CSRF token found for " + UrlUtils.buildFullRequestUrl(request));  
       }  
       if(missingToken) {  
           accessDeniedHandler.handle(request, response, new MissingCsrfTokenException(actualToken));  
       } else {  
           accessDeniedHandler.handle(request, response, new InvalidCsrfTokenException(csrfToken, actualToken));  
       }  
       return;  
   }  
  
   filterChain.doFilter(request, response); 
```

## 使用样例

在web.xml中增加spring的过滤器代理
在spring的配置文件中增加过滤器

```xml
<bean id="csrfFilter" class="org.springframework.security.web.csrf.CsrfFilter">  
    <constructor-arg>  
  <!-- HttpSessionCsrfTokenRepository是把token放到session中来存取  -->
        <bean class="org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository"/>  
    </constructor-arg>  
</bean>  
<!-- 
    如果用的是spring mvc 的form标签，则配置此项时自动将crsf的token放入到一个hidden的input中，而不需要开发人员显式的写入form 
-->  
<bean id="requestDataValueProcessor" class="org.springframework.security.web.servlet.support.csrf.CsrfRequestDataValueProcessor"/> 
```

如果配置了CsrfRequestDataValueProcessor，并且使用了spring的form标签来写表单代码，则这样就可以了。否则需要在页面上书写相关代码
首先获取token

```html
<meta name="_csrf" content="${_csrf.token}"/>  
<meta name="_csrf_header" content="${_csrf.headerName}"/>  
```
然后在发送请求之前将token放入header中（或者form表单中）

```javascript
var token = $("meta[name='_csrf']").attr("content");  
var header = $("meta[name='_csrf_header']").attr("content");  
$(document).ajaxSend(function(e, xhr, options) {  
    xhr.setRequestHeader(header, token);  
});  
```
