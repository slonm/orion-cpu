# jQuery UI. Подключение и использование в ORION-CPU #
## Введение и подключение библиотеки ##
**jQuery UI** — библиотека JavaScript с открытым исходным кодом для создания насыщенного пользовательского интерфейса в веб-приложениях, часть проекта jQuery. Для ее использования в проекте должна быть подключена библиотека jQuery. В данный момент обе библиотеки подключены в _Layout.java_, следовательно доступны на всех страницах. В связи с тем, что аннотацией @Import не получится привязаться к фазам рендеринга страницы, компонента, они подключены к компоненту Layout посредством @Inject @Path:
```
    @Inject
    @Path("../jquery.js")
    private Asset jQueryLibrary;
    @Inject
    @Path("context:webcontent/jquery-ui/js/jquery-ui-1.8.16.custom.min.js")
    private Asset jQueryUILibrary;
```
Как Вы уже успели заметить, библиотека UI берется из _context_, то есть из каталога _Веб-страницы_ проекта _КИС-КПУ_. Данный метод был использован дабы упростить структуру пакетов в проекте веб-интерфейса. Так как библиотека имеет свою иерархическую структуру директорий. Далее все библиотеки подключаются на стадии SetupRender, что и позволяет внедрить их перед теми, что внедрены самой страницей:
```
@SetupRender
    public void SetupRender() {
        List<Asset> libraries = Arrays.<Asset>asList(jQueryLibrary, jQueryUILibrary, jQueryUILocalization,
 jQueryNoConflictLibrary, jQueryUIInterfaceLibrary, jQueryToolTipLibrary, jQueryUISelectMenu, jQueryCPUEffectsLibrary);
        for (Asset library : libraries) {
            javaScriptSupport.importJavaScriptLibrary(library);
        }
    }
```
Другие скрипты и библиотеки в самой странице же теперь можно подключать через @Import.
_Дабы избежать конфликтов с Prototype используется функция jQuery.noConflict() описанная в подключаемом файле:_
```
    @Inject
    @Path("../jquery.noconflict.js")
    private Asset jQueryNoConflictLibrary; 
```

## Использование ##
Для внедрения UI-интерфейса нужно оформлять стандартные HTML элементы согласно некоторым правилам и добавлять в них соответствующие CSS-классы. Ниже представлены требования оформления для некоторых элементов
### _Кнопка_ ###
Для создания кнопки необходимо использовать не стандартный тег HTML:
```
<input type="button" value="OK"></input>
```
А немного измененный:
```
<button class="ui-button">OK</button>
```
Он содержит даже немного меньше символов, что достигается исключением избыточного слова _value_. Но, если говорить о сокращенной форме, то его нельзя сократить как это можно сделать с обычной кнопкой. Плюс данной кнопки в универсальном CSS, а также возможности поставить иконку. Для того чтобы поставить иконку необходимо отредактировать класс CSS. Например, для того чтобы поставить иконку "Добавить" следует написать следующий код:
```
<button class="ui-button-add">ADD</button>
```
Подробней о классах и иконках будет рассказано позже.
### _Select, Text, Password, TextArea, CheckBox и календарь_ ###
Для данных типов предусмотрено автоматическое подключение стиля. То есть при любом их использовании они будут оформлены в соответствии с стилями библиотеки jQuery-UI. Все это достигается благодаря подключению скрипта в компонент Layout:
```
    @Inject
    @Path("../ui-interface.js")
    private Asset jQueryUIInterfaceLibrary;
```
В котором и описаны все эти преобразования. Select является отдельным удобным плагином для jQuery UI, который обеспечивает размещение пунктов выбора в несколько строк, что существенно улучшает дизайн элемента и самой формы, где он располагается. Календарь функциональней того, что предлагает Tapestry. Он заменяет все поля с выбором даты в BeanEditor. Для того чтобы его активировать достаточно щелкнуть по полю выбора даты. В дальнейшем возможен выбор языка для календаря. Все остальные элементы просто оформляются в соответствии с CSS библиотеки jQuery UI. Для оформления CheckBox используются целые методы, которые создают элементы HTML. Но это не столь важно. Так как в итоге клик отрабатывает и на реальном CheckBox.
## Использование универсальных CSS классов ##
Также для оформления неплохо было бы просто использовать универсальные CSS классы. Ниже приведены некоторые из них:
  * **ui-widget-content** - используется для контейнеров. Внутри описывается содержимое виджета. Можно использовать, допустим, для ячеек Grid.
  * **ui-state-default** - стандартный вид элемента UI интерфейса. Самый распространенный класс. Используется для обозначения кнопок, текстовых полей, панелей и т.д.
  * **ui-state-focus** - используется при фокусировке на элемент. Допустим при нажатии и вводе данных в текстовое поле.
  * **ui-state-active** - используется для отображения активного элемента. Отличается от ui-state-default для того, чтобы явно выделить активный элемент. Может использоваться для нажатия на кнопку, для CheckBox или же других целей, которые может реализовать библиотека, но они пока еще не реализованы в проекте.
  * **ui-state-error и ui-state-highlight** - используется для отображения событий - ошибка, информация и другие.
  * **ui-state-disabled** - используется для отображения неактивных элементов.
По правде классов на много больше, но здесь перечислены основные.
## Скрипт инициализации элементов ##
При запуске главная функция выполняет функции оформления каждого из типов элементов:
```
function initializeUIComponents(){
    initializeTextFields();
    initializeCalendar();
    initializeButtons();
    initializeSubmits();
    initializeIcons();
    jQuery("select").selectmenu();
    initializeCheckboxes();
    jQuery("button").parent('a').css({'text-decoration':'none'})
}
```
Последняя строка убирает подчеркивание ссылок, которое видно внутри кнопок в некоторых браузерах. Далее немного подробнее о каждом из элементов.
### _Текстовые поля_ ###
Инициализация тектовых полей на начальном этапе заключается в том, что для каждого из текстовых полей, будь то однострочное или многострочное текстовое поле выполняется функция прикрепления интерфейса:
```
function initializeTextFields(){
    jQuery("input[type=text], textarea").each(function(){
        bindTextElements(this);
    })
}
```
Сама функция элементарно проста:
```
function bindTextElements(element){	
    jQuery(element).addClass("ui-state-default ui-corner-all");
    jQuery(element).bind("focusin", function(e){
        jQuery(this).toggleClass("ui-state-focus ui-state-default");
    });
    jQuery(element).bind("focusout", function(e){
        jQuery(this).toggleClass("ui-state-focus ui-state-default");
    });			 
}
```
Вначале добавляются два класса, первый - для того, чтобы сделать стандартный интерфейс элемента UI. А второй - для того чтобы закруглить все края. Далее прикручивается событие на событие получения фокуса, в котором переключаются классы _ui-state-focus_ и _ui-state-default_. То есть, если есть класс _ui-state-focus_ то он убирается, если нет - добавляется. И так же на счёт второго. Выходит, что при получении фокуса класс _ui-state-focus_ добавляется, а класс _ui-state-default_ - убирается. При потере фокуса выполняются те же действия. Только выходит, что класс _ui-state-default_ возвращается, а второй - удаляется.
### _Календарь_ ###
Сложность инициализации календаря заключается, скорее всего, в самой инициализации параметров для него.
```
function initializeCalendar(){
    jQuery.datepicker.setDefaults(jQuery.datepicker.regional['uk']);
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
    jQuery(".t-calendar-trigger").css({
        'display':'none'
    });
}
```
Вначале устанавливается украинская локаль(есть много языков, нужно только прикрутить к локали браузера или же той, что выбрал пользователь). Далее выполняется инициализация самого календаря с параметрами:
  * Показывать кнопки внизу.
  * Формат даты: dd.mm.yy, например 14.11.2011.
  * Анимация: разворачиваться вниз.
  * Скорость анимации: низкая.
  * Показывать дни с других месяцев.
  * Выбор дней с других месяцев.
  * Показывать недели года.
  * Панель выбора месяца: включена.
  * Панель выбора года: включена.
  * Год в панели выбора года от 1900 до 2050.
Далее выполняется функция, которая прячет стандартную картинку-триггер для создания календаря, что предлагает нам Tapestry.
### _Кнопки_ ###
Инициализация кнопок самая простая. Так как они являются стандартными элементами jQuery UI, то для их инициализации достаточно выполнить одну функцию на селекторе:
```
function initializeButtons(){
    jQuery(".ui-button").button();
}
```
Главное, чтобы у элементов был класс - ui-button.
### _Submit_ ###
Submit - это не такая кнопка как остальные. Она имеет другую структуру и ее описывать нужно немного по другому. А в случае как у нас, что они почти все генерируются самим фреймворком то сложности с созданием для Submit UI интерфейса только появляются. Но, все на много проще. Стоит всего лишь найти все элементы Submit и заменить их на кнопки, а дальше добавить им иконку (галочка) и все. Этим и занимается следующая функция:
```
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
```
### _Иконки для кнопок_ ###
Иконки для кнопок создаются с помощью нескольких шагов. Сначала стоит узнать, что есть у нас два массива - _elements_ и _icons_. Что означает _классы CSS_ и соответствующие им _иконки_. Смысл в том, что каждому i-му элементу из массива elements соответствует i-ый элемент из массива icons. Что позволяет установить связь между этими двумя массивами. Перейдем к самой функции.
```
function initializeIcons(){
    for (var i=0; i<elements.length; i++){
        createIcon(elements[i], icons[i]);
    }
}
```
Для каждого класса из массива _elements_ вызывается функция _createIcon_ в аргументы которой передается класс элемента и требуемая иконка.
```
function createIcon(ec, ic){
    jQuery("."+ec).button({
        icons: {
            primary: ic
        },
        text: ec.indexOf("tfalse")>-1?false:true
    })
}
```
Данная функция создает кнопку с указанной иконкой. Присутствие текста на кнопке определяется наличием подстроки tfalse в CSS классе. Если она есть - текста нет.
Массивы можно дополнять в соответствии с классами иконок, который представлены на здесь: http://jqueryui.com/themeroller. Искать по тексту: Framework Icons.
Сами массивы в текущем состоянии приведены ниже:
```
var elements = new Array("ui-button-close","ui-button-add","ui-button-refresh","ui-button-login","ui-button-quit","ui-button-del","ui-button-cancel","ui-button-edit-tfalse","ui-button-view-tfalse","ui-button-del-tfalse","ui-button-tolist");
var icons = new Array("ui-icon-close","ui-icon-circle-plus","ui-icon-refresh","ui-icon-key","ui-icon-power","ui-icon-trash", "ui-icon-cancel", "ui-icon-pencil","ui-icon-print","ui-icon-trash","ui-icon-arrowreturnthick-1-n");
```
### _Выпадающий список_ ###
Выпадающий список формируется проще всего. Так как это стандартный элемент, который подключается плагинном для него была создана функция:
```
jQuery("select").selectmenu();
```
В дальнейшем возможно вынесение в отдельную функцию, как и в других случаях.
### _CheckBox_ ###
Инициализация checkbox, пожалуй, одна из самых сложных.
Сначала выполняется всех элементов checkbox функции, которая будет заниматься прикреплением интерфейса.
```
function initializeCheckboxes(){
    jQuery("input[type=checkbox]").each(function(){
        bindCheckBoxes(this);
    })   
}
```
Функция bindCheckBoxes:
```
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
```
Сначала выполняется обрамление элемента в фиктивный блок. Далее - установка начального положения флажка. Если стоит флажок, то блок отмечается как выбранный(цвет и иконка)
```
        parent.addClass("ui-icon ui-icon-check");
        parent.parent("span").addClass("ui-state-active");
```
При щелчке мышкой на блоке просто выполняется переключение классов (ui-state-active, ui-icon и ui-icon-check), что обеспечивает визуальное представление checkbox-а, а дальше выполняется самое главное - щелчок на самом элементе checkbox, что и позволяет сохранить его состояние.
### _Дополнительно_ ###
Строка:
```
jQuery("button").parent('a').css({'text-decoration':'none'})
```
Находит все кнопки, находит среди их прямых родителей - ссылки(если есть) и убирает подчеркивание(проявляется в некоторых браузерах).
## Заключение ##
Данный интерфейс значительно приятен на вид, хотя бы тем, что он имеет один стиль. Так же является более гибким и функциональным нежели использования стандартного оформления и обеспечит возможность выбирать темы оформления, которые будут в корне менять цветовую схему сайта. Как использовать описано, следовательно, по возможности - используйте его.
**Полезная книга по jQuery и jQuery UI на русском языке с примерами:** _http://www.wisdomweb.ru/JQ/_
### Спасибо за внимание! ###