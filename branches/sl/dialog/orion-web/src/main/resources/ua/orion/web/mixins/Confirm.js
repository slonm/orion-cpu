/**
 * Функция дополняет обработку нажатия на ссылку события компонента
 * вызовом диалогового окна подтверждения вызова события
 * @author sl
 */
Tapestry.Initializer.oriConfirm = function(o){
    var link=$J("#"+o.id);
    var href = link.attr("href");
    if(o.zone != undefined){
        //Имя нового пользовательского события
        var evName="ori:zoneupdate";
        //FIXME Как удалить только слушатель назначаемый updateZoneOnEvent?
        //сейчас удаляются ВСЕ слушатели click
        Ori.Event.unbind('#'+o.id, 'click');
        Tapestry.Initializer.updateZoneOnEvent(evName, o.id, o.zone, href);
    //Теперь для обновления зоны нужно вызвать событие evName
    }
    link.click(function(event, nested){
        if(nested==true) return;
        event.preventDefault();
        //На базе этого блока построим диалог
        var div=$J("<div/>").appendTo("body").text(o.body!=undefined ? o.body : Ori.Messages.messageConfirmationBody);
        div.dialog({
            modal:true,
            title:o.title!=undefined ? o.title : Ori.Messages.labelConfirmationTitle,
            resizable:false,
            zIndex:9999,
            buttons: [{
                text: o.confirm!=undefined ? o.confirm : Ori.Messages.actionConfirmationConfirm,
                click: function() {
                    if(o.zone == undefined){
                        //Переходим на ссылку
                        window.location.href = href;
                    }else{
                        //Обновляем зону
                        Ori.Event.trigger('#'+o.id, evName);
                    }
                    div.dialog("close");
                }
            }, {
                text: o.decline!=undefined ? o.decline : Ori.Messages.actionConfirmationDecline,
                click: function() {
                    div.dialog("close");
                }
            }],
            close: function() {
                div.dialog("destroy");
                div.remove();
            },
            open: function() {
                div.parents('.ui-dialog-buttonpane button:eq(1)').focus(); 
            }
        });
    });
}
