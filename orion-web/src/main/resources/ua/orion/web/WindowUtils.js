/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008-2010 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
Tapestry.Initializer.showCkWindow = function(opt){
    Tapestry.Initializer.hideGridAjax();
    var win=$(opt.window).getStorage().ck_window;
    //Инициализация ui-интерфейса для формы 
    if (jQuery("ui#interface").text()=="true"){
        //Кнопки в beaneditor справа
        jQuery(".t-beaneditor-row:last").css({
            'float':'right'
        });
        initializeUIComponents();
    }
    //Показывать диалог загрузки при сохранении
    jQuery("form[action*='editform'] div div>button[type='submit'], form[action*='addform'] div div>button[type='submit']").click(function(){
        Tapestry.Initializer.showGridAjaxSave();
    })
    //Получаем высоту окна из параметра
    win.height=opt.height;
    //Получаем ширину окна из параметра
    win.width=opt.width;
    opt.modal=opt.modal!==false?true:false;
    if(opt.atCenter!==false) win.showCenter(opt.modal);
    else win.show(opt.modal);
    if(opt.title) win.setTitle(opt.title);
    //Создание подсказок
    if (opt.showhints=="true"){
        createToolTips();
    }
}

Tapestry.Initializer.updateCkWindow = function(opt){
    var win=$(opt.window).getStorage().ck_window;
    win.updateWidth();
    win.updateHeight();
}

Tapestry.Initializer.closeCkWindow = function(opt){
    Windows.close(opt.window);
}

Tapestry.Initializer.updateGrid = function(opt){
    jQuery(".crud-links a, #crud-add-record-button").click(function(){
        Tapestry.Initializer.showGridAjax();
    })
    Tapestry.Initializer.hideGridAjax();
    updateCSS();
    initializeGridToolTip();
    
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
