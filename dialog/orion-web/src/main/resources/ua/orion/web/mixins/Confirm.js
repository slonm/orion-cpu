/**
 * Функция дополняет обработку нажатия на ссылку события компонента
 * вызовом диалогового окна подтверждения вызова события
 * @author sl
 */
Tapestry.Initializer.oriConfirm = function(o){
    var link=jQuery("#"+o.id);
    var href = link.attr("href");
    if(o.zone != undefined){
        //Имя нового пользовательского события
        var evName="ori:zoneupdate";
        //Remove handlers
        //JQuery
        link.unbind('click');
        //Prototype (if supported) 
        try{
            $(o.id).stopObserving('click');
        }catch(e){}
        //TODO Протестировать для реализации tapestry.js на базе jQuery
        Tapestry.Initializer.updateZoneOnEvent(evName, o.id, o.zone, href);
    //Теперь для обновления зоны нужно вызвать событие evName
    }
    link.click(function(event, nested){
        if(nested==true) return;
        event.preventDefault();
        //На базе этого блока построим диалог
        var div=jQuery("<div/>").appendTo("body").text(o.body!=undefined ? o.body : Ori.Messages.messageConfirmationBody);
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
                        link.trigger(evName);
                        //Prototype (if supported) 
                        try{
                            $(o.id).fire(evName);
                        }catch(e){}
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
