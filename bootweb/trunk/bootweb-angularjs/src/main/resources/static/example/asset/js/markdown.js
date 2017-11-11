var markdown = angular.module('markdown', []);

markdown.controller('indexCtrl', ['$scope', function($scope) {
  $scope.http = {
    url: '/md/text?_=' + new Date().getTime()
  };
}]);

markdown.directive('render', ['$http', function($http) {
  return {
    restrict: 'E',
    scope: {
      src: '='
    },
    template: '<article class="markdown-body"></article>',
    replace: true,
    link: function(scope, element, attrs) {
      $http({
        method: 'GET',
        url: scope.src
      }).then(function success(response) {
        element.html(response.data.html);
        var codes = element.find('code[class*="language-"]');
        codes.each(function() {
          Prism.highlightElement(this, true);// Prism framework to highlight element.
        });
      }, function error(response) {
        console.log('请求失败');
      });
    }
  };
}]);