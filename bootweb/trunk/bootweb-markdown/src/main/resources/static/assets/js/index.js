$(document).ready(function() {
	$('a.openmdurl').on('click', function() {
		var val = $('input[name="markdown"]').val();
		if (val == '') {
			return;
		}
		window.open('/render?name=' + encodeURIComponent(val), '_blank');
	});
	$(":file").filestyle({
		input : false,
		htmlIcon : '<span class="oi oi-random"></span>',
		text: "Find file",
		btnClass: "btn-primary"
	});
});