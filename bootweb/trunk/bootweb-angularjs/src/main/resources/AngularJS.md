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

在使用`template`时，可借助AngularJS缓存：

```javascript
var app = angular.module('app', []);
// 注射器加载完成所有模块时，此方法执行一次。
app.run(function($templateCache) {
  $templateCache.put('hello.html', '<div>Hello World!</div>')
});
app.directive('hello', function($templateCache) {
  return {
    restrict: 'EMAC',
    tempalte: $templateCache.get('hello.html'),
    replace: true
  };
});
```

#### 沟通参数-scope

* scope参数的作用是，隔离指令与所在控制器间的作用域、隔离指令与指令间的作用域。
* scope参数是可选的，默认值为`false`，可选`true`、`{}`：

| scope值 | 作用域 |
| --- | --- |
| `scope: false` | 共享父作用域 |
| `scope: true` | 继承父作用域且新建独立作用域 |
| `scope: {}` | 不继承父域且新建独立作用域 |


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

* `scope: false`特点：
  - (1). 父域修改parentName的同时，指令绑定的parentName的元素会被刷新。
  - (2). 指令内部parentName被修改时，父域的parentName同样会被刷新。
* `scope: true`特点：
  - (1). 在指令已声明parentName的情况下，父域parentName变更，指令中parentName不会发生变化。指令在true参数下，建立了的scope，独立并隔离与父控制器的scope。即使指令中parentName变更，父域也不会发生变化。
  ```javascript
    controller: function ($scope) {
      $scope.parentName = "parent";
    }
  ```
  - (2). 在指令未声明parentName的情况下，父域的parentName变更，指令中parentName也会刷新。这种情况很多时候会被忽略，指令的scope没有声明对象时，其元素绑定的仍然是父域的对象。
  - (3). 在指令未声明parentName的情况下，一旦指令中对parentName赋值或双向绑定变更，对应的独立scope也会自动声明该绑定对象，这就回到了第(1)种情况。然而，指令中parentName变更，父域是不会变化的。
  ```javascript
    controller: function ($scope) {
      // $scope.parentName = "parent";
    }
  ```
  - (4). 在指令已声明parentName的情况下 ，在指令中监听父域parentName 的变化无效。但监听子域parentName的变化有效，独立子域scope，只能监听自己的，不能监听父域的。但通过 $scope.$parent可以监听父域。
  ```javascript
    controller: function ($scope) {
      $scope.parentName = "parent" ;
      //已声明的情况下，$scope.$watch监听的是自己的parentName
      $scope.$watch( 'parentName' , function (n, o) {
          console.log("child watch" + n);
      });
      //$scope.$parent.$watch监听的是父域的parentName
      $scope.$parent.$watch( 'parentName' , function (n, o) {
          console.log("parent watch" + n);
      });
    }
  ```
  - (5). 在指令未声明parentName的情况下 ，在指令中监听父域parentName的变化有效。
  ```javascript
    controller: function ($scope) {
        //$scope.parentName = "parent";
        //未声明的情况下，$scope.$watch监听的是父域的parentName
        $scope.$watch('parentName' , function (n, o) {
            console.log("child watch" + n);
        });
    }
  ```
* `scope: {}`特点：
  - (1). 子域不继承父域，并建立独立作用域。
  - (2). 可通过以下操作符与父级沟通：

  | 操作符 | 说明 | 示例 |
  | --- | --- | --- |
  | @ | 单向数据绑定：用来传递普通字符串 | `<child-c my-age="{{age}}"></child-c>`<br>需要加`{{}}` |
  | = | 双向数据绑定，用于传递对象 | `<child-c my-name="name"></child-c>`<br>不需要加`{{}}` |
  | & | 传递函数 | `<child-c on-say="say('i m ' + name)"></child-c>`<br>&对应的attrName必须以`on-`开头 |

  - (3). 当scope对象为空对象时，无论是父域parentName，还是指令子域parentName发生变更，都不会影响到对方。因为指令建立的独立作用域，与父域是完全隔离的。
  - (4). 当scope对象为非空对象时，指令会将该对象处理成子域scope的扩展属性。而父域与子域之间传递数据的任务，就是可以通过这块扩展属性完成。

  ```html
  <!DOCTYPE html>
  <html ng-app="app">
  <head>
    <meta charset="utf-8">
    <title>指令scope参数(@, =, &)测试</title>
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
    .panel button {
      height: 30px;
    }
    .panel .item {
      margin: 5px;
      padding: 5px;
    }
    </style>
  </head>
  <body>
    <div ng-controller="parentCtrl">
      <h3>指令scope参数(@, =, &)测试</h3>
      <div class="panel">
        <h4>controller父作用域:</h4>
        <div class="item">Name: <input type="text" ng-model="name" />  <span>{{name}}</span></div>
        <div class="item">Gender: <input type="text" ng-model="gender" />  <span>{{gender}}</span></div>
        <div class="item">Age: <input type="text" ng-model="age" />  <span>{{age}}</span></div>
        <div class="item"><button ng-click="say(name)">直接调用</button></div>
      </div>
      <div class="panel">
        <!--特别注意：@与=对应的attr，@是单向绑定父域的机制，记得加上{{}}；&对应的attrName直接调用父作用域-->
        <demo my-name="name" my-gender-attr="gender" my-age="{{age}}" fun-say="say(name)"></demo>
      </div>  
      <!--t1指令模板-->
      <script type="text/html" id="t1">
        <div class="item">Name: <input type="text" ng-model="myName" />  <span>{{myName}}</span></div>
        <div class="item">Gender: <input type="text" ng-model="myGender" />  <span>{{myGender}}</span></div>
        <div class="item">Age: <input type="text" ng-model="myAge" />  <span>{{myAge}}</span></div>
        <div class="item"><button ng-click="mySay(myName)">[&]调用</button></div>
      </div>
      </script>
      <script src="/webjars/angularjs/angular.min.js"></script>
      <script type="text/javascript">
        var app = angular.module("app", []);
        app.controller('parentCtrl', function ($scope) {
          $scope.name = "Jack";
          $scope.gender = "male";
          $scope.age = "28";
          $scope.say = function(msg) {
            console.log('parent message: ' + msg);
            alert('parent message: ' + msg);
          };
        });
        app.directive('demo', function () {
          return {
            restrict: 'E',
            scope: {
              myName: '=',
              myGender: '=myGenderAttr',
              myAge: '@',
              funSay: '&'
            },
            template: function (elem, attr) {
              return "<h4>指令子作用域:</h4>" + document.getElementById('t1').innerHTML;
            },
            controller: function ($scope) {
              console.log('directive controller');
              console.log($scope.myName);
              console.log($scope.myGender);
              console.log($scope.myAge);
              $scope.mySay = function(msg) {
                console.log('directive : ' + msg);
                alert('directive : ' + msg);
                $scope.funSay();
              }
            }
          };
        });
      </script>
    </div>
  </body>
  </html>
  ```
  - (5). [@、=及&]总结
    + `@`（or @Attr)绑定策略——本地作用域属性：使用`@`符号将本地作用域同DOM属性的值进行绑定。指令内部作用域可以使用外部作用域的变量。(单向引用父域对象)。请注意`@`是单向绑定本地作用域，记得加上`{{}}`。
    ```
    <demo my-age="{{age}}"></demo>
    scope: {
      myAge: '@',
    }
    ```
    + `=` （or =Attr）绑定策略——双向绑定：通过`=`可以将本地作用域上的属性同父级作用域上的属性进行双向的数据绑定。就像普通的数据绑定一样，本地属性会反映出父数据模型中所发生的改变。（双向引用父域对象）。`=`策略不需要加上`{{}}`进行绑定。
    ```
    <demo my-name="name"></demo>
    scope: {
      myName: '=',
    }
    ```
    + `&` （or &Attr）绑定策略——通过`&`符号可以对父级作用域进行绑定，以便在其中运行函数。（调用父域函数）。
    ```
    <demo fun-say="say(name)"></demo>
    scope: {
      funSay: '&',
    }
    ```
    父域绑定调用函数及传参：
    ```
    app.controller('parentCtrl', function ($scope) {
      $scope.say = function (msg) {
        alert(msg);
      };
    })
    ```
    + scope扩展对象，既能够解耦父域与子域共域的问题，也能够实现指令与外界通讯的问题，是Angular开发指令化模块化的重要基础。

#### 对外参数——require

* scope是指令与外界作用域通讯的桥梁，而require是指令与指令之间通讯的桥梁。
* `require`参数最大的作用在于，当要开发单指令无法完成，需要一些组合型指令的控件或功能，例如日期控件，通过require参数，指令可以获得外部其他指令的控制器，从而达到交换数据、事件分发的目的。
* 使用方法：`require: String or Array`。
  - String值为引入指令名称，并且有两个寻找指令策略符号`？`与`^`；
  - Array数组则为多个外部指令名称。

* 在link函数第4个参数ctrl中获取注入外部指令的控制器，如果require为String，ctrl为对象，如果require是数组，ctrl为数组。

```javascript
require: '^teacher',
link: function ($scope, $element, $attrs, ctrl) {
    //ctrl指向teacher指令的控制器
}
```

* `？策略`——寻找指令名称，如果没有找到，link函数第4个参数为null；如果没有`？`，则报错。
* `^ 策略`——在自身指令寻找指令名称的同时，向上父元素寻找；如果没有`^`，则仅在自身寻找。
* 如下例子，指令studentA向上可以找到指令teacher及自身，但是不能找到相邻兄弟的student-b。

```
<div teacher>
    <student-a></student-a>
    <student-b></student-b>
</div>
```

```html
<!DOCTYPE html>
<html ng-app="app">
<head>
  <meta charset="utf-8">
  <title>指令require参数测试</title>
  <style type="text/css">
  .panel {
    margin-bottom:20px;
    border: 1px solid #eee;
    padding: 10px;
  }
  .panel .item {
    margin: 5px;
    padding: 5px;
  }
  </style>
</head>
<body>
  <div class="panel" teacher>
    <div class="item">{{name}}</div>
    <student-a></student-a>
    <student-b></student-b>
  </div>
  <script src="/webjars/angularjs/angular.min.js"></script>
  <script type="text/javascript">
    var app = angular.module("app", []);
    //studentA——require指向父级指令teacher
    app.directive('studentA', function () {
      return {
        require: '?^teacher',
        scope: {},
        template: '<div class="item">A\'s teacher name: <span>{{teacherName}}</span></div>',
        link: function ($scope, $element, $attrs, ctrl) {
          //获取teacher指令控制器，并调用其方法sayName()
          $scope.teacherName = ctrl.sayName();
        }
      };
    });
    //studentB——require指向父级指令teacher，及指令studentA
    app.directive('studentB', function () {
      return {
        require: ['?^teacher', '?studentA'],//采取`？`策略，不报错
        // require: ['?^teacher', 'studentA'],//不能获得兄弟，也没有采取`？`策略，导致报错
        scope: {},
        template: '<div class="item">B\'s teacher name: <span>{{teacherName}}</span></div>',
        link: function ($scope, $element, $attrs, ctrl) {
          console.log(ctrl);
          console.log($scope);
          $scope.teacherName = ctrl[0].sayName();
        }
      };
    });
    app.directive('teacher', function () {
      return {
        restrict: 'A',
        controller: function ($scope) {
          $scope.name = "Miss wang";
          //扩展控制器的方法sayName，目的是让外部内获取控制器内部数据
          this.sayName = function () {
              return $scope.name;
          };
        }
      };
    });
  </script>
</body>
</html>
```

#### 行为参数——link与controller
* link与controller都是描述指令行为(指令元素操作行为|指令作用域行为)的参数，但它们是要描述的行为是完全不同的类型。

> controller语法 controller：String or Function
* controller本身的意义就是赋予指令控制器，而控制器就是定义其内部作用域的行为的。所以controller要描述的是：指令的作用域的行为。

```javascript
//指向匿名控制器
controller: function ($scope) {
},
//指向控制器mainCtrl
controller: "mainCtrl"
```

> link语法 link：String Or Function
* ink名称是链接函数，它会在形成模板树之后，在数据绑定之前，从最底部指令开始，逐个指令执行它们的link函数。在这个时间节点的link函数，操作DOM的性能开销是最低，非常适合在这个时机执行DOM的操作，例如鼠标操作或触控事件分发绑定、样式Class设置、增删改元素等等。所以link就是描述指令元素操作行为。

```javascript
link: function (scope, element, attr, ctrl) {
  element.bind("click", function () {
    console.log("绑定点击事件");
  });
  element.append("<p>增加段落块</p>");
  //设置样式
  element.css("background-color", "yellow");
  //不推荐，在link中赋予scope行为
  scope.hello = function () {
    console.log("hello");
  };
}
```

* 在link中定义$scope行为是不推荐的。
* 执行顺序是是先controller，后link。
* 全局顺序：
  - 执行controller，设置各个作用域scope。
  - 加载模板，形成DOM模板树。
  - 执行link，设置DOM各个行为。
  - 数据绑定，最后scope绑上DOM。

```html
<!DOCTYPE html>
<html ng-app="app">
<head>
  <meta charset="utf-8">
  <title>指令require参数测试</title>
  <style type="text/css">
  .panel {
    margin-bottom:20px;
    border: 1px solid #eee;
    padding: 10px;
  }
  .panel .item {
    margin: 5px;
    padding: 5px;
  }
  </style>
</head>
<body>
  <div class="panel" student>
    <div class="item">{{name}}</div>
  </div>
  <script src="/webjars/angularjs/angular.min.js"></script>
  <script type="text/javascript">
    var app = angular.module("app", []);
    app.directive('student', function () {
      return {
        restrict: 'A',
        controller: function ($scope) {
          $scope.name = "Jack";
          console.log('controller running');
        },
        link: function (scope, element) {
          element.append("<p class=\"item\">hello</p>");
          console.log('link running');
        }
      };
    });
  </script>
</body>
</html>
```

#### `compile`

```javascript
var app = angular.module('myApp', []);
app.directive('manyHello', function() {
  return {
  	restrict: 'A',
  	compile: function(element, attrs, transclude) {
  		console.log('开始指令编译');
  		var tpl = element.children().clone();
  		for (var i = 0; i < attrs.numofhello - 1; i++) {
  			element.append(tpl);
  		}
  		return function(scope, element, attrs, controller) {
        console.log('指令连接');
  		}
  	},
  	link: function() {
  		// 该函数是不会执行的。
  		// 同时有compile和link，link函数是不会执行的。
  	}
  };
});
```

* compile函数的作用是对指令的模板进行转换。
* link的作用是在模型和视图之间建立关联，包括在元素上注册事件监听。
* scope在链接阶段才会被绑定到元素上，因此compile阶段操作scope会报错。
* 对于同一个指令的多个实例，compile只会执行一次；而link对于指令的每个实例都会执行一次。
* 一般情况下只要编写link函数就可以了。
* 请注意，如果编写的自定义的compile函数，自定义的link函数无效，因为compile函数应该返回一个link函数供后续处理。

#### 自定义指令总结

```javascript
angular.module('myApp', []).directive('first', [ function(){
  return {
    scope: false, //默认值为 false 共享父作用域 值为true时共享父级作用域并创建指令自己的                      
    controller: function($scope, $element, $attrs, $transclude) {}, //作用域  值为{}时创建全新的隔离作用域, 值为string时为控制器名称
    restrict: 'AE', // E = Element, A = Attribute, C = Class, M = Comment
    template: 'first name:{{name}}',//值为string、function 用于显示dom元素
    templateUrl: 'xxx.html' //值为string function 以id为xxx.html为 调用文件显示
    prioruty: 0 //指明指令的优先级，若在dom上有多个指令优先级高的先执行
    replace: flase // 默认值为false 当为true是直接替换指令所在的标签
    terminal: true //值为true时优先级低于此指令的其它指令无效
    link:function // 值为函数 用来定义指令行为从传入的参数中获取元素并进行处理
  };
}]).directive('second', [ function(){
    return {
      scope: true, 
      // controller: function($scope, $element, $attrs, $transclude) {},
      restrict: 'AE', // E = Element, A = Attribute, C = Class, M = Comment
      //当修改这里的name时，second会在自己的作用域中新建一个name变量，与父级作用域中的
      // name相对独立，所以再修改父级中的name对second中的name就不会有影响了
      template: 'second name:{{name}}',
    };
}])
.controller('DirectiveController', ['$scope', function($scope){
    $scope.name="mike";
}]);
```
### Controller控制器

#### 使用注意点

* 不要试图去复用Controller，一个控制器一般只负责一小块视图。
* 不要在Controller中操作DOM，这不是控制器的职责。
* 不要在Controller里面做数据格式化，ng有很好用的控件。
* 不要在Controller里面做数据过滤操作，ng有$filter服务。
* 一般来说，Controller是不会互相调用的，控制器之间的交互会通过事件进行。

#### 作用域`$scope`,`$rootScope`

* (作用域)`$scope`是应用在HTML(视图)和JavaScript(控制器)之间的数据通道，它是一个POJO(Plain Old Javascript Object)，它提供了一些工具方法`$watch()`/`$apply()`，它可以传播事件，可以向上可以向下。
* 控制器中主要通过`$scope`来交互数据并且是表达式的执行环境，它是实现双向数据绑定的基础，分为本控制器的`$scope`和根作用域`$rootScope`：
* 控制器中的属性对应了视图上的属性，视图中，不需要添加`$scope`前缀, 只需要添加属性名即可，例如：`{{name}}`。
* 根作用域`$rootScope`：所有的应用都有一个`$rootScope`，它可以作用在`ng-app` 指令包含的所有 HTML 元素中。`$rootScope`可作用于整个应用中。是各个controller中scope的桥梁。用`$rootscope`定义的值，可以在各个 controller中使用。
* 控制器中定义的`$rootScope`变量，无论定义先后，在各个控制器中都可以访问。
* 可以使用angular.element($0).scope()进行调试。

```html
<!DOCTYPE html>
<html ng-app="app">
<head>
  <meta charset="utf-8">
  <title>Controller的scope测试</title>
  <style type="text/css">
  .panel {
    margin-bottom:20px;
    border: 1px solid #eee;
    padding: 10px;
  }
  .panel .item {
    margin: 5px;
    padding: 5px;
  }
  </style>
</head>
<body>
  <div class="panel" ng-controller="greetCtrl">
    <div class="item">Hello, {{name}}! {{variable}}</div>
  </div>
  <div class="panel" ng-controller="listCtrl">
    <div class="item">
      <ul>
        <li ng-repeat="name in names">{{name}} from {{department}}</li>
      </ul>
    </div>
  </div>
  <script src="/webjars/angularjs/angular.min.js"></script>
  <script type="text/javascript">
    var app = angular.module("app", []);
    app.controller('greetCtrl', ['$scope', '$rootScope', function ($scope, $rootScope) {
      $scope.name = 'Greeting Controller!';
      $rootScope.department = 'Root Scope AngularJS.';
    }]);
    app.controller('listCtrl', ['$scope', '$rootScope', function($scope, $rootScope) {
      $scope.names = ['Jack', 'Tom', 'Jerry', 'HunnyHu'];
      $rootScope.variable = '后定义的$rootScope变量。';
    }]);
  </script>
</body>
</html>
```

#### 事件分发`$emit`,`$broadcast`,`$on`

* `$on`、`$emit`和`$broadcast`使得event、data在controller之间的传递变的简单。
* `$emit`：子传父传递event与data，`$broadcast`：父传子传递event与data，`$on`：监听或接收数据，用于接收event与data。
* `$broadcast`、`$emit`事件必须依靠其他事件（ng-click等）进行触发，`$on`是属于监听和接收数据的。
* `$on`方法中的event事件参数：
  - event.name：事件名称。
  - event.targetScope：发出或者传播原始事件的作用域。
  - event.currentScope：目前正在处理的事件的作用域。
  - event.stopPropagation()：一个防止事件进一步传播(冒泡/捕获)的函数(这只适用于使用`$emit`发出的事件)。
  - event.preventDefault()：这个方法实际上不会做什么事，但是会设置`defaultPrevented`为true。直到事件监听器的实现者采取行动之前它才会检查`defaultPrevented`的值。
  - event.defaultPrevented：如果调用了`preventDefault`则为true。

```html
<!DOCTYPE html>
<html ng-app="app">
<head>
  <meta charset="utf-8">
  <title>事件分发$emit,$broadcast测试</title>
  <style type="text/css">
  .panel {
    margin-bottom:20px;
    border: 1px solid #eee;
    padding: 10px;
  }
  .panel input {
    height: 20px;
    padding: 5px;
  }
  .panel button {
    height: 30px;
  }
  .panel .item {
    margin: 5px;
    padding: 5px;
  }
  </style>
</head>
<body>
  <div class="panel" ng-controller="eventCtrl">
    <div class="item">
      Root Scope MyEvent count:{{count}}
    </div>
    <div class="panel" ng-controller="eventCtrl">
      <div class="item">
        <button ng-click="$emit('MyEvent')">$emit('MyEvent')自己+向上</button>
        <button ng-click="$broadcast('MyEvent')">$broadcast('MyEvent')自己+向下</button>
      </div>
      <div class="item">
        Middle Scope MyEvent count:{{count}}
      </div>
      <div class="panel" ng-controller="eventCtrl">
        <div class="item">
          Leaf Scope MyEvent count:{{count}}
        </div>
      </div>
      <div class="panel" ng-controller="eventCtrl">
        <div class="item">
          Leaf Scope MyEvent count:{{count}}
        </div>
      </div>
    </div>
  </div>
  <script src="/webjars/angularjs/angular.min.js"></script>
  <script type="text/javascript">
    var app = angular.module("app", []);
    app.controller('eventCtrl', ['$scope', function ($scope) {
      $scope.count = 0;
      $scope.$on('MyEvent', function() {
        $scope.count++;
      });
    }]);
  </script>
</body>
</html>
```

#### 监听值`$scope.$watch`

* `$watch`是一个scope函数，用于监听模型变化，当模型部分发生变化时它会发出通知。
  - `$watch(watchExpression, listener, objectEquality);`
  - 参数的说明如下：
    + `watchExpression`：监听的对象，它可以是一个angular表达式如`'name'`,或函数如`function(){return $scope.name}`。
    + `listener`：当`watchExpression`变化时会被调用的函数或者表达式,它接收3个参数：newValue(新值), oldValue(旧值), scope(作用域的引用)
    + `objectEquality`：是否深度监听，如果设置为true,它告诉Angular检查所监控的对象中每一个属性的变化. 如果你希望监控数组的个别元素或者对象的属性而不是一个普通的值, 那么你应该使用它

* `$watch`性能问题：太多的`$watch`将会导致性能问题，`$watch`如果不再使用，最好将其释放掉。
  - $watch函数返回一个注销监听的函数，如果我们想监控一个属性，然后在稍后注销它，可以使用下面的方式：
  ```javascript
  var watch = $scope.$watch('someModel.someProperty', callback);
  // 满足某些条件后，注销监听，释放资源
  watch();
  ```

* 一个示例：

```html
<!DOCTYPE html>
<html ng-app="app">
<head>
  <meta charset="utf-8">
  <title>事件分发$emit,$broadcast测试</title>
  <style type="text/css">
  .panel {
    margin-bottom:20px;
    border: 1px solid #eee;
    padding: 10px;
  }
  .panel input {
    height: 20px;
    padding: 5px;
  }
  .panel button {
    height: 30px;
  }
  .panel .item {
    margin: 5px;
    padding: 5px;
  }
  </style>
</head>
<body>
  <div class="panel" ng-controller="watchCtrl">
    <div class="item">
      名称：<input type="input" ng-model="name">
    </div>
    <div class="item">
      显示：{{name}}
    </div>
  </div>
  <script src="/webjars/angularjs/angular.min.js"></script>
  <script type="text/javascript">
    var app = angular.module("app", []);
    app.controller('watchCtrl', ['$scope', function ($scope) {
      $scope.$watch('name', function(newName) {
        var val = '新名称：' + newName;
        console.log(val);
      });
    }]);
  </script>
</body>
</html>
```

* `$watch`单一的变量
  - 对于普通的变量时，如数字，字符串等，直接如下写是可以监视到变量的变化，并执行相应的函数的。
  ```javascript
  $scope.count = 1;
  $scope.$watch('count', function(){
    // ...
  });
  ```

* `$watch`多个变量
  - 对于多个变量的监视变化，执行同一函数的话，可以将这几个变量转为字符串，以‘+’号隔开来进行监视
  ```javascript
  //当count或page变化时，都会执行这个匿名函数
  $scope.count = 1;
  $scope.page = 1;
  $scope.$watch('count + page', function() {
    // ...
  });
  ```

* `$watch`对象或数组
  - `$watch`函数其实是有三个变量的，第一个参数是需要监视的对象，第二个参数是在监视对象发生变化时需要调用的函数，实际上watch还有第三个参数，它在默认情况下是false。 
  - 当第三个参数是false时，其实watch函数监视的是数组的地址，而数组的内容的变化不会影响数组地址的变化，所以watch函数此时不起作用。 
  - 如果第三个参数为true，监视的是数组内容的变化。
  ```javascript
  $scope.items=[
    {a:1},
    {a:2}
    {a:3}
  ];
  $scope.$watch('items', function() {...}, true);
  ```
  - 或者将监听返回结果为JSON字符串形式的该对象或数组的的匿名函数
  ```javascript
  $scope.items=[
    {a:1},
    {a:2}
    {a:3}
  ];
  $scope.$watch(function() {
    return JSON.stringify($scope.items);
  },function() {...});
  ```

* `$watch`函数的返回结果
  - 监视对象为“函数名()”的字符串，不要忘记加“()”
  ```javascript
  //未完成的任务个数
  $scope.unDoneCount = function() {
    var count = 0;
    angular.forEach($scope.todoList, function(todo) {
      count += todo.done ? 0 : 1;
    });
    return count;
  };
  //单选影响全选部分
  $scope.$watch('unDoneCount()', function(nv) {
    $scope.isDoneAll = nv ? false : true;
  });
  ```
  - 在监视对象中设置为匿名函数，返回要监视的函数的返回值
  ```javascript
  $scope.$watch(function() {
    return $scope.unDoneCount();//不要忘了(), 要执行的
  }, function(nv) {
    $scope.isDoneAll = nv ? false : true;
  });
  ```

#### 调用方法`$scope.$apply`

* 参考地址
  - [理解Angular中的$apply()以及$digest()](http://blog.csdn.net/dm_vincent/article/details/38705099)
  - [Understanding Angular’s $apply() and $digest()](https://www.sitepoint.com/understanding-angulars-apply-digest/)
* 双向数据绑定其实也就是当模型发生了变化的时候，重绘了DOM，使你看到数据被更新了，引发模型变化的情况有：
  - 1,dom事件；
  - 2,xhr响应触发回调；
  - 3,浏览器的地址变化；
  - 4,计时器触发回调；
  - 以上的某一个情况发生，都会触发模型监控机制，同时调用了$apply方法，重绘了dom;通常情况下，我们使用的一些指令或服务，如$http,$timeout,$location等都会调用$apply方法，从而使用dom被重绘，数据得到更新，实现了双向数据绑定。

* `$apply()`方法可以在angular框架之外执行angular JS的表达式，例如：DOM事件、setTimeout、XHR或其他第三方的库。
* 示例一，视图不会更新值为"Timeout called!"：
```javascript
function update($scope) {
  $scope.message ="Waiting 2000ms for update";    
  setTimeout(function () {
  　$scope.message ="Timeout called!";
    // AngularJS unaware of update to $scope
  }, 2000); 
}
```
* 示例二，视图会更新值为"Timeout called!":
```javascript
function update($scope) {
  $scope.message ="Waiting 2000ms for update"; 
  setTimeout(function () {
  　$scope.$apply(function () {
      $scope.message = "Timeout called!";
    });
  }, 2000); 
}
```
* 示例三，视图会更新值为"Timeout called!":
```javascript
function update($scope, $timeout) {
  $scope.message ="Waiting 2000ms for update"; 
  $timeout(function () {
    $scope.message = "Timeout called!";
  }, 2000); 
}
```


#### 项目结构示例

* 加载首页

```html
<!DOCTYPE html>
<html ng-app="app">
<head>
  <meta charset="utf-8">
  <title>加载首页</title>
  <!-- 加载样式
  <link rel='stylesheet' href='xxx.css'> -->
  <!-- 定义样式
  <style type="text/css">
  </style> -->
</head>
<body>
  <div ng-view></div>
  <script src="/webjars/angularjs/angular.min.js"></script>
  <!-- 加载脚本 <script src="other javascript lib.js"></script> -->
  <!-- 加载脚本 <script src="app.js"></script> -->
  <!-- 加载脚本 <script src="config.js"></script> -->
  <!-- 加载脚本 <script src="controller.js"></script> -->
  <!-- 加载脚本 <script src="filter.js"></script> -->
  <!-- 加载脚本 <script src="service.js"></script> -->
  <!-- 加载脚本 <script src="directive.js"></script> -->
  <!-- 把脚本放在html中 -->
  <script type="text/javascript">
    var app = angular.module("app", []);
    app.controller('helloCtrl', ['$scope', function ($scope) {
      $scope.greeting = 'Hello World!';
    }]);
  </script>
</body>
</html>
```

* 目录结构
* app
  - css
  - framework
  - img
  - js
    + app.js
    + config.js
    + route.js
    + controller/
    + filter/
    + service/
    + directive/
  - tpl
  - index.html

* `app.js`示例：
```javascript
var app = angular.module('app', ['ngRoute', 
  'myCtrls', 'myFilters', 'myServices', 'myDirectives']);
app.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/hello', function() {
    templateUrl: 'tpl/hello.html',
    controller: 'helloCtrl'
  }).when('/list', function() {
    templateUrl: 'tpl/list.html',
    controller: 'listCtrl'
  }).otherwise({
    redirectTo: '/hello'
  });
}]);
```

#### 网络请求`$http`

* `$http`是一个用于与服务器交互数据的服务。
* v1.5中`$http`的`success`和`error`方法已废弃。使用`then`方法替代。

请求示例：

```javascript
var app = angular.module('app', []);
app.controller('httpCtrl', ['$scope', '$http', function($scope, $http) {
    $http({
        method: 'GET',
        url: 'http://localhost:8080/data/get/name.html'
    }).then(function successCallback(response) {
            $scope.names = response.data.name;
        }, function errorCallback(response) {
            // 请求失败执行代码
    });
}]);
```

* 简写方法
  - POST与GET简写方法格式：
  ```javascript
  $http.get('/someUrl', config).then(successCallback, errorCallback);
  $http.post('/someUrl', data, config).then(successCallback, errorCallback);
  ```

* 此外还有以下简写方法，具体参考[官方api](https://docs.angularjs.org/api/ng/service/$http)：
  - `$http.get`
  - `$http.head`
  - `$http.post`
  - `$http.put`
  - `$http.delete`
  - `$http.jsonp`
  - `$http.patch`

#### 从取URL参数

While routing is indeed a good solution for application-level URL parsing, you may want to use the more low-level `$location` service, as injected in your own service or controller:

虽然`$route`路由确实是应用程序级URL解析的一个很好的解决方案，但可能希望在自己的服务或控制器中注入更低层次的`$location`服务：

```javascript
var paramValue = $location.search().myParam; 
```

This simple syntax will work for `http://example.com/path?myParam=paramValue`. However, only if you configured the `$locationProvider` in the HTML 5 mode before:

这个简单的语法将应用于地址`http://example.com/path?myparam=paramvalue`。然而，这个前提是需要配置`$locationProvider`的HTML5模式：

```javascript
$locationProvider.html5Mode(true);
```

For example:
一个配置`$locationProvider`的HTML5模式示例：

```javascript
var app = angular.module('app', []);
app.config(['$locationProvider', function($locationProvider) {
  $locationProvider.html5Mode({
    enabled: true,
    requireBase: false
  });
}]);
```

### 路由

#### ngRoute

* 使用ngRoute进行视图之间的路由
* 路由功能是由`$routeProvider`服务和`ng-view`搭配实现，`ng-view`相当于提供了页面模板的挂载点，当切换URL进行跳转时，不同的页面模板会放在`ng-view`所在的位置; 然后通过`$routeProvider`配置路由的映射。
* 一般主要通过两个方法：
  - `when()`：配置路径和参数;
  - `otherwise`：配置其他的路径跳转，可以想成default。
* when的第二个参数：
  - `controller`：对应路径的控制器函数，或者名称
  - `controllerAs`：给控制器起个别名
  - `template`：对应路径的页面模板，会出现在ng-view处,比如`<div>xxxx</div>`
  - `templateUrl`：对应模板的路径，比如`src/xxx.html`
  - `resolve`：该属性会以键值对对象的形式，给路由相关的控制器绑定服务或者值。然后把执行的结果值或者对应的服务引用，注入到控制器中。如果resolve中是一个promise对象，那么会等它执行成功后，才注入到控制器中，此时控制器会等待resolve中的执行结果。
  - `redirectTo`：重定向地址
  - `reloadOnSearch`：设置是否在只有地址改变时，才加载对应的模板;search和params改变都不会加载模板
  - `caseInsensitiveMatch`：路径区分大小写
* 路由有几个常用的事件：
  - `$routeChangeStart`：这个事件会在路由跳转前触发
  - `$routeChangeSuccess`：这个事件在路由跳转成功后触发
  - `$routeChangeError`：这个事件在路由跳转失败后触发

```html
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>ngRoute & ng-view示例</title>
</head>
<body ng-app="myApp">
  <div>
    <ul>
      <li><a href="#!/">Home</a></li>
      <li><a href="#!/cats">cats</a></li>
    </ul>
  </div>
  <div ng-view></div>
  <script src="/webjars/angularjs/angular.min.js"></script>
  <script src="/webjars/angularjs/angular-route.min.js"></script>
  <script type="text/javascript">
  var app = angular.module('myApp', ['ngRoute']);
	app.controller('RootCtrl', ['$scope', function($scope) {
	  $scope.title = "Home Page";
	}]);
	app.controller('CatsCtrl', ['$scope', function($scope) {
    $scope.title = "Cats Page";
	}]);
	app.config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/', {
        controller : 'RootCtrl',
        template : '<h1>{{title}}</h1>'
      }) //
      .when('/cats', {
        controller : 'CatsCtrl',
        template : '<h1>{{title}}</h1>'
      }) //
      .otherwise({
        redirectTo : '/'
      });
	}]);
  </script>
</body>
</html>
```

```html
<!DOCTYPE html>
<html ng-app="myApp">
<head>
  <meta charset="utf-8">
  <title>ngRoute & ng-view示例</title>
  <style type="text/css">
  div.green {
  	height: 20px;
  	width: 100%;
  	background-color: green;
  }
  div.blue {
  	height: 20px;
  	width: 100%;
  	background-color: blue;
  }
  </style>
</head>
<body>
  <div ng-controller="myCtrl">
    <ul>
      <li><a href="#/a">click a</a></li>
      <li><a href="#/b">click b</a></li>
    </ul>
    <ng-view></ng-view>
  </div>
  <script src="/webjars/angularjs/angular.min.js"></script>
  <script src="/webjars/angularjs/angular-route.min.js"></script>
  <script type="text/javascript">
    angular.module("myApp", ["ngRoute"]) //
    .config(function($routeProvider, $locationProvider) {
      $routeProvider //
      .when('/a', {
        templateUrl: 'a.html',
        controller: 'aCtrl'
      }) //
      .when('/b', {
        template: '<div ng-controller="bCtrl" class="blue">B {{hello}}</div>',
        controller: 'bCtrl',
        resolve: {
          // I will cause a 3 second delay
          delay: function($q, $timeout) {
            var delay = $q.defer();
            $timeout(delay.resolve, 300);
            return delay.promise;
          }
        }
      }) //
      .otherwise({
        redirectTo: '/a'
      });
      $locationProvider.hashPrefix("");
    }) //
    .controller("aCtrl", function($scope) {
      $scope.hello = "hello,a!";
      console.log('A controller');
    }) //
    .controller("bCtrl", function($scope) {
      $scope.hello = "hello,b!";
      console.log('B controller');
    }) //
    .controller("myCtrl", function($scope, $location) {
      $scope.$on("$viewContentLoaded", function() {
        console.log("ng-view content loaded!");
      }); //
      $scope.$on("$routeChangeStart", function(event, next, current) {
        //event.preventDefault(); //cancel url change
        console.log("route change start!");
        console.log("next: " + next);
        console.log("current: " + current);
      }); //
    });
  </script>
</body>
</html>
```

* a.html

```html
<div ng-controller="aCtrl" class="green">A {{hello}}</div>
```

#### `ng-view` & `$route`默认值变更

* The $location service is designed to support hash prefixed URLs
for cases where the browser does not support HTML5 push-state navigation.

* The Google Ajax Crawling Scheme expects that local paths within a SPA start with a hash-bang (e.g. somedomain.com/base/path/#!/client/side/path).

* The $locationProvide allows the application developer to configure the hashPrefix, and it is normal to set this to a bang '!', but the default has always been the empty string ''.

* This has caused some confusion where a user is not aware of this feature and wonders why adding a hash value to the location (e.g. $location.hash('xxx')) results in a double hash: ##xxx.

* This commit changes the default value of the prefix to '!', which is more natural and expected.

* See https://developers.google.com/webmasters/ajax-crawling/docs/getting-started

Closes #13812

BREAKING CHANGE

* The hash-prefix for $location hash-bang URLs has changed from the empty string "" to the bang "!". If your application does not use HTML5 mode or is being run on browsers that do not support HTML5 mode, and you have not specified your own hash-prefix then client side URLs will now contain a "!" prefix. For example, rather than mydomain.com/#/a/b/c will become mydomain/#!/a/b/c.

* If you actually wanted to have no hash-prefix then you should configure this by adding a configuration block to you application:

```
appModule.config(['$locationProvider', function($locationProvider) {
  $locationProvider.hashPrefix("");
}]);
```

* [Reference]()https://github.com/angular/angular.js/pull/14202

#### UI-Route

* [angular-ui.github.io](https://angular-ui.github.io)

### Module

* 模块定义了一个应用程序。
* 模块是应用程序中不同部分的容器。
* 模块是应用控制器的容器。
* 控制器通常属于一个模块。
* 定义一个模块
  - 通过AngularJS的`angular.module`函数来创建模块；
  - `"myApp"`参数对应执行应用的HTML元素。
  - 可以在AngularJS应用中添加控制器，指令，过滤器等。
  - 在模块定义中`[]`参数用于定义模块的依赖关系。
  - 中括号`[]`表示该模块没有依赖，如果有依赖的话会在中括号写上依赖的模块名字。

```html
<div ng-app="myApp">...</div>
<script type="text/javascript">
var app = angular.module("myApp", []); 
</script>
```

### AngularJS全局API

* 常用的如下：

| API | 描述 |
| --- | --- |
| angular.lowercase() | 转换字符串为小写 |
| angular.uppercase() | 转换字符串为大写 |
| angular.isString() | 判断给定的对象是否为字符串，如果是返回 true。 |
| angular.isNumber() | 判断给定的对象是否为数字，如果是返回 true。 |

### Provider

* Provider模式是策略模式和工厂模式的结合体。
* 核心目的是为了让接口和实现分离。
* 在ng中，所有provider都可以用来注入：provider/factory/service/constant/value。
* provider是基础，其它都是调用provider函数实现的，只是参数不同。
* 以下类型的函数可以接受注入：controller/directive/filter/service/factory等。
* ng中的依赖注入是通过provider和injector这两个机制联合实现的。
* 在自定义服务里进行注入，但不能注入$scope作用域对象。

#### 定义Provider

* 定义Provider

```javascript
var app = angular.module('app', []);
app.provider('helloAngular', function() {
  return {
  	$get: function() {
  		var name = 'Greeting!';
  		function getName() {
  			return name;
  		}
  		return {
        getName: getName
  		};
  	}
  };
});
// Controller调用
app.controller('myCtrl', ['$scope', 'helloAngular', function($scope, helloAngular) {
  $scope.name = helloAngular.getName();
}]);
```

* 只有provder是能传`.config()`函数的 service。如果想在service 对象启用之前，先进行模块范围的配置，那就应该选择provider。
  - 需要注意的是：在config函数里注入provider时，名字应该是：providerName+Provider. 
* 使用Provider的优点就是，可以在Provider对象传递到应用程序的其他部分之前在`app.config`函数中对其进行修改。 
* 当使用Provider创建一个service时，唯一的可以在控制器中访问的属性和方法是通过$get()函数返回内容。

```javascript
var app = angular.module('myApp', []);
//需要注意的是：在config函数里注入provider时，名字应该是：`providerName + Provider`   
app.config(function(myProviderProvider){
  myProviderProvider.setName("大圣");       
});
app.provider('myProvider', function() {
  var name = "";
  var test = {"a":1,"b":2};
  //注意的是，setter方法必须是(`set + 变量首字母大写`)格式
  this.setName = function(newName) {
    name = newName  
  }
  this.$get = function($http, $q) {
    return {
      getData : function() {
        var d = $q.defer();
        $http.get("url")//读取数据的函数。
          .success(function(response) {
            d.resolve(response);
          }) //
          .error(function(){
            d.reject("error");
          });
        return d.promise;
      },
      "lastName": name,
      "test": test
    }
  }
});
app.controller('myCtrl', function($scope,myProvider) {
  console.log(myProvider.lastName);
  console.log(myProvider.test.a)
  myProvider.getData().then(function(data) {
    console.log(data);
  }, function(data) {
    console.log(data);
  });
});
```

### Factory

* 在service里面当我们仅仅需要的是一个方法和数据的集合且不需要处理复杂的逻辑的时候，factory()是一个非常不错的选择。
  - 注意：需要使用.config()来配置service的时候不能使用factory()方法。
* angular里的service是一个单例对象，在应用生命周期结束的时候（关闭浏览器）才会被清除。而controllers在不需要的时候就会被销毁了。
* 创建factory，作用就是返回一个有属性有方法的对象。相当于：`var f = myFactory();`

```javascript
//创建模型
var app = angular.module('myApp', []);
//通过工厂模式创建自定义服务
app.factory('myFactory', function() {
  var service = {};//定义一个Object对象'
  service.name = "张三";
  var age;//定义一个私有化的变量
  //对私有属性写getter和setter方法
  service.setAge = function(newAge){
    age = newAge;
  }
  service.getAge = function(){
    return age; 
  }
  return service;//返回这个Object对象
});
//创建控制器
app.controller('myCtrl', ['$scope', 'myFactory', function($scope, myFactory) {
  myFactory.setAge(20);
  $scope.name = myFactory.getAge();
}]);
```

### Service

* Service都是单例的。
* Service由$injector负责实例化。
* Service在整个应用的生命周期中存在，可以用来共享数据。
* 在需要使用的地方利用依赖注入机制注入Service。
* 自定义的Service需要写在内置的Service后面。
* 内置Service的命名以$符号开头，自定义Service应该避免。
* Service、Provider、Factory本质上都是Provider。
* service()方法很适合使用在功能控制比较多的service里面，它是一个可注入的构造器，在AngularJS中它是单例的，用它在Controller中通信或者共享数据都很合适。
  - 在service里面可以不用返回东西，因为AngularJS会调用new关键字来创建对象。但是返回一个自定义对象也不会出错。
  - 注意：需要使用.config()来配置service的时候不能使用service()方法。
* 定义Service：通过service方式创建自定义服务，相当于new的一个对象：`var s = new myService();`，只要把属性和方法添加到this上才可以在controller里调用。

```javascript
var app = angular.module('app', []);
app.service('helloAngular', function() {
  this.name = 'Greeting!';
  this.getName = function() {
  	return this.name;
  }
});
// Controller调用
app.controller('myCtrl', ['$scope', 'helloAngular', function($scope, helloAngular) {
  $scope.name = helloAngular.getName();
}]);
```

#### `$http` 服务

* `$http`是 AngularJS 应用中最常用的服务。 服务向服务器发送请求，应用响应服务器传送过来的数据。

```javascript
var app = angular.module('myApp', []);
app.controller('myCtrl', function($scope, $http) {
  $http.get("welcome.htm").then(function (response) {
    $scope.myWelcome = response.data;
  });
});
```

* 以上是一个非常简单的 $http 服务实例，更多 $http 服务应用请查看[AngularJS Http](http://www.runoob.com/angularjs/angularjs-http.html)教程。

#### `$timeout`服务

* AngularJS`$timeout`服务对应了JS `window.setTimeout`函数。

```javascript
var app = angular.module('myApp', []);
app.controller('myCtrl', function($scope, $timeout) {
  $scope.myHeader = "Hello World!";
  $timeout(function () {
    $scope.myHeader = "How are you today?";
  }, 2000);
});
```

#### `$interval`服务

* AngularJS`$interval`服务对应了JS`window.setInterval`函数。

```javascript
var app = angular.module('myApp', []);
app.controller('myCtrl', function($scope, $interval) {
  $scope.theTime = new Date().toLocaleTimeString();
  $interval(function () {
    $scope.theTime = new Date().toLocaleTimeString();
  }, 1000);
});
```

### `constant` & `value`

* AngularJS可以通过`constant(name, value)`和`value(name, value)`创建服务。
  - 相同点是：都可以接受两个参数，name和value。
  - 不同点是：
    + 1. value不可以在config里注入，但是constant可以。
    + 2. value可以修改，但是constant不可以修改，一般直接用constant配置一些需要经常使用的数据。

#### `constant(name, value)`

* `constant(name, value)`可以将一个已经存在的变量值注册为服务，并将其注入到应用的其他部分中。其中，name为注册的常量的名字，value为注册的常量的值或对象。

```javascript
angular.module('myApp') //
.constant('apiKey', '123123123') //
.controller('myController', function($scope, apiKey) {
  // 可以像上面一样用apiKey作为常量
  // 用123123123作为字符串的值
  $scope.apiKey = apiKey;
});

angular.module('myApp') //
.constant('apiKey', {name:[], age:[], date:[]}) //
.factory('myFactory', function(apiKey, $scope) {
  apiKey.name = "名称测试";
});
```

#### `value(name, value)`

* `value(name, value)`的name同样是需要注册的服务名,value将这个值将作为可以注入的实例返回。

```javascript
angular.module('myApp').value('apiKey', '123123123');
```

#### `constant` & `value`区别

* 最大的区别是：常量`constant`可以注入到配置函数中，而值`value`不行。
* 通常情况下，可以通过`value()`来注册服务对象或函数，用`constant()`来配置数据。

```javascript
angular.module('myApp', []) //
.constant('apiKey', '123123123') //
.config(function(apiKey) {
  // 在这里apiKey将被赋值为123123123
  // 就像上面设置的那样
})
.value('FBid','231231231') //
.config(function(FBid) {
  // 这将抛出一个错误，未知的provider: FBid
  // 因为在config函数内部无法访问这个值
});
```
* 当想要创建一个服务，并且这个服务只需要返回数据时，就可以使用constant(name, value)和value(name,value)，不过，它们有两个显著的区别：
  - 1.value不可以在config里注入，但是constant可以。
  - 2.value可以修改，但是constant不可以修改，一般直接用constant配置一些需要经常使用的数据。

### 使用`$filter`服务

* 定义filter

```javascript
var app = angular.module('app', []);
app.filter('myFormat',['helloAngular', function(helloAngular) {
    return function(x) {
        return helloAngular.getName(x);
    };
}]);
```

* `$filter`是用来进行数据格式的专用服务。
* AngularJS内置了9个filter：
  - currency 货币格式化
  ```javascript
  {{ 250 | currency }}            // 结果：$250.00
  {{ 250 | currency:"RMB ￥ " }}  // 结果：RMB ￥ 250.00
  ```
  - date 格式化
  ```javascript
  {{1490161945000 | date:"yyyy-MM-dd HH:mm:ss"}} // 2017-03-22 13:52:25
  ```
  - filter查找：输入过滤器可以通过一个管道字符（|）和一个过滤器添加到指令中，该过滤器后跟一个冒号和一个模型名称。
  ```javascript
   // 查找name为iphone的行
  {{ [{"age": 20,"id": 10,"name": "iphone"},
      {"age": 12,"id": 11,"name": "sunm xing"},
      {"age": 44,"id": 12,"name": "test abc"}] | filter:{'name':'iphone'} }}  
  ```
  - json 格式化json对象
  ```javascript
  {{ jsonTest | json}} //作用与JSON.stringify()一样
  ```
  - limitTo截取
  ```javascript
  {{"1234567890" | limitTo :6}} // 从前面开始截取6位
  {{"1234567890" | limitTo:-4}} // 从后面开始截取4位
  ```
  - lowercase
  ```javascript
  {{ "TANK is GOOD" | lowercase }}      // 结果：tank is good
  ```
  - uppercase
  ```javascript
  {{ "lower cap string" | uppercase }}   // 结果：LOWER CAP STRING
  ```
  - number
  ```javascript
  {{149016.1945000 | number:2}} // 格式化（保留小数）
  ```
  - orderBy
  ```javascript
  // 根id降序排
  {{ [{"age": 20,"id": 10,"name": "iphone"},
      {"age": 12,"id": 11,"name": "sunm xing"},
      {"age": 44,"id": 12,"name": "test abc"}] | orderBy:'id':true }}
  // 根据id升序排
  {{ [{"age": 20,"id": 10,"name": "iphone"},
      {"age": 12,"id": 11,"name": "sunm xing"},
      {"age": 44,"id": 12,"name": "test abc"}] | orderBy:'id' }}
  ```

* filter可以嵌套使用（用管道符号|分隔）。
  - 在模板中使用filter:可以直接在{{}}中使用filter，跟在表达式后面用 | 分割，语法如下：
  ```
  {{ expression | filter }}
  // 也可以多个filter连用，上一个filter的输出将作为下一个filter的输入
  {{ expression | filter1 | filter2 | ... }}
  // filter可以接收参数，参数用 : 进行分割
  {{ expression | filter:argument1:argument2:... }}
  // 可以在指令中使用filter，例如先对数组array进行过滤处理，然后再循环输出：
  <span ng-repeat="a in array | filter ">
  ```
  - 在controller和service中使用filter
  ```javascript
  // 使用currency过滤器，只需将它注入到该controller中即可
  app.controller('testCtrl', function($scope, currencyFilter) {
    $scope.num = currencyFilter(123534);  
  }
  // 或者注入$filter服务可以来调用所需的filter
  app.controller('testCtrl', function($scope, $filter){
    $scope.num = $filter('currency')(123534);
    $scope.date = $filter('date')(new Date());  
  }
  ```

* filter是可以传递参数的。
  - filter可以接收参数，参数用`:`进行分割。
  ```javascript
  {{149016.1945000 | number:2}} // 格式化（保留小数）
  ```

* 用户可以定义自己的filter:使用module的filter方法，返回一个函数，该函数接收输入值，并返回处理后的结果。

```javascript
app.filter('odditems', function() {
  return function(inputArray) {
    var array = [];
    for(var i = 0;i < inputArray.length; i++){
      if (i % 2 !== 0) {
        array.push(inputArray[i]);
      }
    }
    return array;
  }
});
```

### Angualr进阶

* angular对象上的静态工具方法

```javascript
/**
 * angular对象上的静态工具方法
 * @type {number}
 */
var counter = 0;
for (var p in angular) {
	counter++;
	if (angular.isFunction(angular[p])) {
		console.log('Function->' + p);
	} else {
		console.log('Property-->' + p + '-->' + angular[p]);
	}
}
console.log(counter);
```

```javascript
var injector = angular.injector();
console.log(injector);

var module = angular.module('app', []);
console.log(module);
```

#### 自动启动

* 引入AngularJS文件。
* 在元素上有一个ng-app指令。

#### 手动启动

* 引入AngularJS文件。
* 文档中无`ng-app`指令。
* 手动调用代码`angular.bootstrap(document, ['app']);`执行启动。

```html
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>手动启动AngularJS测试</title>
  <style type="text/css">
  .panel {
    margin-bottom:20px;
    border: 1px solid #eee;
    padding: 10px;
  }
  .panel input {
    height: 20px;
    padding: 5px;
  }
  .panel button {
    height: 30px;
  }
  .panel .item {
    margin: 5px;
    padding: 5px;
  }
  </style>
</head>
<body>
  <div class="panel" ng-controller="hiCtrl">
    <div class="item">
      名称：<input type="input" ng-model="name">
    </div>
    <div class="item">
      显示：{{name}}
    </div>
  </div>
  <script src="/webjars/angularjs/angular.min.js"></script>
  <script type="text/javascript">
    var app = angular.module("app", []);
    app.controller('hiCtrl', ['$scope', function ($scope) {
    	$scope.name = 'AngularJS start up.';
      $scope.$watch('name', function(newName, oldName) {
        var val = '新名称：' + newName + '，旧名称：' + oldName;
        console.log(val);
      });
    }]);
    // 要使用document加载完成后再调用。
    angular.element(document).ready(function() {
      setTimeout(function() {
        angular.bootstrap(document, ['app']);// 手工启动。
      }, 2000); // 2s后再加载。
    });
  </script>
</body>
</html>
```

#### 多个ng-app

* 如果一个文档中写了多个`ng-app`，则只会自动启动第一个angular。
* 其它的可以手动启动。

#### 绑定jQuery

#### 全局angular(injector方法)

##### 参数注入

* 推断型注入：根据参数名称`$scope`推断，注入`$scope`，函数的参数名称必须要与被注入的对象相同。
  - 可通过`ctrl.$inject = ['$scope']`的方式声明注入。

```javascript
var app = angular.module('app', []);

var ctrl = function($scope) {
  $scope.name = 'Hello world!';
};
app.controller('ctrl', ctrl);
```

* 标注式注入：通过`$inject`的方式注入

```javascript
var app = angular.module('app', []);

var ctrl = function($scope) {
  $scope.name = 'Hello world!';
};
ctrl.$inject = ['$scope'];//如果需要混淆js，可声明注入。
app.controller('ctrl', ctrl);
```

* 内联式注入：在定义时即声明

```javascript
var app = angular.module('app', []);
app.controller('ctrl', ['$scope', function($scope) {
  $scope.name = 'Hello world!';
}]);
```

##### 注入调用

* 使用`$injector`注入时调用服务

```html
<!DOCTYPE html>
<html ng-app="app">
<head>
  <meta charset="utf-8">
  <title>注入调用测试</title>
  <style type="text/css">
  .panel {
    margin-bottom:20px;
    border: 1px solid #eee;
    padding: 10px;
  }
  .panel input {
    height: 20px;
    padding: 5px;
  }
  .panel button {
    height: 30px;
  }
  .panel .item {
    margin: 5px;
    padding: 5px;
  }
  </style>
</head>
<body>
  <div class="panel" ng-controller="hiCtrl">
    <div class="item">
      名称：<input type="input" ng-model="name">
    </div>
    <div class="item">
      显示：{{name}}
    </div>
  </div>
  <script src="/webjars/angularjs/angular.min.js"></script>
  <script type="text/javascript">
  var app = angular.module('app', []);
  app.factory('hello', function() {
    return {
      name: 'Hello World!'
    };
  });
	app.controller('hiCtrl', ['$scope', '$injector', function($scope, $injector) {
	  console.log($scope);
	  console.log($injector);
	  $injector.invoke(function(hello) {// 直接调用服务。
	    console.log(hello.name);
	    $scope.name = hello.name
	  });
	  // 可以使用$injector获取服务
	  // $injector.get('serviceName');
	  // 可以使用$injector判断属性
	  // $injector.has('property');
	}]);
  </script>
</body>
</html>
```

* 使用`$injector`的方法获取函数参数名称

```javascript
$injector.annotate(function(arg0, arg1){});
// will output: ["arg0", "arg1"]
```

* 以下的方式是等效的

```javascript
var $injector = angular.injector();
expect($injector.get('$injector').toBe($injector));
expect($injector.invoke(function($injector) {
	return $injector;
})).toBe($injector);
```

具体可以参考官方关于`angular.injector`的说明。

### AngularJS第三方组件

[Kissy Gallery]，[angular-ui]

* 知道了一个东西的优点，你只是入门了。
* 理解了一个东西的缺点，说明你精通了。
