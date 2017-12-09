angular.module('JWTDemoApp')
// Creating the Angular Controller
.controller('UsersController', function($http, $scope, AuthService) {
	var edit = false;
	$scope.buttonText = 'Create';
	var init = function() {
		$http.get('api/users').then(function(response) {
		  var res = response.data;
			$scope.users = res;
			
			$scope.userForm.$setPristine();
			$scope.message='';
			$scope.appUser = null;
			$scope.buttonText = 'Create';
			
		}, function(response) {
		  var error = response.data;
			$scope.message = error.message;
		});
	};
	$scope.initEdit = function(appUser) {
		edit = true;
		$scope.appUser = appUser;
		$scope.message='';
		$scope.buttonText = 'Update';
	};
	$scope.initAddUser = function() {
		edit = false;
		$scope.appUser = null;
		$scope.userForm.$setPristine();
		$scope.message='';
		$scope.buttonText = 'Create';
	};
	$scope.deleteUser = function(appUser) {
		$http.delete('api/users/'+appUser.id).then(function(res) {
			$scope.deleteMessage ="Success!";
			init();
		}, function(response) {
		  var error = response.data;
			$scope.deleteMessage = error.message;
		});
	};
	var editUser = function(){
		$http.put('api/users', $scope.appUser).then(function(res) {
			$scope.appUser = null;
			$scope.confirmPassword = null;
			$scope.userForm.$setPristine();
			$scope.message = "Editting Success";
			init();
		}, function(error) {
			$scope.message =error.message;
		});
	};
	var addUser = function(){
		$http.post('api/users', $scope.appUser).then(function(res) {
			$scope.appUser = null;
			$scope.confirmPassword = null;
			$scope.userForm.$setPristine();
			$scope.message = "User Created";
			init();
		}, function(error) {
			$scope.message = error.message;
		});
	};
	$scope.submit = function() {
		if(edit){
			editUser();
		}else{
			addUser();	
		}
	};
	init();

});
