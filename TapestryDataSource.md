# Сервис `TapestryDataSource` #

Сервис является источником для ориентированных на сущности источников данных и моделей данных для компонентов библиотеки Tapestry core. Каждый запрашиваемый объект создается с помощью сервиса `TapestryDataFactory` и проходит по цепочке фильтров типа `TapestryDataTransformer`, которые являются вкладом в `TapestryDataSource`. Последнее звено цепочки `TapestryDataTransformer` это `EntityTapestryDataTransformer`. Этот трансформер ищет во всех _библиотеках компонентов_ в подпакете **transformers** класс реализующий `TapestryDataTransformer` с именем `Entity``TapestryDataTransformer`, где `Entity` - имя сущности, для которой запрашивается объект. Если такой трансформер будет найден, то он будет задействован. При написании своего `TapestryDataTransformer` рекомендуется расширять абстрактный класс `AbstractTapestryDataTransformer` и переопределять в нем только нужные методы.

# Пример трансформера #

Сущности:

```
package test.licensing.entities;

public class LicenseRecord {

    @Id private Integer id;
    private String code;
    @OneToMany
    private TrainingDirection trainingDirection;
    @Formula("(select ka.code from Training_Direction tr join ref.knowledge_Area ka on tr.knowledge_Area=ka.id where tr.id=training_Direction)")
    private String knowledgeAreaCode;
    @Formula("(select ka.name from Training_Direction tr join ref.knowledge_Area ka on tr.knowledge_Area=ka.id where tr.id=training_Direction)")
    private String knowledgeAreaName;
    ...
    }

public class TrainingDirection {

    @Id private Integer id;
    private String code;
    private String name;
    @OneToMany
    private KnowledgeArea knowledgeArea;
    ...
}

public class KnowledgeArea {

    @Id private Integer id;
    private String code;
    private String name;
    ...
}
```

При отображении `LicenseRecord` нужно видеть код и название `KnowledgeArea`.
При заполнении `LicenseRecord` не нужно видеть код и название `KnowledgeArea`, но нужно выбирать подходящий `TrainingDirection`, причем видеть к какому `KnowledgeArea` он относится.

Для непосредственного доступа к значениям кода и названия `KnowledgeArea` введены вычислимые поля `knowledgeAreaCode` и `knowledgeAreaName`.
Т.е. в `BeanModel` для отображения они будут присутствовать, но так же они будут присутствовать и в `BeanModel` для ввода/редактирования.

Кроме этого в списке вариантов `TrainingDirection` будет видно только имя `TrainingDirection`, а к какому `KnowledgeArea` он принадлежит - нет.

Сделаем `TapestryDataTransformer` для `LicenseRecord`. Имя будет `LicenseRecordTapestryDataTransformer`, а пакет это подпакет transformers любой библиотеки компонентов Tapestry.

Трансформер для всех методов возвращающих `BeanModel` для редактирования удаляет свойства `"KnowledgeAreaCode"` и `"KnowledgeAreaName"`.

Изменение в `SelectModel` для поля `TrainingDirection` приводят к тому, что вместо просто названия `TrainingDirection` в списке опций будет показано название в виде `"KnowledgeArea - TrainingDirection"`

```
package test.web.licensing.transformers;

public class LicenseRecordTapestryDataTransformer extends AbstractTapestryDataTransformer {

    private final EntityService es;

    public LicenseRecordTapestryDataTransformer(EntityService es) {
        this.es = es;
    }
    
    @Override
    public <T> BeanModel<T> transformBeanModelForAdd(BeanModel<T> model) {
        return transform(model);
    }

    @Override
    public <T> BeanModel<T> transformBeanModelForAdd(BeanModel<T> model, Messages messages) {
        return transform(model);
    }

    @Override
    public <T> BeanModel<T> transformBeanModelForEdit(BeanModel<T> model) {
        return transform(model);
    }

    @Override
    public <T> BeanModel<T> transformBeanModelForEdit(BeanModel<T> model, Messages messages) {
        return transform(model);
    }

    private <T> BeanModel<T> transform(BeanModel<T> model) {
        model.exclude("KnowledgeAreaCode", "KnowledgeAreaName");
        return model;
    }

    @Override
    public <T> SelectModel transformSelectModel(SelectModel model, Class<T> entityClass, String property) {
        if ("TrainingDirection".equalsIgnoreCase(property)) {
            ListIterator<OptionModel> it = (ListIterator<OptionModel>) model.getOptions().listIterator();
            while(it.hasNext()){
                OptionModel om=it.next();
                final TrainingDirection tdos=(TrainingDirection) om.getValue();
                it.set(new AbstractOptionModel() {

                @Override
                public String getLabel() {
                    return es.getStringValue(tdos.getKnowledgeArea())+" - "+es.getStringValue(tdos);
                }

                @Override
                public Object getValue() {
                    return tdos;
                }
            });
            }
        }
        return model;
    }
}
```