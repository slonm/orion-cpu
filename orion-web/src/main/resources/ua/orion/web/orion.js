var $J = jQuery;
var Ori = {
    /**
    * Обертки для методов работы с событиями, позволяющие абстрагироваться от JQuery и Prototype
    */       
    Event : {
        trigger : function(selector, eventName){
            $J(selector).trigger(eventName);
            try{
                $$(selector).each(function(e){e.fire(eventName)});
            }catch(e){}
        },
        bind : function(selector, eventName, handler){
            $J(selector).bind(eventName, handler);
            try{
                $$(selector).each(function(e){e.observe(eventName, handler)});
            }catch(e){}
        },
        unbind : function(selector, eventName){
            $J(selector).unbind(eventName);
            try{
                $$(selector).each(function(e){e.stopObserving('click')});
            }catch(e){}
        }
    },
    AjaxIndicator : {
        oldAjaxRequest : Tapestry.ajaxRequest,
        indicator: undefined,
        on : function(){
            if(Ori.AjaxIndicator.indicator===undefined){
                Ori.AjaxIndicator.indicator=$J("<img/>").appendTo("body")
                .addClass("ori-ajax-loading").attr("src", Ori.ORION_ASSET_PATH+"images/progressbar5.gif");
            }
        },
        off : function(){
            if(Ori.AjaxIndicator.indicator!==undefined){
                Ori.AjaxIndicator.indicator.remove();
                Ori.AjaxIndicator.indicator=undefined;
            }            
        }
    }
}

//Сделаем перехватчик AJAX запросов Tapestry, который будет
//индицировать процесс обработки запроса анимированной картинокой
jQuery(document).ready(function(){
    Tapestry.ajaxRequest=function(url, options){            
        if (Object.isFunction(options)) {
            options={
                onSuccess : options
            };
        }
        Ori.AjaxIndicator.on();
        var wrapOptions=Object.clone(options || {});
        wrapOptions.onSuccess=function(response, jsonResponse){
            Ori.AjaxIndicator.off();
            options.onSuccess(response, jsonResponse);
        }
        wrapOptions.onException=function(response, jsonResponse){
            if(options.onException !==undefined) options.onException(response, jsonResponse);
            else Tapestry.ajaxExceptionHandler(response, jsonResponse);
            Ori.AjaxIndicator.off();
        }
        wrapOptions.onFailure=function(response, jsonResponse){
            if(options.onFailure !==undefined) options.onFailure(response, jsonResponse);
            else Tapestry.ajaxFailureHandler(response, jsonResponse);
            Ori.AjaxIndicator.off();
        }
        return Ori.AjaxIndicator.oldAjaxRequest(url, wrapOptions);
    };
});