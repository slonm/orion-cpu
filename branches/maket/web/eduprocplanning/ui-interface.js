//Инициализация кнопок.
function initializeButtons(){
    jQuery("input[type=button]").each(function(){
        jQuery(this).after("<button class=\"ui-button\">"+jQuery(this).val()+"</button>").remove();
    })
    jQuery(".ui-button").button();
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


//Инициализация компонентов
function initializeUIComponents(){
    initializeTextFields();
    initializeCalendar();
    initializeButtons();
    //Создание выпадающего списка из select
    jQuery("select").selectmenu();
    initializeCheckboxes();
    jQuery("button").parent('a').css({
        'text-decoration':'none'
    })
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
