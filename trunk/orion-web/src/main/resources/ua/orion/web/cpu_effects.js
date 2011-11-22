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
    //Инициализация подсказок
    initializeGridToolTip();
    //Инициализация окна для отображения загрузки Ajax
    jQuery('#ui-ajax-loading-window').dialog({
        modal:true, //модальность
        autoOpen: false, //автоматическое открытие
        open: function() { 
            jQuery(".ui-dialog-titlebar-close").hide(); //убирается кнопка закрытия
        },
        resizable: false //возможность маштабирования отключена
    });
        jQuery("#crud-add-record-button").click(function(){
        Tapestry.Initializer.showGridAjax(); //При добавлении записи появляется окно загрузки
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
    jQuery("input[type=text]").easyTooltip({
        ctype:"input[type=text]"
    });
    jQuery("select").easyTooltip({
        ctype:"select"
    });
}


/**
 * Появление подсказки над строками Grid с кнопками actioncell. 
 */
function initializeGridToolTip(){
    //При наведении на строку появляется tooltip
    jQuery("table.t-data-grid tbody tr").each(function(){
        jQuery(this).tooltip({
            effect: 'slide', //Тип эффекта
            delay: '150', //Время исчезновения при отведении мыши
            tip: jQuery(this).find(".ui-grid-cell-action-tip"), //Элемент с подсказкой
            position: 'bottom center' //Позиция
        });
    })   
    //Настройки изменения стилей при наведении и отведении мыши
    jQuery(".ui-grid-cell-action-tip").css({
        'width':jQuery("table.t-data-grid").css("width")-2,
        'margin-left':jQuery("table.t-data-grid").css("margin-left")
        });
    jQuery("table.t-data-grid tbody tr td[class!='action']").bind("mouseenter", function(){
        jQuery(this).parent().find("td[class!='action']").addClass("ui-state-highlight").removeClass("ui-widget-content");
    });
    jQuery("table.t-data-grid tbody tr td[class!='action']").bind("mouseleave", function(){
        jQuery(this).parent().find("td[class!='action']").addClass("ui-widget-content").removeClass("ui-state-highlight");
    });	
}