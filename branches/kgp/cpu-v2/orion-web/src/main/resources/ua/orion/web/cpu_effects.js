/*
 * Функция обеспечивает совместимость с другими библиотеками, например - Prototype
 */
jQuery.noConflict();
/*
 * Функция, которая будет выполняться, когда объектная часть документа готова
 * к использованию
 */
jQuery(document).ready(function(){
    jQuery("#sp_logo1").fadeIn(3500);
    jQuery("#wlctocpu").fadeIn(1500);   
})

/*
 *Декорация для кнопки в BeanEditor
 */
function decorateButtonInBeanEditor(){
    var bESBM=jQuery("DIV.t-beaneditor DIV.t-beaneditor-row input.t-beaneditor-submit");
    bESBM.addClass("btn btnbe");  
    if (jQuery("DIV.t-beaneditor DIV.t-beaneditor-row input[type=submit]").size()>0){
        jQuery("DIV.t-beaneditor DIV.t-beaneditor-row input[type=submit]").after("<br/><br/>");
    } 
}

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
