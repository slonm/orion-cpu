/**
 * @class Window 
 * @author sl
 */
Tapestry.Initializer.oriWindow = function(opt){
    //Если окно небыло инициализировано ранее, то следующая строка 
    //ничего не сделает, а иначе будет инициализация окна с параметрами
    //по умолчанию. Только выключено автоматическое открытие
    jQuery("#" + opt.id).dialog({
        autoOpen: false
    });
    jQuery("#" + opt.id).dialog('option', opt);
    if(opt.event!=undefined){
        jQuery("#" + opt.id).dialog(opt.event);
    }
};
