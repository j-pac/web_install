
$(document).ready(function () {
    $(".locale_select").change(function(){
        var target = $('#locale_select :selected').val();
        if(target) window.location = target;
    });
    
    $("#apk_locale").change(function(){
        $("#account-form").submit();
    });
});

