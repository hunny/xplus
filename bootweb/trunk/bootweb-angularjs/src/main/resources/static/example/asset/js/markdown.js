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
          var test = angular.element('<a href="/example/asset/show.html?html=' + encodeURIComponent(this.innerHTML) + '" target="_blank">测试一下</a>');
          angular.element(this).parent().after(test);
          //.insertAfter("<div>测试insertAfter</div>");
          Prism.highlightElement(this, true);// Prism framework to highlight element.
        });
      }, function error(response) {
        console.log('请求失败');
      });
    }
  };
}]);