//Декорация UI интерфейса после загрузки страницы и после 
//обновления зоны
$J(document).ready(function(){
    //Инициализация классов кнопок, к которым присоеденяются иконки
    //TODO Почему бы не сделать это в виде ассоциативного массива? Пример:
    //var elements={"ui-button-close":"ui-icon-close",
    //              "ui-button-add":"ui-icon-circle-plus"}
    //tfalse в конце используется для того, чтобы сделать кнопку без текста. 
    //var elements = new Array("ui-button-close","ui-button-add","ui-button-refresh","ui-button-login","ui-button-quit","ui-button-del","ui-button-cancel","ui-button-edit-tfalse","ui-button-view-tfalse","ui-button-del-tfalse","ui-button-tolist","ui-button-right-list-tfalse");
    var elements = new Array("ui-button-close","ui-button-add","ui-button-refresh","ui-button-login","ui-button-quit","ui-button-del","ui-button-cancel","ui-button-edit-tfalse","ui-button-view-tfalse","ui-button-del-tfalse","ui-button-tolist","ui-button-right-list-tfalse",
        "ui-button-detail", "ui-button-edit","ui-button-view","ui-button-del", "ui-button-check");
    //Инициализация классов иконок, для кнопок в массиве elements
    //var icons = new Array("ui-icon-close","ui-icon-circle-plus","ui-icon-refresh","ui-icon-key","ui-icon-power","ui-icon-trash", "ui-icon-cancel", "ui-icon-pencil","ui-icon-print","ui-icon-trash","ui-icon-arrowreturnthick-1-n","ui-icon-triangle-1-e");
    var icons = new Array("ui-icon-close","ui-icon-circle-plus","ui-icon-refresh","ui-icon-key","ui-icon-power","ui-icon-trash", "ui-icon-cancel", "ui-icon-pencil","ui-icon-print","ui-icon-trash","ui-icon-arrowreturnthick-1-n","ui-icon-triangle-1-e", 
        "ui-icon-zoomin", "ui-icon-pencil", "ui-icon-search", "ui-icon-circle-minus", "ui-icon-circle-check");
    
    function initUI(event){
        //Если event не задан, то применим инициализацию ко всему телу документа,
        //иначе это инициализация зоны
        root=!event?$J("body")[0]:this;
        
        //Инициализация иконок на кнопках
        //ec - класс элемента HTML, ic - класс иконик (из $J-ui)
        function createIcon(ec, ic){
            $J("."+ec).button({
                icons: {
                    primary: ic
                },
                text: ec.indexOf("tfalse")>-1?false:true
            })
        }
        //Инициализация кнопок.
        function initializeButtons(){
            $J(".ui-button[role!=button]", root).button();
        }
        //Инициализация кнопок для Submit
        function initializeSubmits(){
            //Submit преобразуется в <button type="submit"/>SUBMIT</button>
            $J(".t-beaneditor-row input[type=submit], .ui-submit[role!=button]", root).each(function(){
                $J(this).replaceWith('<button class="ui-submit" type="' + $J(this).attr('type') + '">' + $J(this).val() + '</button>');
            });
            $J(".ui-submit", root).button({
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
            $J("input[type=checkbox]", root).each(function(){
                bindCheckBoxes(this);
            })   
        }
        //Создание ui-текстовых полей
        function initializeTextFields(){
            $J("input[type=text], textarea", root).each(function(){
                bindTextElements(this);
            })
        }
        /**
 * Добавление UI-классов к элементам beaneditor и beandisplay
 */
        function addUIClassesToBeanEditView(){
            $J("DIV.t-beaneditor", root).addClass("ui-widget-content").removeClass("t-beaneditor");
            $J("dl.t-beandisplay", root).addClass("ui-widget-content").removeClass("t-beandisplay");
        }

        //Инициализация компонентов
        function initializeUIComponents(){
            addUIClassesToBeanEditView();
            initializeTextFields();
            initializeCalendar();
            initializeButtons();
            initializeSubmits();
            initializeIcons();
            //Создание выпадающего списка из select
            $J("select", root).selectmenu();
            initializeCheckboxes();
            $J(".ui-buttonset", root).buttonset();
            $J("button", root).parent('a').css({
                'text-decoration':'none'
            })
        }
        //Инициализация календаря
        function initializeCalendar(){
            //Установка локали
            $J.datepicker.regional['uk'].dateFormat=$J.datepicker.regional['ru'].dateFormat;
            $J.datepicker.setDefaults($J.datepicker.regional[Ori.LOCALE]);
            //Создание календаря
            $J(".t-calendar-trigger", root).parent().find("input").datepicker({
                showButtonPanel: true,  
                //        dateFormat: 'dd.mm.yy',
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
            $J(".t-calendar-trigger", root).css({
                'display':'none'
            });
        }

        //Инициализация полей ввода
        function bindTextElements(element){	
            //Скругление текстовых полей
            $J(element).addClass("ui-state-default ui-corner-all");
            //При активации текстового поля оно подсвечивается
            $J(element).bind("focusin", function(e){
                $J(this).toggleClass("ui-state-focus ui-state-default");
            });
            //При деактивации текстового поля оно теряет подсветку(фокус)
            $J(element).bind("focusout", function(e){
                $J(this).toggleClass("ui-state-focus ui-state-default");
            });			 
        }

        //Инициализация checkbox-ов
        function bindCheckBoxes(element){
            //Выполняются специальный обрамления
            $J(element).wrap("<sp />");
            $J(element).addClass("ui-state-default");
            $J(element).parent("sp").after("<span />");
            var parent =  $J(element).parent("sp").next();
            $J(element).addClass("ui-helper-hidden");
            parent.css({
                width:20,
                height:20,
                display:"block"
            });
            parent.wrap("<span class='ui-state-default ui-corner-all' style='display:inline-block;width:16px;height:16px;margin-right:5px;'/>");		 
            parent.parent().addClass('hover');
            //Выполняется установка начального состояния
            if ($J(element).is(":checked")){
                parent.addClass("ui-icon ui-icon-check");
                parent.parent("span").addClass("ui-state-active");
            }
            //Обработка нажатия на фиктивный checkbox
            parent.parent("span").click(function(event){
                $J(this).toggleClass("ui-state-active");
                parent.toggleClass("ui-icon ui-icon-check");
                $J(element).click();		
            });		 
        }

        /**
 * Добавление UI-классов к элементам интерфейса
 */
        function updateCSS(){
            //$J("table.t-data-grid tbody tr td[class!='action']").addClass("ui-widget-content");
            $J("table.t-data-grid thead tr th[class!='action t-last']").addClass("ui-state-default");
            $J("div.t-data-grid-pager a").addClass("ui-corner-all ui-state-default");
            $J("div.t-data-grid-pager span.current").addClass("ui-corner-all ui-state-active");
            $J(".ui-grid-cell-action-tip").addClass("ui-corner-all ui-state-default");
        }

        /*
 * Создание подсказок для элементов. 
 * Используется модифицированный плагин Easy Tooltip 1.0
 * функция easyTooltip вызывается на элементах, на которые необходимо
 * назначить подсказки и параметром в ней является текстовая строка, которая 
 * служит для идентификации типа элемента. 
 */
        function createToolTips(){
            if (Ori.SHOW_HINTS){
                $J("input[type=text]").easyTooltip({
                    ctype:"input[type=text]"
                });
                $J("select").easyTooltip({
                    ctype:"select"
                });
            }
        }
        
        //Инициализация ui-интерфейса
        if ($J("ui#interface").text()=="true"){
            initializeUIComponents();   
        }
        updateCSS();
        //Создание подсказок
        createToolTips();
    }
    
    //Регистация слушателя событие на обновление зоны
    Ori.Event.bind('.t-zone', Tapestry.ZONE_UPDATED_EVENT, initUI);
    initUI();
});
