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
  .when('/md/file/search', {
    templateUrl: 'assets/index/tpl/md-file-search.html',
    controller: 'fileListController'
  }) //
  .otherwise({
    redirectTo: '/tool'
  });
  $locationProvider.hashPrefix("");
});