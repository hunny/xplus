angular.module("myApp", ["ngRoute", //
  "myApp.ctrl.LoginController", //
  "myApp.ctrl.HomeController" //
]) //
.config(function($routeProvider, $locationProvider) {//
  $routeProvider //
  .when('/login', { //
    templateUrl: 'index/login.html', // 
    controller: 'LoginController' // 
  }) //
  .when('/home', { // 
	  templateUrl: 'index/home.html', // 
    controller: 'HomeController', // 
    resolve: { // 
      // I will cause a 3 second delay
      delay: function($q, $timeout) { // 
        var delay = $q.defer(); // 
        $timeout(delay.resolve, 300); // 
        return delay.promise; // 
      } // 
    } // 
  }) //
  .otherwise({ // 
    redirectTo: '/login' // 
  }); // 
  $locationProvider.hashPrefix(""); //
}); //
