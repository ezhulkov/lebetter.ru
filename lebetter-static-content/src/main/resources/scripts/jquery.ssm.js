var _pos = new Object();
;(function($) {
    var timeout_handles = [],
    highestZindex = function()
    {
        var _highestZindex = 0;
        jQuery(".d-shadow").each(function() {
            var index_current = parseInt($(this).css("zIndex"), 10);
            if (index_current > _highestZindex) {
                _highestZindex = index_current;
            }
        });
        return _highestZindex
    },
    _ie7 = (jQuery.browser.version == '7.0') ? 16 : 0

    _show = function(obj, _options, _id) {
        _css = {};
        WW = jQuery(window).width();
        WP = 1000;
        NX = (WW > WP) ? (WW - WP)/2 : 0;
        options = $.extend({}, $.fn.ssm.defaults, _options);

            jQuery("#ssm_" + _id).remove()
                                  //  jQuery(".d-shadow").remove();
                                    $('body').append(wrap = $('<div class="d-shadow tp_ssm" id="ssm_' + _id + '"></div>'));
                                    outer = $('<div class="d-shadow-wrap"></div>');
                                    pointer_ = $('<div class="pointer"/>').addClass(options.pointer).appendTo(wrap);
                                    if(options.x_close) { x_close = $('<div class="x_close"><img src="/images/ks-front/but/but-close-basket.gif" alt=""/></div>').bind('click', function(){jQuery(this).parent().parent().remove(); options.onClose();}).appendTo(outer); }

                                    content = $('<span/>').append((options.content) ? options.content.clone() : jQuery(obj).children()).appendTo(outer);
                                    if(!options.x_close) { content.css({'cursor': 'pointer'}).bind('click', function(){jQuery(this).parent().parent().remove(); options.onClose();}) }
                                    top_ = $('<div class="d-sh-cn d-sh-tl"></div><div class="d-sh-cn d-sh-tr"></div>').appendTo(outer);
                                    outer.appendTo(wrap);
                                    bottom_= $('<div class="d-sh-cn d-sh-bl"></div><div class="d-sh-cn d-sh-br"></div>').appendTo(wrap);


        if (options.relative) {
            _pos = {};
            if (options.left != undefined) {
                _pos.left = obj.offset().left + options.left + obj.width();
                _css.left = _pos.left - NX - 500;
            }
            if (options.right != undefined) {
                _pos.left = obj.offset().left - options.right - options.width;
                _css.left = _pos.left - NX - 500;
            }

            _pos.top = obj.offset().top + options.top - obj.height() / 2;
            _css.top = _pos.top - _ie7;
            _css.margin = '0 50%';

        } else {
            if (options.left != 'undefined') _css.left = options.left - 500;
            _css.right = options.right - 500;

            if (options.top != 'undefined') _css.top = options.top - _ie7;
            _css.bottom = options.bottom;

            _css.position = options.position;
            _css.margin = '0 50%';

        }
            _css.width =  options.width;
            wrap.css(_css);
            if (options.zindex) wrap.css({'z-index': options.zindex});
            options.onStart();

            if (options.autoclose) {
                if ((_id) in timeout_handles) {
                    clearTimeout(timeout_handles[_id])
                }
                timeout_handles[_id] = setTimeout(function() {
                    jQuery("#ssm_" + _id).remove()
                }, options.autoclose);
            }

    };

    $.fn.ssm = function(ev, options) {

            
            this.each(function() {
                var _id_ = (jQuery(this).attr('id')) ? jQuery(this).attr('id') : (options.id) ? options.id : Math.floor((100000 * Math.random())).toString() ;

                    if (ev == 'init') {
                        options.relative = false
                        _show(jQuery(this), options, _id_);
                    } else {
                        jQuery(this).bind(ev, function() {
                            _show(jQuery(this), options, _id_);
                        });
                    }
            });
    };

	$.ssm = function() {


        /*
        if (ev == 'init') {
            options.relative = false
            _show(jQuery(this), options);
        } else {



                 jQuery(this).bind(ev, function() {
                    _show(jQuery(this), options, _id_);
                });
            });

        }

        */
	};

    $.fn.ssm.defaults = {
        relative: false                 //                  ili samo po sebe
        ,position: 'absolute'           //                  fixed
        ,autoclose:0                       //                  5000
        ,x_close : true                 //
        ,pointer : 'none'               //
        ,content : null               //
        ,onStart : function() {}
        ,onClose : function() {}
        ,width : 650
    };
})(jQuery);