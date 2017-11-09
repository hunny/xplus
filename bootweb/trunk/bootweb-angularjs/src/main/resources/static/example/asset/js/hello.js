var app = angular.module('app', []);
app.controller('HelloWorld', function($scope) {
	$scope.data = {
		name : 'Hello World!'
	};
});