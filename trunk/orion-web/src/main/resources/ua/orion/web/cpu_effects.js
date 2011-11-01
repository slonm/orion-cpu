/*
 * Функция, которая будет выполняться, когда объектная часть документа готова
 * к использованию
 */
jQuery(document).ready(function(){
    jQuery("#sp_logo1").fadeIn(3500);
    jQuery("#wlctocpu").fadeIn(1500);
    //Инициализация ui-интерфейса
    if (jQuery("ui#interface").text()=="true"){
        initializeUIComponents();   
    }
})


/*
 * Создание подсказок для элементов. 
 * Используется модифицированный плагин Easy Tooltip 1.0
 * @param функция easyTooltip вызывается на элементах, на которые необходимо
 * назначить подсказки и параметром в ней является текстовая строка, которая 
 * служит для идентификации типа элемента. 
 */
function createToolTips(){
    jQuery("input[type=text]").easyTooltip({
        ctype:"input[type=text]"
    });
    jQuery("select").easyTooltip({
        ctype:"select"
    });
}
