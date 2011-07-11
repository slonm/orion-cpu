package trash;

import java.util.Collections;
import java.util.List;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import orion.tapestry.grid.lib.field.sort.GridFieldSort;
import orion.tapestry.grid.lib.field.sort.GridFieldSortComparator;

/**
 * Форма для управления видимостью и последовательностью
 * отметки о видимости и управление последовательностью колонок
 * @author Gennadiy Dobrovolsky
 */
@IncludeJavaScriptLibrary({"gridsort.js","scriptaculous.js","dragdrop.js","effects.js"})
public class GridSort {
    /**
     * Ссылка на фрагмент шаблона, у которого установлен t:id="gridviewform"
     * используется для сообщений об ошибках
     */
    @Component(id = "gridsortform")
    private Form _form;

    /**
     * Источник/приёмник данных формы,
     * источник данных для цикла
     * Цикл объявлен в шаблоне
     */
    @Parameter(principal = true)
    private List<GridFieldSort> fieldSortList;

    public List<GridFieldSort> getFieldSort(){
        Collections.sort(this.fieldSortList, new GridFieldSortComparator());
        return this.fieldSortList;
    }

    /**
     * Временная переменная для цикла
     * Цикл объявлен в шаблоне
     */
    @SuppressWarnings("unused") // <= меньше мусора при компиляции
    @Property                   // <= чтобы не писать примитивные методы get...() и set...()
    private GridFieldSort fieldSortElement;


    /**
     * Вспомогательный кодировщик
     * используется для рисования элементов формы в цикле
     * см. ниже
     *   - private class FieldFilterElementPrimaryKeyEncoder
     *   - void onPrepare()
     */
    @Property // <= чтобы не писать примитивные методы get...() и set...()
    private GridSortElementPrimaryKeyEncoder encoder;

    /**
     * Счётчик для внутренних нужд
     */
    private int counter=0;

    /**
     * Кодировщик для формы
     * Исполняет преобразование Object <=> String
     * Используется для привязки имени элемента формы к объекту из списка
     */
    private class GridSortElementPrimaryKeyEncoder implements ValueEncoder<GridFieldSort> {

        @Override
        public String toClient(GridFieldSort value) {
            return "" + value.getUid();
        }

        @Override
        public GridFieldSort toValue(String keyAsString) {
            for (GridFieldSort ps : fieldSortList) {
                if (ps.getUid().equalsIgnoreCase(keyAsString)) {
                    return ps;
                }
            }
            return null;
        }

        public final List<GridFieldSort> getAllValues() {
            return fieldSortList;
        }
    };

    // Form triggers the  PREPARE event during form render and form submission.
    void onPrepare() {
        encoder = new GridSortElementPrimaryKeyEncoder();
    }

    public int getCounter(){
        return counter++;
    }
}
