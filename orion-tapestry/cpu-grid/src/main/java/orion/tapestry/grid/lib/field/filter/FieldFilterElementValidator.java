package orion.tapestry.grid.lib.field.filter;

/**
 * Проверка и/или преобразование строки к нужному типу данных.
 * Строка вводится пользователем в форме фильтрации.
 * @author Gennadiy Dobrovolsky
 */
public interface FieldFilterElementValidator<T> {

    /**
     * метод проверяет, можно ли преобразовать строку в объект типа T
     * @param value строка, которую следует проверить.
     * @return true, если строку можно превратить в объект типа T и в false в проивном случае.
     */
    public boolean isValid(String value);

    /**
     * @param value строка, которую следует превратить в объект.
     * @return объект типа T, созданный из строки value, или null, если преобразование не удалось.
     */
    public T fromString(String value);

    /**
     *
     * @return функция javascript, которую следует использовать для валидации на стороне клиента
     */
    public String getJSValidator();
}
