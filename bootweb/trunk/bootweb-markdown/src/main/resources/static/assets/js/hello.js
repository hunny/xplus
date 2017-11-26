var app = angular.module('app', []);
app.controller('helloWorld', ['$scope', 
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