# AngularJS简介

AngularJS 是一个JavaScript框架。AngularJS通过指令扩展了HTML，且通过表达式绑定数据到HTML。

> [AngularJS 速查中文教程](http://www.runoob.com/angularjs/angularjs-intro.html)
> [AngularJS Tutorial](https://docs.angularjs.org/tutorial)
> [AngularJS API Docs](https://docs.angularjs.org/api)

## 快速上手

* 编写HTML (hello.html)

```html
<!DOCTYPE html>
<html ng-app="app">
<head>
  <meta charset="utf-8">
  <title>Hello AngularJS!</title>
</head>
<body>
  <div ng-controller="helloWorld">{{data.name}}</div>
  <script src="/webjars/angularjs/angular.min.js"></script>
  <script src="/example/asset/js/hello.js"></script>
</body>
</html>
```

* 编写Javascript (hello.js)

```javascript
var app = angular.module('app', []);
app.controller('helloWorld', ['$scope', 
	function($scope) {
		$scope.data = {
			name : 'Hello World!'
		};
	} 
]);
```

## Angular四大核心特征

### MVC

* Model:数据模型层

```javascript
$scope.data = {
	name : 'Hello World!'
};
```

* View:视图层，负责展示

```html
<div ng-controller="helloWorld">{{data.name}}</div>
```

* Controller:业务逻辑和控制逻辑

```javascript
app.controller('helloWorld', ['$scope', 
	function($scope) {
		$scope.data = {
			name : 'Hello World!'
		};
	} 
]);
```

### 模块化

* Modules are Containers
	- Module->Config->Routes
	- Module->Filter
	- Module->Directive
	- Module->Factory->Service->Provider->Value
	- Module->Controller

* 一切都是从`angular.module`、`ng-app="app"`开始：

```
<html ng-app="app">
```

```javascript
var app = angular.module('app', []);
```

### 指令系统

* 编写指令`hello`：

```javascript
var app = angular.module('app', []);
app.directive('hello', function() {
	return {
		restrict: 'E',
		template: '<div>Hi, AngujarJS directive demo!</div>',
		replace: true
	};
});
```

* 编写调用指令`<hello></hello>`：

```html
<!DOCTYPE html>
<html ng-app="app">
<head>
  <meta charset="utf-8">
  <title>AngularJS Directive!</title>
</head>
<body>
  <hello></hello>
  <script src="/webjars/angularjs/angular.min.js"></script>
  <script src="/example/asset/js/hello.js"></script>
</body>
</html>
```

### 双向数据绑定

* 定义模型`ng-model="name"`，显示绑定数据`{{name}}`：

```html
<!DOCTYPE html>
<html ng-app="app">
<head>
  <meta charset="utf-8">
  <title>AngularJS Two-Way Data Binding!</title>
</head>
<body>
  <div ng-controller="dataBinding">
    <p>Hello, {{name}}</p>
    <p>Hello, <input type="text" ng-model="name" /></p>
  </div>
  <script src="/webjars/angularjs/angular.min.js"></script>
  <script type="text/javascript">
  	var app = angular.module('app', []);
  	app.controller('dataBinding', ['$scope', function($scope) {
		$scope.name = 'world!';
  	}]);
  </script>
</body>
</html>
```

## 前端开发工具

| 名称 | 说明 |
| --- | --- |
| [Bower](http://bower.io) | Web sites are made of lots of things — frameworks, libraries, assets, and utilities. Bower manages all these things for you. |
| [gulp](http://www.gulpjs.com.cn) | [gulp.js](https://gulpjs.com/)，gulp is a toolkit for automating painful or time-consuming tasks in your development workflow, so you can stop messing around and build something. |
| [grunt](http://www.gruntjs.net/) | 代码合并和混淆 |
| [Sublime](http://www.sublimetext.com/) | 代码编辑 |
| [NodeJS](https://nodejs.org) | 开发和调试 |
| [Git](https://git-scm.com/) | 版本管理 |
| [Karma](https://karma-runner.github.io/) | Karma is to bring a productive testing environment to developers.  |
| [Jasmine](https://jasmine.github.io/) | Jasmine is a behavior-driven development framework for testing JavaScript code. It does not depend on any other JavaScript frameworks. It does not require a DOM. And it has a clean, obvious syntax so that you can easily write tests. |
| [Protractor](http://www.protractortest.org/) | Protractor is an end-to-end test framework for Angular and AngularJS applications. Protractor runs tests against your application running in a real browser, interacting with it as a user would. |
| [http-server](https://www.npmjs.com/package/http-server) | http-server is a simple, zero-configuration command-line http server. It is powerful enough for production usage, but it's simple and hackable enough to be used for testing, local development, and learning. |

## 基本概念和用法

### 简介

* AngularJS 是一个JavaScript框架
  - AngularJS 是一个JavaScript框架。
  - AngularJS 是以一个JavaScript文件形式发布的，通过`script`标签添加到网页中。

* AngularJS 扩展了HTML
  - AngularJS 通过`ng-directives`扩展了HTML。
  - `ng-app` 指令定义一个 AngularJS 应用程序。
  - `ng-model` 指令把元素值（比如输入域的值）绑定到应用程序。
  - `ng-bind` 指令把应用程序数据绑定到HTML视图。

* AngularJS运行过程
  - 当网页加载完毕，AngularJS自动开启。
  - `ng-app` 指令告诉AngularJS，拥有`ng-app`的元素是AngularJS应用程序的"所有者"。
  - `ng-model` 指令把输入域的值绑定到应用程序变量 name。
  - `ng-bind` 指令把应用程序变量 name 绑定到某个段落的 innerHTML。

### 表达式

* AngularJS 表达式
  - 写在双大括号内：`{{ expression }}`。
  - 把数据绑定到 HTML。
  - 将在表达式书写的位置"输出"数据。
  - 很像JavaScript表达式：它们可以包含文字、运算符和变量。

```html
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>AngularJS expression.</title>
</head>
<body>
  <div ng-app="">
     <p>我的第一个AngularJS表达式： {{ 5 + 3 }}</p>
     <p>我的第二个AngularJS表达式： {{ 'Hello' + 3 }}</p>
  </div>
  <script src="/webjars/angularjs/angular.min.js"></script>
</body>
</html>
```

* AngularJS 数字

```html
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>AngularJS 数字串表达式.</title>
</head>
<body>
  <div ng-app="" ng-init="quantity=1;cost=5">
     <p>总价： {{ quantity * cost }}</p>
     <p>使用ng-bind显示总价： <span ng-bind="quantity * cost"></span></p>
  </div>
  <script src="/webjars/angularjs/angular.min.js"></script>
</body>
</html>
```

* AngularJS 字符串

```html
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>AngularJS 字符串表达式.</title>
</head>
<body>
  <div ng-app="" ng-init="firstName='John';lastName='Doe'">
     <p>姓名： {{ firstName + " " + lastName }}</p>
     <p>使用ng-bind显示姓名： <span ng-bind="firstName + ' ' + lastName"></span></p>
  </div>
  <script src="/webjars/angularjs/angular.min.js"></script>
</body>
</html>
```

* AngularJS 对象

```html
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>AngularJS 对象表达式.</title>
</head>
<body>
  <div ng-app="" ng-init="person={firstName:'John',lastName:'Doe'}">
     <p>姓名： {{ person.lastName }}</p>
     <p>使用ng-bind显示姓名： <span ng-bind="person.lastName"></span></p>
  </div>
  <script src="/webjars/angularjs/angular.min.js"></script>
</body>
</html>
```

* AngularJS 数组

```html
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>AngularJS 数组表达式.</title>
</head>
<body>
  <div ng-app="" ng-init="points=[1,15,19,2,40]">
     <p>第三个值为 {{ points[2] }}</p>
     <p>使用ng-bind显示第三个值为 <span ng-bind="points[2]"></span></p>
  </div>
  <script src="/webjars/angularjs/angular.min.js"></script>
</body>
</html>
```

* AngularJS 表达式 与 JavaScript 表达式
  - 类似于JavaScript表达式，AngularJS 表达式可以包含字母，操作符，变量。
  - 与JavaScript表达式不同，AngularJS 表达式可以写在HTML中。
  - 与JavaScript表达式不同，AngularJS 表达式不支持条件判断，循环及异常。
  - 与JavaScript表达式不同，AngularJS 表达式支持过滤器。

### AngularJS指令

* AngularJS指令是以`ng`作为前缀的HTML属性。
  - `ng-app`指令初始化一个 AngularJS 应用程序。
  	+ `ng-app` 指令定义了AngularJS应用程序的根元素。
  	+ `ng-app` 指令在网页加载完毕时会自动引导（自动初始化）应用程序。
  - `ng-init`指令可以初始化AngularJS应用程序变量。
  - `ng-model`或`ng-bind`指令或angular表达式把元素值绑定到应用程序。
    + `ng-model`是用于表单元素的，支持双向绑定。对普通元素无效。
    + `ng-bind`用于普通元素，不能用于表单元素，应用程序单向地渲染数据到元素。
    + 当`ng-bind`和`{{}}`同时使用时，`ng-bind`绑定的值覆盖该元素的内容。
  - `ng-model`指令还可以：
  	+ 为应用程序数据提供类型验证（number、email、required）。
  	+ 为应用程序数据提供状态（invalid、dirty、touched、error）。
  	+ 为HTML元素提供CSS类。
  	+ 绑定HTML元素到HTML表单。
  - `ng-repeat`指令对于集合中（数组中）的每个项会克隆一次HTML元素。
```html
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>AngularJS ng-init Directive.</title>
</head>
<body>
  <div ng-app="" ng-init="firstName='John'">
	<p>姓名为 <span ng-bind="firstName"></span></p>
  </div>
  <script src="/webjars/angularjs/angular.min.js"></script>
</body>
</html>
```

* 数据绑定

```html
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>AngularJS ng-init Directive.</title>
</head>
<body>
  <div ng-app="" ng-init="quantity=1;price=5">
	<h2>价格计算器</h2>
	数量： <input type="number" ng-model="quantity">
	价格： <input type="number" ng-model="price">
	<p><b>总价：</b> {{ quantity * price }}</p>
  </div>
  <script src="/webjars/angularjs/angular.min.js"></script>
</body>
</html>
```

* 重复HTML元素
  - 使用`ng-repeat`指令重复HTML元素

```html
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>AngularJS ng-init Directive.</title>
</head>
<body>
  <div ng-app="" ng-init="names=['Jani','Hege','Kai'];objects=[
{name:'Jani',country:'Norway'},
{name:'Hege',country:'Sweden'},
{name:'Kai',country:'Denmark'}]">
    <h2>使用ng-repeat来循环数组</h2>
    <ul>
      <li ng-repeat="x in names">
        {{ x }}
      </li>
    </ul>
    <h2>使用ng-repeat指令循环对象</h2>
    <ul>
	  <li ng-repeat="x in objects">
	    {{ x.name + ', ' + x.country }}
	  </li>
	</ul>
  </div>
  <script src="/webjars/angularjs/angular.min.js"></script>
</body>
</html>
```

### 自定义指令

* 使用`.directive`函数来添加自定义的指令。
* 调用自定义指令，HTML元素上需要添加自定义指令名。
* 定义指令时，若使用驼峰法来命名一个指令， `helloWorld`, 但在使用它时需要以`-`分割, `hello-world`。
* 以下为一个调用示例：
```html
<!DOCTYPE html>
<html ng-app="app">
<head>
  <meta charset="utf-8">
  <title>AngularJS 自定义指令.</title>
</head>
<body>
  <p>
    第一种元素名调用方式：<hello-world></hello-world>
  </p>
  <p>
    第二种属性调用方式：<div hello-world></div>
  </p>
  <p>
    第三种类名调用方式：<div class="hello-world"></div>
  </p>
  <p>
    第四种注释调用方式：
    <!-- directive: hello-world -->
  </p>

  <!-- 以下调用方式不能起作用 -->
  <helloWorld></helloWorld>

  <script src="/webjars/angularjs/angular.min.js"></script>
  <script type="text/javascript">
    var app = angular.module("app", []);
    app.directive("helloWorld", function() {
        return {
            restrict: "EACM",
            replace: true,
            template: "<h2>Hello World, 自定义指令!</h2>"
        };
    });
  </script>
</body>
</html>
```

* 指令参数`restrict`说明

| restrict | 声明 | 调用示例 |
| --- | --- | --- |
| E  | 元素名 | `<hello-world></hello-world>` |
| A  | 属性 | `<div hello-world></div>` |
| C  | 类名 | `<div class="hello-world"></div>` |
| M  | 注释 | `<!-- directive: hello-world -->` |
| EA | 默认EA | 可通过元素名和属性名来调用指令 |

  - 声明M注释指令时，注释：`<!--` 两边一定要留空格，不然什么都不会发生 `-->`。
  - 声明M注释指令时，需要在该指令添加`replace:true`属性，否则注释指令是不可见的。

### 指令参数

* 一个指令包含的参数，如下：

```javascript
angular.module('app', []) //
.directive('myDirective', function() {
    return {
      restrict: String,                
      priority: Number,
      terminal: Boolean,
      template: String or Template Function: function(element, attrs) {...},
      templateUrl: String,
      replace: Boolean or String,
      scope: Boolean or Object,
      transclude: Boolean,
      controller: String or function(scope, element, attrs, transclude, otherInjectables) { ... },
      controllerAs: String,
      require: String,
      link: function(scope, iElement, iAttrs) { ... },
      compile: function(element, attrs, transclude) {// 返回一个对象或连接函数，如下所示：
        return {
          pre: function(scope, element, attrs, controller) { ... },
          post: function(scope, element, attrs, controller) { ... }
        }
        return function postLink(...) { ... }
      }
    };
 });
```

* 指令包含的参数分成三类：
  - 描述指令或DOM本身特性的内部参数。
  - 连接指令外界、与其他指令或控制器沟通的沟通参数。
  - 描述指令本身行为的行为参数。

### 指令参数详解

#### 内部参数

| 指令 | 类型 | 说明 |
| --- | --- | --- |
| `restrict` | String | 声明指令的使用特性 |
| `priority` | Number | 指令执行优先级 |
| `template` | String | 指令链接DOM模板，例如`<h1>{{head}}</h1>` |
| `templateUrl` | String | DOM模板路径 |
| `replace` | Boolean | 指令链接模板是否替换原有元素 |

其中，`restrict`指令，详解如下：

| restrict | 声明 | 调用示例 |
| --- | --- | --- |
| E  | 元素名 | `<hello-world></hello-world>` |
| A  | 属性 | `<div hello-world></div>` |
| C  | 类名 | `<div class="hello-world"></div>` |
| M  | 注释 | `<!-- directive: hello-world -->` |
| EA | 默认EA | 可通过元素名和属性名来调用指令 |

> 请注意：
  - 定义指令时，若使用驼峰法来命名一个指令：`helloWorld`, 在使用它时需要以`-`分割：`hello-world`。
  - 声明M注释指令时，注释：`<!--` 两边一定要留空格，不然什么都不会发生 `-->`。
  - 声明M注释指令时，需要在该指令添加`replace:true`属性，否则注释指令是不可见的。

#### 沟通参数-scope

* scope参数的作用是，隔离指令与所在控制器间的作用域、隔离指令与指令间的作用域。
* scope参数是可选的，默认值为false，可选true、对象{}：
  - false：共享父作用域；
  - true：继承父作用域且新建独立作用域；
  - 对象{}：不继承父域且新建独立作用域；

```html
<!DOCTYPE html>
<html ng-app="app">
<head>
  <meta charset="utf-8">
  <title>AngularJS scope参数的作用.</title>
  <style type="text/css">
  .panel {
    margin-bottom:20px;
    border: 1px solid #eee;
    padding: 10px;
  }
  .panel h4 {
    margin-top:2px;
    margin-bottom:10px;
  }
  .panel input {
    height: 20px;
    padding: 5px;
  }
  </style>
</head>
<body>
    <div ng-controller='parentCtrl'>
      <h3>指令scope参数——false、true、{}对比测试</h3>
      <div class="panel">
        <h4>controller父作用域:</h4>
        <input type="text" ng-model="parentName" />
        <span>  {{parentName}}</span>
      </div>
      <div class="panel">
        <child-a></child-a>
      </div>
      <div class="panel">
        <child-b></child-b>
      </div>
      <div class="panel">
        <child-c></child-c>
      </div>
    </div>
    <!--t1指令模板-->
    <script type="text/html" id="t1">
      <input type="text" ng-model="parentName" /><span>  {{parentName}}</span>
    </script>
    <script src="/webjars/angularjs/angular.min.js"></script>
    <script type="text/javascript">
      var app = angular.module("app", []);
      app.controller('parentCtrl', function ($scope) {
        $scope.parentName = "父作用域默认值";
      });

      //false：共享作用域
      app.directive('childA', function () {
        return {
          restrict: 'E',
          scope: false,
          template: function (elem, attr) {
            return "<h4>指令参数{scope:false}共享父作用域</h4>" + document.getElementById('t1').innerHTML;
          }
        };
      });

      //true：继承父域，并建立独立作用域
      app.directive('childB', function () {
        return {
          restrict: 'E',
          scope: true,
          template: function (elem, attr) {
            return "<h4>指令参数{scope:true}继承父作用域且新建独立作用域</h4>" + document.getElementById('t1').innerHTML;
          },
          controller: function ($scope) {
            //$scope.parentName = "parent";
            //已声明的情况下，$scope.$watch监听的是自己的parentName
            $scope.$watch('parentName', function (n, o) {
              console.log("child watch" + n);
            });
            //$scope.$parent.$watch监听的是父域的parentName
            $scope.$parent.$watch('parentName', function (n, o) {
              console.log("parent watch" + n);
            });
          }
        };
      });

      //{}：不继承父域，建立独立作用域
      app.directive('childC', function () {
        return {
          restrict: 'E',
          scope: {},
          template: function (elem, attr) {
            return "<h4>指令参数{scope:{}}不继承父域且新建独立作用域</h4>" + document.getElementById('t1').innerHTML;
          },
          controller: function ($scope) {
            console.log($scope);
          }
        };
      });
    </script>
</body>
</html>
```

