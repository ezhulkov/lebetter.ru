//FancyLogin 1.0 jQuery Plugin
//Author: Patrick Rodgers
//URL: http://www.tgpo.org
(function(jQuery)
{

  jQuery.fn.fancylogin = function() {
  	jQuery(this).each(function (i) {
  	var input = jQuery(this).find("input");

  			if ( input.val() == "" )
  				input.addClass("blank");

  			input.focus(function () {
  				input.removeClass("blank");
  			});

  			input.blur(function () {

  				input.each(function (i) {
  					if ( !jQuery(this).val() == "" ) {
  						jQuery(this).removeClass("blank");
  					}
  					else {
  						jQuery(this).addClass("blank");
  					}
  				});
  			});

  		});
  };

})(jQuery);