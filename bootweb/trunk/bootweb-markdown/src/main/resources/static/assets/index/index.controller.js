app.controller("fileListController", ['$scope', '$http', function($scope, $http) {
  $scope.items = [];
  $scope.path = '';
  $scope.showTable = ($scope.items.length != 0);
  $scope.list = function() {
    $http.get('/md/file/list?_=' + new Date().getTime() + '&path=' + $scope.path) //
    .then(function(response) {
      $scope.items = response.data;
      $scope.showTable = ($scope.items.length != 0);
    }, function() {
      console.log('Request Error.');
    });
  }
}]);
app.controller("toolController", ['$scope', function($scope) {
  $scope.name = 'Hello World!';
}]);
app.controller("appController", ['$scope', function($scope) {
  $scope.name = 'Hello World!';
}]);
