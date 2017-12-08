angular.module("myApp", [ "ngRoute", //
"myApp.ctrl.LoginController", //
"myApp.ctrl.HomeController" //
]) //
.config(['$routeProvider', '$locationProvider', '$httpProvider', function($routeProvider, $locationProvider, $httpProvider) {//
	$routeProvider //
	.when('/login', { //
		templateUrl : 'index/login.html', // 
		controller : 'LoginController' // 
	}) //
	.when('/home', { // 
		templateUrl : 'index/home.html', // 
		controller : 'HomeController', // 
		resolve : { // 
			// I will cause a 3 second delay
			delay : function($q, $timeout) { // 
				var delay = $q.defer(); // 
				$timeout(delay.resolve, 300); // 
				return delay.promise; // 
			} // 
		}
	}) //
	.otherwise({ // 
		redirectTo : '/login' // 
	}); // 
	$locationProvider.hashPrefix(""); //
	$httpProvider.interceptors.push('HeaderInterceptor');
}]) //
.factory('HeaderInterceptor', ['$q', '$location', function($q, $location) {
	return {
		// Send the Authorization header with each request
		'request' : function(config) {
			config.headers = config.headers || {};
			config.headers.Authorization = 'Basic 123';
			return config;
		},
    'response' : function(response) {
      console.log('HeaderInterceptor response:');
      console.log(response);
      return response;
    },
    'responseError': function (response) {
      console.log('HeaderInterceptor responseError:');
      console.log(response);
      if (response.status === 401) {
      	  $location.path('/login');
      }
    	return $q.reject(response);//忽略请求向成功的地方传播
    }
	};
} ]); //
