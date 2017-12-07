angular.module("myApp.ctrl.HomeController", []) //
.factory('HomeService', [ '$http', '$q', function($http, $q) {
	var factory = {
		getAbout : httpAbout
	};
	return factory;
	
	function httpAbout() {
		var deferred = $q.defer();
		$http({
			method : 'GET',
			url : '/about'
		}).then(function success(response) {
			console.log(response);
			deferred.resolve(response.data);
		}, function error(response) {
			deferred.reject(response);
		});
		return deferred.promise;
	}
} ])//
.controller("HomeController", [ '$scope', '$location', '$http', '$q', 'HomeService', //
function($scope, $location, $http, $q, homeService) {
	$scope.hello = "Hello World!";
	$scope.back = function() {
		$location.path('/login');
	}
	function myAbout() {
		var deferred = $q.defer();
		$http({
			method : 'GET',
			url : '/about'
		}).then(function success(response) {
			console.log(response);
			deferred.resolve(response.data);
		}, function error(response) {
			deferred.reject(response);
		});
		return deferred.promise;
	}
	$scope.about = function() {
//		myAbout().then(function(data) {
//			$scope.hello = data.data;
//		}, function() {
//			console.log('请求失败');
//		});
		homeService.getAbout().then(function(data) {
			$scope.hello = data.data;
		}, function() {
			console.log('请求失败');
		});
	}
} ]);//