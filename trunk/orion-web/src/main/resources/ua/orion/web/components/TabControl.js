/**
 * @class TabControl - Панель вкладок, которая взаимодействует с Tapestry. 
 * @author dvelopp (Переписан скрипт CK для jQuery)
 */
var TabControl = (function(){
    
    /**
     * ID панель вкладок
     */ 
    var tabPanelId;
    /**
     * ID активного элеметна
     */
    var activeId;
    /**
     * Вкладки(ссылки)
     */
    var panelLinks;
    
    /**
     * Конструктор
     */
    var Init = function(tabPanelIdValue, activeIdValue){
        tabPanelId = tabPanelIdValue;
        activeId = activeIdValue;
        panelLinks = jQuery("#"+tabPanelId).find("a");
        //Удаляется активный стиль со всех вкладок
        Init.prototype.removeActiveStyle();
        //Добавляется стиль активной для вкладки, которая в данный активна
        panelLinks.each(function(){
            if (jQuery(this).parent().attr("id")==activeId){
                Init.prototype.addActiveStyle(jQuery(this).parent());
            }
        });
        //Обработчик нажатия на вкладку
        panelLinks.click(function(){
            Init.prototype.removeActiveStyle();
            Init.prototype.addActiveStyle(jQuery(this).parent());
        })
        //Обработчик наведения мыши на вкладку
        panelLinks.parent().mouseenter(function(){
            Init.prototype.addHoverStyle(jQuery(this));
        })
        //Обработчик покидания мышью вкладки
        panelLinks.parent().mouseleave(function(){
            Init.prototype.removeHoverStyle();
        })
      };
     
    //Public-часть
      Init.prototype = {
            name: "TabControl Class",
        removeActiveStyle:function(){
            panelLinks.each(function ()
            {
                jQuery(this).parent().removeClass("ui-state-active");
            })
        },
        removeHoverStyle:function(){
            panelLinks.each(function ()
            {
                jQuery(this).parent().removeClass("ui-state-focus");
            })
        },
        addActiveStyle:function (element){
            element.addClass("ui-state-active");
        },
        addHoverStyle:function (element){
            element.addClass("ui-state-focus");
        }
      };
      return Init;
}());
