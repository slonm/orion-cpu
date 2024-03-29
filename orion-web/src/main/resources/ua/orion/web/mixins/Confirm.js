/**
 * Функция дополняет обработку нажатия на ссылку события компонента
 * вызовом диалогового окна подтверждения вызова события
 * @author sl
 */
Tapestry.Initializer.oriConfirm = function(o){
    var link=$J("#"+o.id);
    var href = link.attr("href");
    if(o.zone != undefined){
        Ori.Event.unbind('#'+o.id, Tapestry.ACTION_EVENT);
    }
    link.click(function(){
        event.preventDefault();
        //На базе этого блока построим диалог
        var div=$J("<div/>").appendTo("body").html(o.body!=undefined ? o.body : Ori.Messages.messageConfirmationBody);
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
                        var zoneObject = Tapestry.findZoneManagerForZone(o.zone);
                        if (zoneObject) zoneObject.updateFromURL(href);
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
