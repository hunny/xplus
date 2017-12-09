# AngularJS中change事件

## AngularJS中locationchange、routechange事件

假设有这样一场景：在用户切换到另外一个route时，需要检查当前表单（内容）是否已保存？考虑下在AngularJS中如何实现？

我们首先来分析下，场景中有那些事件流。
①：用户编辑了表单
②：用户准备点击另外一个route url，触发提示‘是否要保存’
③：点击‘是’，route切换事件停止；点击‘否’，route切换事件继续

主要流程就这些其实很简单。这里主要涉及到了AngularJS中的事件处理模块。

首先我们需要知道的是$locationChangeStart和$routeChangeStart哪个事件先触发。

```
$route is used for deep-linking URLs to controllers and views (HTML partials). It watches $location.url() and tries to map the path to an existing route definition.  
```
route是监控location.url的，然后采取操作的。所以事件的触发应该是先location后route事件

```javascript
var app = angular.module('myApp', ['ngRoute']);  
  
//  
app.config(['$routeProvider', function ($routeProvider) {  
    $routeProvider.when('/view1', {  
        templateUrl: 'view1.html'  
    }).when('/view2', {  
            templateUrl: 'view2.html'  
        }).otherwise({redirectTo: '/view1'});  
}]);  
  
app.run(['$rootScope', '$window', '$location', '$log', function ($rootScope, $window, $location, $log) {  
    var locationChangeStartOff = $rootScope.$on('$locationChangeStart', locationChangeStart);  
    var locationChangeSuccessOff = $rootScope.$on('$locationChangeSuccess', locationChangeSuccess);  
  
    var routeChangeStartOff = $rootScope.$on('$routeChangeStart', routeChangeStart);  
    var routeChangeSuccessOff = $rootScope.$on('$routeChangeSuccess', routeChangeSuccess);  
  
    function locationChangeStart(event) {  
        $log.log('locationChangeStart');  
        $log.log(arguments);  
    }  
  
    function locationChangeSuccess(event) {  
        $log.log('locationChangeSuccess');  
        $log.log(arguments);  
    }  
  
    function routeChangeStart(event) {  
        $log.log('routeChangeStart');  
        $log.log(arguments);  
    }  
  
    function routeChangeSuccess(event) {  
        $log.log('routeChangeSuccess');  
        $log.log(arguments);  
    }  
}]); 
```

运行结果：

```
locationChangeStart
locationChangeSuccess
routeChangeStart
routeChangeSuccess
```
这个其实框架原型已经出现了

我们只需要处理`locationChangeStart`，这里给出部分实现：

```javascript
function locationChangeStart(event, newUrl) {  
    $log.log('locationChangeStart');  
    $log.log(arguments);  

    var ret = $window.confirm('Are you sure to give it up? ');  
    if (ret) {  
        locationChangeStartOff(); //Stop listening for location changes or you can do others  
        $location.path(newUrl);  
        return;  
    }  
    event.preventDefault();  
    return;  
} 
```
我们来总结下：
我们利用location改变事件来实现了在提交Location之前做了定制化，除了location事件，还有route事件，利用route事件我们可以做‘loading..’效果。

## AngularJS监听路由变化$location和$route实例

```javascript
//增加路由跳转时的判断，如果是同一个页面重新刷新，则让其跳转到相应的页面。
        app.run(['$rootScope', '$window', '$location', '$log', function ($rootScope, $window, $location, $log) {
            var locationChangeStartOff = $rootScope.$on('$locationChangeStart', locationChangeStart);
            var locationChangeSuccessOff = $rootScope.$on('$locationChangeSuccess', locationChangeSuccess);
            var routeChangeStartOff = $rootScope.$on('$routeChangeStart', routeChangeStart);
            var isSecond = false;
//
            function locationChangeStart(event, newUrl, currentUrl) {
                //调试用信息，测试无误后可删除
                console.log('arguments = ', arguments);
                console.log('newUrl = ', newUrl);
                console.log('decode -> newUrl = ', decodeURIComponent(newUrl));
                console.log('currentUrl = ', currentUrl);
                if (decodeURIComponent(newUrl) == currentUrl) {
                    console.log('currentUrl.indexof = ', currentUrl.indexOf('upload_topic_image'));
                    if (currentUrl.indexOf('upload_topic_image') >= 0) {
                        if (isSecond) {
                            console.log("$location.path('http://ctb.qingguo.com/weixinCt/main#/upload_topic_start')");
                            $location.path('http://ctb.qingguo.com/weixinCt/main#/upload_topic_start');
                            isSecond = false;
                        } else {
                            isSecond = true;
                            console.log('isSecond =  ', isSecond);
                        }
                        event.preventDefault();
                        return;
                    }
                }
                console.log('判断结束 ');

            }
            function locationChangeSuccess(event, newUrl, currentUrl) {
                //调试用信息，测试无误后可删除
                console.log('arguments = ', arguments);
                console.log('newUrl = ', newUrl);
                console.log('decode -> newUrl = ', decodeURIComponent(newUrl));
                console.log('currentUrl = ', currentUrl);
                if (decodeURIComponent(newUrl) == currentUrl) {
                    console.log('currentUrl.indexof = ', currentUrl.indexOf('upload_topic_image'));
                    if (currentUrl.indexOf('upload_topic_image') >= 0) {
                        if (isSecond) {
                            console.log("$location.path('http://ctb.qingguo.com/weixinCt/main#/upload_topic_start')");
                            $location.path('http://ctb.qingguo.com/weixinCt/main#/upload_topic_start');
                            isSecond = false;
                        } else {
                            isSecond = true;
                            console.log('isSecond =  ', isSecond);
                        }
                        event.preventDefault();
                        return;
                    }
                }
                console.log('判断结束 ');
            }
            function routeChangeStart(event, newUrl, currentUrl) {
                //调试用信息，测试无误后可删除
                console.log('routeChangeStart-----开始 ');
                console.log('arguments = ', arguments);

                if(newUrl != undefined && currentUrl != undefined && newUrl.$$route != undefined && currentUrl.loadedTemplateUrl !=undefined) {
                    console.log('newUrl = ', newUrl);
                    console.log('newUrl.url = ', newUrl.$$route.templateUrl);
                    console.log('currentUrl = ', currentUrl.loadedTemplateUrl);
                    if (newUrl.$$route.templateUrl == currentUrl.loadedTemplateUrl) {
                        console.log('currentUrl.indexof = ', currentUrl.loadedTemplateUrl.indexOf('upload_topic_image'));
                        if (currentUrl.loadedTemplateUrl.indexOf('upload_topic_image') >= 0) {
//                        if (isSecond) {
                            console.log("$location.path('http://ctb.qingguo.com/weixinCt/main#/upload_topic_start')");
                            $location.path('http://ctb.qingguo.com/weixinCt/main#/upload_topic_start');
                            isSecond = false;
//                        } else {
//                            isSecond = true;
//                            console.log('isSecond =  ', isSecond);
//                        }
                            event.preventDefault();
                            return;
                        }
                    }
                }
                console.log('routeChangeStart-----结束 ');
            }
        }]);
```




