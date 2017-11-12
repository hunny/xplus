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
          if (angular.element(this).hasClass('language-html')) {
            var test = angular.element('<a href="/example/asset/show.html?html=' //
                    + encodeURIComponent(this.innerHTML) //
                    + '" target="_blank">测试一下</a>');
            angular.element(this).parent().after(test);
          }
          Prism.highlightElement(this, true);// Prism framework to highlight element.
        });
        angular.element('h2, h3, h4') //
          .css({'cursor': 'pointer'}) //
          .on('click', function() {
          var elem = angular.element(this);
          var tagName = this.tagName;
          var all = elem.nextAll();
          var length = all.length;
          for (var i = 0; i < length; i++) {
            var nelem = all.get(i);
            if (nelem.tagName.toUpperCase() == tagName.toUpperCase()) {
              break;
            }
            $(nelem).toggle();
          }
        });
      }, function error(response) {
        console.log('请求失败');
      });
    }
  };
}]);