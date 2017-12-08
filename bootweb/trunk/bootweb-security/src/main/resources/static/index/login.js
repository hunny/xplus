angular.module("myApp.ctrl.LoginController", []) //
.factory('LoginService', [ '$http', '$q', function($http, $q) {
  var factory = {
    login : login
  };
  return factory;
  
  function login(user) {
    var deferred = $q.defer();
    $http({
      method : 'POST',
      url: '/login',
      //data需要以字符串的形式定义，不要定义成对象
      data:'username=' + user.username + '&password=' + user.password,
      headers: {
        //发送表单数据，默认发的是json，spring security的登录不处理
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    }).then(function success(response) {
      console.log(response);
      deferred.resolve(response.data);
    }, function error(response) {
      deferred.reject(response);
    });
    return deferred.promise;
  }

} ])//
.controller("LoginController", ['$scope', '$location', 'LoginService', function($scope, $location, loginService) {
    $scope.user = {
      username: "user",
      password: "password"
    };
	  $scope.myLogin = function() {
      console.log('登录请求。');
	    console.log($scope.user);
	    loginService.login($scope.user) //
	    .then(function(data) {
	      console.log('登录成功。');
        $location.path('/home');
	    }, function() {
	      console.log('请求失败');
	    });
	  }
    $scope.myReset = function() {
      $scope.user = {
        username: "",
        password: ""
      };
    }
  }//
]);//