
Tapestry.Initializer.showCkWindow = function(opt){
    Tapestry.Initializer.hideGridAjax();
    if (jQuery("ui#interface").text()=="true"){
        //Кнопки в beaneditor справа
        jQuery(".t-beaneditor-row:last").css({
            'float':'right'
        });
        initializeUIComponents();
    }
    //    jQuery("form[action*='editform'] div div>button[type='submit'], form[action*='addform'] div div>button[type='submit']").click(function(){
    //        Tapestry.Initializer.showGridAjaxSave();
    //    })
    //Создание подсказок
    if (Ori.SHOW_HINTS){
        createToolTips();
    }
}

Tapestry.Initializer.updateGrid = function(){
    jQuery(".ui-grid-cell-action-tip a").click(function(){
        Tapestry.Initializer.showGridAjax();
    })
    Tapestry.Initializer.hideGridAjax();
    initializeGridToolTip();
    jQuery("table.t-data-grid").wrapAll("<div id=\"grid-container\"></div>");
    updateCSS();
}

/**
 * Показать полосу загрузки в шапке actioncell по нажатию на следующие кнопки
 */
Tapestry.Initializer.showGridAjax = function(){
    jQuery("#ui-ajax-loading-window-load-text").css({
        'display':'block'
    });
    jQuery('#ui-ajax-loading-window').dialog("open");     
}

/**
 * Показать полосу загрузки в шапке actioncell по нажатию на следующие кнопки
 */
Tapestry.Initializer.showGridAjaxSave = function(){
    jQuery("#ui-ajax-loading-window-save-text").css({
        'display':'block'
    });
    jQuery('#ui-ajax-loading-window').dialog("open");     
}

/**
 * Скрыть полосу загрузки в шапке actioncell
 */
Tapestry.Initializer.hideGridAjax = function(){
    jQuery("#ui-ajax-loading-window-load-text").css({
        'display':'none'
    });
    jQuery("#ui-ajax-loading-window-save-text").css({
        'display':'none'
    });
    jQuery('#ui-ajax-loading-window').dialog("close");   
}
