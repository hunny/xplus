var app = angular.module('app', []);
app.directive('navigate', function() {
	return {
		restrict: 'E',
		templateUrl: '/example/asset/template/navigate.html',
		replace: true
	};
});
app.directive('leftmenu', function() {
  return {
    restrict: 'E',
    templateUrl: '/example/asset/template/leftmenu.html',
    replace: true
  };
});
app.directive('contentup', function() {
  return {
    restrict: 'E',
    templateUrl: '/example/asset/template/contentup.html',
    replace: true
  };
});
app.directive('paneltable', function() {
  return {
    restrict: 'E',
    scope: {
    },
    templateUrl: '/example/asset/template/paneltable.html',
    replace: true,
    link: function(scope, element, attr) {
      scope.title = attr.title;
    }
  };
});