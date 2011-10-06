AG = {
    colorCell: function(el) {
        jQuery(el).addClass('sel');
    },
    processChange: function() {
        if (jQuery("select#type").val() == 'DICTIONARY') {
            jQuery('#DICT').css('display', 'block');
        } else {
            jQuery('#DICT').css('display', 'none');
        }
    },
    initMainPage: function() {
        /* Start carousel */
        jQuery(function() {
            jQuery(".slider").jCarouselLite({
                                                btnNext: ".next",
                                                btnPrev: ".prev",
                                                visible: 1,
                                                auto: 5000,
                                                speed: 1000
                                            });
        });
        /* Correct for pictures */
        jQuery(".pictures_box ul li:last-child").css("background", "none");
    },
    gridManipulation: function() {

       jQuery("ul.prod_cat_grid2 li:nth-child(5n)").addClass("row");

    },
    menuPlay: function() {

        function megaHoverOver() {
            //jQuery(this).find(".sub").stop().fadeTo('fast', 1).css("display","block");
            jQuery(this).find(".sub").stop().fadeTo(1, 1).show();

            //Calculate width of all ul's
            (function(jQuery) {
                jQuery.fn.calcSubWidth = function() {
                    rowWidth = 0;
                    //Calculate row
                    jQuery(this).find("ul.sub1").each(function() {
                        rowWidth += jQuery(this).width();
                    });
                };
            })(jQuery);

            if (jQuery(this).find(".row").length > 0) { //If row exists...
                var biggestRow = 0;
                //Calculate each row
                jQuery(this).find(".row").each(function() {
                    jQuery(this).calcSubWidth();
                    //Find biggest row
                    if (rowWidth > biggestRow) {
                        biggestRow = rowWidth;
                    }
                });
                //Set width
                jQuery(this).find(".sub").css({'width' :biggestRow});
                jQuery(this).find(".row:last").css({'margin':'0'});

            } else { //If row does not exist...

                jQuery(this).calcSubWidth();
                //Set Width
                jQuery(this).find(".sub").css({'width' : rowWidth + 20});

            }
        }

        function megaHoverOut() {
            jQuery(this).find(".sub").stop().fadeTo(1, 0, function() {
                jQuery(this).hide();
            });
        }


        var config = {
            sensitivity: 1000, // number = sensitivity threshold (must be 1 or higher)
            interval: 100, // number = milliseconds for onMouseOver polling interval
            over: megaHoverOver, // function = onMouseOver callback (REQUIRED)
            timeout: 1, // number = milliseconds delay before onMouseOut
            out: megaHoverOut // function = onMouseOut callback (REQUIRED)
        };

        /*
         jQuery.fn.markerUndermenuCols = function(){
         var uml = jQuery(this);
         jQuery.each(uml, function(i) {
         var umwidth = jQuery(this).width();
         jQuery(this).css("width", umwidth);
         jQuery(this).addClass("um" + i);
         });
         };
         */

        jQuery("ul#topnav li .sub").parent("li").addClass("u");
        jQuery("ul#topnav li .sub ul li:last-child").addClass("l");
        jQuery("<li class='menu2nd'><img src='/images/0.gif' alt=''></li>").prependTo("ul#topnav li ul.sub2");
        jQuery("ul#topnav li .sub").css({'opacity':'0'});
        //jQuery("ul#topnav li .sub > ul").markerUndermenuCols();
        jQuery("ul#topnav li").hoverIntent(config);

    }

};