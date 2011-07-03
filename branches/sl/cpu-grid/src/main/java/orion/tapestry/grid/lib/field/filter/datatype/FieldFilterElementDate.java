package orion.tapestry.grid.lib.field.filter.datatype;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import orion.tapestry.grid.lib.field.filter.FieldFilterElementDataType;

/**
 *
 * @author dobro
 */
public class FieldFilterElementDate implements FieldFilterElementDataType<Date> {

    /**
     * Форматы дат в виде массива строк
     */
    private static final String[] formatString = {
        "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
        "yyyy-MM-dd HH:mm:ss",
        "yyyy-MM-dd HH:mm",
        "yyyy-MM-dd",
        "HH:mm:ss dd.MM.yyyy",
        "HH:mm dd.MM.yyyy",
        "HH:mm:ss dd.MM.yy",
        "HH:mm dd.MM.yy",
        "dd.MM.yyyy HH:mm:ss",
        "dd.MM.yyyy HH:mm",
        "dd.MM.yy HH:mm:ss",
        "dd.MM.yy HH:mm",
        "dd.MM.yy",
        "EEE, MMM d, ''yy",
        "MM/dd/yyyy HH:mm:ss",
        "MM/dd/yyyy HH:mm",
        "MM/dd/yyyy",
        "MM/dd/yy HH:mm:ss",
        "MM/dd/yy HH:mm",
        "MM/dd/yy"
    };
    /**
     * Объекты для форматирования даты разными способами
     */
    private static SimpleDateFormat[] dateFormatter;

    // Создаём объекты для форматирования даты
    static {
        dateFormatter = new SimpleDateFormat[formatString.length];
        int cnt = formatString.length;
        for (int i = 0; i < cnt; i++) {
            dateFormatter[i] = new java.text.SimpleDateFormat(formatString[i]);
        }
    }

    @Override
    public boolean isValid(String value) {
        return this.fromString(value)!=null;
    }

    /**
     * Пробуем опознать дату в строке, полученной из формы
     * @param dateString строка, полученная из формы
     * @return объект {@link java.util.Date}, описывающий дату
     */
    @Override
    public Date fromString(String dateString) {
        Date dt = null;
        for (SimpleDateFormat dft : FieldFilterElementDate.dateFormatter) {
            try {
                // извлекаем значение
                dt = dft.parse(dateString);
                return dt;
            } catch (ParseException ex) {
            }
        }
        return null;
    }

    @Override
    public String getJSValidator() {
        return "validator_require_date";
    }
}