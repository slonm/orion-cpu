//Инициализация классов кнопок, к которым присоеденяются иконки
//tfalse в конце используется для того, чтобы сделать кнопку без текста. 
var elements = new Array("ui-button-close","ui-button-add","ui-button-refresh","ui-button-login","ui-button-quit","ui-button-del","ui-button-cancel","ui-button-edit-tfalse","ui-button-view-tfalse","ui-button-del-tfalse");
//Инициализация классов иконок, для кнопок в массиве elements
var icons = new Array("ui-icon-close","ui-icon-circle-plus","ui-icon-refresh","ui-icon-key","ui-icon-power","ui-icon-trash", "ui-icon-cancel", "ui-icon-pencil","ui-icon-print","ui-icon-trash");
    
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
    jQuery(".ui-button").button();
}
//Инициализация кнопок для Submit
function initializeSubmits(){
    //Submit преобразуется в <button type="submit"/>SUBMIT</button>
    jQuery(".t-beaneditor-row input[type=submit], .ui-submit").each(function(){
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

//Инициализация компонентов
function initializeUIComponents(){
    initializeTextFields();
    initializeCalendar();
    initializeButtons();
    initializeSubmits();
    initializeIcons();
    //Создание выпадающего списка из select
    jQuery("select").selectmenu();
    initializeCheckboxes();
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
}
