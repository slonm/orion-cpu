/*
 * Функция, которая будет выполняться, когда объектная часть документа готова
 * к использованию
 */
jQuery(document).ready(function(){
    jQuery("#sp_logo1").fadeIn(3500);
    jQuery("#wlctocpu").fadeIn(1500);
    //Обработка menunavigator-а
    jQuery(".ui-menunavigator-link").hover(function(){
        jQuery(this).parent().removeClass("ui-state-default").addClass("ui-state-focus");
        jQuery("#ui-menu-navigator-detail").html(jQuery(this).find(".ui-menunavigator-link-text").text());
    },function(){
        jQuery(this).parent().removeClass("ui-state-focus");
    });
    jQuery(".menu-navigator-item-block").click(function(){
        jQuery(".ui-menunavigator-link").parent().removeClass("ui-state-active");
        jQuery(this).find(".ui-menunavigator-link").removeClass("ui-state-focus").addClass("ui-state-active");
    })
    jQuery(".menuarrow, .menutitle").hover(function(){
        jQuery(this).addClass("ui-state-active");
    },function(){
        jQuery(this).removeClass("ui-state-active");
    })
    jQuery(".menuitem").hover(function(){
        jQuery(this).addClass("ui-state-active");
    },function(){
        jQuery(this).removeClass("ui-state-active");
    })
})


