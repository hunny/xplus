$(document).ready(function() {
	$('a.openmdurl').on('click', function() {
		var val = $('input[name="markdown"]').val();
		if (val == '') {
			return;
		}
		window.open('/render?name=' + val, '_blank');
	});
});