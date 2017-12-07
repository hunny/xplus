angular.module("myApp.ctrl.HomeController", []) //
.controller("HomeController", ['$scope', '$location', function($scope, $location) {
  $scope.hello = "Hello World!";
  $scope.back = function() {
  	$location.path('/login');
  }
}]);//