# Как использовать компоненту Grid #


В классе страницы надо включить два сервиса
```
    /**
     * Сервис-создатель источников данных
     */
    @Inject
    private CpuGridDataSourceFactory cpuGridDataSourceFactory;
    /**
     * Сервис для создания модели, описывающей таблицу
     */
    @Inject
    private GridBeanModelSource gridBeanModelSource;
```
Свойство currentRow необходимо для доступа к строкам таблицы,
его тоже надо добавить в страницу
```
    /**
     * Временная переменная для цикла по строкам
     * Цикл объявлен в шаблоне компоненты Grid
     */
    @SuppressWarnings("unused")
    @Property
    private IEntity currentRow;
```
Для работы компоненты обязательно нужен источник данных,
поэтому создаем метод, который возвращает этот источник данных
```
    /**
     * Источник данных таблицы
     */
    public DataSource getDataSource() {
      // objectClass - это класс сущности JPA
      DataSource ds = cpuGridDataSourceFactory.createDataSource(objectClass);
      return ds;
    }
```
Тип источника данных -
```
public interface orion.tapestry.grid.lib.datasource.DataSource extends org.apache.tapestry5.grid.GridDataSource
```
Сервис CpuGridDataSourceFactory создает типичный источник данных.
В данном случае это экземпляр класса JPADataSource.


В файле шаблона следует добавить компоненту grid.Grid:
```
<t:grid.Grid source="dataSource" row="currentRow"/>
```


## Настройка компоненты ##

Ниже в шаблоне используются:
  * Пространство имен t - это элементы шаблона tapestry (в заголовке шаблона добавлено xmlns:t="http://tapestry.apache.org/schema/tapestry_5_1_0.xsd")
  * Пространство имен p - это параметры шаблона tapestry (в заголовке шаблона добавлено xmlns:p="tapestry:parameter")

Добавление дополнительной колонки в таблицу выполняется с помощью параметра **add** в шаблоне. **add** - перечисленные через запятую названия колонок, которые следует добавить к модели таблицы. Ячейки в добавленных колонках будут пустыми, если не задать шаблон из содержимого. Поэтому следует определить внутри элемента <t:grid.Grid> шаблон содержимого ячейки. Например, чтобы добавить колонку с названием **yourColumn**, надо написать в шаблоне страницы:
```
<t:grid.Grid source="dataSource" row="currentRow" add="yourColumn">
        <p:yourColumnCell>
           Содержимое дополнительной колонки
        </p:yourColumnCell>
</t:grid.Grid>
```


Шаблон ячейки можно задать внутри элемента.
Пусть, например, в таблице есть колонка name. Тогда можно задать особенный шаблон ячейки с помощью параметра p:nameCell.
```
<t:grid.Grid source="dataSource" row="currentRow">
        <p:nameCell>
           ${currentRow.name}
        </p:nameCell>
</t:grid.Grid>
```

Колонки, которые должны остаться в компоненте, можно через запятую перечислить в параметре **include**. Причем эти колонки будут выстроены именно в том порядке, в котором перечислены.

Параметр **exclude** - это перечисленные через запятую имена колонок, которые надо исключить из таблицы.

Параметр **reorder** - это перечисленные через запятую имена колонок, которые указывают, в каком порядке следует упорядочить колонки. Если имя колонки не упомянуто в параметре **reorder**, то колонка будет показана правее перечисленных колонок в **reorder** колонок. Порядок колонок можно потом изменить в процессе просмотра таблицы с помощью формы свойств.





Если надо создать особенную модель,
проще всего использовать сервис GridBeanModelSource (довольно сложная вещь).
А потом модифицировать полученную модель.
Например, следующий метод используется в странице CrudList,
чтобы заменить стандартный источник сообщений:
```
    public GridBeanModel getModel() {
      //  MsgAdapter модифицированный источник сообщений
      return gridBeanModelSource.createDisplayModel(objectClass, new MsgAdapter());
    }
```



Если надо создать какой-нибудь другой источник данных (например, дополнить условия выборки из базы данных)
то можно воспроизвести и слегка модифицировать метод createDataSource класса orion.tapestry.grid.services.CpuGridDataSourceFactoryImpl
```
   @Inject EntityManager entityManager;

    public DataSource getDataSource(Class objectClass) {
        // создаём модель данных
        GridBeanModel gridBeanModel = gridBeanModelSource.createDisplayModel(objectClass, messages);
        // создаем редактор условия выборки
        RestrictionEditorJPACriteria editor = new RestrictionEditorJPACriteria(objectClass, entityManager);
        // создаём источник данных
        JPADataSource dataSource = new JPADataSource(gridBeanModel, entityManager, editor){
           /**
            * Применяет дополнительные условия выборки строк
            * этот метод следует перекрывать
            * @param criteria
            */
            protected void applyAdditionalConstraints(CriteriaQuery criteria) {
               // здесь можно модифицировать условия выборки из БД
            }
        };
        return dataSource;
    }
```

Существует источник данных на основе списка
orion.tapestry.grid.lib.datasource.impl.ListDataSource


чтобы подключить компоненту к проекту, надо добавить в проект maven зависимости
```
        <dependency>
            <groupId>orion</groupId>
            <artifactId>cpu-grid</artifactId>
            <version>2.1-SNAPSHOT</version>
            <type>jar</type>
        </dependency>
        <dependency>
            <groupId>orion</groupId>
            <artifactId>cpu-grid-jpa</artifactId>
            <version>2.0-SNAPSHOT</version>
            <type>jar</type>
        </dependency>
        <dependency>
            <groupId>orion</groupId>
            <artifactId>cpu-grid-jpa-crud</artifactId>
            <version>2.0-SNAPSHOT</version>
            <type>jar</type>
        </dependency>
```

и добавить в параметры аннотации @SubModule
классы CpuGridModule.class, CpuGridJPA.class, CpuGridJPACrudModule.class

## Отображение нестандартных типов данных ##

## Созранение настроек ##