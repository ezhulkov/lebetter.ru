var highestZIndex=100;
jQuery.fn.selectbox=function(e){var f={className:"jquery-selectbox",animationSpeed:"fast",listboxMaxSize:10,replaceInvisible:false,shadowMode:true};
var h="jquery-custom-selectboxes-replaced";
var d=false;
var g=navigator.userAgent.toLowerCase();
var c=function(j){highestZIndex=highestZIndex+1;
var i=j.parents("."+f.className+"");
d=true;
i.addClass("selecthover");
i.css("z-index",highestZIndex);
j.css("display","block");
i.find("p").addClass(f.className+"-shown");
i.find(".jquery-selectbox-currentItem").addClass("lowlight");
j.css("z-index","1");
jQuery(document).bind("click",b);
return j
};
var a=function(j){var i=j.parents("."+f.className+"");
d=false;
i.css("z-index",highestZIndex);
i.removeClass("selecthover");
j.css("display","none");
j.css("z-index","1");
i.find("p").removeClass(f.className+"-shown");
i.find(".jquery-selectbox-currentItem").removeClass("lowlight");
jQuery(document).unbind("click",b);
return j
};
var b=function(k){var i=k.target;
var j=jQuery("."+f.className+"-list:visible").parent().find("*").andSelf();
if(jQuery.inArray(i,j)<0&&d){a(jQuery("."+h+"-list"))
}return false
};
f=jQuery.extend(f,e||{});
return this.each(function(){var n=jQuery(this);
if(n.filter(":visible").length==0&&!f.replaceInvisible){return
}var k=jQuery('<div class="'+f.className+" "+h+'"><p class="'+f.className+'-shadow" /><div class="'+f.className+'-moreButton"><span class="'+f.className+'-currentItem"/></div><div class="'+f.className+"-list "+h+'-list" /></div>');
jQuery("option,optgroup",n).each(function(p,o){var o=jQuery(o);
if(o.attr("label")&&o.attr("label")!="undefined"){var q=jQuery('<span class="optgroupon">'+o.attr("label")+"</span>")
}else{if(o.val()==""){var q=jQuery('<span class="'+f.className+"-item value-NONE item-"+p+'">'+((o.text()!="")?o.text():"Любое значение")+"</span>")
}else{var q=jQuery('<span class="'+f.className+"-item value-"+o.val()+" item-"+p+'">'+((o.text()!="")?o.text():"Любое значение")+"</span>")
}}q.click(function(){var v=jQuery(this);
var u=v.parents("."+f.className);
var w=v[0].className.split(" ");
for(k1 in w){if(/^item-[0-9]+$/.test(w[k1])){w=parseInt(w[k1].replace("item-",""),10);
break
}}var r=v[0].className.split(" ");
for(k1 in r){if(/^value-.+$/.test(r[k1])){r=r[k1].replace("value-","");
if(r=="NONE"){r=""
}break
}}var s=v.text();
if(s==" "){u.find("."+f.className+"-currentItem").addClass("is_text_empty").text("Любое значение")
}else{u.find("."+f.className+"-currentItem").removeClass("is_text_empty").text(v.text())
}if(o.attr("colorCode")&&o.attr("colorCode")!="undefined"){jQuery("."+f.className+"-currentItem",k).wrapInner('<b class="sel_spanwrap"/>');
jQuery("."+f.className+"-currentItem",k).prepend('<em class="colored_option" style="background:'+o.attr("colorCode")+';"></em>')
}u.find("select").val(r).triggerHandler("change");
var t=u.find("."+f.className+"-list");
if(t.filter(":visible").length>0){a(t)
}else{c(t)
}}).bind("mouseenter",function(){jQuery(this).addClass("listelementhover")
}).bind("mouseleave",function(){jQuery(this).removeClass("listelementhover")
});
jQuery("."+f.className+"-list",k).append(q);
if(o.filter(":selected").length>0){if(o.text()!=""){jQuery("."+f.className+"-currentItem",k).removeClass("is_text_empty").text(o.text())
}else{jQuery("."+f.className+"-currentItem",k).addClass("is_text_empty").text("Выберите значение...")
}if(o.attr("colorCode")&&o.attr("colorCode")!="undefined"){jQuery("."+f.className+"-currentItem",k).prepend('<em class="colored_option" style="background:'+o.attr("colorCode")+';"></em>')
}}});
k.find("."+f.className+"-moreButton").click(function(){var q=jQuery(this);
var p=jQuery("."+f.className+"-list").not(q.siblings("."+f.className+"-list"));
a(p);
var o=q.siblings("."+f.className+"-list");
if(o.filter(":visible").length>0){a(o)
}else{c(o)
}}).bind("mouseenter",function(){jQuery(this).addClass("morebuttonhover")
}).bind("mouseleave",function(){jQuery(this).removeClass("morebuttonhover")
});
n.hide().replaceWith(k).appendTo(k);
var m=k.find("."+f.className+"-list");
var l=k.find("."+f.className+"-shadow");
var i=m.find("."+f.className+"-item").length;
if(i>f.listboxMaxSize){i=f.listboxMaxSize
}if(i==0){i=1
}var j=Math.round(n.width()+15);
k.css("width",Math.round(j)+"px");
m.css({width:Math.round(j)+6+"px",height:i*25+"px"});
l.css({height:i*25+12+7+"px",display:"none"});
jQuery(".fe-cat-controls span.jquery-selectbox-item").css("margin-top","2px")
})
};
jQuery.fn.unselectbox=function(){var a="jquery-custom-selectboxes-replaced";
return this.each(function(){var b=jQuery(this).filter("."+a);
b.replaceWith(b.find("select").show())
})
};