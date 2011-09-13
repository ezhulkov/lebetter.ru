/**
 * jQuery custom selectboxes
 *
 * Copyright (c) 2008 Krzysztof Suszyński (suszynski.org)
 * Licensed under the MIT License:
 * http://www.opensource.org/licenses/mit-license.php
 *
 * @version 0.6.1
 * @category visual
 * @package jquery
 * @subpakage ui.selectbox
 * @author Krzysztof Suszyński <k.suszynski@wit.edu.pl>
**/
var highestZIndex=100;
jQuery.fn.selectbox = function(options){
	/* Default settings */
	var settings = {
		className: 'jquery-selectbox',
		animationSpeed: "fast",
		listboxMaxSize: 10,
		replaceInvisible: false,
		shadowMode: true
	};
	var commonClass = 'jquery-custom-selectboxes-replaced';
	var listOpen = false;

var userAgent = navigator.userAgent.toLowerCase();
 
	var showList = function(listObj) {
		highestZIndex=highestZIndex+1;
		var selectbox = listObj.parents('.' + settings.className + '');
        listOpen = true;
    selectbox.addClass('selecthover');
    selectbox.css("z-index",highestZIndex);
    listObj.css("display","block");
		selectbox.find('p').addClass(settings.className + '-shown');
		selectbox.find('.jquery-selectbox-currentItem').addClass('lowlight');
    listObj.css("z-index","1");
		jQuery(document).bind('click', onBlurList);
		return listObj;
	}

	var hideList = function(listObj) {
		var selectbox = listObj.parents('.' + settings.className + '');
        listOpen = false;
    selectbox.css("z-index",highestZIndex);
    selectbox.removeClass('selecthover');
    listObj.css("display","none");
    listObj.css("z-index","1");
		selectbox.find('p').removeClass(settings.className + '-shown');
    selectbox.find('.jquery-selectbox-currentItem').removeClass('lowlight');
		jQuery(document).unbind('click', onBlurList);
		return listObj;
	}
	var onBlurList = function(e) {
		var trgt = e.target;
		var currentListElements = jQuery('.' + settings.className + '-list:visible').parent().find('*').andSelf();
		if(jQuery.inArray(trgt, currentListElements)<0 && listOpen) {
			hideList( jQuery('.' + commonClass + '-list') );
		}
		return false;
	}

	/* Processing settings */
	settings = jQuery.extend(settings, options || {});
	/* Wrapping all passed elements */
	return this.each(function() {
		var _this = jQuery(this);

		if(_this.filter(':visible').length == 0 && !settings.replaceInvisible)
			return;
		var replacement = jQuery(
			'<div class="' + settings.className + ' ' + commonClass + '">' +
				'<p class="' + settings.className + '-shadow" />' + '<div class="' + settings.className + '-moreButton">' + '<span class="' + settings.className + '-currentItem"/>' + '</div>' +
				'<div class="' + settings.className + '-list ' + commonClass + '-list" />' +
			'</div>'
		);


        jQuery('option,optgroup', _this).each(function(k, v) {
            var v = jQuery(v);
            if (v.attr('label') && v.attr('label') != 'undefined') {
                var listElement = jQuery('<span class="optgroupon">' + v.attr('label') + '</span>');

            } else {
                if (v.val() == '') {
                    var listElement = jQuery('<span class="' + settings.className + '-item value-NONE item-' + k + '">' + ((v.text() != '') ? v.text() : 'Любое значение')  + '</span>');
                } else {
                    var listElement = jQuery('<span class="' + settings.className + '-item value-' + v.val() + ' item-' + k + '">' + ((v.text() != '') ? v.text() : 'Любое значение')  + '</span>');
                }
            }
			listElement.click(function(){
				var thisListElement = jQuery(this);
				var thisReplacment = thisListElement.parents('.'+settings.className);
				var thisIndex = thisListElement[0].className.split(' ');
				for( k1 in thisIndex ) {
					if(/^item-[0-9]+$/.test(thisIndex[k1])) {
						thisIndex = parseInt(thisIndex[k1].replace('item-',''), 10);
						break;
					}
				};
				var thisValue = thisListElement[0].className.split(' ');
				for( k1 in thisValue ) {
					if(/^value-.+$/.test(thisValue[k1])) {
						thisValue = thisValue[k1].replace('value-','');
                        if (thisValue == "NONE") {
                            thisValue = '';
                    }
						break;
					}
				};
        var is_text_empty = thisListElement.text();
        if (is_text_empty == ' ') {
          thisReplacment
                  .find('.' + settings.className + '-currentItem')
                  .addClass("is_text_empty")
                  .text("Любое значение");
        } else {
          thisReplacment
                  .find('.' + settings.className + '-currentItem')
                  .removeClass("is_text_empty")
                  .text(thisListElement.text());
        }
                    if (v.attr('colorCode') && v.attr('colorCode') != 'undefined') {
                        jQuery('.'+settings.className + '-currentItem', replacement).wrapInner('<b class="sel_spanwrap"/>');
                        jQuery('.'+settings.className + '-currentItem', replacement).prepend('<em class="colored_option" style="background:' + v.attr('colorCode')  + ';"></em>');
                    }
				thisReplacment
					.find('select')
					.val(thisValue)
					.triggerHandler('change');

				var thisSublist = thisReplacment.find('.' + settings.className + '-list');
				if(thisSublist.filter(":visible").length > 0) {
					hideList( thisSublist );
				}else{
					showList( thisSublist );
				}
			}).bind('mouseenter',function(){
				jQuery(this).addClass('listelementhover');
			}).bind('mouseleave',function(){
				jQuery(this).removeClass('listelementhover');
			});
			jQuery('.' + settings.className + '-list', replacement).append(listElement);
			if(v.filter(':selected').length > 0) {
                if (v.text() != '') {
                  jQuery('.' + settings.className + '-currentItem', replacement).removeClass("is_text_empty").text(v.text())
                } else {
                  jQuery('.' + settings.className + '-currentItem', replacement).addClass("is_text_empty").text('Выберите значение...')
                }


        if (v.attr('colorCode') && v.attr('colorCode') != 'undefined') {
                        //jQuery('.'+settings.className + '-currentItem', replacement).css('padding-left','40px');
                        jQuery('.'+settings.className + '-currentItem', replacement).prepend('<em class="colored_option" style="background:' + v.attr('colorCode')  + ';"></em>');
                    }
			}
		});
		replacement.find('.' + settings.className + '-moreButton').click(function(){
			var thisMoreButton = jQuery(this);
			var otherLists = jQuery('.' + settings.className + '-list')
				.not(thisMoreButton.siblings('.' + settings.className + '-list'));
			hideList( otherLists );
			var thisList = thisMoreButton.siblings('.' + settings.className + '-list');
			if(thisList.filter(":visible").length > 0) {
				hideList( thisList );
			}else{
				showList( thisList );
			}
		}).bind('mouseenter',function(){
			jQuery(this).addClass('morebuttonhover');
		}).bind('mouseleave',function(){
			jQuery(this).removeClass('morebuttonhover');
		});
		_this.hide().replaceWith(replacement).appendTo(replacement);
		var thisListBox = replacement.find('.' + settings.className + '-list');
		var thisShadowBox = replacement.find('.' + settings.className + '-shadow');

		var thisListBoxSize = thisListBox.find('.' + settings.className + '-item').length;
		if(thisListBoxSize > settings.listboxMaxSize)
			thisListBoxSize = settings.listboxMaxSize;

		if(thisListBoxSize == 0)
			thisListBoxSize = 1;
		var thisListBoxWidth = Math.round(_this.width() + 15);

        //alert (thisListBoxWidth);
		//if(jQuery.ssafari.safari)thisListBoxWidth = thisListBoxWidth + 24;
//        replacement.css('z-index', '1');
		replacement.css('width', Math.round(thisListBoxWidth) + 'px');
		thisListBox.css({
			width: Math.round(thisListBoxWidth) + 'px',
			height: thisListBoxSize * 25  + 'px'
		});
		thisShadowBox.css({
			height: thisListBoxSize * 25 + 12 + 7  + 'px',
			display: "none"
		});
		jQuery(".fe-cat-controls span.jquery-selectbox-item").css("margin-top","2px");
	});
}
jQuery.fn.unselectbox = function(){
	var commonClass = 'jquery-custom-selectboxes-replaced';
	return this.each(function() {
		var selectToRemove = jQuery(this).filter('.' + commonClass);
		selectToRemove.replaceWith(selectToRemove.find('select').show());
	});
}