app.controller("fileListController", ['$scope', '$http', '$filter', function($scope, $http, $filter) {
  $scope.items = [];
  $scope.path = '';
  $scope.showTable = ($scope.items.length != 0);
  $scope.list = function() {
	$scope.path = $scope.path.replace(/\\/g, '/');
    $http.get('/md/file/list?_=' + new Date().getTime() + '&path=' + $scope.path) //
    .then(function(response) {
      $scope.items = response.data;
      $scope.showTable = ($scope.items.length != 0);
    }, function() {
      console.log('Request Error.');
    });
  }
  $scope.sort = function(name) {
    if (name == 'name') {
      $scope.sortName = $scope.sortName == '-alt' ? '' : '-alt';
    } else if (name = 'path') {
      $scope.sortPath = $scope.sortPath == '-alt' ? '' : '-alt';
    }
    $scope.items = $filter('orderBy')($scope.items, name, $scope.reverse);
    $scope.reverse = !$scope.reverse;
  }
}]);
app.controller("toolController", ['$scope', function($scope) {
  $scope.name = 'Hello World!';
}]);
app.controller("appController", ['$scope', function($scope) {
  $scope.name = 'Hello World!';
}]);
