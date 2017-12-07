'use strict';

angular.module('myApp.login', ['ngRoute','ngResource'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/login', {
    templateUrl: 'login/login.html',
    controller: 'LoginCtrl'
  });
}])

.controller('LoginCtrl', ['$scope', '$http', '$location', function($scope, $http, $location) {
  $scope.user = {
    name:"user",
    password:"password"
  };
  $scope.login = function () {
      $http({
          method: 'POST',
          url: '/login',
          //data需要以字符串的形式定义，不要定义成对象
          data:'username='+$scope.user.name+'&password='+$scope.user.password,
          headers: {
              //发送表单数据，默认发的是json，spring security的登录不处理
              'Content-Type': 'application/x-www-form-urlencoded'
          }
      }).then(function(response) {
          //请求成功回调函数
        console.log('请求成功回调函数');
        console.log(response);
        $location.path('/home');
      }, function(response) {
        console.log('请求失败回调函数');
          //请求失败回调函数
          console.log(response);
      });
  };
}]);