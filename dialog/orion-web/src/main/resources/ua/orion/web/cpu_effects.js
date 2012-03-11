/*
 * Функция, которая будет выполняться, когда объектная часть документа готова
 * к использованию
 */
jQuery(document).ready(function(){
    jQuery("#sp_logo1").fadeIn(3500);
    jQuery("#wlctocpu").fadeIn(1500);
    //Инициализация ui-интерфейса
    if (jQuery("ui#interface").text()=="true"){
        initializeUIComponents();   
    }
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
    jQuery(".crud-add-record-button").click(function(){
        Tapestry.Initializer.showGridAjax(); //При добавлении записи появляется окно загрузки
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


/*
 * Создание подсказок для элементов. 
 * Используется модифицированный плагин Easy Tooltip 1.0
 * @param функция easyTooltip вызывается на элементах, на которые необходимо
 * назначить подсказки и параметром в ней является текстовая строка, которая 
 * служит для идентификации типа элемента. 
 */
function createToolTips(){
    if (Ori.SHOW_HINTS){
        jQuery("input[type=text]").easyTooltip({
            ctype:"input[type=text]"
        });
        jQuery("select").easyTooltip({
            ctype:"select"
        });
    }
}
