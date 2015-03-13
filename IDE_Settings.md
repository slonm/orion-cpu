# Настройка среды разработки #

Для настройки среды программирования на языке Java необходимо:

## Установить Java Development Kit ##
  * Скачать и установить [Java Development Kit 1.7](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
  * Установить переменную JAVA\_HOME. Например `C:\Program Files\Java\jdk1.7.0_2` для Windows.
  * В переменную среды PATH добавить путь к исполняемым файлам. Например  `C:\Program Files\Java\jdk1.7.0_2\bin\` для Windows

## Установить Apache Maven ##
_**Maven устанавливать не обязательно. NetBeans содержит встроенный maven.**_
  * Скачать и распаковать [Maven 3](http://maven.apache.org/download.html) в `C:\maven`.
  * Установить переменную M2\_HOME. Например `C:\maven` для Windows.
  * В переменную среды PATH добавить путь к исполняемым файлам. Например  `C:\maven\bin\` для Windows

  * Создать каталог C:\maven-repo.
  * В файле C:\maven\conf\settings.xml в теге settings добавить тег `<localRepository>C:\maven-repo</localRepository>`

#### Дополнительная информация ####
  * [Apache Maven — основы](http://m.habrahabr.ru/post/77382/)


## Установить `NetBeans` ##
  * Скачать и установить [NetBeans](http://www.netbeans.org/downloads/index.html) из дистрибутива с конфигурацией, включающей JavaEE (Java или Все).

  * В файле конфигурации `NetBeans` `C:\Program Files\NetBeans 7.0\etc\netbeans.conf` (В Linux `/usr/local/netbeans-x.x/etc/netbeans.conf`) к netbeans\_default\_options добавить `-J-Dfile.encoding=UTF-8 --fontsize 16 --locale en:US`

  * Выбрать Tools > Plugins > Installed > Downloaded. Нажать Add Plugin и установить плагины:
org-netbeans-modules-tapestry.nbm
org-netbeans-modules-tapestry-codetemplates.nbm
org-netbeans-modules-web-tapestry-templates.nbm (можно взять [тут](http://orion-cpu.googlecode.com/files/nbtapestrysupport.zip))
(версии файлов актуальны на 18.09.2009, в версиях Netbeans > 7.1 эти плагины могут не устанавливаться. Новые версии плагина смотреть на  [nbtapestrysupport.dev.java.net nbtapestrysupport.dev.java.net]).
Перезапустить `NetBeans`. Установленные модули будут перечислены в
> Tools > Plugins > Installed > User Installed Plugins

  * Выбрать ''Tools > Plugins''. Вкладка Installed.
Выбрать ''Maven Project Support'' модуль и нажать кнопку активировать. Перезапустить `NetBeans`.

  * Выбрать Tools > Options > Miscellaneous > Maven. В поле External Maven Home указать местонахождение Maven (C:\maven).
В поле Local Repository указать путь к локальному репозиторию Maven C:\maven-repo.
Нажать кнопку "Edit Global Custom Goal Definitions..." и создать цели для запуска проекта в Jetty:

http://orion-cpu.googlecode.com/svn/wiki/jetty_run_Goal.PNG

и для запуска так же в Jetty, но в отладочном режиме:

http://orion-cpu.googlecode.com/svn/wiki/Debug_jetty_run_Goal.PNG

Рекомендуется сделать дополнительную оптимизацию NetBeans для конкретной рабочей станции: [Ускорение и оптимизация работы NetBeans 7.1](http://jakeroid.com/uskorenie-i-optimizatsiya-rabotyi-netbeans-7-1.html)

## Настройка клиента системы контроля версий ##

Реализованная стратегия использования svn ресурсов основана на первом варианте из http://www.gnuman.ru/stuff/svn_strateg/
Структура ресурса следующая:
```
/ 
        /trunk 
        /tags 
        /branches 
            /user1 
            /user2 
            /user3 
```
Стратегия использования различается от описанной только в ветке branches. Пользователи создают совместно разрабатываемые бранчи в каталоге branches, а индивидуально разрабатываемые в каталогах, которые соответствуют их имени (user1, user2).

Плагин NetBeans для работы с SVN плохо ведет себя при манипуляции с ветками (слияние, создание). Кроме этого внутренний клиент не поддерживает формат рабочей копии 1.7. Его лучше использовать только для элементарных операций (checkout, commit, update). Для работы с ветками рекомендуется использовать [TortoiseSVN](http://tortoisesvn.net/) для Windows или [RabbitVCS](http://www.rabbitvcs.org/) для Linux. Так же в для удобства работы в них рекомендуется установить diff утилиту [KDiff3](http://kdiff3.sourceforge.net/), которая позволяет сравнивать как файлы, так и каталоги.

Плагин NetBeans для работы с SVN настраивается в этом случае следующим образом:
  * Указываем путь к внешнему клиенту Subversion: Tools > Options > Versioning > Subversion. В поле "Path to the SVN executable File:" Указать "C:\Program Files\TortoiseSVN\bin"
  * В параметрах запуска добавляем опцию для использования команд командной строки при работе с SVN: "-J-DsvnClientAdapterFactory=commandline"

Теперь можно получить рабочую копию проекта: Team > Subversion > Checkout. Указать путь к репозиторию: https://orion-cpu.googlecode.com/svn/trunk/, При первом подключении будет выведено окно с предложением добавить в Вашу систему высылаемый сервером сертификат безопасности. Необходимо его принять (кнопка Accept Permanently).
После нажатия Next можно выбрать ту часть репозитория с которой необходимо работать.


#### Дополнительная информация ####
  * [Работа с ветками SVN](http://habrahabr.ru/blogs/development_tools/45203/)

## Запуск, отладка и Live class reloading ##

Для запуска проекта модно использовать стандартные средства deploy NetBeans (нажатием кнопки Run). В этом случае происходит deploy war архива проекта на указанный при запуске сервер приложений.
Tapestry5 поддерживает "горячую" подмену классов и ресурсов (см. [Live Class and Template Reloading](http://tapestry.apache.org/class-reloading.html)). Но для ее работы нужно, что-бы сервер приложений работал не с war архивом, а с каталогами classpath, в которых лежат классы и ресурсы. Тогда Tapestry5 при обнаружении изменений в файлах классов будет автоматически делать их горячую замену. Запуск сервера приложений из каталога поддерживает Jetty. Цели для запуска Jetty сконфигурированы выше, а сам проект уже содержит нужные настройки как для целей maven, так и опций проекта для NetBeans.
Для запуска проекта в Jetty нужно выбрать нужную цель maven в меню:

![http://orion-cpu.googlecode.com/svn/wiki/menu_maven_goal.png](http://orion-cpu.googlecode.com/svn/wiki/menu_maven_goal.png)

(настройка сделана по статье [RAD w/ Tapestry 5, NetBeans 6.7, Maven, and Jetty : Really !!!](http://www.troymaxventures.com/2009/05/rad-w-tapestry-5-netbeans-67-maven-and.html)).

### Проверка русской орфографии ###
Запускаем NetBeans, открываем вкладку Tools -> Options -> Miscellaneous ->Spellchecker.
На вкладке нажимаем Add… и выбираем наш файл [aspell\_dump-en\_US+ru-yeyo+uk.txt](http://orion-cpu.googlecode.com/files/aspell_dump-en_US%2Bru-yeyo%2Buk.rar). Устанавливаем кодировку словаря в UTF-8 и вписываем локаль «ru».
Нажимаем Add — словарь должен появиться в списке. Для проверки орфографии русского языка выбираем локаль по-умолчанию «ru». Словарь будет использоваться только когда локаль словаря точно совпадает с локалью по-умолчанию.
Закрываем диалог и переходим в редактор. Окрываем любой файл, пишем слово по русски и сохраняем. NetBeans запустит процесс проверки орфографии, который в первый раз будет генерировать индекс из нашего словаря. Поэтому не торопимся и ждем.
Индексы словарей хранятся в папке ~/.netbeans/7.1beta/var/cache/dict/. Для нашего словаря должен появится файл dictionary\_ru.trie1.
При удалении словаря из NetBeans индексный файл не удаляется, нужно удалить его вручную.
(взято из [Установка словарей для проверки орфографии в NetBeans](http://habrahabr.ru/blogs/java/133552/))

Настройка среды завершена.