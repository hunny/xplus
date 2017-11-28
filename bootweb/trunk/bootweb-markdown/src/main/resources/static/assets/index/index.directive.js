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
    controller: ['$scope', '$location', '$http', function($scope, $location, $http) {
      $scope.items = [];
      $http({
        method: 'GET',
        url: '/menu/list?_=' + new Date().getTime()
      }).then(function success(response) {
        $scope.items = response.data;
        $scope.defaultActive();
      }, function error(response) {
        console.log('请求失败');
      });
      $scope.myclick = function() {
        var length = $scope.items.length;
        for (var i = 0; i < length; i++) {
          var mItem = $scope.items[i];
          for (var n = 0; n < mItem.length; n++) {
            mItem[n].active = false;
          }
        }
        this.item.active = true;
      }
      $scope.defaultActive = function() {
        var length = $scope.items.length;
        for (var i = 0; i < length; i++) {
          var mItem = $scope.items[i];
          var found = false;
          for (var n = 0; n < mItem.length; n++) {
            if (('/' + mItem[n].href) == $scope.location.path) {
              mItem[n].active = true;
              found = true;
              break;
            }
          }
          if (found) {
            break;
          }
        }
      }
      $scope.$on("$viewContentLoaded", function() {
        console.log("ng-view content loaded!" + JSON.stringify($location.path()));
        $scope.location = {
           path: $location.path()
        };
      }); //
      $scope.$on("$routeChangeStart", function(event, next, current) {
        // event.preventDefault(); //cancel url change
        // console.log("next: " + JSON.stringify(next));
        // console.log("current: " + current);
      });
    }]
  };
});