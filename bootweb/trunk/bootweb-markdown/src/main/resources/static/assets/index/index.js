var app = angular.module('app', ["ngRoute"]);
app.config(function($routeProvider, $locationProvider) {
  $routeProvider //
  .when('/tool', {
    templateUrl: 'assets/index/tpl/tool.html',
    controller: 'toolController'
  }) //
  .when('/tool2', {
    templateUrl: 'assets/index/tpl/tool2.html',
    controller: 'toolController'
  }) //
  .when('/tool3', {
    templateUrl: 'assets/index/tpl/tool3.html',
    controller: 'toolController'
  }) //
  .otherwise({
    redirectTo: '/tool'
  });
  $locationProvider.hashPrefix("");
});
app.directive('navView', function() {
  return {
    restrict: 'E',
    templateUrl: 'assets/index/tpl/nav-view.html',
    replace: true
  };
});
app.directive('leftMenu', function() {
  return {
    restrict: 'E',
    templateUrl: 'assets/index/tpl/left-menu.html',
    replace: true
  };
});
app.controller("toolController", ['$scope', function($scope) {
  $scope.name = 'Hello World!';
}]);
app.controller("appController", ['$scope', function($scope) {
  $scope.name = 'Hello World!';
}]);