Tapestry.Initializer.crudUIListeners = function(o){
    try{
        $(o.popupZone).observe(Tapestry.ZONE_UPDATED_EVENT, Ori.crudLoadPopupZone);
        $(o.listZone).observe(Tapestry.ZONE_UPDATED_EVENT, Ori.crudLoadListZone);
    }catch(e){}
    $J("#"+o.popupZone).bind(Tapestry.ZONE_UPDATED_EVENT, Ori.crudLoadPopupZone);
    $J("#"+o.listZone).bind(Tapestry.ZONE_UPDATED_EVENT, Ori.crudLoadListZone);
    Ori.crudLoadListZone();
}

Ori.crudLoadPopupZone = function(){
    if ($J("ui#interface").text()=="true"){
        //Кнопки в beaneditor справа
        $J(".t-beaneditor-row:last").css({
            'float':'right'
        });
        initializeUIComponents();
    }
    //Создание подсказок
    createToolTips();
}

Ori.crudLoadListZone = function(){
    //Добавим кнопку, при наведении на которую появится tooltip
    //с кнопками действий 
    $J(".ui-grid-cell-action-tip").each(function(){
        $J(this).css("display", "none");
        $J("<button class='ui-button-right-list-tfalse'/>").appendTo($J(this).parent())
        .tooltip({
            effect: 'fade', //Тип эффекта
            delay: '150', //Время исчезновения при отведении мыши
            tip: jQuery(this), //Элемент с подсказкой
            position: 'center right' //Позиция
        });
    })   
    //Настройки изменения стилей при наведении и отведении мыши
    $J(".ui-grid-cell-action-tip").css({
        'width':$J("table.t-data-grid").css("width")-2,
        'margin-left':$J("table.t-data-grid").css("margin-left")
    });
    $J("table.t-data-grid").wrapAll("<div id=\"grid-container\"></div>");
    updateCSS();
    initializeUIComponents();
}