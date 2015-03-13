# Введение #

Данный mixin позволяет обрабатывать события не только стандартные события от компонентов tapestry, такие как onAction, onSubmit и т.д., но и события javascript.
Подробнее с ними Вы можете ознакомиться здесь:
http://www.quirksmode.org/js/events_events.html

# Описание использования #
## Шаблон страницы ##
В применении "миксин" очень прост. Сперва нужно подключить зависимость библиотеки ChenilleKit в pom.xml:
```
        <dependency>
            <groupId>org.chenillekit</groupId>
            <artifactId>chenillekit-tapestry</artifactId>
            <version>1.3.2</version>
        </dependency>
```
Затем в шаблоне назначить одному из компонентов несколько параметров. А именно:
Подключить сам миксин:
```
t:mixins="ck/OnEvent"
```
Определить событие, на которое будет реагировать компонент, допустим, нажатие ЛКМ:
```
event="click"
```
А также, определить какая функция JavaScript будет выполняться после того, как будет получен ответ от сервера. В этой функции Вы сможете обрабатывать полученный ответ. Допустим, если ответ от сервера содержит JSON объект с названием "resp", то Вы можете использовать его данные, например таким образом:
```
alert(resp.BirthDay);
```
Или обновлять контент при помощи innerHTML, или же встроенных функций JavaScript фреймворков, которые поддерживают более удобные селекторы.
Само же название функции определяется как параметр. Например:
```
onCompleteCallback="onCompleteFunction"
```
В итоге, ниже приведен пример шаблона описания компонента TextField, которые будет реагировать на событие нажатия ЛКМ, и при ответе от сервера вызывать функцию JavaScript:
```
function onCompleteFunction(response)
```
Полный текст примера:
```
<form t:type="Form">
        <input t:type="TextField" value="literal:textFieldValue" t:mixins="ck/OnEvent"
            event="click" onCompleteCallback="onCompleteFunction"/>;
</form>
```
```
<script type="text/javascript">
        function onCompleteFunction(response)
        {
            $('element').update("<strong>" + response.BirthDay + "</strong>");
        }
</script>
```
Здесь Вы можете использовать как чистый JS так и любой фреймворк. При использовании jQuery не забываем про jQuery.noConflict(), так как без этого будут конфликты с Prototype. Подробнее можно прочесть здесь:
http://jquery-docs.ru/core/jquery-noconflict/
## Класс страницы ##
Для начала не забудьте подключить импорт:
```
import org.apache.tapestry5.annotations.Mixins;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.json.JSONObject;
...
```
Указан лишь основной импорт, который случайно можно спутать с другим, и тогда пример откажется работать. Например класс JSONObject есть и здесь:
```
import org.chenillekit.google.utils.JSONObject; 
```
Но это не тот класс, который нужен.
Далее в теле класс необходимо объявить сам компонент с миксином:
```
    @Mixins({"ck/OnEvent"})
    @Property
    private TextField textField;
```
Над самим методом-обработчиком события должна быть аннотация:
```
@OnEvent(component = "textfield", value = "click")
```
Сам метод может выглядеть примерно так:
```
    public JSONObject onClickEvent(String value) {
        String birthDay=null;
        Calendar cal = new GregorianCalendar();
        birthDay = new SimpleDateFormat("dd.MM.yyyy").format(cal.getTime());
        JSONObject js = new JSONObject();
        js.put("BirthDay", birthDay);
        return js;
    }
```
Не сильно обращайте внимание на сами методы. Они примитивны. Цель данной статьи ознакомить Вас с принципами использования данного миксина. А какую обработку выполнять - это уже лично Ваше дело.
В итоге, страница выглядит примерно так:
```
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.apache.tapestry5.annotations.Mixins;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.json.JSONObject;

public class Index {

    @Mixins({"ck/OnEvent"})
    @Property
    private TextField textField;
    
    @OnEvent(component = "textfield", value = "click")
    public JSONObject onClickEvent(String value) {
        String birthDay=null;
        Calendar cal = new GregorianCalendar();
        birthDay = new SimpleDateFormat("dd.MM.yyyy").format(cal.getTime());
        JSONObject js = new JSONObject();
        js.put("BirthDay", birthDay);
        return js;
    }
}
```
Запись:
```
@Mixins({"ck/OnEvent"}) 
```
Не обязательна. Она есть на шаблоне.
# Заключение #
_Данный миксин можно использовать для того, чтобы сделать контент страницы более динамичным, не используя при этом такие вещи, для организации Ajax, как Zone. Так как Вы можете обновить абсолютно любую часть контента веб-страницы. А также по необходимости вызывать некоторые служебные, для улучшения качества работы приложения. Например, генерировать окошки с формами ввода, выводить информацию, результаты расчетов и прочее.
Надеюсь, что данный пример будет более понятен, чем тот, который на оригинальном сайте. Так как там допущены ошибки, пропущены некоторые детали, такие как импорт. Можно перепутать множество вещей, и проект не будет корректно работать._
**Удачи в использовании!**