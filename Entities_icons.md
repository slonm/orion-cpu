# Entities icons #

Иконки для обозначения сущностей в меню и в MenuNavigator-е.

# Принцип работы и как использовать #

В шаблон компонента **меню** и **MenuNavigator** был добавлен блок для отображения иконки:
```
<div class="e-icon-16x16" style="${UidIcon}"></div>
```
Для работы данного блока на страницу был добавлен get метод для **UidIcon**:
```
    public String getUidIcon() {
        String iconURL = messages.get(_Item.getUid() + "-icon");
        if (iconURL.indexOf("missing key") < 1) {
            return "background-image: url('/webcontent/e-icons/" + iconURL + "')";
        } else {
            String[] menuParts = _Item.getUid().split(">");
            String lastMenuPart = menuParts[menuParts.length - 1];
            iconURL = messages.get(lastMenuPart + "-icon");
            if (iconURL.indexOf("missing key") < 1) {
                return "background-image: url('/webcontent/e-icons/" + iconURL + "')";
            }
        }
        return "background-image: url('/webcontent/e-icons/folder.png')";
    }
```
Данный метод возвращает CSS свойство, которое отвечает за фоновое изображение блока - иконку 16х16. В случае если иконка для сущности не определенна, будет использоваться стандартная иконка в виде папки.
Файл конфигурации пути к иконкам подключается с помощью **OrionWebIOCModule**:
```
    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping("ori", "ua.orion.web"));
        configuration.add(new LibraryMapping("eicons", "ua.orion"));
    }
```
Каталог сообщений **eiconsWeb.properties** является общим. Вне зависимости от выбранного Вами языка. В нем хранят пути меню сущностей и их соответствия иконкам в директории **/webcontent/e-icons/**
Для того чтобы добавить свой ключ в данный каталог сообщений можно воспользоваться двумя способами.

---

1) Указать точный путь меню(наиболее приоритетный вариант). Например, Start>EduProcPlanning>EduPlan.

---

2) Можно указать просто, окончание пути. Например, в данном случае - EduPlan. В таком случае данная иконка будет применяться ко всем сущностям EduPlan, для которых не определен полный путь меню.

---

В каталоге есть более 30 иконок. Если есть необходимость загрузить новую, Вы должны соблюдать следующие правила.
  * иконка должна быть 16x16px
  * иконка должна быть с прозрачным фоном. Такой тип файла как jpg, jpeg и прочие не рекомендуется!
  * Иконка может быть формата png, gif и т.д.
  * Иконка может быть и других размеров. В таком случае она будет ограничена размерами блока.
**Иконки можно взять с различных источников. Я рекомендую сайты:**
  * _Хороший русскоязычный ресурс:_ http://www.iconsearch.ru/
  * _Очень хороший как русский так и зарубежный поиск:_ http://iconizer.net/
  * _Хорошие зарубежный ресурс:_ http://www.iconfinder.com/
  * _Больше сервисом можно узнать здесь:_ http://ruseller.com/lessons.php?rub=29&id=626