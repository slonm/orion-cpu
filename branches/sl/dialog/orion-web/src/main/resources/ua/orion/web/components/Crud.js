Tapestry.Initializer.crudUIListeners = function(o){
    Ori.Event.bind('#'+o.popupZone, Tapestry.ZONE_UPDATED_EVENT, Ori.crudLoadPopupZone);
    Ori.Event.bind('#'+o.listZone, Tapestry.ZONE_UPDATED_EVENT, Ori.crudLoadListZone);
    Ori.crudLoadListZone();
}

Ori.crudLoadPopupZone = function(){
    //Кнопки в beaneditor справа
    $J(".t-beaneditor-row:last").css({
        'float':'right'
    });
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
            tip: $J(this), //Элемент с подсказкой
            position: 'center right' //Позиция
        });
    })   
    //Настройки изменения стилей при наведении и отведении мыши
    $J(".ui-grid-cell-action-tip").css({
        'width':$J("table.t-data-grid").css("width")-2,
        'margin-left':$J("table.t-data-grid").css("margin-left")
    });
    $J("table.t-data-grid").wrapAll("<div id=\"grid-container\"></div>");
}