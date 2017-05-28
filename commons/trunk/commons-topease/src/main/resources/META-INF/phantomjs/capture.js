var system = require('system')
var address = system.args[1]; // url
var name = system.args[2]; // screen png
var cookie = system.args[3];// cookies
var page = require('webpage').create();
page.settings.userAgent = 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36';
page.onError = function(msg, trace) {
	var msgStack = ['ERROR: ' + msg];
	if (trace && trace.length) {
		msgStack.push('TRACE:');
		trace.forEach(function(t) {
			msgStack.push(' -> ' + t.file + ': ' + t.line + (t.function ? ' (in function "' + t.function +'")' : ''));
		});
	}
	console.error(msgStack.join('\n'));
};
page.viewportSize = {
	width: 1024,
	height: 800
};
if (undefined !== cookie) {
	try {
		var cookieArray = eval(cookie);
		for (var i = 0; i < cookieArray.length; i++) {
			var c = cookieArray[i];
			phantom.addCookie({
				'name'     : c.name, 
				'value'    : c.value, 
				'domain'   : c.domain,
				'path'     : c.path, 
				'httponly' : c.httponly,
				'secure'   : c.secure,
				'expires'  : c.expires
			});
		}
	} catch (e) {
		console.log(e.message);
	}
}
page.open(address, function(status) {
	if (status !== 'success') {
		console.log('Unable to request ' + url);
		phantom.exit();
	} else {
		console.log(cookie);
		window.setTimeout(function() {
			page.render(name); // 截图
			phantom.exit();
		}, 2000);
	}
});
