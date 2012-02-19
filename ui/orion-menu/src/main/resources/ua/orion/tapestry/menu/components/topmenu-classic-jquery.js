/**
 * @name topmenu-classic
 * Реализация скрипта инициализации меню на jQuery
 */
////Используется для определения является ли клик вызовом меню или закрытием его
//при щелчке в области документа.
firstClick=false;
//Используется для хранения состояния меню - открыто или закрыто.
menuIsOpen=false;
//Хранит все элементы меню. Используется для более быстрой и простой мани-
//пуляции ими. 
var allmenuitems;
//Хранит все кнопки вызова меню
var menuopeners;


//Вызывается, когда DOM структура готова к использованию
jQuery(document).ready(function(){
    //Когда все элементы DOM загружены, в переменную загружаются все элементы
    //меню
    allmenuitems=jQuery("span.menuitems");
    //Вызывается метод инициализации. Прикручивание событий. 
    initApp();
})


//Метод инициализации
function initApp()
{
    //Метод вызова меню
    function invokeMenu(e){
        //Определяются все элементы меню, для текущего родителя
        var menuitems = jQuery(e).parent().parent().find("span.menuitems");
        //Если меню уже открыто, оно закрывается
        if (menuIsOpen==true){
            menuIsOpen=false;
            menuitems.css("display","none")
        //Если же меню не открыто, оно открывается
        } else if(menuIsOpen==false){
            menuIsOpen=true;
            firstClick=true;
            menuitems.css("display","block")
        }
    }
    //Определяются все элементы для раскрытия меню
    menuopeners=jQuery("span.menuarrow");
    //Для каждой кнопки раскрытия меню назначаются события
    menuopeners.each(function(){
        //По нажатию на кнопку - вызвать меню
        jQuery(this).click(
            function(){
                invokeMenu(this);
            })
        //По наведению на кнопку
        menuopeners.mouseenter(function(){
            //Проверить открыто ли одно из меню
            if (menuIsOpen){
                //Если открыто то закрыть его
                allmenuitems.css("display","none");
                menuIsOpen=false;
                //И открыть то меню, на элемент вызова которого была наведена
                //мышь
                invokeMenu(this);
                //Так как клика не было, выполняется обнуление firstClick
                firstClick=false;
            }
        })
    }
    );
    //По нажатию в области документа
    jQuery(document).click(function (){
        //Проверяется был ли это первый клик, если да то обнуляется
        if (firstClick) firstClick=false
        else {
            //Если нет, то скрывается меню. 
            allmenuitems.css("display","none");
            menuIsOpen=false;
        }
    }
    );
}