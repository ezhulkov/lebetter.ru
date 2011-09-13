var catTypes = new Array();
var catMultiples = new Array();
var addCatCounter = 0;
var singleCount = 0;
var singleTagCount = 0;
var allCount = 0;

function setSingleCount(c) {
    singleCount = c;
}

function setAllCount(c) {
    allCount = c;
}

function getSingleBoxHeight() {
    return 40 + singleCount * 15;
}

function getAllBoxHeight() {
    return 40 + allCount * 15;
}

function initCatArray(catId, catType, catMulti) {
    catTypes[catId] = catType;
    catMultiples[catId] = catMulti;
}

function addMultipleCategory(catId, valName) {

    jQuery("div.c-clear").css("display", "block");

    var catType = catTypes[catId];
    var catHolder = jQuery("#cat-holder-multi").find("#mas" + catId).find("#maph" + catId);
    addCatCounter++;
    var newTagId = catId + "-" + addCatCounter;
    if (catHolder.html() == null) {
        jQuery("#mas" + catId).clone(true).appendTo("#cat-holder-multi");//копируем рамку
        jQuery('#a' + catId).parent().css('display', 'none');//прячем ссылку на добавление
        catHolder = jQuery("#cat-holder-multi").find("#mas" + catId).find("#maph" + catId);//ищем куда сваливать теги
        var plusHolder = jQuery("#cat-holder-multi").find("#mas" + catId).find("#plush" + catId);//ищем куда сваливать плюсик
        jQuery("#mcadd" + catId).clone(true).appendTo(plusHolder);//копируем плюсик
        jQuery(catHolder).find("#mcadd" + catId).bind("click", function() {//вешаем на плюсик обработчик
            addMultipleCategory(catId, null);
            return false;
        })
    }

    var newTag = jQuery("#mc" + catId).clone(true);
    var newBox = jQuery("#mb" + catId).clone(true);
    newTag = replaceId(newTag, "id", catId, newTagId);
    newTag = replaceId(newTag, "name", catId, newTagId);
    jQuery(newTag).appendTo(jQuery(newBox).find("#mbe"));//переносим доп теги

    var newTagDel = jQuery("#dc" + catId).clone();
    var newTagDelId = catId + "-" + addCatCounter;
    newTag = replaceId(newTagDel, "id", catId, newTagDelId);
    newTag = replaceId(newTagDel, "name", catId, newTagDelId);
    jQuery(newTagDel).appendTo(jQuery(newBox).find("#mbd"));//переносим кнопку "удалить тег"
    jQuery(newTagDel).bind("click", function() {
        delMultiCategory($(this));
        return false;
    });
    jQuery(newBox).appendTo(catHolder);//переносим бокс с категорией и кнопкой

    initializeCategory(valName, catId, newTagId);

    var ctn = '2RM-SELECT-' + catId;
    jQuery("#cat-holder-multi").find("[id^='" + ctn + "']").selectbox();

}

function replaceId(ob, attrName, oldId, newId) {
    if (ob.attr(attrName) != null)
    {
        ob.attr("id", ob.attr(attrName).replace(oldId, newId));
    }
    var allOldEls = jQuery(ob).find("*");
    for (var i = 0; i < allOldEls.length; i++) {
        var o = jQuery(allOldEls[i]);
        var oldIdFull = o.attr(attrName);
        if (oldIdFull != null) {
            var newIdFull = oldIdFull.replace(oldId, newId);
            o.attr(attrName, newIdFull);
        }
    }
    return ob;
}

function addSingleCategory(catId, valName) {

    jQuery("div.c-clear").css("display", "block");

    var catType = catTypes[catId];
    jQuery("#sc" + catId).clone(true).appendTo('#cat-holder-single');
    jQuery('#a' + catId).parent().css('display', 'none');
    jQuery('#d' + catId).parent().css('display', 'block');
    jQuery('#d' + catId).css('display', 'block');

    initializeCategory(valName, catId, catId);

    singleTagCount = singleTagCount + 1;

    var ctn = '2RM-SELECT-' + catId;

    //var sbox1 = jQuery("#cat-holder").find("[id^='" + ctn + "']").selectbox();
    //var sbox2 = jQuery("select.catselect").selectbox();

   // setTimeout( function(){sbox1}, 10000 );
   // setTimeout( function(){sbox2}, 10000 );
    jQuery(document).ready(function() {
      jQuery("#cat-holder").find("[id^='" + ctn + "']").selectbox();
      jQuery("select.catselect").selectbox();
    });
}

function initializeColor(propId, valId) {
    var id="#2RM-COLORDIV-"+propId+"-"+valId;
    var id2="#2RM-COLOR-"+propId+"-"+valId;
    jQuery("form").find("div"+id).addClass("ks_select_color");
    jQuery("form").find("input"+id2).val(valId);
}

function initializeCategory(valName, catId, newCatId) {

    var catType = catTypes[catId];
    if (valName != null) {
        if (catType == 'LIST' || catType == 'DICTIONARY') {
            var selectChildren = jQuery("#cat-holder").find("#sc" + newCatId).find("select").children("option");
            //trace(selectChildren);
            if (selectChildren.length == 0)
            {
                selectChildren = jQuery("#cat-holder").find("#mc" + newCatId).find("select").children("option");
            }
            for (var i = 0; i < selectChildren.length; i++) {
                if (selectChildren[i].innerHTML == valName) {

                    var op = navigator.userAgent.toLowerCase();
                    isOpera = (op.indexOf("opera") != -1);
                    if(isOpera == true){
                      selectChildren[i].selected=true;
                    };
                    selectChildren[i].setAttribute("selected", "selected");

                }
            }
        } else {
            if (catType == 'VALUE') {
                var selectInput = jQuery("#cat-holder").find("#sc" + newCatId).find("input");
                if (selectInput.attr("name") == null)
                {
                    selectInput = jQuery("#cat-holder").find("#mc" + newCatId).find("input");
                }
                jQuery(selectInput).attr("value", valName);
            } else {
                if (catType == 'GROUP') {
                    var selectInputs = jQuery("#cat-holder").find("#sc" + newCatId).find("input");
                    var values = valName.split("-@-");
                    for (var i = 0; i < selectInputs.length; i++) {
                        jQuery(selectInputs[i]).attr("value", values[i]);
                    }
                }
            }
        }
    }
}

function delMultiCategory(ob) {
    var catId = ob.id.substring(2, ob.id.length);
    jQuery("#cat-holder-multi").find("#mc" + catId).remove();
    jQuery("#cat-holder-multi").find("#dc" + catId).remove();
}


//#SEARCH
var pTypeIds = new Array();
var pSubTypeIds = new Array();
var pTypeNames = new Array();
var catNames = new Array();
var valNames = new Array();
var valColors = new Array();
var colorNames = new Array();
var pTypeCatIds = new Array();
var catValuesIds = new Array();
var initCatIds = new Array();
var counter = 0;
var counterSub = 0;
var counterCat = 0;
var counterCat2 = 0;
var counterVal = 0;
var counterVal2 = 0;

function processSelectType() {
    var pId = jQuery("select#selectType").attr("value");
    jQuery("select#search-cat-select").each(function(index, opt) {
        jQuery(opt).find('option:first').attr('selected', 'selected').parent('select');
    });
    jQuery("select#selectSubType").css("display", "none");
    jQuery("select#selectSubType").empty();
    document.form.submit();
}

function processSelectSubType() {
    jQuery("select#search-cat-select").each(function(index, opt) {
        jQuery(opt).find('option:first').attr('selected', 'selected').parent('select');
    });
    document.form.submit();
}

function setType(pId) {
    jQuery("select#selectType").find("option").each(function(index, opt) {
        jQuery(opt).removeAttr("selected");
    });
    jQuery("select#selectType").find("option#" + pId).attr("selected", "yes");
}

function getFirstSubTypeId(pId) {
    var subArray = pSubTypeIds[pId];
    return subArray[0];
}

function setSubType(pId, pSID) {

    var subArray = pSubTypeIds[pId];
    var subSelectElement = jQuery("select#selectSubType");
    subSelectElement.empty();

    if (subArray != null) {
        if (pSID == null || pSID == "") {
            pSID = getFirstSubTypeId(pId);
        }
        subSelectElement.css("display", "block");
        for (var i = 0; i < subArray.length; i++) {
            if (subArray[i] == pSID) {
                subSelectElement.append("<option value='" + subArray[i] + "' selected='true'>" + pTypeNames[subArray[i]] + "</option>");
            } else {
                subSelectElement.append("<option value='" + subArray[i] + "'>" + pTypeNames[subArray[i]] + "</option>");
            }
        }
    } else {
        subSelectElement.css("display", "none");
    }
}

function setCategories(pId, expandedSearch) {

    var catsElement = jQuery("div#search-cat-holder");
    var catsSelectHolder = jQuery("div#search-cat-select-holder");
    var catsSelect = jQuery("select#search-cat-select");
    catsSelectHolder.empty();

    if (pTypeCatIds[pId] == null) {
        catsElement.css("display", "none");
        return;
    }

    catsElement.css("display", "block");
    var cats = pTypeCatIds[pId];

    var limit = cats.length;
    for (var i = 0; i < limit; i++) {
        var catBox = jQuery("div#search-cat-template").clone(true);
        jQuery(catBox).attr("id", "search-cat-box" + cats[i]);
        if (i >= 3) {
            jQuery(catBox).addClass("cat-expanded-view");
            if (expandedSearch == true) {
                jQuery(catBox).css("display", "block");
            } else {
                jQuery(catBox).css("display", "none");
            }
        } else {
            jQuery(catBox).css("display", "block");
        }
        jQuery(catBox).appendTo(catsSelectHolder);
        jQuery(catBox).find("div#search-cat-name").append(catNames[cats[i]]);
        var sel = jQuery(catBox).find("select#search-cat-select");
        jQuery(sel).attr("name", "2RM-SB-" + cats[i]);

        var vals = catValuesIds[cats[i]];
        if (vals != null) {
            for (var j = 0; j < vals.length; j++) {
                if (initCatIds[cats[i]] == vals[j]) {
                    jQuery(sel).append("<option value='" + vals[j] + "' selected='true' colorCode='" + valColors[vals[j]] + "'>" + valNames[vals[j]] + "</option>");
                } else {
                    jQuery(sel).append("<option value='" + vals[j] + "' colorCode='" + valColors[vals[j]] + "'>" + valNames[vals[j]] + "</option>");
                }
            }
        }

    }

}