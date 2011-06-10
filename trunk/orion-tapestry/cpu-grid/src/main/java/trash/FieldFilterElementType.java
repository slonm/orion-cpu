package trash;

/**
 * Возможные типы елементов формы
 * @author root
 */
public enum FieldFilterElementType {
  NUMBER     , // Число,  короткое текстовое поле
  STRING     , // Строка, длинное текстовое поле
  DATE       , // Ввод даты, текстовое поле как для числа + выпадающий календарь
  TEXT       , // Текстовая область
  DIV        , // Разделитель
  LABEL      , // Надпись

  RADIOGROUP , // Группа радиокнопок
  SELECT     , // Выпадающий список
  CHECKBOX     // Флажок
}
