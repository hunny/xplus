# Angular中的interceptors拦截器

## 一个基本示例

```html
<!DOCTYPE html>
<html ng-app="nickApp">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, width=device-width">
  <title>interceptors</title>
  <script src="http://apps.bdimg.com/libs/angular.js/1.4.6/angular.min.js"></script>
  <script>
    /*
     $http service在Angular中用于简化与后台的交互过程，其本质上使用XMLHttpRequest或JSONP进行与后台的数据交互。
     在与后台的交互过程中，可能会对每条请求发送到Server之前进行预处理（如加入token），或者是在Server返回数据到达客户端还未被处理之前进行预处理（如将非JSON格式数据进行转换）；
     当然还有可能对在请求和响应过程过发生的问题进行捕获处理。所以Angular为我们提供了$http拦截器，用来实现上述需求。*/
    /*
     $httpProvider中有一个 interceptors 数组，而所谓拦截器只是一个简单的注册到该数组中的常规服务工厂。
     1 首先 创建一个拦截器服务工厂
     */
    angular.module('nickApp', [])
        .factory('NickInterceptor', ['$q', function ($q) {
          return {
            // 可选，拦截成功的请求
            /*
             该方法会在$http发送请求到后台之前执行，因此你可以修改配置或做其他的操作。
             该方法接收请求配置对象(request configuration object)作为参数，然后必须返回配置对象或者promise 。
             如果返回无效的配置对象或者 promise 则会被拒绝，导致$http 调用失败
             */
            request: function (config) {
              // 进行预处理
              // 例如加令牌
              config.headers['Authorization'] = 'token666';
              /*
               Request Headers
               token:token666 //加的令牌
               */
              return config || $q.when(config);
            },
            // 可选，拦截成功的响应
            /*
             该方法会在$http接收到从后台过来的响应之后执行，因此你可以修改响应或做其他操作。
             该方法接收响应对象(response object)作为参数，
             然后必须返回响应对象或者promise。响应对象包括了请求配置(request configuration)，头(headers)，状态(status)和从后台过来的数据(data)。
             如果返回无效的响应对象或者 promise 会被拒绝，导致$http调用失败。
             */
            response: function (response) {
              // 进行预处理
              // 例如 JSON.parse(response)等
              return response || $q.when(reponse);
            },
            // 可选，拦截失败的请求
            /*
             有时一个请求发送失败或者被拦截器拒绝了。requestError拦截器会捕获那些被上一个请求拦截器中断的请求。
             它可以用来恢复请求或者有时可以用来撤销请求之前所做的配置，比如关闭遮罩层，显示进度条，激活按钮和输入框之类的。
             */
            requestError: function (rejection) {
              // 对失败的请求进行处理
              // 例如 统一的弹窗提示
              return $q.reject(rejection);
            },
            // 可选，拦截失败的响应
            /*
             有时候我们后台调用失败了。也有可能它被一个请求拦截器拒绝了，或者被上一个响应拦截器中断了。
             在这种情况下，响应异常拦截器可以帮助我们恢复后台调用。
             */
            responseError: function (rejection) {
              // 对失败的响应进行处理
              // 例如 统一的弹窗提示
              return $q.reject(rejection);
            }
          };
        }])
        /*
         $httpProvider中有一个 interceptors 数组，而所谓拦截器只是一个简单的注册到该数组中的常规服务工厂。
         2 在config方法中，将拦截器加入到$httpProvider.interceptors数组中
         */
        .config(['$httpProvider', function ($httpProvider) {
          $httpProvider.interceptors.push('NickInterceptor');
        }])
        .controller('bodyCtl', ['$scope', '$http', function ($scope, $http) {
          $scope.test1 = function () {
            console.log(11);
            $http.get('interceptors.html');
          };
        }])
  </script>
</head>
<body ng-controller="bodyCtl">
<button class="btn" ng-click="test1()">click me</button>
<div ng-view></div>
</body>
</html>
```

## angularjs给Model添加拦截过滤器,路由增加限制，实现用户登录状态判断

使用angularjs的但页面应用时，由于是本地路由在控制页面跳转，但是有的时候我们需要判断用户是否登录来判断用户是否能进入界面。

angularjs是mvc架构所以实现起来很容易也很灵活，我们只MainController里增加一个路由事件侦听并判断，这样就可以避免未登录用户直接输入路由地址来跳转到登录界面地址了

> 代码中的 $rootScope.user是登录后把用户信息放到了全局rootScope上，方便其他地方使用，$rootScope.defaultPage也是默认主页面，初始化的时候写死到rootScope里的。

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

别忘了要注册拦截器到angularjs的config中

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
