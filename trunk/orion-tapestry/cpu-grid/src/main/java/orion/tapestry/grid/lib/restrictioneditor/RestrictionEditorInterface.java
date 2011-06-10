package orion.tapestry.grid.lib.restrictioneditor;

import java.util.Date;


/**
 * Логическое выражение
 * в формате обратной бесскобочной записи
 * @param <T> тип вычисленного выражения
 * @author Gennadiy Dobrovolsky
 */
public interface RestrictionEditorInterface<T> {

    /**
     * оператор "больше чем",
     * применим к числам, строкам, датам, атрибутам объектов
     * имеет два операнда
     * возвращает значение типа constBoolean
     * @return текущий обект для построения цепочки
     * @throws RestrictionEditorException означает, что оператор неприменим в данном месте
     */
    public RestrictionEditorInterface<T> gt() throws RestrictionEditorException;

    /**
     * оператор "больше или равно чем",
     * применим к числам, строкам, датам, атрибутам объектов
     * имеет два операнда
     * возвращает значение типа constBoolean
     * @return текущий обект для построения цепочки
     * @throws RestrictionEditorException означает, что оператор неприменим в данном месте
     */
    public RestrictionEditorInterface<T> ge() throws RestrictionEditorException;

    /**
     * оператор "равно",
     * применим к числам, строкам, датам, атрибутам объектов
     * имеет два операнда
     * возвращает значение типа constBoolean
     * @return текущий обект для построения цепочки
     * @throws RestrictionEditorException означает, что оператор неприменим в данном месте
     */
    public RestrictionEditorInterface<T> eq() throws RestrictionEditorException;

    /**
     * оператор "не равно",
     * применим к числам, строкам, датам, атрибутам объектов
     * имеет два операнда
     * возвращает значение типа constBoolean
     * @return текущий обект для построения цепочки
     * @throws RestrictionEditorException означает, что оператор неприменим в данном месте
     */
    public RestrictionEditorInterface<T> neq() throws RestrictionEditorException;

    /**
     * оператор "меньше или равно чем",
     * применим к числам, строкам, датам, атрибутам объектов
     * имеет два операнда
     * возвращает значение типа constBoolean
     * @return текущий обект для построения цепочки
     * @throws RestrictionEditorException означает, что оператор неприменим в данном месте
     */
    public RestrictionEditorInterface<T> le() throws RestrictionEditorException;

    /**
     * оператор "меньше чем",
     * применим к числам, строкам, датам, атрибутам объектов
     * имеет два операнда
     * возвращает значение типа constBoolean
     * @return текущий обект для построения цепочки
     * @throws RestrictionEditorException означает, что оператор неприменим в данном месте
     */
    public RestrictionEditorInterface<T> lt() throws RestrictionEditorException;

    /**
     * оператор "принадлежит множеству",
     * применим к числам, строкам, датам, атрибутам объектов
     * имеет два операнда, первый - значение, второй - множество
     * возвращает значение типа constBoolean
     * @return текущий обект для построения цепочки
     * @throws RestrictionEditorException означает, что оператор неприменим в данном месте
     */
    public RestrictionEditorInterface<T> in() throws RestrictionEditorException;

    /**
     * оператор "равно NULL",
     * применим к числам, строкам, датам, атрибутам объектов
     * имеет один операнд
     * возвращает значение типа constBoolean
     * @return текущий обект для построения цепочки
     * @throws RestrictionEditorException означает, что оператор неприменим в данном месте
     */
    public RestrictionEditorInterface<T> isNull() throws RestrictionEditorException;

    /**
     * оператор "не равно NULL",
     * применим к числам, строкам, датам, атрибутам объектов
     * имеет один операнд
     * возвращает значение типа constBoolean
     * @return текущий обект для построения цепочки
     * @throws RestrictionEditorException означает, что оператор неприменим в данном месте
     */
    public RestrictionEditorInterface<T> isNotNull() throws RestrictionEditorException;

    /**
     * оператор "подходит по шаблону",
     * применим к строкам, и строковым атрибутам объектов
     * имеет два операнда
     * возвращает значение типа constBoolean
     * @return текущий обект для построения цепочки
     * @throws RestrictionEditorException означает, что оператор неприменим в данном месте
     */
    public RestrictionEditorInterface<T> like() throws RestrictionEditorException;


    /**
     * оператор "содержит подстроку",
     * применим к строкам, и строковым атрибутам объектов
     * имеет два операнда
     * возвращает значение типа constBoolean
     * @return текущий обект для построения цепочки
     * @throws RestrictionEditorException означает, что оператор неприменим в данном месте
     */
    public RestrictionEditorInterface<T> contains() throws RestrictionEditorException;

    /**
     * оператор "подходит по шаблону без учёта регистра символов",
     * применим к строкам, и строковым атрибутам объектов
     * имеет два операнда
     * возвращает значение типа constBoolean
     * @return текущий обект для построения цепочки
     * @throws RestrictionEditorException означает, что оператор неприменим в данном месте
     */
    public RestrictionEditorInterface<T> ilike() throws RestrictionEditorException;

    /**
     * комбинация двух выражений с помощью логического И
     * применим к строкам, и строковым атрибутам объектов
     * имеет два операнда
     * возвращает значение типа constBoolean
     * @return текущий обект для построения цепочки
     * @throws RestrictionEditorException означает, что оператор неприменим в данном месте
     */
    public RestrictionEditorInterface<T> and() throws RestrictionEditorException;

    /**
     * комбинация двух выражений с помощью логического ИЛИ
     * применим к строкам, и строковым атрибутам объектов
     * имеет два операнда
     * возвращает значение типа constBoolean
     * @return текущий обект для построения цепочки
     * @throws RestrictionEditorException означает, что оператор неприменим в данном месте
     */
    public RestrictionEditorInterface<T> or() throws RestrictionEditorException;

    /**
     * отрицание применим к constBoolean
     * имеет один операнд
     * возвращает значение типа constBoolean
     * @return текущий обект для построения цепочки
     * @throws RestrictionEditorException означает, что оператор неприменим в данном месте
     */
    public RestrictionEditorInterface<T> not() throws RestrictionEditorException;

    /**
     * Метод вычисляет окончательное выражение
     * @return выражение для фильтра,
     * которое можно напрямую использовать при выборке строк
     * @throws RestrictionEditorException ошибка, возникшая от невозможности составить выражение 
     */
    public T getValue() throws RestrictionEditorException;



    /**
     * Создать новое выражение
     */
    public void createEmpty();

    /**
     * Метод подсчитывает количество независимых частей,
     * которые ещё предстоит обработать.
     * Например, если выражение содержит 2 слагаемых, то size() вернёт число 2
     * @return размер выражения, количество ещё не независимых частей
     */
    public int size();

    /**
     * название атрибута объекта
     * операция - добавление операнда в выражение
     * @param value 
     * @return текущий обект для построения цепочки
     */
    public RestrictionEditorInterface<T> constField(String value);


    /**
     * Константа, которую надо вставить в запрос
     * операция - добавление операнда в выражение
     * @param value значение, которое надо добавить в выражение
     * @return текущий обект для построения цепочки
     */
    public RestrictionEditorInterface<T> constValue(Object value);


    /**
     * Константа, которую надо вставить в запрос
     * операция - добавление операнда в выражение
     * @param value значение, которое надо добавить в выражение
     * @return текущий обект для построения цепочки
     */
    public RestrictionEditorInterface<T> constValueList(Object ... value);

//    /**
//     * дата и время
//     * операция - добавление операнда в выражение
//     * @param value значение, которое надо добавить в выражение
//     * @return текущий обект для построения цепочки
//     */
//    public RestrictionEditorInterface<T> constDate(Date value);
//
//    /**
//     * строковая константа
//     * операция - добавление операнда в выражение
//     * @param value значение, которое надо добавить в выражение
//     * @return текущий обект для построения цепочки
//     */
//    public RestrictionEditorInterface<T> constString(String value);
//
//    /**
//     * числовая константа, действительное число
//     * операция - добавление операнда в выражение
//     * @param value значение, которое надо добавить в выражение
//     * @return текущий обект для построения цепочки
//     */
//    public RestrictionEditorInterface<T> constFloat(Float value);
//
//    /**
//     * числовая константа, целое число
//     * операция - добавление операнда в выражение
//     * @param value значение, которое надо добавить в выражение
//     * @return текущий обект для построения цепочки
//     */
//    public RestrictionEditorInterface<T> constInteger(Integer value);
//
//    /**
//     * числовая константа, целое число
//     * операция - добавление операнда в выражение
//     * @param value значение, которое надо добавить в выражение
//     * @return текущий обект для построения цепочки
//     */
//    public RestrictionEditorInterface<T> constLong(Long value);
//    /**
//     * Массив целых чисел
//     * операция - добавление операнда в выражение
//     * @param value значение, которое надо добавить в выражение
//     * @return текущий обект для построения цепочки
//     */
//    public RestrictionEditorInterface<T> constIntegerList(Integer ... value);
//
//    /**
//     * булевская константа(TRUE|FALSE),
//     * операция - добавление операнда в выражение
//     * @param value значение, которое надо добавить в выражение
//     * @return текущий обект для построения цепочки
//     */
//    public RestrictionEditorInterface<T> constBoolean(Boolean value);
//
//    /**
//     * Массив строковых констант
//     * операция - добавление операнда в выражение
//     * @param value значение, которое надо добавить в выражение
//     * @return текущий обект для построения цепочки
//     */
//    public RestrictionEditorInterface<T> constStringList(String ... value);

}
