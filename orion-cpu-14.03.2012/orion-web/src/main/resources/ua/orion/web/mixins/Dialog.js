/**
 * Инициализация и выполнение показа/скрытия диалогового окна
 * @author sl
 */
Tapestry.Initializer.oriDialog = function(opt){
    //Если окно не было инициализировано ранее, то следующая строка 
    //ничего не сделает. Иначе будет инициализация окна с параметрами
    //по умолчанию, только выключено автоматическое открытие.
    jQuery("#" + opt.oriId).dialog({
        autoOpen: false
    });
    //Установка параметров окна
    jQuery("#" + opt.oriId).dialog('option', opt);
    //Показ/скрытие окна
    if(opt.oriEvent!=undefined){
        jQuery("#" + opt.oriId).dialog(opt.oriEvent);
    }
};
