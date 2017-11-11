# AngularJS简介

AngularJS 是一个 JavaScript 框架。AngularJS 通过 指令 扩展了 HTML，且通过 表达式 绑定数据到 HTML。

## 快速上手

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

| 表格 | 说明 |
| --- | --- |
|09 | OK |
|01 | PK |

