/**
 * @class Window 
 * @author sl
 */
Ori.confirm = function(event, opt){
    if(!opt) return true;
    event.preventDefault();
    event.stopPropagation();
    var targetUrl = jQuery("#"+opt.id).attr("href");
    var div=jQuery("<div/>").appendTo("body").text(opt.body!=undefined ? opt.body : Ori.Messages.messageConfirmationBody);
    div.dialog({
        modal:true,
        title:opt.title!=undefined ? opt.title : Ori.Messages.labelConfirmationTitle,
        resizable:false,
        buttons: [{
            text: opt.decline!=undefined ? opt.decline : Ori.Messages.actionConfirmationDecline,
            click: function() {
                div.dialog("close");
            }
        },{
            text: opt.confirm!=undefined ? opt.confirm : Ori.Messages.actionConfirmationConfirm,
            click: function() {
                if(opt.ajax == undefined){
                    window.location.href = targetUrl;
                }else{
                    jQuery("#"+opt.id).trigger('click', false);
                }
                div.dialog("close");
            }
        } ],
        close: function() {
            div.dialog("destroy");
            div.remove();
        }
    });
    return false;
};
