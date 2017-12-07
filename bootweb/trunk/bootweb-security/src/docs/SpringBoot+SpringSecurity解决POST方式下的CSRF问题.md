# Spring Boot + Spring Security解决POST方式下的CSRF问题

问题现象：

```
HTTP Status 403－Invalid CSRF Token 'null' was found on the request parameter '_csrf' or header 'X-CSRF-TOKEN'.
```

## 1.定义headers，post方式提交的时候带上headers的信息。

```javascript
var headers = {};
headers['X-CSRF-TOKEN'] = "[[${_csrf.token}]]";
$.ajax({
    url: url,
    type: "POST",
    headers: headers,
    dataType: "json",
    success: function(result) {
    }
});
```

## 2.直接作为参数提交。

```javascript
$.ajax({
    url: url,
    data: {
        "[[${_csrf.parameterName}]]": "[[${_csrf.token}]]"
        },
    type: "POST",
    dataType: "json",
    success: function(result) {
    }
});
```

## 3.form提交的时候，如果出现csrf问题，可将该参数作为隐藏项。跟第二种方式类似。

```html
<input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}">
```

## 4.在`@EnableWebSecurity`配置中，禁用CSRF。

```java
http.csrf().disable();
```