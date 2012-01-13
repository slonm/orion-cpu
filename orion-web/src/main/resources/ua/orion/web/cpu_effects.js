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
    jQuery(".ui-menunavigator-link").hover(function(){
        jQuery(this).parent().attr("class","ui-state-focus");
    },function(){
        jQuery(this).parent().attr("class","ui-state-default");
    })
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
    jQuery("table.t-data-grid tbody tr td .ui-button-right-list-tfalse").each(function(){
        jQuery(this).tooltip({
            effect: 'slide', //Тип эффекта
            delay: '150', //Время исчезновения при отведении мыши
            tip: jQuery(this).parent().parent().find(".ui-grid-cell-action-tip"), //Элемент с подсказкой
            position: 'center right' //Позиция
        });
    })   
    //Настройки изменения стилей при наведении и отведении мыши
    jQuery(".ui-grid-cell-action-tip").css({
        'width':jQuery("table.t-data-grid").css("width")-2,
        'margin-left':jQuery("table.t-data-grid").css("margin-left")
    });

}