
$(window).load(function(){
    $('#coda-slider-1').codaSlider({
        dynamicArrows: false,
        dynamicTabs: false,
        slideEaseDuration: 1000, // Velocidade do Slider
        slideEaseFunction: "easeOutCirc" // Efeito do Slider, ver mais em http://www.ndoherty.biz/forums/viewtopic.php?f=4&t=2
    });
});

/* Adding the Like button*/
(function(d, s, id) {
  var js, fjs = d.getElementsByTagName(s)[0];
  if (d.getElementById(id)) return;
    js = d.createElement(s);
    js.id = id;
  js.src = "../../../../../../connect.facebook.net/en_US/all.js#xfbml=1";
  fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));
/*END*/

/* Adding the +1 button*/

  (function() {
    var po = document.createElement('script');
    po.type = 'text/javascript';
    po.async = true;
    po.src = '../../../../../../apis.google.com/js/plusone.js';
    var s = document.getElementsByTagName('script')[0];
    s.parentNode.insertBefore(po, s);
  })();
/*END*/

/* Adding the Tweet Button */
!function (d, s, id) {
    var js, fjs = d.getElementsByTagName(s)[0];
    if (!d.getElementById(id)) {
        js = d.createElement(s);
        js.id = id;
        js.src = "../../../../../../platform.twitter.com/widgets.js";
        fjs.parentNode.insertBefore(js, fjs);
    }
}(document, "script", "twitter-wjs");
/*END*/