# 详解AngularJs HTTP响应拦截器实现登陆、权限校验

$httpAngularJS 的 $http 服务允许我们通过发送 HTTP 请求方式与后台进行通信。在某些情况下，我们希望可以俘获所有的请求，并且在将其发送到服务端之前进行操作。还有一些情况是，我们希望俘获响应，并且在完成完成调用之前处理它。一个很好例子就是处理全局 http 异常。拦截器(Interceptors)应运而生。

## 什么是拦截器？

$httpProvider 中有一个 interceptors 数组，而所谓拦截器只是一个简单的注册到了该数组中的常规服务工厂。下面的例子告诉你怎么创建一个拦截器:

```javascript
module.factory('myInterceptor', ['$log', function($log) {
  $log.debug('$log is here to show you that this is a regular factory with injection');
  var myInterceptor = {
    // ....
    // ....
    // ....
  };
 
  return myInterceptor;
}]);
```

然后通过它的名字添加到 $httpProvider.interceptors 数组:

```javascript
module.config(['$httpProvider', function($httpProvider) {
  $httpProvider.interceptors.push('myInterceptor');
}]);
```

* 拦截器允许你:
  - 通过实现 request 方法拦截请求: 该方法会在 $http 发送请求道后台之前执行，因此你可以修改配置或做其他的操作。该方法接收请求配置对象(request configuration object)作为参数，然后必须返回配置对象或者 promise 。如果返回无效的配置对象或者 promise 则会被拒绝，导致 $http 调用失败。
  - 通过实现 response 方法拦截响应: 该方法会在 $http 接收到从后台过来的响应之后执行，因此你可以修改响应或做其他操作。该方法接收响应对象(response object)作为参数，然后必须返回响应对象或者 promise。响应对象包括了请求配置(request configuration)，头(headers)，状态(status)和从后台过来的数据(data)。如果返回无效的响应对象或者 promise 会被拒绝，导致 $http 调用失败。
  - 通过实现 requestError 方法拦截请求异常: 有时候一个请求发送失败或者被拦截器拒绝了。请求异常拦截器会俘获那些被上一个请求拦截器中断的请求。它可以用来恢复请求或者有时可以用来撤销请求之前所做的配置，比如说关闭进度条，激活按钮和输入框什么之类的。
  - 通过实现 responseError 方法拦截响应异常: 有时候我们后台调用失败了。也有可能它被一个请求拦截器拒绝了，或者被上一个响应拦截器中断了。在这种情况下，响应异常拦截器可以帮助我们恢复后台调用。

angularJs提供四种拦截器，其中两种成功拦截器（request、response），两种失败拦截器（requestError、responseError）。

```javascript
angular.module("myApp", [])
  .factory('httpInterceptor', [ '$q', '$injector',function($q, $injector) {
    var httpInterceptor = {
      'responseError' : function(response) {
        // ......
        return $q.reject(response);
      },
      'response' : function(response) {
        // ......
        return response;
      },
      'request' : function(config) {
        // ......
        return config;
      },
      'requestError' : function(config){
        // ......
        return $q.reject(config);
      }
    }
    return httpInterceptor;
  }
```

因此，我们可以通过拦截器来判断用于的登陆与权限问题。
代码中的 $rootScope.user是登录后把用户信息放到了全局rootScope上，方便其他地方使用，$rootScope.defaultPage也是默认主页面，初始化的时候写死到rootScope里的。

```javascript
$rootScope.$on('$stateChangeStart',function(event, toState, toParams, fromState, fromParams){
 if(toState.name=='login')return;// 如果是进入登录界面则允许
 // 如果用户不存在
 if(!$rootScope.user || !$rootScope.user.token){
 event.preventDefault();// 取消默认跳转行为
 $state.go("login",{from:fromState.name,w:'notLogin'});//跳转到登录界面
 }
});
```

另外还有用户已经登录，但是登录超时了，还有就是增加后台接口的判断来增强安全性。不能完全依靠本地逻辑
我们在model里面增加一个用户拦截器,在rensponseError中判断错误码，抛出事件让Contoller或view来处理

```javascript
app.factory('UserInterceptor', ["$q","$rootScope",function ($q,$rootScope) {
 return {
    request:function(config){
      config.headers["TOKEN"] = $rootScope.user.token;
      return config;
    },
    responseError: function (response) {
      var data = response.data;
  // 判断错误码，如果是未登录
      if(data["errorCode"] == "500999"){
  // 清空用户本地token存储的信息，如果
        $rootScope.user = {token:""};
  // 全局事件，方便其他view获取该事件，并给以相应的提示或处理
        $rootScope.$emit("userIntercepted","notLogin",response);
      }
  // 如果是登录超时
  if(data["errorCode"] == "500998"){
        $rootScope.$emit("userIntercepted","sessionOut",response);
      }
      return $q.reject(response);
    }
  };
}]);
```

别忘了要注册拦截器到angularjs的config中哦

```javascript
app.config(function ($httpProvider) {
  $httpProvider.interceptors.push('UserInterceptor');
});
```

最后在controller中处理错误事件

```javascript
$rootScope.$on('userIntercepted',function(errorType){
  // 跳转到登录界面，这里我记录了一个from，这样可以在登录后自动跳转到未登录之前的那个界面
  $state.go("login",{from:$state.current.name,w:errorType});
});
```

最后还可以在loginController中做更多的细节处理

```javascript
// 如果用户已经登录了，则立即跳转到一个默认主页上去，无需再登录
if($rootScope.user.token){
  $state.go($rootScope.defaultPage);
  return;
}
```

另外在登录成功回调后还可以跳转到上一次界面，也就是上面记录的from

```javascript
var from = $stateParams["from"];
$state.go(from && from != "login" ? from : $rootScope.defaultPage);
```