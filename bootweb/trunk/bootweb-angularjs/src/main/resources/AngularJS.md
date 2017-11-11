# AngularJS简介

AngularJS 是一个 JavaScript 框架。AngularJS 通过 指令 扩展了 HTML，且通过 表达式 绑定数据到 HTML。

## 快速上手

```
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

