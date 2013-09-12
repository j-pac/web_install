$(document).ready(function(){

	$('#profiler thead tr:gt(0), #profiler tbody').hide();

	$('#profiler').click(function() {
		if ($(this).hasClass('profilerVisible')) {
			$(this).removeClass('profilerVisible');
			$(this).find('thead tr:gt(0)').stop(true,true).fadeOut();
			$(this).find('tbody').stop(true,true).fadeOut();
		} else {
			$(this).addClass('profilerVisible');
			$(this).find('thead tr:gt(0)').stop(true,true).fadeIn();
			$(this).find('tbody').stop(true,true).fadeIn();
		}
	});

});
