angular.module("myApp.ctrl.LoginController", []) //
.controller("LoginController", ['$scope', '$location', 
  function($scope, $location) {
    $scope.user = {
      username: "user",
      password: "password"
    };
	  $scope.myLogin = function() {
	  	$location.path('/home');
	  };
    $scope.myReset = function() {
      $scope.user = {
        username: "",
        password: ""
      };
    };
  }//
]);//