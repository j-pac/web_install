
var categories, categoriesListing, userRepos, userReposListing;
var appsMore, appsMoreListing, userReposHelp, userReposListingHelp;
var categoriesHelp, categoriesListingHelp;
var abcRepos, appsRepos, downloadsRepos;
var abcReposListing, appsReposListing, downloadsReposListing;
var abcNMoreListing, abcNMore, appsNMoreListing, appsNMore, downloadsNMoreListing, downloadsNMore;

$(document).ready(function(){

    // Dom mapping
    categories = $("#categories");
    categoriesListing = $("#categoriesListing");
    userRepos = $("#userRepos");
    userReposListing = $("#userReposListing");
    appsMore = $("#appsMore");
    appsMoreListing = $("#appsMoreListing");
    userReposHelp = $("#userReposHelp");
    userReposListingHelp = $("#userReposListingHelp");
    categoriesHelp = $("#categoriesHelp");
    categoriesListingHelp = $("#categoriesListingHelp");
    

    // Hide User Repos and other stuff
    chosenOrderBy = 0;
    userReposListing.hide();
    appsMoreListing.hide();
    categoriesListingHelp.hide();
    $("#userReposLoader").hide();

    // Events
    categoriesHelp.click(function() {
    	resetUserReposListing();
        userReposListing.slideUp('fast');
        appsMoreListing.slideUp('fast');
        categoriesListing.slideDown('fast');
        userReposListingHelp.slideDown('fast');
        categoriesListingHelp.slideUp('fast');
        $(this).blur();
        return false;
    });

    userRepos.click(function() {
    	if (typeof abcNMoreListing != "undefined")
    		resetUserReposListing();
    	else
    		$("#userReposLoader").show();
    	
    	categoriesListing.slideUp('fast');
    	categoriesListingHelp.slideDown('fast');
    	userReposListing.slideDown('fast');
        userReposListingHelp.slideUp('fast');
        appsMoreListing.hide();
        appsMore.show();
        $(this).blur();
        
        if (typeof abcNMoreListing == "undefined")
        	getAJAX_UserRepositories();
    	
        return false;
    });
    
    appsMore.click(function() {
    	appsMoreListing.slideDown('fast');
    	appsMore.slideUp('fast');
        $(this).blur();
        return false;
    });
    
    userReposHelp.click(function() {
    	userRepos.click();
    });
    
    categoriesHelp.click(function() {
    	categories.click();
    });
    
    
    
    //+ versions
    $("span.app_versions").click(function(e){
    	
    	if (!e) var e = window.event;
	    e.cancelBubble = true;
	    if (e.stopPropagation) 
	    	e.stopPropagation();
    	
    	var elemid = ".dropdown_versions[dropdownID="+$(this).attr('dropdownID')+"]";
    	var fadingOut = $(elemid).is(':visible');
    	$(".dropdown_versions").not(elemid).hide();
    	$(elemid).fadeToggle('fast');
    	if (fadingOut){
    		$(elemid).find(".dropdown_more_container").fadeOut('fast');
    		$(elemid).find(".dropdown_more").fadeIn('fast');
    	}
    	$(".dropdown_more_container").not($(elemid).find(".dropdown_more_container")).hide();
    	$(".dropdown_more").not($(elemid).find(".dropdown_more")).show();
    	
    	var pos = $(this).offset();
    	var posParent = $(this).parent().offset();
    	$(elemid).css({'top': (pos.top + $(this).height()), 'left': (posParent.left + 40)});
    });
    
    $(window).click(function(){
    	$(".dropdown_versions").fadeOut('fast');
    	$(".dropdown_more_container").fadeOut('fast');
    	$(".dropdown_more").fadeIn('fast');
    });
    
    $(".dropdown_versions a").click(function(e){
    	if (!e) var e = window.event;
	    e.cancelBubble = true;
	    if (e.stopPropagation) 
	    	e.stopPropagation();
    });
    
    $(".dropdown_more_container").hide();
    
    $(".dropdown_more").click(function(e){
    	
    	if (!e) var e = window.event;
	    e.cancelBubble = true;
	    if (e.stopPropagation) 
	    	e.stopPropagation();
    	
    	var elemid = ".dropdown_more_container[dropmoreID="+$(this).attr('dropmoreID')+"]";
    	$(elemid).fadeIn('fast');
    	$(this).hide();
    });
});



function hideAJAXUserRepos(){

    // Dom mapping
    abcRepos = $("#abcRepos");
    abcReposListing = $("#abcReposListing");
    appsRepos = $("#appsRepos");
    appsReposListing = $("#appsReposListing");
    downloadsRepos = $("#downloadsRepos");
    downloadsReposListing = $("#downloadsReposListing");
    abcNMore = $("#abcNMore");
    abcNMoreListing = $("#abcNMoreListing");
    appsNMore = $("#appsNMore");
    appsNMoreListing = $("#appsNMoreListing");
    downloadsNMore = $("#downloadsNMore");
    downloadsNMoreListing = $("#downloadsNMoreListing");
    

    // Hide User Repos and other stuff
    chosenOrderBy = 0;
    appsReposListing.hide();
    downloadsReposListing.hide();
    abcNMoreListing.hide();
    appsNMoreListing.hide();
    downloadsNMoreListing.hide();
    abcRepos.css('font-weight', 'bold');

    // Events    
    abcRepos.click(function() {
    	resetUserReposListing();
    	abcRepos.css('font-weight', 'bold');
    	appsRepos.css('font-weight', 'normal');
    	downloadsRepos.css('font-weight', 'normal');
    	abcReposListing.fadeIn('fast');
    	appsReposListing.fadeOut('fast');
    	downloadsReposListing.fadeOut('fast');
        return false;
    });
    
    appsRepos.click(function() {
    	resetUserReposListing();
    	abcRepos.css('font-weight', 'normal');
    	appsRepos.css('font-weight', 'bold');
    	downloadsRepos.css('font-weight', 'normal');
    	abcReposListing.fadeOut('fast');
    	appsReposListing.fadeIn('fast');
    	downloadsReposListing.fadeOut('fast');
        return false;
    });
    
    downloadsRepos.click(function() {
    	resetUserReposListing();
    	abcRepos.css('font-weight', 'normal');
    	appsRepos.css('font-weight', 'normal');
    	downloadsRepos.css('font-weight', 'bold');
    	abcReposListing.fadeOut('fast');
    	appsReposListing.fadeOut('fast');
    	downloadsReposListing.fadeIn('fast');
        return false;
    });
    
    abcNMore.click(function() {
    	abcNMoreListing.slideDown('fast');
    	abcNMore.hide();
        return false;
    });
    
    appsNMore.click(function() {
    	appsNMoreListing.slideDown('fast');
    	appsNMore.hide();
        return false;
    });
    
    downloadsNMore.click(function() {
    	downloadsNMoreListing.slideDown('fast');
    	downloadsNMore.hide();
        return false;
    });
}

function resetUserReposListing()
{
	abcNMoreListing.hide();
    appsNMoreListing.hide();
    downloadsNMoreListing.hide();
    abcNMore.show();
    appsNMore.show();
    downloadsNMore.show();
}
