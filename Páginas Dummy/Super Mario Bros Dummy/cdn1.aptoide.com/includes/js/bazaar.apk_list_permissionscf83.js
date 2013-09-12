$(document).ready(function(){
    $(".list-permissions").hide();
    $(".show-hide-permissions").show("fast");
        
    $('.show-hide-permissions').click(function(){
        $(".list-permissions").toggle("fast");
    });

});

