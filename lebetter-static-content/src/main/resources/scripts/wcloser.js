jQuery.fn.popupboxGrid = function() {
    var itemButGrid = jQuery("img.addf-but"); // кнопка
    var itemWindowGrid = 'box' + jQuery(this).attr("id"); // кнопка

    itemButGrid.click(function(e) {
        jQuery('.rm_ctrl_wcwin').removeClass("rm_ctrl_visible");
        jQuery('#box' + jQuery(this).attr("id")).addClass("rm_ctrl_visible");

    });
};

// Функция управления всплывающими окнами
jQuery.fn.popupbox = function(){

    var pw = jQuery(this);
    jQuery.each(pw, function(i) {
      jQuery(this).addClass("rm_ctrl_wci_" + i);
      //jQuery(this).find(".rm_ctrl_wcb")[0].className = jQuery(this).find(".rm_ctrl_wcb")[0].className.replace(/\brm_ctrl_wcb_i_.\d-*?\b/g, '');
      jQuery(this).find(".rm_ctrl_wcb").addClass("rm_ctrl_wcb_i_" + i);



      jQuery(this).find(".rm_ctrl_wcwin").addClass("rm_ctrl_wcwin_i_" + i);

      var itemi = jQuery(".rm_ctrl_wci_" + i);
      var itemBut = jQuery(".rm_ctrl_wcb_i_" + i); // кнопка
      var itemWindow = itemi.find(".rm_ctrl_wcwin_i_" + i); // всплывающее окошко

      if (itemBut.is('img.rm_ctrl_wcb_toggle')) {

          itemBut.toggle(function(){
            jQuery('.rm_ctrl_wcwin').removeClass("rm_ctrl_visible");
            itemWindow.addClass("rm_ctrl_visible");
            // jQuery(this).popupboxcoord();
          },
          function(){
            jQuery('.rm_ctrl_wcwin').removeClass("rm_ctrl_visible");
            itemWindow.removeClass("rm_ctrl_visible");
          }
        );

      }else{
        // если НЕ требуется toggle()
        itemBut.click(function(e){
          jQuery('.rm_ctrl_wcwin').removeClass("rm_ctrl_visible");
          itemWindow.addClass("rm_ctrl_visible");
          jQuery('.rm_ctrl_wcwin').find('select').selectbox(); // для всплывающих окон с выпадающим списком запускаем функцию
          if (itemWindow.is('div.rm_ctrl_close')) {
            itemWindow.find("input:nth-child(1)").focus(); // фокусируемся на первом поле в окне
          }
          jQuery('.rm_ctrl_wcwin').css('margin-top', 'auto');
          pospos(itemWindow, itemBut);

        });

      }

      // Считаем координаты окна, чтобы располагать его всегда в видимой части окна

      function pospos(obj, objbut) {
          var visibleHeight = jQuery(window).height();
          var scrollHeight = jQuery(document).scrollTop();
          var InnerBlocksHeight = Math.round(obj.height() + objbut.height());
          var ExtremumHeight = Math.round(scrollHeight + (visibleHeight*0.7));

          if (obj.offset().top > ExtremumHeight) obj.css('margin-top', - InnerBlocksHeight + 'px');
      }
      jQuery(window).scroll(function() {
          jQuery('.rm_ctrl_wcwin').css('margin-top', 'auto');
          obj_vis = jQuery('div.rm_ctrl_visible');
          obj_vis_but = obj_vis.parent();
          if (obj_vis.length != 0) pospos(obj_vis, obj_vis_but);
      });
      // pospos(itemWindow, itemBut);

      jQuery(document).click(function(e){
        if (jQuery(e.target).filter('.rm_ctrl_wcb').length != 1 && jQuery(e.target).parents().filter('.rm_ctrl_wcwin').length != 1 && jQuery(e.target).filter('.rm_ctrl_wcwin:visible').length != 1) {
        jQuery('.rm_ctrl_wcwin').removeClass("rm_ctrl_visible");
        jQuery(".t-error-popup").css("display","none");
      }

      });

      jQuery(".NFButtonRes, #buttonEsc").click(
          function (e) {
            if (jQuery(e.target).parents().is('div.rm_ctrl_visible')) {
              jQuery(e.target).parents().removeClass("rm_ctrl_visible");
              jQuery(".t-error-popup").css("display","none");
            }
          }
      );

      jQuery(".NFButton, #buttonAdd, .rm_ctrl_closelnk").click(
          function (e) {
            if (jQuery(e.target).parents().is('div.rm_ctrl_close') && jQuery('input.t-error').length != 1) {
              jQuery(e.target).parents().removeClass("rm_ctrl_visible");
              jQuery(".t-error-popup").css("display","none");
            }else{

            }
          }
      );

    });

};


// Делаем всплываюшие сообщения сайта (мессаджи под лого) плавающими

jQuery(document).ready(function() {

  var toppaderr = jQuery(document).scrollTop();

  // alert(toppaderr);

  jQuery(window).scroll(function() {
    //alert(jQuery(document).scrollTop());
    if(jQuery(document).scrollTop()>100) {
       jQuery('#err').addClass('fixed').end();
    } else {
       jQuery('#err').removeClass('fixed').end();
    }

  });

});


// Делаем кнопку далее в пошаговом поиске плавающей

jQuery(document).ready(function() {

  var topBut = jQuery('div.gridspace_but');

  if(topBut.length > 0){

    jQuery(window).scroll(function() {
      stepbutpos(topBut);
    });
    jQuery(window).resize(function() {
      stepbutpos(topBut);
    });
    stepbutpos(topBut);

  }

  function stepbutpos(i) {
    var top = i.position().top;

    if(jQuery(document).scrollTop()>top + 10) {
       i.addClass('fixedbut').end();
    } else {
       i.removeClass('fixedbut').end();
    }
    var botLineWin = jQuery(document).scrollTop() + jQuery(window).height();
    if(i.position().top > botLineWin) {
       i.addClass('fixedbut').end();
    } else {
       i.removeClass('fixedbut').end();
    }

  }

});



// Собираем карту сайта
jQuery(document).ready(function() {

  var sm = jQuery("div.rm_stmap_prods > .rm_stmap_item_num");
  jQuery.each(sm, function(i) {

     if(i<3) {
       jQuery(this).appendTo(".rm_stmap_col_1");
     };
     if(i>=3 && i<6) {
       jQuery(this).appendTo(".rm_stmap_col_2");
     };
     if(i>=6 && i<9) {
       jQuery(this).appendTo(".rm_stmap_col_3");
     };
     if(i>=9) {
       jQuery(this).appendTo(".rm_stmap_col_4");
     };

  });

});


