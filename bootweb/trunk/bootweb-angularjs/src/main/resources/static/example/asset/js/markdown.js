var markdown = angular.module('markdown', []);
markdown.config(['$locationProvider', function($locationProvider) {
  $locationProvider.html5Mode({
    enabled: true,
    requireBase: false
  });
}]);
markdown.controller('indexCtrl', ['$scope', '$location', function($scope, $location) {
  var file = $location.search().filePath;
  if (file) {
    file = '&filePath=' + file;
  } else {
    file = '';
  }
  $scope.http = {
    url: '/md/text?_=' + new Date().getTime() + file
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
        angular.element('h1, h2, h3, h4') //
          .css({'cursor': 'pointer'}).on('click', function() {
          var elem = angular.element(this);
          var tagName = this.tagName.toUpperCase();
          if (tagName == 'H1') {
            elem.nextAll().show();
            return;
          } else {
            var hn = tagName.replace(/^H/, '');
            var all = elem.nextAll();
            var length = all.length;
            for (var i = 0; i < length; i++) {
              var nelem = all.get(i);
              var ntag = nelem.tagName.toUpperCase();
              var toggle = true;
              if (/H\d+/.test(ntag)) {
                for (var n = parseInt(hn); n >= 1; n--) {
                  if (ntag == ('H' + n)) {
                    toggle = false;
                    break;
                  }
                }
                if (!toggle) {
                  break;
                }
              }
              $(nelem).toggle();
            }
          }
        });
      }, function error(response) {
        console.log('请求失败');
      });
    }
  };
}]);