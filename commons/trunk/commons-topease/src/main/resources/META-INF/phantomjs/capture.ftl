var page = require('webpage').create();
page.open('${url}', function() {
  page.render('${path}');
  phantom.exit();
});