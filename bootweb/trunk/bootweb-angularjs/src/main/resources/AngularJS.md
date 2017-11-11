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

### 指令系统

### 双向数据绑定

<div>测试</div>

```javascript
var app = angular.module('app', []);
app.controller('HelloWorld', ['$scope', 
	function($scope) {
		$scope.data = {
			name : 'Hello World!'
		};
	} 
]);
app.directive('hello', function() {
	return {
		restrict: 'E',
		template: '<div>Hi, AngujarJS directive demo!</div>',
		replace: true
	};
});
```

```html
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="github-markdown.css">
<style>
	.markdown-body {
		box-sizing: border-box;
		min-width: 200px;
		max-width: 980px;
		margin: 0 auto;
		padding: 45px;
	}

	@media (max-width: 767px) {
		.markdown-body {
			padding: 15px;
		}
	}
</style>
<article class="markdown-body">
	<h1>Unicorns</h1>
	<p>All the things</p>
</article>
```

```json
{
 ok : 12,
 name: "中国人"
}
```

| 表格 | 说明 |
| --- | --- |
|09 | OK |
|01 | PK |

