/*
 * 	Easy Tooltip 1.0 - jQuery plugin
 *	written by Alen Grakalic	
 *	http://cssglobe.com/post/4380/easy-tooltip--jquery-plugin
 *
 *	Copyright (c) 2009 Alen Grakalic (http://cssglobe.com)
 *	Dual licensed under the MIT (MIT-LICENSE.txt)
 *	and GPL (GPL-LICENSE.txt) licenses.
 *
 *	Built for jQuery library
 *	http://jquery.com
 *
 *     Модифицирован dvelopp
 *     Теперь есть возможность динамического формирования подсказок,
 *     выбора элементов, на которые будут распространятся подсказки. 
 *     Подсказки теперь отображают значение или текст, находящийся в 
 *     элементе, а не его атрибут title. 
 */
 
/*
 * Функция обеспечивает совместимость с другими библиотеками, например - Prototype
 */
jQuery.noConflict();
 
(function(jQuery) {

    jQuery.fn.easyTooltip = function(options){
	  
        // default configuration properties
        var defaults = {	
            xOffset: 10,		
            yOffset: 25,
            tooltipId: "easyTooltip",
            clickRemove: false,
            content: "",
            useElement: ""
        }; 
			
        var options = jQuery.extend(defaults, options);  
        var content;
        /* Данная переменная служит для того, чтобы назначить правильный обработчик 
        формирования подсказки */
        var ctype=options.ctype;		
        this.each(function() { 		
            jQuery(this).hover(function(e){	
                var title;
                //Обработчик формирования подсказки для текстовых полей
                if (ctype=="input[type=text]"){                    
                    title = jQuery(this).val();
                //Обработчик формирования подсказки для выпадающих списков(select)
                } else if (ctype=="select"){
                    title = jQuery(this).find("option:selected").text();
                } //Здесь можно дописать еще обработчики	
                content = (options.content != "") ? options.content : title;
                content = (options.useElement != "") ? jQuery("#" + options.useElement).html() : content;
                jQuery(this).attr("title","");	
                if (content != "" && content != undefined){			
                    jQuery("body").append("<div id='"+ options.tooltipId +"'>"+ content +"</div>");		
                    jQuery("#" + options.tooltipId)
                    .css("position","absolute")
                    .css("top",(e.pageY - options.yOffset) + "px")
                    .css("left",(e.pageX + options.xOffset) + "px")						
                    .css("display","none")
                    //Устанавливаем слой для подсказки выше, чем у окна с формой
                    .css("z-index",parseInt(jQuery("#popupWindow").css("z-index"))+1)
                    .fadeIn("fast")
                }
            },
            function(){	
                jQuery("#" + options.tooltipId).remove();
                jQuery(this).attr("title",title);
            });		
            jQuery(this).mousemove(function(e){
                jQuery("#" + options.tooltipId)
                .css("top",(e.pageY - options.yOffset) + "px")
                .css("left",(e.pageX + options.xOffset) + "px")					
            });	
            if(options.clickRemove){
                jQuery(this).mousedown(function(e){
                    jQuery("#" + options.tooltipId).remove();
                    jQuery(this).attr("title",title);
                });				
            }
        });
	  
    };

})(jQuery);
