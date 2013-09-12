/**
 * Aptoide Read More - Make expandable with "more" link
 * @date 10 February 2012
 * @author Fábio Teixeira
 * @version 2.0
 */
$(document).ready(function() {
    $(".aplicacao_descricao_with_truncate").readmore();
});

(function($){
    $.fn.readmore = function(options) {
        var divHeight  = $(this).height();
        var divContent = $(this);

        var defaults = {
            height: 225,
            moreText: " More[+]",
            lessText: " Less[-]",
            link: {
                href: "#", 
                'class': "truncate_more_link"
            },
            moreAni:'slow',
            lessAni: 'slow'
        };
        
        var divEventsAttr = {id: "div_truncate_events"};

        options = $.extend(defaults, options);
        return divContent.each(function() {
            if(divHeight > options.height) {
                
                    /* Creating Elments and adding to containers */
                    var divEvents = createElement("div", divEventsAttr); 
                    var linkEvent = createElement("a", options.link);
                    
                    $(linkEvent).text(options.moreText);
                    
                    $(divEvents).append(linkEvent);
                    
                    divContent.after(divEvents);
                    /*END*/
                    
                    /*Truncate text*/
                    divContent.css({height: options.height, overflow: 'hidden'});
                    /*END*/

                    // Set onclick event for more/less link
                    var moreLink = $("." + options.link['class']);
                    moreLink.click(function() {
                        var curHeight = divContent.height();
                        if(curHeight < divHeight) {
                            divContent.animate({height: divHeight}, options.moreAni);
                            $(linkEvent).text(options.lessText);
                        } else {
                            divContent.animate({height: options.height}, options.lessAni);
                            $(linkEvent).text(options.moreText);
                        }
                        return false;
                    });
            } // end if

        });
    };
})(jQuery);

function createElement(element, attr){
    var obj = document.createElement(element);
    $(obj).attr(attr);
    return obj;
}