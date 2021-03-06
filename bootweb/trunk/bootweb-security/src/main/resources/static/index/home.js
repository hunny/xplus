angular.module("myApp.ctrl.HomeController", []) //
.factory('HomeService', [ '$http', '$q', function($http, $q) {
	var factory = {
		getAbout : httpAbout,
		getAboutById : httpAboutById
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
	
	function httpAboutById(id) {
		var deferred = $q.defer();
		$http.get('/about/' + id).then(function (data) {
			deferred.resolve(data);
		}, function (response) {
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
	function logout() {
	  var deferred = $q.defer();
	  $http.post('/logout')
	  .then(function success(response) {
	    console.log(response);
	    deferred.resolve(response.data);
	  }, function error(response) {
	    deferred.reject(response);
	  });
	  return deferred.promise;
	}
	$scope.logout = function() {
	  logout().then(function(response) {
	    console.log('logout ok.');
	    console.log(response);
	  }, function() {
      console.log('logout error.');
	  });
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
	$scope.check = function() {
	  if ($scope.status == '' //
	    || $scope.status == null
	    || $scope.status == undefined) {
	    $scope.status = 200;
	  }
		homeService.getAboutById($scope.status) //
		.then(function(data) {
			console.log('请求成功');
			console.log(data);
		}, function(data) {
			console.log('请求失败');
			console.log(data);
		});
	}
} ]);//