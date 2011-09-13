var delim = '@';
var trailLength = 3;
var chunks;
var DAY = 24 * 60 * 60 * 1000;

jQuery(document).ready(function() {
    //Login box - select auth type
    jQuery(".ks_social_auth_option").click(function() {
        jQuery('.ks_social_auth_option').removeClass("ks_social_auth_option_sel");
        jQuery(this).addClass("ks_social_auth_option_sel");
        jQuery('.social_auth_form').hide();
        var a = jQuery(this).attr('id');
        jQuery('#social_auth_form_' + a).fadeIn(500);
    });
    /*background-fix-horizontally*/
    jQuery(window).resize(function() {
        if (jQuery(window).width() < 938) {
            jQuery("body").css("background-position", "-446px 0");
        } else {
            jQuery("body").css("background-position", "50% 0");
        }
    });
    KS.highlight();
    KS.decor_date();
    KS.delattr();
    jQuery('#searchStr').val('Желтое платье');
    jQuery('#searchStr').focusin(function() {
        if (jQuery(this).val() === 'Желтое платье') {
            jQuery(this).val('');
        }
        jQuery(this).parent().addClass("ks_top_block_menu_infosrch_active");
    });

    jQuery('#searchStr').focusout(function() {
        if (jQuery(this).val() == '') {
            jQuery(this).val('поиск')
        }
        jQuery(this).parent().removeClass("ks_top_block_menu_infosrch_active");
    });

    cur_get_height = KS.get_height();

    jQuery(window).scroll(function() {
//        console.log((jQuery(window).scrollTop() - cur_get_height + jQuery('#browse_filter').height()) + '   '+ jQuery(document).height());

        if (jQuery('#browse_filter').length) {
            jQuery('#ks_content_categories').css('height', cur_get_height);
            if (jQuery(window).scrollTop() >= 135) {
                if ((jQuery(window).scrollTop() - cur_get_height + jQuery('#browse_filter').height()) > 184) {
                    jQuery('#browse_filter').addClass('ks_menu_fixxed2');
                } else {
                    jQuery('#browse_filter').removeClass('ks_menu_fixxed2');
                }
                jQuery('#browse_filter').addClass('ks_menu_fixxed');
            } else {
                jQuery('#browse_filter').removeClass('ks_menu_fixxed2');
                jQuery('#browse_filter').removeClass('ks_menu_fixxed');
            }
        }

    });
    /* Ob */
    var obh = jQuery(".ks_obs_formsend").height();
    if (obh >= 158) {
        jQuery(".ks_obs_proddescr_short").css({
            'height': '38px',
        });
        jQuery(".ks_obsindex_20").css({
            'display': 'block'
        });
    } else {
        jQuery(".ks_obsindex_20").css({
            'display': 'block'
        });
    }

});


/* Functions */
KS = {
    promo_index: Number,
    cucl: Number,
    processEnterOnInput: function(event, form) {
        if (event.keyCode == '13') {
            jQuery(form).submit();
        }
    },
    accordion: function(curCategoryIndex) {
        selected_one = jQuery("#accordion li.ks_menu_subCategory_selected");
        selected_ul = jQuery('#accordion ul.ui-accordion-content-active');
        selected_ul_first = selected_ul.find("li:first");
        jQuery("#ks_menu_girls:not(.sex_active)").mouseover(
                function() {
                    jQuery(this).addClass("ks_menu_girls_a");
                }).mouseleave(function() {
                    jQuery(this).removeClass("ks_menu_girls_a");
                });

        jQuery("#ks_menu_boys:not(.sex_active)").mouseover(
                function() {
                    jQuery(this).addClass("ks_menu_boys_a");
                }).mouseleave(function() {
                    jQuery(this).removeClass("ks_menu_boys_a");
                });

        var index;
        var str = jQuery.url.attr("directory");
        if (str.indexOf('/obs') == -1) {
            index = jQuery.cookie("open_cat");
            if (index == undefined) {
                index = 0;
            }
        } else {
            index = curCategoryIndex;
        }

        jQuery('#accordion div.ks_menu_category:last').addClass('ks_menu_h4last_category');
        jQuery('#accordion div.ks_menu_category:first').addClass('ks_menu_h4first_category');
        jQuery('#accordion ul').find('li:last').addClass('ks_menu_last_category');
        jQuery('#accordion ul').find('li:first').addClass('ks_menu_first_category');
        jQuery('#accordion ul:last li:last').addClass('ks_menu_overlast_category');
        jQuery("#accordion").accordion({
            event: 'click',
            autoHeight: false,
            header: 'div.ks_menu_category',
            active: parseInt(index),
            change: function(event, ui) {
                var active = jQuery("#accordion").accordion("option", "active");
                jQuery.cookie("open_cat", active, {expires: 1000, path: "/"});
            }
        }).css("display", "block");
    },
    accordion_ready: function() {
        jQuery("#accordion .ks_menu_category").click(function() {
            jQuery("#accordion .ks_menu_category").removeClass("ks_menu_oneCategory_selected");
            jQuery(this).addClass("ks_menu_oneCategory_selected");
        });

        jQuery("#pm").click(function() {
            jQuery('#accordion .ks_menu_category').slideDown(300);
            jQuery('#accordion ul li').removeClass("c12px_down").slideDown(300);
            jQuery('#pm').hide();
            jQuery('#accordion').css({'margin-bottom': '4px'});
        });
    },
    checkEmail: function(link) {
        var param = jQuery("#email").val();
        if (param != "") {
            param = KS.encodeURIComponentForTapestry(param);
            link = link.replace("PARAM", param);
            KS.tapestry.triggerLink("proxyEmailAjax", link);
        }
    },
    vk: {
        wishOrder: function(sid, pid, name, desc, price, url, vkSig) {
            return {
                merchant_id:sid,
                item_id:pid,
                item_name:name,
                item_description:desc,
                item_currency:'RUB',
                item_price:price,
                item_photo_url:url,
                sig:vkSig,
                testmode:"false"
            }
        },
        buyOrder: function(sid, pid, name, desc, price, url, uid) {
            var order = {
                merchant_id:sid,
                fail_url:'http://kindershopping.ru/vk_order_failed',
                success_url:'http://kindershopping.ru/vk_order_success',
                items:[],
                required_fields:'recipient_phone',
                custom:{},
                testmode:"false"
            };
            var item = {
                id:pid,
                name:name,
                description:desc,
                currency:'RUB',
                quantity:1,
                price:price,
                photo_url:url
            };
            order.items.push(item);
            order.custom[1] = uid;
            return order;
        }
    },
    basket: {
        initChangeParams : function() {
            jQuery(".buttonFilter").bind({'click' : function() {
                jQuery.fancybox.close();
            }
            });
            jQuery("div.prch_params_colors div").click(function() {
                var sel = jQuery(this).hasClass("ks_select_color");
                var pid = jQuery(this).attr("pid");
                jQuery("#prch_params" + pid).find("div.prch_params_colors div").removeClass("ks_select_color");
                if (sel) {
                    jQuery(this).removeClass("ks_select_color");
                } else {
                    jQuery(this).addClass("ks_select_color");
                }
                if (jQuery(this).hasClass("ks_select_color")) {
                    jQuery("#prch_params" + pid).find("input#KS-PROP-COLOR").attr("value", jQuery(this).attr("id"));
                } else {
                    jQuery("#prch_params" + pid).find("input#KS-PROP-COLOR").removeAttr("value");
                }
            });
        }
    },
    tapestry : {
        /*
         many thanks to ingesol
         http://tinybits.blogspot.com/2009/10/missing-javascript.html
         */
        submitZoneForm : function(elementId) {
            var element = $(elementId);
            element.fire(Tapestry.FORM_PROCESS_SUBMIT_EVENT);
        },
        triggerLink : function(elementId, url) {
            var pElementId = jQuery("a[id^='" + elementId + "']").attr("id");
            var element = $(pElementId);
            var zoneObject = Tapestry.findZoneManager(element);
            if (zoneObject) {
                zoneObject.updateFromURL(url);
            }
        }
    },
    soc_auth :  {
        fbLogin: function() {
            FB.getLoginStatus(function(response) {
                if (response.session) {
                    window.location = "http://kindershopping.ru/extAuth?url=" + window.location +
                            "&uid=" + response.session.uid +
                            "&sid=fb&ses=" + response.session.access_token;
                }
            });
        },
        vkLogin: function() {
            VK.Auth.getLoginStatus(function(response) {
                if (response.session) {
                    window.location = "http://kindershopping.ru/extAuth?url=" + window.location +
                            "&uid=" + response.session.mid +
                            "&sid=vk&ses=" + response.session.sid;
                }
            });
        }
    },
    shopbrands : function(j) {
        var shopbrands = jQuery(".ks_shops_detail_brands > .ks_shops_detail_brands_item");
        var shopbrandsl = jQuery(".ks_shops_detail_brands > .ks_shops_detail_brands_item").length;

        jQuery.each(shopbrands, function(i) {
            jQuery(this).prepend("<span class='ks_shops_detail_brands_item_num'>" + (i + 1) + ".</span>");
            if (i <= shopbrandsl / 2) {
                jQuery(this).appendTo("td.ks_shops_detail_brands_l");
            }
            if (i >= shopbrandsl / 2) {
                jQuery(this).appendTo("td.ks_shops_detail_brands_r");
            }

        });
    },
    manageControls : function(position, numberOfSlides) {
        // manageControls: Hides and Shows controls depending on currentPosition
        // Hide left arrow if position is first slide
        if (position == 0) {
            jQuery('#leftControl').hide()
        } else {
            jQuery('#leftControl').show()
        }
        // Hide right arrow if position is last slide
        if (position == numberOfSlides - 1) {
            jQuery('#rightControl').hide()
        } else {
            jQuery('#rightControl').show()
        }
    },
    pagedLoop :  {
        moveBackNextLinks: function(loopId) {
            jQuery('#backLinkPlace_' + loopId).html('');
            jQuery('#backLink_' + loopId).appendTo('#backLinkPlace_' + loopId);
            jQuery('#nextLinkPlace_' + loopId).html('');
            jQuery('#nextLink_' + loopId).appendTo('#nextLinkPlace_' + loopId);
        },
        createPageEnd : function(loopId) {
            var children = jQuery('#pages_' + loopId).children();
            var id;

            if (children.length == 0) {
                id = 'page_' + loopId + '_0';
            } else {
                var lastChild = children[children.length - 1];
                var prevId = lastChild.id;
                id = 'page_' + loopId + '_' + (parseInt(prevId.substring(6 + loopId.length)) + 1);
            }


//            console.info('Appending page end. Children.length=' + children.length + '. new id=' + id);

            var div = jQuery('<div></div>');
            div.attr("id", id);
            div.css('display', 'none');
            div.appendTo('#pages_' + loopId);
            return id;
        },
        createPageBegin : function(loopId) {

            var children = jQuery('#pages_' + loopId).children();
            var id;
            if (children.length == 0) {
                id = 'page_' + loopId + '_0';
            } else {
                var firstChild = children[0];
                var prevId = firstChild.id;
                id = 'page_' + loopId + '_' + (parseInt(prevId.substring(6 + loopId.length)) - 1);
            }
            var div = jQuery('<div></div>');
            div.attr("id", id);
            div.css('display', 'none');
            div.prependTo('#pages_' + loopId);
            return id;
        },
        createPage : function(loopId, num) {
            var id = 'page_' + loopId + '_' + num;
            var div = jQuery('<div></div>');
            div.attr("id", id);
            div.css('display', 'none');
            div.prependTo('#pages_' + loopId);
        },
        dropPagesBegin : function(loopId, slidingFrameSize) {
            var children = jQuery('#pages_' + loopId).children();
            for (var i = 0; children.length >= i + slidingFrameSize; i++) {
//                console.info('Drop page ' + i);
                children.eq(i).remove();
            }
        },
        dropPagesEnd : function(loopId, slidingFrameSize) {
            var children = jQuery('#pages_' + loopId).children();
            for (var i = slidingFrameSize; children.length >= i; i++) {
//                console.info('Drop page ' + (i - 1));
                children.eq(i - 1).remove();
            }
        },
        backPressed : function(loopId, slidingFrameSize, pagePosition, callback) {
            KS.pagedLoop.dropPagesEnd(loopId, slidingFrameSize);
            var id = KS.pagedLoop.createPageBegin(loopId);
            jQuery('#pageZoneContainer_' + loopId + '_' + pagePosition).appendTo('#' + id);
            jQuery('#pageZoneContainer_' + loopId + '_' + pagePosition).show();
            jQuery('#' + id).show();
            if (callback) {
                callback();
            }
        },
        nextPressed : function(loopId, slidingFrameSize, pagePosition, callback) {
            KS.pagedLoop.dropPagesBegin(loopId, slidingFrameSize);
            var id = KS.pagedLoop.createPageEnd(loopId);

//            console.info('id=' + id + ';container = #pageZoneContainer_' + loopId + '_' + pagePosition);

            jQuery('#pageZoneContainer_' + loopId + '_' + pagePosition).appendTo('#' + id);
            jQuery('#pageZoneContainer_' + loopId + '_' + pagePosition).show();
            jQuery('#' + id).show();
            if (callback) {
                callback();
            }
        },
        pagePressed : function(loopId, pagePosition, callback) {
            KS.pagedLoop.dropAllPages(loopId);
            KS.pagedLoop.createPage(loopId, pagePosition);
            jQuery('#pageZoneContainer_' + loopId + '_' + pagePosition).appendTo('#page_' + loopId + '_' + pagePosition);
            jQuery('#pageZoneContainer_' + loopId + '_' + pagePosition).show();
            jQuery('#page_' + loopId + '_' + pagePosition).show();
            if (callback) {
                callback();
            }
        },
        dropAllPages : function(loopId) {
            var children = jQuery('#pages_' + loopId).children();
            jQuery.each(children, function(i, val) {
                children.eq(i).remove();
            });
        },
        restoreState: function (hash, pageKey, loopId, slidingFrameSize, pagePosition, loadLink, loadLinkName) {
            if (hash == "") {
                hash = '0|0';
            }

            var splitted = hash.split('|');
            var pageFrom = parseInt(splitted[0]);
            var pageTo = parseInt(splitted[1]);

            if (isNaN(pageFrom)) {
                pageFrom = 0;
            }
            if (isNaN(pageTo)) {
                pageTo = pageFrom;
            }


            // first load all navigation history by separated queries
//            console.info('Was opened from ' + pageFrom + ' to ' + pageTo);


            var pagesOnDisplay = pageTo - pageFrom + 1;
            if (pagesOnDisplay > slidingFrameSize) {
                pagesOnDisplay = slidingFrameSize;
                pageFrom = pageTo - pagesOnDisplay + 1;
            }

            KS.pagedLoop.loadPage(pageFrom, pageTo, pagesOnDisplay, pageKey, loadLink, loadLinkName, 'page');

        },
        loadPage:function (pageFrom, pageTo, pagesOnDisplay, pageKey, loadLink, loadLinkName, direction) {
            if (pageFrom > pageTo) {
                return;
            }

//            console.info('Loading page contents ' + pageFrom + '. PagesOnDisplay=' + pagesOnDisplay);

            var linkObj = jQuery('.' + loadLinkName);

            var link = loadLink.
                    replace("PARAM1", pageFrom).
                    replace("PARAM2", pageTo).
                    replace("PARAM3", pageKey).
                    replace("PARAM4", pagesOnDisplay).
                    replace("PARAM5", direction);
            KS.tapestry.triggerLink(linkObj.attr('id'), link);
        }
    },
    decor_date : function() {
        var d = new Date;
        var d1 = d.getMonth() + 1;
        var d2 = d.getDate();

        /* for NY */
        if (d1 == 12 && d2 >= 20 || d1 == 1 && d2 <= 18) {
            jQuery(".ks_garland_l").addClass("ks_top_ny_l");
            jQuery(".ks_garland_r").addClass("ks_top_ny_r");
            jQuery(".ks_bodytable").addClass("ks_mid_ny");
            jQuery(".ks_bottom_block").addClass("ks_bot_ny");
        }
        ;
        /* for 23feb */
        if (d1 == 2 && d2 == 22 || d1 == 2 && d2 == 23) {
            jQuery(".ks_garland_l").addClass("ks_top_23f_l");
            jQuery(".ks_garland_r").addClass("ks_top_23f_r");
            jQuery(".ks_bodytable").addClass("ks_mid_23f");
            jQuery(".ks_bottom_block").addClass("ks_bot_23f");
        }
        ;
        /* for 8mart */
        if (d1 == 3 && d2 == 7 || d1 == 3 && d2 == 8) {
            jQuery(".ks_garland_l").addClass("ks_top_8m_l");
            jQuery(".ks_garland_r").addClass("ks_top_8m_r");
            jQuery(".ks_bodytable").addClass("ks_mid_8m");
            jQuery(".ks_bottom_block").addClass("ks_bot_8m");
        }
        ;
        /* for 9may */
        if (d1 == 5 && d2 == 8 || d1 == 5 && d2 == 9) {
            jQuery(".ks_garland_l").addClass("ks_top_9m_l");
            jQuery(".ks_garland_r").addClass("ks_top_9m_r");
            jQuery(".ks_bodytable").addClass("ks_mid_9m");
            jQuery(".ks_bottom_block").addClass("ks_bot_9m");
        }
        ;

    },
    /* Delete for no-valivate ImagesAttributs  */
    delattr : function() {
        jQuery("img").removeAttr("align");
    },
    encodeURIComponentForTapestry : function(uri) {
        var VALID_T5_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890-_.:".split("");
        if (uri == null) {
            return "$N";
        }
        if (uri.length == 0) {
            return "$B";
        }
        var result = "";
        for (i = 0; i < uri.length; i++) {
            var c = uri.charAt(i);
            if (VALID_T5_CHARS.indexOf(c) > -1) {
                result += c;
            }
            else {
                result += "$00" + uri.charCodeAt(i).toString(16);
            }
        }
        return result;
    },
    color_cell : function(id) {
        var el = document.getElementById(id);
        if (el != null) {
            jQuery(el).addClass("sel");
        }
        KS.highlight();
    },
    bread_crumbs : function(m) {
        jQuery("#bread_crumbs_box").empty();
        for (i = 0; i < m.length; i++) {
            if (i == m.length - 1) {
                jQuery("#bread_crumbs_box").append(m[i][0]);
            } else {
                jQuery("#bread_crumbs_box").append("<a href='" + m[i][1] + "'>" + m[i][0] + "</a><span class='ks_status_hr'>&#0187;</span>");
            }
            ;
        }
    },
    bread_crumbs_t : function(box) {
        jQuery("#bread_crumbs_box").empty();
        jQuery(box).removeClass("ks_display_none");
        jQuery("#bread_crumbs_box").append(jQuery(box).html());
        jQuery(box).remove();

    },
    highlight : function() {
        jQuery("input.NFText").focus(
                function() {
                    jQuery(this).parents("td.f-ipt").find(".NFTextLeft, .NFTextCenter, .NFTextRight").addClass("NFh");
                    //jQuery(".NFTextCenter, .NFTextLeft, .NFTextRight").addClass("NFh");
                }).blur(function() {
                    jQuery(this).parents("td.f-ipt").find(".NFTextLeft, .NFTextCenter, .NFTextRight").removeClass("NFh");
                    //jQuery(".NFTextCenter, .NFTextLeft, .NFTextRight").removeClass("NFh");
                });

        jQuery("textarea.NFTextarea").focus(
                function() {
                    jQuery(this).parents("td.f-ipt").find(".NFTextareaTop, .NFTextareaBottom, .NFTextareaTopLeft, .NFTextareaBottomLeft").addClass("NFhr");
                    jQuery(this).parents("td.f-ipt").find(".NFTextareaLeft").removeClass("NFTextareaLeft").addClass("NFTextareaLeftH");
                    jQuery(this).parents("td.f-ipt").find(".NFTextareaRight").removeClass("NFTextareaRight").addClass("NFTextareaRightH");
                }).blur(function() {
                    jQuery(this).parents("td.f-ipt").find(".NFTextareaTop, .NFTextareaBottom, .NFTextareaTopLeft, .NFTextareaBottomLeft").removeClass("NFhr");
                    jQuery(this).parents("td.f-ipt").find(".NFTextareaLeftH").removeClass("NFTextareaLeftH").addClass("NFTextareaLeft");
                    jQuery(this).parents("td.f-ipt").find(".NFTextareaRightH").removeClass("NFTextareaRightH").addClass("NFTextareaRight");
                });
    },
    /*
     bsearch_images : function() {
     var bsizeimg = jQuery(".ks_brandSearchTab .ks_basket_img img");
     var bsizeimgh = jQuery(".ks_brandSearchTab .ks_basket_img img").height();
     var bsizeimgw = jQuery(".ks_brandSearchTab .ks_basket_img img").width();

     if (bsizeimgh > bsizeimgw){
     if (bsizeimgh >= 150) {
     bsizeimg.css("height","108px"); // portrait mode
     }
     }else{
     if (bsizeimgw >= 150) {
     bsizeimg.css("width","150px");  // landscape mode
     }
     };
     },
     */
    ob_fit_images : function() {
        var morepics = document.getElementById('ks_ob_col_left');
        var obr = jQuery(".ks_ob_col_right_tab").outerHeight();

        //left colonum
        jQuery(".ks_ob_lnk_morepics").live('click', function () {

                    if (morepics.style.height == '371' || morepics.style.height == 'auto') {
                        jQuery(this).text("Ещё фотографии");
                        jQuery(".ks_ob_col_left").css("height", "371px");
                        if (obr < 332) { //right col corrected
                            jQuery(".ks_ob_col_right_tab").css("height", "353px");
                        }
                    } else {
                        jQuery(this).text("Меньше фотографий");
                        jQuery(".ks_ob_col_left").css("height", "auto");
                        var oblopen = jQuery(".ks_ob_col_left").outerHeight();
                        jQuery(".ks_ob_col_right_tab").css("height", + (oblopen - 38) + "px");

                    }
                }
        );

        //right colonum
        if (obr < 332) {
            jQuery(".ks_ob_col_right_tab").css("height", "353px");
        }
    },
    ksavatar : function() {
        var avasizeimg = jQuery("img.ks_profile_avatar2_img");
        var avasizeimgw = jQuery(avasizeimg).width();
        if (avasizeimgw >= 200) {
            avasizeimg.css("width", "200px");
            avasizeimg.css("visibility", "visible");
        } else {
            avasizeimg.css("visibility", "visible");
        }
    },
    /* Resise for ShopSEARCHimages */
    /*
     ssearch_images : function() {
     var ssizeimg = jQuery(".ks_shopSearchTab .ks_basket_img img");
     var ssizeimgh = jQuery(".ks_shopSearchTab .ks_basket_img img").height();
     var ssizeimgw = jQuery(".ks_shopSearchTab .ks_basket_img img").width();

     if (ssizeimgh > ssizeimgw){
     if (ssizeimgh >= 150) {
     ssizeimg.css("height","108px"); // portrait mode
     }
     }else{
     if (ssizeimgw >= 150) {
     ssizeimg.css("width","150px");
     }
     };
     },
     */
    /* Promo block */
    whosnext : function(a) {
        if (a >= jQuery(".ks_promo_outer").length - 1) {
            a = 0;
        } else {
            a = a + 1;
        }
        return a;
    },
    offers : function () {
        jQuery(".carousel .ks_head_offer_loop").jCarouselLite({
            auto: 5000,
            speed: 3000,
            vertical: true,
            scroll: 2,
            visible: 2
        });
    },
    gop : function (io) {
        jQuery(".ks_promo_outer").fadeOut(300);
        jQuery(".ks_promo_outer:eq(" + io + ")").fadeIn(800);
        jQuery(".but_qq").removeClass("ks_promo_but_active");
        jQuery(".but_qq:eq(" + io + ")").addClass("ks_promo_but_active");
        i = io;
        clearInterval(KS.cucl);
    },
    timedCount : function() {
        KS.gop(KS.promo_index);
        var previ = KS.promo_index;
        KS.promo_index = KS.whosnext(KS.promo_index);
        if (previ != KS.promo_index) {
            KS.cucl = setTimeout("KS.timedCount()", 10000);
        }
    },
    fit_news : function() {
        var item = jQuery(".ks_headings_heading");
        jQuery.each(item, function(i) {
            jQuery(this).find(".ks_headings_item").first().addClass("ks_headings_item_first");
            jQuery(this).find(".ks_headings_item:last-child").addClass("ks_headings_item_last");
        });
    },
    count_symbols: function() {
        if (jQuery('p.rm_comtree_comments_empty').length != 0) {
            jQuery("p.rm_comtree_comments_empty").css("display", "none");
            jQuery(".rm_comtree_bar_txt").empty().text("Комментариев нет").css("color", "#898989");
        }

        // счётчики символов в комментариях
        jQuery(".commentbox textarea").live("keyup", function() {
            var len = this.classList.length;
            for (var i = 0; i < len; i++) {
                var clazzz = this.classList[i];
                if (clazzz.startsWith("comment_text_")) {
                    var id = clazzz.substring(13);
                    var s = 512 - jQuery(this).val().length;
                    var syLimit = jQuery("#char_comment_" + id);

                    syLimit.css({'color': '#ccc'});
                    syLimit.html(s);
                    if (jQuery(this).val().length >= 512) {
                        syLimit.html(0);
                        syLimit.css({'color': '#f00'});
                    }
                    break;
                }
            }
        });
    },

    cookieTest: function(name) {
        try {
            setCookie(name, 'true', 1, "/");
            chunks = document.cookie.split("; ");
            return (getCookie(name) == 'true');
        } catch(e) {
            return false;
        }

    },

    getCookie: function(name) {
        var returnVal = null;
        for (var i in chunks) {
            var chunk = chunks[i].split("=");
            returnVal = (chunk[0] == name)
                    ? unescape(chunk[1])
                    : returnVal;
        }
        return returnVal;
    },

    setCookie: function(name, value, days, path) {
        var expiry =
                new Date(new Date().getTime() + days * DAY);

        if (days == 999) {
            document.cookie = name + "=" + escape(value) + "; path=" + path;
        } else {
            document.cookie = name + "=" + escape(value) + "; path=" + path + "; expires=" + expiry.toGMTString();
        }
        chunks = document.cookie.split("; ");
    },

    submitFilterSearch: function(special) {
        if (special == true) {
            jQuery("#browse .special_false").val(null);
            jQuery("#KS-PROP-COLOR").attr('value', '');
            jQuery(".color").removeClass('ks_select_color');
        }
        document.forms[jQuery('form.filterForm').attr('id')].submit();
    },

    initBrandsPage: function() {
        jQuery(".ks_lnk_filter_reset").appendTo(".ks_listtable_fl_categories_foot");
        jQuery(".ks_lnk_filter_open, .ks_lnk_filter_open_img").toggle(
                function () {
                    jQuery(".ks_lnk_filter_open").text("скрыть категории");
                    jQuery(".ks_listtable_fl_categories_list").css("height", "auto");
                },
                function () {
                    jQuery(".ks_lnk_filter_open").text("смотреть все категории");
                    jQuery(".ks_listtable_fl_categories_list").css("height", "18px");
                }
        );
    },

    initProductPage: function(ratingLink, pid) {
        jQuery(".atabs-a").click(function() {
            jQuery(".a-tabs li").removeClass('sel');
        });

        jQuery("select.purchase_select").css('width', '197px');
        jQuery(".ks_grid_zoom").fancybox({
            'titlePosition' : 'outside',
            'transitionIn' : 'none',
            'transitionOut' : 'none'
        });
        /* AD Gallery for this item */
        jQuery(".ks_content_ob_table .ks_grid_zoom").click(function() {

            var odj = jQuery(this).attr('href');
            var index_ob_open = jQuery('.ks_content_ob_table .ks_grid_zoom').index(this);
            if (index_ob_open > 3)
                index_ob_open = 0;
            jQuery(odj).adGallery({
                start_at_index: index_ob_open
            });
        });
        /* AD Gallery for bottom grid */
        jQuery(".ks_content_hits_grid .ks_grid_zoom").click(function() {
            var odj = jQuery(this).attr('href');
            jQuery(odj).adGallery();
        });

        jQuery('.star2 a').live('click', function() {
            var link = ratingLink;
            var r = jQuery(this).attr("title");
            if (r != null) {
                link = link.replace("PARAM", pid + '/' + r);
                KS.tapestry.triggerLink("proxyAjaxRating", link);
            }
        });

        KS.ob_fit_images();
    },

    commentTreeCallback: function() {
        jQuery('input[type=radio].star').rating();
        KS.commentReplyFancyBox();
        KS.count_symbols();
    },

    initProductPurchase: function() {
        jQuery("#size_transform_but").fancybox({
            'autoDimensions': false,
            'width': 521,
            'title': 'Таблица размеров',
            'titlePosition' : 'outside',
            'transitionIn' : 'none',
            'transitionOut' : 'none'
        });
        jQuery("#prod_deliv_a").fancybox({
            'autoDimensions': false,
            'width': 521,
            'title': 'Условия доставки и оплаты',
            'titlePosition' : 'outside',
            'transitionIn' : 'none',
            'transitionOut' : 'none'
        });
        jQuery("div#colors_purchase").find("div").click(function() {
            var sel_purchase = jQuery(this).hasClass("ks_select_color");
            jQuery("div#colors_purchase").find("div").removeClass("ks_select_color");
            if (sel_purchase) {
                jQuery(this).removeClass("ks_select_color");
            } else {
                jQuery(this).addClass("ks_select_color");
            }
            if (jQuery(this).hasClass("ks_select_color")) {
                jQuery("input#KS-PROP-COLOR-P").attr("value", jQuery(this).attr("id"));
            } else {
                jQuery("input#KS-PROP-COLOR-P").removeAttr("value");
            }
        });
        jQuery("select.purchase_select").selectbox();
    },

    initProductFilter: function() {

        //jQuery("select.product_filter").css('width', '197px');
        //jQuery("select.product_filter").selectbox();
        jQuery(".ks_content_filter").find("select.product_filter").change(function() {
            var special = jQuery(this).hasClass("special_true");
            KS.submitFilterSearch(special);
        });
        jQuery(".ks_content_filter").find("#colors").find("div").click(function() {
            var sel = jQuery(this).hasClass("ks_select_color");
            jQuery("div#colors").find("div").removeClass("ks_select_color");
            if (sel) {
                jQuery(this).removeClass("ks_select_color");
            } else {
                jQuery(this).addClass("ks_select_color");
            }
            if (jQuery(this).hasClass("ks_select_color")) {
                jQuery("input#KS-PROP-COLOR").attr("value", jQuery(this).attr("id"));
            } else {
                jQuery("input#KS-PROP-COLOR").removeAttr("value");
            }
            KS.submitFilterSearch(false);
        });
        jQuery("#buttonFilter_reset").click(function () {
            jQuery("#browse .product_filter").val(null);
            jQuery("#KS-PROP-COLOR").attr('value', '');
            KS.submitFilterSearch(false);
        });
        jQuery("#size_transform2_but").fancybox({
            'autoDimensions': false,
            'width': 521,
            'overflow': 'no',
            'title': 'Таблица размеров',
            'titlePosition' : 'outside',
            'transitionIn' : 'none',
            'transitionOut' : 'none'
        });
        jQuery("#size_transform3_but").fancybox({
            'autoDimensions': false,
            'width': 521,
            'overflow': 'no',
            'title': 'Таблица размеров',
            'titlePosition' : 'outside',
            'transitionIn' : 'none',
            'transitionOut' : 'none'
        });
        jQuery("#size_transform4_but").fancybox({
            'autoDimensions': false,
            'width': 295,
            'overflow': 'no',
            'title': 'Таблица размеров',
            'titlePosition' : 'outside',
            'transitionIn' : 'none',
            'transitionOut' : 'none'
        });
    },
    extLink: function(id, href) {
        jQuery("#" + id).click(function() {
            var prevForm = document.getElementById("ks_redirect_form");
            if (prevForm != null) {
                document.body.removeChild(prevForm);
            }
            var form = document.createElement("form");
            form.id = "ks_redirect_form";
            form.method = "GET";
            form.action = href;
            form.target = "_blank";
            document.body.appendChild(form);
            form.submit();
        });
    },
    get_height: function() {
        get_height = jQuery(document).height() - 475;
        return get_height;
    },

    sets: {
        map: Object,
        render: Object,
        maxZ: Number,
        state: String,
        autoSaveLink: String,
        prodDropLink: String,
        prodDeleteLink: String,
        t: Number,
        timer_is_on:Number,
        initSetPreviewPage: function () {
            jQuery(".atabs-a").click(function() {
                jQuery(".a-tabs li").removeClass('sel');
            });
            jQuery("table.ks_basket_table .ks_basket_table_underline_tr:last-child").css("border-bottom", "0px solid #E5E5E5");
            jQuery("div#add_tools_box").empty();
            jQuery("div.ks_status").css("display", "block");
            jQuery("div.ks_status").appendTo("div#add_tools_box");
        },
        make_prod_params: function (enc) {
            var prods = "";
            for (var key in KS.sets.map) {
                var entry = KS.sets.map[key];
                var pid = entry.pid;
                var xpos = entry.x;
                var ypos = entry.y;
                var scale = entry.s.toFixed(2);
                var angle = entry.a;
                var zindex = entry.zindex;
                prods += pid + "," + xpos + "," + ypos + "," + scale + "," + angle + "," + zindex + ";";
            }

            var param = jQuery.base64Encode(prods);
            if (enc == true) {
                param = KS.encodeURIComponentForTapestry(param);
            }
            return param;
        },
        /*pid_init*/
        pid_init: function(pid, x, y, s, a, width, height, url1, url2, url3, name, price, zindex, seeit, block, rating) {
            if (typeof(KS.sets.map) != "object") {
                KS.sets.map = new Object();
            }
            KS.sets.map[pid] = {pid: pid, x: x, y: y, s: s, a: a, width: width, height: height, url1: url1, url2: url2, url3: url3, name: name, price: price, zindex: zindex, seeit: seeit, block: block, rating: rating};
            KS.sets.once_ico_render(pid);
        },
        /*ico_render*/
        once_ico_render: function(pid) {
            jQuery('<li class="mini_ico" id="mini_' + pid + '"><a href="' + KS.sets.map[pid].url3 + '"><img class="icon" title="' + KS.sets.map[pid].name + '" alt="' + KS.sets.map[pid].name + '" src="' + KS.sets.map[pid].url1 + '" alt="' + KS.sets.map[pid].url1 + '"/></a></li>')
                    .appendTo("#do_ico")
                    .draggable({
                        containment: 'document',
                        helper: 'clone',
                        opacity: 0.5
                    });
        },
        /*getCorners*/
        getCorners: function(m) {
            _this = m._this;
            _thisW = m.w;
            _thisH = m.h;
            _control = 12;
            _dia = Math.sqrt(Math.pow((_thisW / 2), 2) + Math.pow((_thisH / 2), 2));
            _uG = Math.atan2(_thisH / 2, _thisW / 2) * 180 / Math.PI;
            _thisC = {
                'x': (_thisW - _control) / 2,
                'y': (_thisH - _control) / 2
            };
            _thisRight = {
                'x' : _thisC.x + (6 + _thisC.x) * Math.cos(m.a * Math.PI / 180) * m.s,
                'y' : _thisC.y + (6 + _thisC.x) * Math.sin(m.a * Math.PI / 180) * m.s
            };
            _this.find('.right').css({
                left : _thisRight.x,
                top :  _thisRight.y
            });
        },
        /*once_highlight*/
        once_highlight: function(pid, act) {
            var brda;
            KS.sets.map[pid].s < 1 ? brda = Math.round(1 / KS.sets.map[pid].s) : brda = 1;
            var item_highlight = jQuery("#case_" + pid + " img");
            var legend_highlight = jQuery("#mini_" + pid);

            if (jQuery.browser.msie) {
                item_highlight.css({'border': brda * act + 'px solid #ff8000', 'margin-top': -2 * act + 'px','margin-left': -2 * act + 'px'})
            } else {
                item_highlight.css({'border': brda * act + 'px solid #ff8000', 'margin-top': -brda * act + 'px','margin-left': -brda * act + 'px'})
            }
            act ? b_fix = '7px' : b_fix = '10px';
            legend_highlight.css({'border': 3 * act + 'px solid #ff8000', 'margin-top': -1 * act + 'px', 'margin-bottom': b_fix, 'margin-left': -3 * act + 'px'});
        },
        once_render2: function(pid) {
            jQuery("#preload_" + pid).hide();
            jQuery('#img' + pid).css('visibility', 'visible');
            jQuery('#case_' + pid + ' .vr_handler').show();
            jQuery('#img' + pid).animate({rotate: KS.sets.map[pid].a, scale: KS.sets.map[pid].s}, 0, function() {
            });
        },
        /*once_render*/
        once_render: function(pid) {
            var bg_flow = 60 - 12 * KS.sets.map[pid].rating;
            KS.sets.map[pid].seeit ? seee = 'block' : seee = 'none';
            _case = jQuery("<div/>", {
                "id": "case_" + pid,
                "class": "case"
            }).css({'position' : 'absolute','z-index' : KS.sets.map[pid].zindex, display : seee})
                    .appendTo('#canvas')
                    .bind({"click" : function() {
                        jQuery('#info_base').show();
                        jQuery('#current_ob_title').html('<a href="' + KS.sets.map[pid].url3 + '" target="_blank">' + KS.sets.map[pid].name + '</a>');
                        jQuery('#current_ob_price_val').html(KS.sets.map[pid].price);
                        jQuery('#current_ob_rating').css("background-position", "100% -" + bg_flow + "px");
                        jQuery('#current_ob_del_button').unbind().bind({'click' : function() {
                            jQuery("#case_" + pid).remove();
                            jQuery("#mini_" + pid).remove();
                            delete KS.sets.map[pid];
                            var link = KS.sets.prodDeleteLink;
                            var param = KS.encodeURIComponentForTapestry(pid);
                            link = link.replace("PARAM", param);
                            KS.tapestry.triggerLink("proxyAjax", link);
                            jQuery('#info_base').hide();
                        }});
                    },
                        "mouseover" : function() {
                            KS.sets.once_highlight(pid, 1)
                        },
                        "mouseout" : function() {
                            KS.sets.once_highlight(pid, 0)
                        }
                    }).draggable({ /* grid: [5, 5],*/
                        refreshPositions: true,
                        disabled: KS.sets.map[pid].block,
                        start: function(event, ui) {
                            var nMaxZ = KS.sets.getMaxZIndex() + 1;
                            drag_pid = jQuery(this).attr('id').replace('case_', '');
                            jQuery(this).css("z-index", nMaxZ);
                            KS.sets.map[drag_pid].zindex = nMaxZ;
                        },
                        drag: function(event, ui) {
                            var Dragpos = jQuery(this).position();
                            KS.sets.map[drag_pid].x = Dragpos.left;
                            KS.sets.map[drag_pid].y = Dragpos.top;
                        }
                    });
            _preload = jQuery('<img src="/images/ks-front/34-1.gif" id="preload_' + pid + '" />');
            _preload.appendTo(_case).css({
                position: 'absolute',
                display : 'none',
                left : KS.sets.map[pid].width / 2,
                top :  KS.sets.map[pid].height / 2}).show();

            _img = jQuery("<img src='" + KS.sets.map[pid].url2 + "'/>").attr({
                "id": "img" + pid,
                "title": KS.sets.map[pid].name,
                "alt": KS.sets.map[pid].name,
                "class": "r_image",
                "r_width": KS.sets.map[pid].width,
                "r_height": KS.sets.map[pid].height
            }).appendTo(_case).bind({
                        "mouseover" : function() {
                            KS.sets.once_highlight(pid, 1);
                        },
                        "mouseout" : function() {
                            KS.sets.once_highlight(pid, 0);
                        }
                    }).css({
                        'visibility' : 'hidden'
                    }).load(function() {

                        KS.sets.once_render2(pid);
                        jQuery("#preload_" + pid).hide();
                    });
            _this = _case;
            _thisW = KS.sets.map[pid].width;
            _thisH = KS.sets.map[pid].height;

            _this.css({
                'left': KS.sets.map[pid].x,
                'top': KS.sets.map[pid].y
            });

            KS.sets.getCorners({
                _this : _this,
                w: _thisW,
                h: _thisH,
                s: KS.sets.map[pid].s,
                a: KS.sets.map[pid].a
            });


            _handler = jQuery("<div class='vr_handler right'></div>");

            _handler.appendTo(_this).css({
                left : _thisRight.x,
                top :  _thisRight.y,
                display :  'none'
            }).draggable({
                        containment: '#canvas', /* grid: [5, 5],*/
                        refreshPositions: true,
                        start: function(event, ui) {
                            _image = jQuery(this).parent().find('.r_image');
                            contDimensionsX = KS.sets.map[pid].width;
                            contDimensionsY = KS.sets.map[pid].height;
                        },
                        // containment : jQuery('.box'),
                        drag: function(event, ui) {
                            var nMaxZ = KS.sets.getMaxZIndex() + 1;
                            var drag_pid = jQuery(this).parent().attr('id').replace('case_', '');
                            var Dragpos = jQuery(this).position();
                            var contSize = _image.position();
                            vector = KS.sets.getAngle(Dragpos.left, Dragpos.top, contDimensionsX, contDimensionsY);
                            angle = Math.round(vector.a * (180 / Math.PI));
                            _image.transform({rotate: angle + 'deg', scale: vector.h / contDimensionsX * 2});
                            KS.sets.map[drag_pid].s = vector.h / contDimensionsX * 2;
                            KS.sets.map[drag_pid].a = angle;
                            jQuery(this).parent().css("z-index", nMaxZ);
                            KS.sets.map[drag_pid].zindex = nMaxZ;
                            jQuery('#info_base').show();
                            jQuery('#current_ob_title').html(KS.sets.map[drag_pid].name);
                            jQuery('#current_ob_price_val').html(KS.sets.map[drag_pid].price);
                            jQuery('#current_ob_artikul').html(KS.sets.map[drag_pid].pid);
                            jQuery('#current_ob_del_button').unbind().bind({'click' : function() {
                                jQuery("#case_" + drag_pid).remove();
                                jQuery("#mini_" + drag_pid).remove();
                                delete KS.sets.map[drag_pid];
                                var link = KS.sets.prodDeleteLink;
                                var param = KS.encodeURIComponentForTapestry(drag_pid);
                                link = link.replace("PARAM", param);
                                KS.tapestry.triggerLink("proxyAjax", link);
                                jQuery('#info_base').hide();
                            }});
                            KS.sets.once_highlight(pid, 1);
                        }
                    });
        },
        /*all_render*/
        all_render: function() {
            for (var key in KS.sets.map) {
                KS.sets.once_render(key);
            }
            return true;
        },
        getMaxZIndex: function() {
            for (var key in KS.sets.map) {
                if (KS.sets.maxZ <= KS.sets.map[key].zindex) {
                    KS.sets.maxZ = KS.sets.map[key].zindex;
                }
            }
            return KS.sets.maxZ;
        },
        getMinZIndex: function() {
            var minZ = KS.sets.getMaxZIndex();
            for (var key in KS.sets.map) {
                if (minZ >= KS.sets.map[key].zindex) {
                    minZ = KS.sets.map[key].zindex;
                }
            }
            return minZ;
        },
        // Get Angle
        getAngle: function(mx, my, wi, hi) {
            center_coords = {
                'x': wi * 0.5,
                'y': hi * 0.5
            };
            mouse_coords = {
                'x': mx,
                'y': my
            };
            var x = mouse_coords.x - center_coords.x + 10 ,
                    y = - mouse_coords.y + center_coords.y - 10,
                    hyp = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)),
                    angle = (y < 0) ? Math.acos(x / hyp) : angle = - Math.acos(x / hyp);
            vector = {
                'a': angle,
                'h': hyp
            };
            return vector;
        },
        /*page_scripts*/
        rotater_activate: function() {
            var getNew_top = jQuery(window).height() - 44;
            if (getNew_top > 652) getNew_top = 652;
            jQuery("#button_up").toggleClass('button_set_1');
            if (state == 'closed') {
                jQuery("#help_activator, #info_base, #button_up").hide();
                jQuery("#rotater_shadow").show();
                jQuery("#rotater").find(".containerPlus").css('width', '100px');
                jQuery("#rotater").animate({width: 100}, 300, function() {
                    KS.sets.show_tooltip('help_enter');
                    jQuery("#do_ico").stop().css({
                        left: 518
                    }).fadeIn(500);
                    KS.setCookie('open_rotater', 0, 30, "/");

                });
                KS.sets.hide_tooltip('help_activator');
                state = 'active';
                jQuery("#help_link").css('top', getNew_top - 90).show();
                return false;
            }
            if (state == 'active') {
                jQuery("#button_up").show();
                jQuery("#do_ico").hide();
                jQuery("#rotater").animate({width: 0}, 300, function() {
                    jQuery("#rotater").find(".containerPlus").css('width', '0');
                    KS.setCookie('open_rotater', 0, 0, "/");
                });
                KS.sets.hide_tooltip('help_enter');
                state = 'closed';
                jQuery("#help_link").css('top', getNew_top - 90).show();
                return false;
            }
            if (state == 'opened') {
                jQuery("#rotater").animate({
                    left: 500
                }, 500, function() {
                    jQuery("#canvas .case").remove();
                    KS.sets.render = false;
                    jQuery("#rotater_shadow").show();
                    jQuery("#buttoms, #info_base").hide();
                    jQuery("#rotater").animate({
                                width: 0
                            }, 500,
                            function() {
                                jQuery("#button_up").show();
                                jQuery("#rotater").find(".containerPlus").css('width', '0');
                                KS.setCookie('open_rotater', 0, 0, "/");
                            });
                });
                state = 'closed';
                jQuery("#help_link").css('top', getNew_top - 90).show();
                KS.sets.stopCount();
                return false;
            }
        },
        rotater_activate_s: function() {
            jQuery("#help_link").hide();
            var getNew_top = jQuery(window).height() - 44;
            if (getNew_top > 652) getNew_top = 652;
            jQuery("#button_up").toggleClass('button_set_1');
            if (state == 'closed') {
                jQuery("#help_activator, #info_base, #button_up").hide();
                jQuery("#rotater").find(".containerPlus").css({'height': '108px', 'width': '957px'});
                jQuery("#rotater").animate({height: 108}, 300, function() {
                    KS.sets.show_tooltip('help_enter_s');
                    jQuery("#do_ico").fadeIn(1000);
                    KS.setCookie('open_rotater', 0, 30, "/");
                });
                KS.sets.hide_tooltip('help_activator_s');
                state = 'active';
                jQuery("#help_link").css('top', 30).show();
                return false;
            }
            if (state == 'active') {
                jQuery("#button_up").show();
                jQuery("#do_ico").hide();
                jQuery("#rotater").animate({height: 0}, 300, function() {
                    jQuery("#rotater").find(".containerPlus").css('height', '0');
                    KS.setCookie('open_rotater', 0, 0, "/");
                });
                KS.sets.hide_tooltip('help_enter_s');
                state = 'closed';
                jQuery("#help_link").css('top', getNew_top - 90).show();
                return false;
            }
            if (state == 'opened') {
                jQuery("#button_up").show();
                jQuery("#rotater").animate({height: 0}, 300, function() {
                    jQuery("#rotater").find(".containerPlus").css('height', '0');
                    KS.setCookie('open_rotater', 0, 0, "/");
                    jQuery("#canvas .case").remove();
                    KS.sets.render = false;
                    jQuery("#buttoms, #info_base").hide();
                });
                KS.sets.hide_tooltip('help_enter_s');
                state = 'closed';
                KS.sets.stopCount();
                jQuery("#help_link").css('top', getNew_top - 90).show();
                return false;
            }
        },
        rotater_open: function() {
            jQuery("#help_link").hide();
            KS.sets.hide_tooltip('help_enter');
            KS.sets.hide_tooltip('help_activator');
            var getNew_top = jQuery(window).height() - 44;
            if (getNew_top > 652) getNew_top = 652;
            jQuery("#rotater").find(".containerPlus").css('width', '1000px');
            jQuery("#do_ico").animate({
                left: 1000
            });
            jQuery("#rotater").animate({
                width: 1000
            }, 500, function() {
                jQuery("#do_ico, #rotater_shadow").hide();
                jQuery("#rotater").animate({
                    left: -500
                }, 500, function() {
                    jQuery("#buttoms").css('top', getNew_top - 200);
                    jQuery("#help_link").css('top', getNew_top - 90).show();
                    jQuery("#button_up, #buttoms").show();
                    if (!KS.sets.render) {
                        KS.sets.render = KS.sets.all_render();
                    }
                    KS.sets.doTimer();
                })
            });

            state = 'opened';
        },
        rotater_open_s: function() {
            jQuery("#help_link").hide();
            KS.sets.hide_tooltip('help_enter_s');
            KS.sets.hide_tooltip('help_activator_s');
            var getNew_top = jQuery(window).height() - 44;
            if (getNew_top > 652) getNew_top = 652;
            jQuery("#do_ico").fadeOut(1000);
            jQuery("#rotater").animate({
                height: getNew_top
            }, 500, function() {
                jQuery("#buttoms").css('top', getNew_top - 230);
                jQuery("#help_link").css('top', getNew_top - 90).show();
                jQuery("#button_up, #buttoms").show();
                if (!KS.sets.render) {
                    KS.sets.render = KS.sets.all_render();
                }
                KS.sets.doTimer();
            });

            state = 'opened';
        },
        rotater_close: function() {
            jQuery("#help_link").hide();
            var getNew_top = jQuery(window).height() - 44;
            if (getNew_top > 652) getNew_top = 652;
            jQuery("#rotater").animate({
                left: 500
            }, 500, function() {
                jQuery("#do_ico").animate({
                    left: 518
                }).fadeIn(1000);
                jQuery("#rotater_shadow").show();
                jQuery("#buttoms").hide();
                jQuery("#button_up").hide();
                jQuery("#rotater").animate({
                    width: 100
                }, 500, function() {
                    jQuery("#rotater").find(".containerPlus").css('width', '100px');
                })
            });
            jQuery("#canvas .case").remove();
            jQuery("#info_base").hide();
            jQuery(".legend").hide();
            jQuery("#help_link").css('top', getNew_top - 90).show();
            KS.sets.render = false;
            KS.sets.stopCount();
            state = 'active';
        },
        rotater_close_s: function() {
            jQuery("#help_link").hide();
            jQuery("#buttoms").hide();
            jQuery("#canvas .case").remove();
            jQuery("#info_base").hide();
            jQuery(".legend").hide();
            jQuery("#rotater").animate({
                height: 108
            }, 500, function() {
                jQuery("#rotater").find(".containerPlus").css('height', '108px');
                KS.sets.render = false;
                KS.sets.stopCount();
                state = 'active';
                jQuery("#do_ico").fadeIn(1000);
            });
            jQuery("#help_link").css('top', 30).show();
        },
        blinkblink: function(ti) {
            jQuery("#canvas").addClass('ui-state-hover');
            setTimeout(function() {
                jQuery("#canvas").removeClass('ui-state-hover');
                setTimeout(function() {
                    jQuery("#canvas").addClass('ui-state-hover');
                    setTimeout(function() {
                        jQuery("#canvas").removeClass('ui-state-hover');
                    }, ti);
                }, ti);
            }, ti);
        },
        hilight: function() {
            jQuery("#auto_save_marker").fadeIn(100,
                    function() {
                    }).delay(300).fadeOut(100);
        },
        _auto_save: function() {
            t = setTimeout(function() {
                KS.sets.hilight();
                KS.sets._auto_save();
            }, 60000);
        },
        auto_save: function() {
            var link = KS.sets.autoSaveLink;
            link = link.replace("PARAM", KS.sets.make_prod_params(true));
            KS.tapestry.triggerLink("proxyAjax", link);
        },
        doTimer: function() {
            if (!KS.sets.timer_is_on) {
                KS.sets.timer_is_on = 1;
                KS.sets._auto_save();
            }
        },
        stopCount: function() {
            clearTimeout(t);
            KS.sets.timer_is_on = 0;
        },
        nevamo: function(help_id) {
            KS.setCookie(help_id, 0, 30, "/");
            jQuery("#ssm_" + help_id).remove();
            return false;
        },
        show_tooltip: function(show_id) {
            if (!jQuery.cookie(show_id)) {
                switch (show_id) {
                    case 'help_enter':
                        jQuery("#help_enter").ssm('init', {
                            top:48
                            ,left:728
                            ,width:260
                            ,position: 'fixed'
                            ,relative: false
                            ,x_close : true
                            ,content: jQuery('<h3>Что мне с этим делать?</h3> <p>Просто перетащите сюда понравившиеся Вам товары. Далее, раскрыв эту панель, можно отредактировать свой сет и опубликовать его, сделав доступным для всех посетителей портала.</p> <a class="ks_lnk_braun_dot" href="#" onclick="KS.sets.nevamo(\'help_enter\'); return false;">спасибо, я в курсе</a> ')
                            ,pointer : 'left'
                            ,zindex : 1100
                            ,onClose : function() {
                                KS.setCookie('help_enter', 0, 999, "/");
                            }
                        });
                        break;

                    case 'help_activator':
                        jQuery("#help_activator").ssm('init', {
                            top:90
                            ,left:976
                            ,width:160
                            ,position: 'fixed'
                            ,relative: false
                            ,x_close : true
                            ,content: jQuery('<h3>Создайте свой сет</h3><p>Начните создание нового сета с нажатия этой кнопки</p><a class="ks_lnk_braun_dot" href="#" onclick="KS.sets.nevamo(\'help_activator\'); return false;">спасибо, я в курсе</a>')
                            ,pointer : 'bottom'
                            ,onClose : function() {
                                KS.setCookie('help_activator', 0, 999, "/");
                            }
                        });

                        break;

                    case 'help_enter_s':
                        jQuery("#help_enter_s").ssm('init', {
                            bottom:124
                            ,left:35
                            ,width:260
                            ,position: 'fixed'
                            ,relative: false
                            ,x_close : true
                            ,content: jQuery('<h3>Что мне с этим делать?</h3> <p>Просто перетащите сюда понравившиеся Вам товары. Далее, раскрыв эту панель, можно отредактировать свой сет и опубликовать его, сделав доступным для всех посетителей портала.</p> <a class="ks_lnk_braun_dot" href="#" onclick="KS.sets.nevamo(\'help_enter_s\'); return false;">спасибо, я в курсе</a> ')
                            ,pointer : 'top'
                            ,onClose : function() {
                                KS.setCookie('help_enter_s', 0, 999, "/");
                            }
                        });
                        break;

                    case 'help_activator_s':
                        jQuery("#help_activator_s").ssm('init', {
                            bottom:49
                            ,left:830
                            ,width:160
                            ,position: 'fixed'
                            ,relative: false
                            ,x_close : true
                            ,content: jQuery('<h3>Создайте свой сет</h3><p>Начните создание нового сета с нажатия этой кнопки</p><a class="ks_lnk_braun_dot" href="#" onclick="KS.sets.nevamo(\'help_activator_s\'); return false;">спасибо, я в курсе</a>')
                            ,pointer : 'top'
                            ,onClose : function() {
                                KS.setCookie('help_activator', 0, 999, "/");
                            }
                        });

                        break;
                    default:

                }
            }

        },
        hide_tooltip: function(show_id, st) {
            if (st) KS.setCookie(show_id, 0, 999, "/");
            jQuery("#ssm_" + show_id).remove();
        },
        initSetArea: function(autoSaveLink, prodDropLink, prodDeleteLink) {

            KS.sets.autoSaveLink = autoSaveLink;
            KS.sets.prodDropLink = prodDropLink;
            KS.sets.prodDeleteLink = prodDeleteLink;
            KS.sets.render = false;
            KS.sets.maxZ = 0;
            KS.sets.timer_is_on = 0;
            KS.sets.state = 'closed';

            if (!navigator.userAgent.toLowerCase().match(/ip(hone|ad)/i)) {
                jQuery('.no_mobile').show();
            }
            var screenSmall = (1024 >= screen.width) ? true : false;
            var dest_dim = 'width';
            var dest_start_val = '100px';
            var dest_dim_val = '1000px';
            jQuery('html').css('height', 'auto');
            var getNew_top = jQuery(window).height() - 44;
            if (getNew_top > 652) getNew_top = 652;
            jQuery("#help_link").css('top', getNew_top - 90).show();

            if (screenSmall) {
                jQuery("#help_link").css({'top': 30, 'left': 871}).show();
                dest_dim = 'height';
                dest_dim_val = '652px';
                dest_start_val = '108px';
                jQuery("#rotater_shadow").hide();
                jQuery("#button_up").css({
                    'top':'auto',
                    'bottom': 0,
                    'margin-right': '-479px',
                    'background-position' : 'left -50px'
                });
                jQuery("#do_ico").css({
                    'height': '53px',
                    'width': 'auto',
                    'right': 'auto',
                    'left': '50%',
                    'margin-left': '-462px',
                    'margin-top': '10px',
                    'position': 'fixed',
                    'z-index': 1050,
                    'top': 'auto',
                    'bottom': 0

                });
                jQuery("#rotater").css({
                    'left': '-521px',
                    'right': '0',
                    'margin-left': '50%',
                    'margin-right': 'auto',
                    'bottom': '0',
                    'top': 'auto',
                    'width': '1000px',
                    'height': '108px'
                });
                jQuery("#rotater").find('.containerPlus').css({'width':'957px'});
            }

            jQuery("#rotator_save_button").click(
                    function() {
                        KS.sets.hilight();
                    });
            jQuery("#help_enter_close").click(
                    function() {
                        KS.sets.hide_tooltip('help_enter', true);
                        return false;
                    });
            jQuery("#help_activator_close").click(
                    function() {
                        KS.sets.hide_tooltip('help_activator', true);
                        return false;
                    });
            jQuery("#help_enter_close_s").click(
                    function() {
                        KS.sets.hide_tooltip('help_enter_s', true);
                        return false;
                    });
            jQuery("#help_activator_close_s").click(
                    function() {
                        KS.sets.hide_tooltip('help_activator_s', true);
                        return false;
                    });


            if (jQuery.cookie("open_rotater")) {
                jQuery("#rotater").css(dest_dim, dest_start_val);
                jQuery("#button_up").addClass('button_set_1').hide();
                jQuery("#do_ico").fadeIn(1000);

                state = 'active';
            } else {
                // if (!jQuery.cookie("help_activator")) {
                if (screenSmall) {
                    KS.sets.show_tooltip('help_activator_s');
                } else {
                    KS.sets.show_tooltip('help_activator');
                }
                //  } else {

                //   }
                jQuery("#rotater").css(dest_dim, "0px");
                state = 'closed';
            }

            jQuery("#button_up, #button_x").click(
                    function() {
                        if (screenSmall) {
                            KS.sets.rotater_activate_s();
                        } else {
                            KS.sets.rotater_activate();
                        }

                    }
            );
            var close_timeout;
            var open_timeout;
            jQuery("#button_left").click(
                    function() {
                        if (state == 'opened') {
                            if (screenSmall) {
                                KS.sets.rotater_close_s();
                            } else {
                                KS.sets.rotater_close();
                            }
                        } else {
                            if (screenSmall) {
                                KS.sets.rotater_open_s();
                            } else {
                                KS.sets.rotater_open();
                            }

                        }
                        clearTimeout(close_timeout);
                        clearTimeout(open_timeout);
                    });
            jQuery(".r_draggable .r_image").liveDraggable({ helper: 'clone', opacity: 1, refreshPositions: true, scroll: false, zIndex: 1507});
            jQuery("#canvas").droppable({
                activeClass: 'ui-state-hover',
                hoverClass: 'ui-state-active',
                accept: ".r_draggable .r_image",
                drop: function(event, ui) {
                    drop_clone = jQuery(ui.draggable);
                    var pid = drop_clone.attr('id').replace('x_', '');
                    if (KS.sets.map[pid] == undefined) {
                        if (!jQuery("#mini_" + drop_clone.attr('id').replace('x_', '')).length) {
                            KS.sets.pid_init(pid,
                                    0,
                                    0,
                                    0.5,
                                    0,
                                    drop_clone.attr('r_width'),
                                    drop_clone.attr('r_height'),
                                    drop_clone.attr('url1'),
                                    drop_clone.attr('url2'),
                                    drop_clone.attr('url3'),
                                    drop_clone.attr('title'),
                                    drop_clone.attr('price'),
                                    KS.sets.getMaxZIndex() + 1,
                                    1,
                                    false,
                                    drop_clone.attr('rating'));
                        }
                        var link = KS.sets.prodDropLink;
                        var param = KS.encodeURIComponentForTapestry(pid);
                        link = link.replace("PARAM", param);
                        KS.tapestry.triggerLink("proxyAjax", link);
                    }
                }
            });
            jQuery('body').droppable({
                accept: ".mini_ico",
                drop: function(event, ui6) {
                    ico_clone = jQuery(ui6.draggable);
                    var ico_pid = ico_clone.attr('id').replace('mini_', '');
                    jQuery("#mini_" + ico_pid).fadeOut(700, function() {
                        jQuery("#mini_" + ico_pid).remove();
                    });
                    jQuery("#mini_" + ico_pid).css({'left': 'auto', 'top': 'auto'});
                    delete KS.sets.map[ico_pid];
                    var link = KS.sets.prodDeleteLink;
                    var param = KS.encodeURIComponentForTapestry(ico_pid);
                    link = link.replace("PARAM", param);
                    KS.tapestry.triggerLink("proxyAjax", link);
                }
            });
            jQuery('#do_ico').droppable({
                accept: ".mini_ico",
                greedy: true,
                tolerance: 'touch',
                drop: function(event, ui7) {
                    ico_clone = jQuery(ui7.draggable);
                    var ico_pid = ico_clone.attr('id').replace('mini_', '');
                    jQuery("#mini_" + ico_pid).css({'left': 'auto', 'top': 'auto'});
                }
            });
            jQuery("#del-btn_confirm").fancybox({
                'autoDimensions': false,
                'width': 260,
                'height': 86,
                'title': 'Удаление объекта',
                'padding': 10,
                'titlePosition' : 'outside',
                'transitionIn' : 'none',
                'transitionOut' : 'none',
                'modal': true
            });
            jQuery("#ks_publish_set_btn").fancybox({
                'autoDimensions': false,
                'width' : 419,
                'height' : 35,
                'padding' : 15,
                'title': 'Публикация сета',
                'titlePosition' : 'outside',
                'transitionIn' : 'none',
                'transitionOut' : 'none'
            });
            jQuery("#rename-btn_confirm").fancybox({
                'autoDimensions': false,
                'width' : 419,
                'height' : 35,
                'padding' : 15,
                'title': 'Введите новое имя',
                'titlePosition' : 'outside',
                'transitionIn' : 'none',
                'transitionOut' : 'none',
                onComplete            :    function() {
                    jQuery("#setName").focus();
                }
            });
            jQuery("#ks_publish_cancel").bind({'click' : function() {
                jQuery.fancybox.close();
            }
            });
            jQuery("#ks_publish_go").click(function() {
                var inp = document.getElementById("KS-PRODS");
                inp.value = KS.sets.make_prod_params(false);
                KS.tapestry.submitZoneForm("publishSetFormAjax");
            });
            jQuery("a.clear_all").click(function() {
                for (var key in KS.sets.map) {
                    var entry = KS.sets.map[key];
                    jQuery("#case_" + entry.pid).remove();
                    jQuery("#mini_" + entry.pid).remove();
                    delete KS.sets.map[key];
                    jQuery("#info_base").hide();
                }
            });
        },
        init_set_tooltip: function(t, l, ob) {
            jQuery(ob).ssm('mouseover', {
                top:t
                ,left:l
                ,width:205
                ,position: 'absolute'
                ,relative: true
                ,autoclose:1000
                ,zindex:800
                ,content: jQuery("<span>Этот товар можно добавить в сет</span>")
                ,pointer : 'right'
                ,x_close : false
                ,onStart : function() {
                    if (state == 'closed') {
                        var screenSmall = (1024 >= screen.width) ? true : false;
                        if (screenSmall) {
                            KS.sets.rotater_activate_s();
                            KS.sets.blinkblink(350);
                        } else {
                            KS.sets.rotater_activate();
                            KS.sets.blinkblink(350);
                        }
                    } else {
                        KS.sets.blinkblink(350);
                    }
                }
            });
        }

    },
    flash_response: function(txt) {
        jQuery('.flash_msg').ssm('init', {
            id : 'flash_msg'
            ,top : 23
            ,left : 36
            ,width : 220
            ,position: 'fixed'
            ,relative: false
            ,autoclose : 5000
            ,x_close : false
            ,pointer : 'bottom'
            ,content: jQuery(txt)
            ,onStart : function() {
                var scrollTop = jQuery(document).scrollTop();
                var top_correction = (scrollTop > 93) ? 44 : (scrollTop < 93) ? 138 - scrollTop : 44;
                jQuery('#ssm_flash_msg').css({'top': 23 + top_correction});
            }
        });
        jQuery(window).scroll(function() {
            var scrollTop = jQuery(document).scrollTop();
            if (jQuery(document).scrollTop() < 93) {
                var top_correction = (scrollTop > 93) ? 44 : (scrollTop < 93) ? 138 - scrollTop : 44
                jQuery('#ssm_flash_msg').css({'top': 23 + top_correction})
            } else {
                jQuery('#ssm_flash_msg').css({'top' : 23 + 44})
            }
        });

    },
    basket_response: function() {
        jQuery("#basket_msg").ssm('init', {
            top:62
            ,left:575
            ,width:425
            ,position: 'fixed'
            ,relative: false
            ,autoclose : 5000
            ,x_close : true
            ,pointer : 'bottom'
        });
    },
    commentReplyFancyBox: function() {
        jQuery(".commentComment").fancybox({
            'type': 'inline',
            'autoDimensions': true,
            'padding': 20,
            'scrolling': 'visible',
            'title': 'Ответить',
            'titlePosition' : 'outside',
            'transitionIn' : 'none',
            'transitionOut' : 'none',
            onComplete :    function() {
                jQuery("#fancybox-content textarea").focus();
            },
            onCleanup : function() {
                jQuery(".t-error-popup").hide();
            }

        });
    },
    submitPhotoDelete: function(block, deleteLink) {
        var dels = "";
        jQuery(block + " .ch-msg:checked").each(function() {
            dels += jQuery(this).attr("name") + "-";
        });
        var param = KS.encodeURIComponentForTapestry(dels);
        var link = deleteLink.replace("PARAM", param);
        KS.tapestry.triggerLink("proxyListAjax", link);
    },
    hit_labeled: function() {
        if (jQuery('.ks_objectsearchconditions_2').attr('checked')) {
            jQuery('#hitPosition_label').show();
        } else {
            jQuery('#hitPosition_label').hide();
        }
    },
    lettersSelect: function(str) {
        jQuery("a.ks_filter_non").click(function() {
            return false;
        });
        str_arr = str.split('-');
        for (i = 0; i <= str_arr.length; i++) {
            jQuery("#F_" + str_arr[i]).removeClass("ks_filter_non");
            jQuery("#F_" + str_arr[i]).click(function() {
                window.location = jQuery(this).attr("href");
            });
        }
    }

};
jQuery.fn.friends = function() {
    var fr = jQuery(this);
    jQuery.each(fr, function(i) {
        jQuery(this).bind({
            click: function() {
                //jQuery.cookie(frid, "hide", {expires: 10});
                jQuery(this).parents("li.ks_user__all_item").addClass("ks_user__deleted");
                jQuery(this).parents("li.ks_user__all_item").find("span.ks_user__all_item_d2").css("display", "none");
            }
        });
    });
};

jQuery.fn.liveDraggable = function (opts) {
    this.live("mouseover", function() {
        if (!jQuery(this).data("init")) {
            jQuery(this).data("init", true).draggable(opts);
        }
    });
};

/* Resise for PRODUCTimages */
jQuery.fn.prod_images2 = function() {
    var pim = jQuery(this);
    jQuery.each(pim, function(i) {
        var psizeimg = jQuery(this);
        var psizeh = 270;
        var psizew = 398;
        var psizeimgh = psizeimg.height();
        var psizeimgw = psizeimg.width();
        var psizecomp = psizeh / psizew;
        var psizeimgcomp = psizeimgh / psizeimgw;

        var ua = navigator.userAgent;
        var IEoffset = ua.indexOf("MSIE ");
        var MSIEVersionNumber = parseFloat(ua.substring(IEoffset + 5, ua.indexOf(";", IEoffset)));
        if (MSIEVersionNumber == 7 && psizeimgw < 398 && psizeimgh < 270) {
            var mp = (psizeh - psizeimgh) / 2;
            psizeimg.css("margin-top", + mp + "px");
        }

        if (psizeimgcomp < psizecomp) { // landscape mode
            if (psizeimgh > 270) {
                //alert("По-новому lm, >270");
                psizeimg.css("width", "396px");
                psizeimg.css("visibility", "visible");
            } else {

            }
        }
        else if (psizeimgcomp > psizecomp) { // portrait mode
            if (psizeimgh > 270) {
                //alert("По-новому pm, >270");
                psizeimg.css("height", "270px");
                psizeimg.css("visibility", "visible");
            }
        }
        else { // square
            psizeimg.css("visibility", "visible");
        }
    });
};
