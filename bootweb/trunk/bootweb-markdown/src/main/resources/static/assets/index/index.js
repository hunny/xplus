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
    replace: true,
    controller: ['$scope', '$location', function($scope, $location) {
      $scope.items = [
        [
          {href: 'tool', name: '命令行创建工程', active: false},
          {href: 'tool2', name: 'tool2', active: false},
          {href: 'tool3', name: 'tool3', active: false}
        ],[
          {href: 'tool', name: '命令行创建工程', active: false},
          {href: 'tool2', name: 'tool2', active: false},
          {href: 'tool3', name: 'tool3', active: false}
        ]
      ];
      $scope.myclick = function() {
        var length = $scope.items.length;
        for (var i = 0; i < length; i++) {
          var mItem = $scope.items[i];
          for (var n = 0; n < mItem.length; n++) {
            mItem[n].active = false;
          }
        }
        this.item.active = true;
      };
      $scope.$on("$viewContentLoaded", function() {
        console.log("ng-view content loaded!" + JSON.stringify($location.path()));
        var length = $scope.items.length;
        for (var i = 0; i < length; i++) {
          var mItem = $scope.items[i];
          var found = false;
          for (var n = 0; n < mItem.length; n++) {
            if (('/' + mItem[n].href) == $location.path()) {
              mItem[n].active = true;
              found = true;
              break;
            }
          }
          if (found) {
            break;
          }
        }
      }); //
      $scope.$on("$routeChangeStart", function(event, next, current) {
        //event.preventDefault(); //cancel url change
        console.log("route change start!");
        console.log("next: " + JSON.stringify(next));
        console.log("current: " + current);
      }); //
    }]
  };
});
app.controller("toolController", ['$scope', function($scope) {
  $scope.name = 'Hello World!';
}]);
app.controller("appController", ['$scope', function($scope) {
  $scope.name = 'Hello World!';
}]);