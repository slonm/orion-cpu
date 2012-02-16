//Инициализация классов кнопок, к которым присоеденяются иконки
//tfalse в конце используется для того, чтобы сделать кнопку без текста. 
var elements = new Array("ui-button-close","ui-button-add","ui-button-refresh","ui-button-login","ui-button-quit","ui-button-del","ui-button-cancel","ui-button-edit-tfalse","ui-button-view-tfalse","ui-button-del-tfalse","ui-button-tolist","ui-button-right-list-tfalse");
//Инициализация классов иконок, для кнопок в массиве elements
var icons = new Array("ui-icon-close","ui-icon-circle-plus","ui-icon-refresh","ui-icon-key","ui-icon-power","ui-icon-trash", "ui-icon-cancel", "ui-icon-pencil","ui-icon-print","ui-icon-trash","ui-icon-arrowreturnthick-1-n","ui-icon-triangle-1-e");
    
//Инициализация иконок на кнопках
//ec - класс элемента HTML, ic - класс иконик (из jquery-ui)
function createIcon(ec, ic){
    jQuery("."+ec).button({
        icons: {
            primary: ic
        },
        text: ec.indexOf("tfalse")>-1?false:true
    })
}
//Инициализация кнопок.
function initializeButtons(){
    jQuery(".ui-button[role!=button]").button();
}
//Инициализация кнопок для Submit
function initializeSubmits(){
    //Submit преобразуется в <button type="submit"/>SUBMIT</button>
    jQuery(".t-beaneditor-row input[type=submit], .ui-submit[role!=button]").each(function(){
        jQuery(this).replaceWith('<button class="ui-submit" type="' + jQuery(this).attr('type') + '">' + jQuery(this).val() + '</button>');
    });
    jQuery(".ui-submit").button({
        icons: {
            primary: "ui-icon-circle-check"
        }
    });
}
//Инициализация иконок для кнопок
function initializeIcons(){
    for (var i=0; i<elements.length; i++){
        createIcon(elements[i], icons[i]);
    }
}
//Создание ui-checkbox-ов 
function initializeCheckboxes(){
    jQuery("input[type=checkbox]").each(function(){
        bindCheckBoxes(this);
    })   
}
//Создание ui-текстовых полей
function initializeTextFields(){
    jQuery("input[type=text], textarea").each(function(){
        bindTextElements(this);
    })
}
/**
 * Добавление UI-классов к элементам beaneditor и beandisplay
 */
function addUIClassesToBeanEditView(){
    jQuery("DIV.t-beaneditor").addClass("ui-widget-content").removeClass("t-beaneditor");
    jQuery("dl.t-beandisplay").addClass("ui-widget-content").removeClass("t-beandisplay");
}

//Инициализация компонентов
function initializeUIComponents(){
    addUIClassesToBeanEditView();
    putCookieThemeToSelect();
    initializeTextFields();
    initializeCalendar();
    initializeButtons();
    initializeSubmits();
    initializeIcons();
    //Создание выпадающего списка из select
    ///jQuery("select").selectmenu();
    initializeCheckboxes();
    jQuery("button").parent('a').css({
        'text-decoration':'none'
    })
    applyCookieTheme();
}
//Инициализация календаря
function initializeCalendar(){
    //Установка локали
    jQuery.datepicker.setDefaults(jQuery.datepicker.regional['uk']);
    //Создание календаря
    jQuery(".t-calendar-trigger").parent().find("input").datepicker({
        showButtonPanel: true,  
        dateFormat: 'dd.mm.yy',
        showAnim: 'slideDown',
        duration: 'slow',
        showOtherMonths: true,
        selectOtherMonths: true,
        showWeek: true,
        changeMonth: true,
        changeYear: true,
        yearRange: '1900:2050'
    });
    //Скрытие стандартного вызова календаря от Tapestry
    jQuery(".t-calendar-trigger").css({
        'display':'none'
    });
}

//Инициализация полей ввода
function bindTextElements(element){	
    //Скругление текстовых полей
    jQuery(element).addClass("ui-state-default ui-corner-all");
    //При активации текстового поля оно подсвечивается
    jQuery(element).bind("focusin", function(e){
        jQuery(this).toggleClass("ui-state-focus ui-state-default");
    });
    //При деактивации текстового поля оно теряет подсветку(фокус)
    jQuery(element).bind("focusout", function(e){
        jQuery(this).toggleClass("ui-state-focus ui-state-default");
    });			 
}

//Инициализация checkbox-ов
function bindCheckBoxes(element){
    //Выполняются специальный обрамления
    jQuery(element).wrap("<sp />");
    jQuery(element).addClass("ui-state-default");
    jQuery(element).parent("sp").after("<span />");
    var parent =  jQuery(element).parent("sp").next();
    jQuery(element).addClass("ui-helper-hidden");
    parent.css({
        width:20,
        height:20,
        display:"block"
    });
    parent.wrap("<span class='ui-state-default ui-corner-all' style='display:inline-block;width:16px;height:16px;margin-right:5px;'/>");		 
    parent.parent().addClass('hover');
    //Выполняется установка начального состояния
    if (jQuery(element).is(":checked")){
        parent.addClass("ui-icon ui-icon-check");
        parent.parent("span").addClass("ui-state-active");
    }
    //Обработка нажатия на фиктивный checkbox
    parent.parent("span").click(function(event){
        jQuery(this).toggleClass("ui-state-active");
        parent.toggleClass("ui-icon ui-icon-check");
        jQuery(element).click();		
    });		 
}

Tapestry.Initializer.initializeIcons = function(opt){
    initializeIcons();
    initializeUIClasses();
}

//Установка начального значения выбранной темы в select
function putCookieThemeToSelect(){
    jQuery("#ui-select-theme option").each(function(){
        if (jQuery(this).html()==jQuery.cookie("ui-theme")){
            jQuery(this).attr("selected", "selected");
        }
    })
}

//Применение темы
function applyCookieTheme(){
    if (jQuery.cookie("ui-theme")!=null){
        var what = "css/.*/jquery-ui-1.8.16.custom.css";
        var to = "css/"+jQuery.cookie("ui-theme")+"/jquery-ui-1.8.16.custom.css";
        jQuery("head link").each(function(){
            if (jQuery(this).attr("href").indexOf("jquery-ui-1.8.16.custom.css")>-1) {
                jQuery(this).attr("href",jQuery(this).attr("href").replace(new RegExp (what, 'g'), to));
            }
        });
    }
}
/**
 * Добавление UI-классов к элементам интерфейса
 */
function updateCSS(){
    jQuery("table.t-data-grid tbody tr td[class!='action']").addClass("ui-widget-content");
    jQuery("table.t-data-grid thead tr th[class!='action t-last']").addClass("ui-state-default");
    jQuery("div.t-data-grid-pager a").addClass("ui-corner-all ui-state-default");
    jQuery("div.t-data-grid-pager span.current").addClass("ui-corner-all ui-state-active");
    jQuery(".ui-grid-cell-action-tip").addClass("ui-corner-tr ui-corner-br ui-state-default");
    initializeUIComponents();
}

//При полностью загруженной структуре DOM
jQuery(document).ready(function(){
    jQuery("#select-theme-button").click(function(){
        jQuery.cookie("ui-theme", jQuery("#ui-select-theme option:selected").text());
        applyCookieTheme();
    })
    jQuery("#ui-show-select-theme-dialog-button").click(function(){
        jQuery("#ui-select-theme-dialog").dialog()
    })
})