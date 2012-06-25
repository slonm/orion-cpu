//Декорация UI интерфейса после загрузки страницы и после 
//обновления зоны
$J(document).ready(function(){
    //Инициализация классов кнопок, к которым присоеденяются иконки
    //Инициализация классов иконок, для кнопок в массиве elements
    //notext в конце используется для того, чтобы сделать кнопку без текста. 
    var elements={
        "ori-button-close":"ui-icon-close",
        "ori-button-add":"ui-icon-circle-plus",
        "ori-button-refresh":"ui-icon-refresh",
        "ori-button-login":"ui-icon-key",
        "ori-button-quit":"ui-icon-power",
        "ori-button-del":"ui-icon-trash",
        "ori-button-cancel":"ui-icon-cancel",
        "ori-button-edit-notext":"ui-icon-pencil",
        "ori-button-view-notext":"ui-icon-print",
        "ori-button-del-notext":"ui-icon-trash",
        "ori-button-tolist":"ui-icon-arrowreturnthick-1-n",
        "ori-button-right-list-notext":"ui-icon-triangle-1-e",
        "ori-button-detail":"ui-icon-zoomin", 
        "ori-button-edit":"ui-icon-pencil",
        "ori-button-view":"ui-icon-search",
        "ori-button-check":"ui-icon-circle-check"
    }
    
    function initUI(event){
        //Если event не задан, то применим инициализацию ко всему телу документа,
        //иначе это инициализация зоны
        root=!event?$J("body")[0]:this;
        
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
            for(cls in elements){
                $J("."+cls).button({
                    icons: {
                        primary: elements[cls]
                    },
                    text: cls.indexOf("notext")>-1?false:true
                })
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

        /**
         * Декорирование компонента Alerts
         */
        function decorateAlerts(){
            dismiss_i18n={
                'ru':"Убрать",
                'uk':"Убрати",
                'en':"Dismiss"
            }
            $J(".t-dismiss", root).attr("title", dismiss_i18n[Ori.LOCALE]);
            $J("div.t-alert-container div.t-error", root).css({
                border: "1px solid red",
                "background-color": "#FEF1EC",
                color: "red"
            });
            $J("div.t-alert-container div.t-warn", root).css({
                border: "1px solid #FFCC66",
                "background-color": "#FBF9EE",
                color: "#CC6600"
            });
            $J("div.t-alert-container div.t-info", root).css({
                border: "1px solid blue",
                "background-color": "#aaddFF",
                color: "black"
            });
            $J("div.t-alert-controls", root).css({
                border: "1px solid lightGrey"
            });
        }

        //Инициализация компонентов
        function initializeUIComponents(){
            addUIClassesToBeanEditView();
            decorateAlerts();
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
            $J(".ori-grid-cell-action-tip").addClass("ui-corner-all ui-state-default ui-widget-header");
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
