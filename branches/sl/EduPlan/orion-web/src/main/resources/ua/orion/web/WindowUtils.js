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
        jQuery(".t-beaneditor-row:last").css({'float':'right'});
        initializeUIComponents();
    }
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

/**
 * Показать полосу загрузки в шапке actioncell по нажатию на следующие кнопки
 */
Tapestry.Initializer.showGridAjax = function(opt){
    jQuery(".crud-edit-pic, .crud-view-pic, .crud-del-pic, .crud-add").click(function(){
        jQuery("th.t-last").html("<div class=\"ajax-state-grid\"></div>");
    });     
}
/**
 * Скрыть полосу загрузки в шапке actioncell
 */
Tapestry.Initializer.hideGridAjax = function(opt){
    jQuery("th.t-last").html("");    
}