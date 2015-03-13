# CSS для библиотек компонентов #

Если требуется добавить какие-то стили, используемые в библиотеке модели, то достаточно создать файл стиля с именем `<LibName>`.css в директории ua/orion/web. Стили могут быть локализованы по общим правилам добавлением суффикса к имени файла. **Все** такие стили будут включены во **все** страницы, поэтому злоупотреблять ими не следует и использовать только при необходимости.

Так как для каждой библиотеки модели можно создать библиотеку компонентов, то в стили этой библиотеки может включаться что-то специфичное для модели данных. Например локализованные перечисления значений.

Другой пример - изменение стиля отображения ячеек Grid. У Grid каждая ячейка имеет собственный класс соответствующий атрибуту. Если нужно сделать что-бы свойство isObsolete выравнивалось по центру, а rang из IRangiable по правому краю нужно создать следующий файл стилей:

`OrionCore.css`
```
table.t-data-grid tbody tr td.isObsolete {
    text-align: center;
}

table.t-data-grid tbody tr td.rang {
    text-align: right;
}
```

# Реализация #

Эту функциональность целиком реализует следующий метод в модуле конфигурации IOC библиотеки orion-web:
```
    public void contributeMarkupRenderer(OrderedConfiguration<MarkupRendererFilter> configuration,
            final AssetSource assetSource, final Environment environment,
            final ModelLibraryService modelLibraryService) {
        for (ModelLibraryInfo modelLibraryInfo : modelLibraryService.getModelLibraryInfos()) {
            try {
                final Asset stylesheet = assetSource.getClasspathAsset("ua/orion/web/" + modelLibraryInfo.getLibraryName() + ".css");
                MarkupRendererFilter injectDefaultStylesheet = new MarkupRendererFilter() {

                    public void renderMarkup(MarkupWriter writer, MarkupRenderer renderer) {
                        DocumentLinker linker = environment.peekRequired(DocumentLinker.class);
                        linker.addStylesheetLink(new StylesheetLink(stylesheet.toClientURL()));
                        renderer.renderMarkup(writer);
                    }
                };
                configuration.add("Inject" + modelLibraryInfo.getLibraryName() + "Styleheet",
                        injectDefaultStylesheet, "after:InjectDefaultStyleheet");
            } catch (RuntimeException e) {
            }
        }
    }
```