jQuery.noConflict();
jQuery(document).ready(function(){
    jQuery("#sp_logo1").fadeIn(3500);
    jQuery("#wlctocpu").fadeIn(1500);
})

//Декорация для кнопки в BeanEditor
function decorateButtonInBeanEditor(){
    var bESBM=jQuery("DIV.t-beaneditor DIV.t-beaneditor-row input.t-beaneditor-submit");
    bESBM.addClass("btn btnbe");  
    if (jQuery("DIV.t-beaneditor DIV.t-beaneditor-row input[type=submit]").size()>0){
        jQuery("DIV.t-beaneditor DIV.t-beaneditor-row input[type=submit]").after(jQuery("a[shape=rect].close"));
        jQuery("a[shape=rect].close").after("<br/><br/><br/>");
    } 
}


