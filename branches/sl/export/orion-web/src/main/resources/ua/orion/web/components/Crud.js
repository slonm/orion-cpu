Tapestry.Initializer.crudUIListeners = function(o){
    var crudLoadPopupZone = function(){
        //Кнопки в beaneditor справа
        $J(".t-beaneditor-row:last").css({
            'float':'right'
        });
    }

    var crudLoadListZone = function(){
        //Добавим кнопку, при наведении на которую появится tooltip
        //с кнопками действий
        $J(".ori-grid-cell-action-tip").css("display", "none")
        .each(function(){
            var panel=$J(this);
            var cell=panel.parent();
            cell.append("<b>&nbsp;&gt;&gt;</b>").addClass("ui-state-default")
            .css("min-width", "1.8em").hover(
                function () {
                    $J(this).addClass("ui-state-hover");
                }, 
                function () {
                    $J(this).removeClass("ui-state-hover");
                })
            .tooltip({
                effect: 'fade', //Тип эффекта
                delay: '150', //Время исчезновения при отведении мыши
                tip: panel, //Элемент с подсказкой
                position: 'center right' //Позиция
            });
            panel.find("a").bind("click", function(){
                panel.hide();
            });
        });
    }
    
    Ori.Event.bind('#'+o.popupZone, Tapestry.ZONE_UPDATED_EVENT, crudLoadPopupZone);
    Ori.Event.bind('#'+o.listZone, Tapestry.ZONE_UPDATED_EVENT, crudLoadListZone);
    crudLoadListZone();
}

