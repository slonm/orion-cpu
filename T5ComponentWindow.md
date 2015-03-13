# О компоненте #

Пример того, что представляет из себя компонент можно увидеть [тут](http://www.chenillekit.org/demo/tapcomp/windowdemo). Демо устаревшее.



# Как использовать #
Цель: создать страницу со списком объектов в виде таблицы. В таблице должен быть столбец с действиями над этим объектом - просмотр и редактирование. Оба действия будут выполнятся в модальном окне.

Создадим функцию инициализации и отображения окна и функцию скрытия окна. В нее передадим JSON структуру opt со следующими полями:

  * window - имя окна
  * modal - модальность окна
  * atCenter - позиционирование по центру
  * title - заголовок окна

WindowUtils.js - вспомогательные функции
```
Tapestry.Initializer.showCkWindow = function(opt){
    var win=$(opt.window).getStorage().ck_window;
    opt.modal=opt.modal!==false?true:false;
    if(opt.atCenter!==false) win.showCenter(opt.modal);
    else win.show(opt.modal);
    if(opt.title) win.setTitle(opt.title);
    
}

Tapestry.Initializer.closeCkWindow = function(opt){
    Windows.close(opt.window);
}
```

Что-бы иметь возможность вызова этих функций во время обновления зон, создадим следующий компонент.

Компонент для вызова JavaScript функций во время перерисовки зоны InitCall.java.
```
package ua.orion.web.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * A non visual component.
 * Adds a call to a client-side function inside the Tapestry.Initializer namespace. 
 * It same as call JavascriptSupport.addInitializerCall method.
 * Calls to this method are aggregated into a call to the Tapestry.init() function. 
 * Initialization occurs at {@link InitializationPriority#NORMAL} priority.
 * All informal parameters has been added as JSON parametrs.
 * @author sl
 */
@SupportsInformalParameters
public class InitCall {

    /**
     * Client-side function inside the Tapestry.Initializer namespace 
     */
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL, allowNull = false)
    private String function;
    /**
     * Add function call at page render request
     */
    @Parameter(value = "true", defaultPrefix = BindingConstants.LITERAL, allowNull = false)
    private boolean html;
    /**
     * Add function call at component event's Zone update
     */
    @Parameter(value = "true", defaultPrefix = BindingConstants.LITERAL, allowNull = false)
    private boolean ajax;
    @Environmental
    private JavaScriptSupport javascriptSupport;
    @Inject
    private Request request;
    @Inject
    private ComponentResources resources;

    Object beginRender() {
        if ((request.isXHR() && ajax) || (!request.isXHR() && html)) {
            JSONObject options = new JSONObject();
            for (String name : resources.getInformalParameterNames()) {
                options.put(name, resources.getInformalParameter(name, String.class));
            }
            javascriptSupport.addInitializerCall(function, options);
        }
        return false;
    }
}
```

Теперь делаем саму страницу.
Класс страницы Crud.java
```
@Import(library = "../WindowUtils.js")
public class Crud {
    @Component
    private Zone listZone;
    @Inject
    private Block editBlock;
    @Inject
    private Block viewBlock;
    @Property
    private MyEntity object;

    public GridDataSource getObjects() {
        //return GridDataSource object
    }

    public Object onSuccessFromEditForm() {
        //Save instructions
        return listZone.getBody();
    }

    public Object onEdit(MyEntity object) {
        this.object = object;
        return editBlock;
    }

    public Object onView(MyEntity object) {
        this.object = object;
        return viewBlock;
    }
}
```

При нажатии ссылок действий будет обновляться зона popupZone блоком editBlock или viewBlock и отображаться окно. При обновлении зоны listZone окно будет автоматически скрываться.
Шаблон страницы Crud.tml
```
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<t:layout xmlns:t="http://tapestry.apache.org/schema/tapestry_5_1_0.xsd"
              xmlns:p="tapestry:parameter">
    <t:zone t:id="listZone">
        <t:ori.InitCall function="closeCkWindow" window="popupWindow"/>
        <t:grid source="objects" row="object" inplace="true" add="action">
            <p:actionCell>
                <t:EventLink t:event="edit" context="object" zone="popupZone" title="message:action.edit">Edit</t:EventLink>&nbsp;
                <t:EventLink t:event="view" context="object" zone="popupZone" title="message:action.view">View</t:EventLink>
            </p:actionCell>
        </t:grid>
    </t:zone>
    <t:block id="editBlock">
            <t:ori.InitCall function="showCkWindow" window="popupWindow" title="Edit MyEntity" html="false"/>
            <t:beaneditform t:id="editForm" object="object" zone="listZone" context="object" submitLabel="Save"/>
    </t:block>
    <t:block id="viewBlock">
        <t:ori.InitCall function="showCkWindow" window="popupWindow" title="View MyEntity" html="false" />
        <t:beandisplay t:id="display" object="object"/>
    </t:block>
    <t:ck.Window t:id="popupWindow" t:style="bluelighting" show="false"
                 modal="true" title="mode"
                 width="900" height="500">
        <t:zone t:id="popupZone"/>
        <a href="#" onclick="Windows.close('popupWindow')">Close window</a>        
    </t:ck.Window>
</t:layout>
```

Альтернативный вариант решения этой задачи [тут](http://tawus.wordpress.com/2011/07/02/a-modal-dialog-for-tapestry/)