jQuery.noConflict();
jQuery(document).ready(function(){
    //Появление картинки КПУ на главной странице
    jQuery("#sp_logo1").fadeIn(3500);
    //Появление надписи приветствия
    jQuery(".userntable, #wlctocpu").fadeIn(1500);
    //Появление окошка с ошибкой
    jQuery(".error-report-table").css({
        'width':'300px'
    }).animate({
        width: "800px"
    }, 1500, function(){
        jQuery(this).find("tbody tr.title-error-report-mes td p").fadeIn(1500).css({
            'line-height':'100%',
            'margin-bottom':'0em'
        });
    }).find("tbody tr.title-error-report-mes td p").css({
        'display':'none'
    });
    //Редактирование записи в таблице при двойном щелчке
    jQuery("table.t-data-grid tbody tr td").dblclick(function(){
        var editg = jQuery(this).parent().find("td.action").find("a img[alt='Редагувати']");
        if (editg.size()>0){
            if (jQuery.browser.opera) {
                if (confirm("EDIT: "+jQuery(this).parent().find("td:first").text()+"?")) {
                    editg.click();
                }
            }
            else {
                if (confirm("Editing function is not supported by your browser! Try it anyway?")){
                    editg.click();
                }
            }
        }   
    })
    //Скрытие кнопки авторизации в окне авторизации
    if ((document.location.href.indexOf("login")!=-1)&&(document.location.href.indexOf("loginfailed")<0)){
        jQuery(".userntable").css({
            'display':'none'
        });
       
    }
})