$(function() {

	$(".flag_btn").click(function(){
		var flag = $("input[name='flags']:checked").val();
		if(flag!=undefined)
			window.flag(flag);
	});

	window.flag = function(flag) {
		console.log(flag);
		params = {
			data : {
				flag : flag,
				md5sum : window.md5sum
			},
			success : function(data) {
				if (data.errors.length == 0) {
					$(".flag_controller").removeClass("flagged");
					$.each(data.current,function(){
						window.updateChart(this);
						$(".flag_controller."+this.name).removeClass("bigger");
						if(this.is_biggest){
							$(".flag_controller."+this.name).addClass("bigger");
						}
					});
					$(".flag_controller."+data.user_flag).addClass("flagged");
				} else {
					$(".errorMsg").html("");
					$.each(data.errors, function() {
						$(".errorMsg").append("<p>" + this + "</p>");
					});
					$(".errorMsg").show();
				}
			},
			error : function(data) {
				console.log(":(");
			}
		};
		window.doFlagAjax(params);
	};
	
	window.updateChart = function(chart){
		$(".flag_body_text."+chart.name+" span").html(chart.count);
	};


	window.doFlagAjax = function(params) {
		$.ajax({
			url : window.ajaxApkFlags,
			type : "POST",
			data : params.data,
			success : params.success,
			error : params.error
		});
	};
}); 