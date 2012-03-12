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
    var panel=$J(".ui-grid-cell-action-tip");
    panel.css("display", "none");
    $J("<button class='ui-button-right-list-tfalse'/>").appendTo(panel.parent())
    .tooltip({
        effect: 'fade', //Тип эффекта
        delay: '150', //Время исчезновения при отведении мыши
        tip: panel, //Элемент с подсказкой
        position: 'center right' //Позиция
    });
    $J(".ui-grid-cell-action-tip a").bind("click", function(){
        panel.hide();
    });
    //Настройки изменения стилей при наведении и отведении мыши
    panel.css({
        'width':$J("table.t-data-grid").css("width")-2,
        'margin-left':$J("table.t-data-grid").css("margin-left")
    });
    $J("table.t-data-grid").wrapAll("<div id=\"grid-container\"></div>");
}