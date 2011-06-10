package orion.tapestry.grid.lib.field.filter;

/**
 * Возможные типы елементов формы
 * @author root
 */
public enum FieldFilterElementType {
  TEXTAREA   , // Текстовая область
  DATE       , // Ввод даты, текстовое поле как для числа + выпадающий календарь
  TEXT       , // текстовое поле

  RADIOGROUP , // Группа радиокнопок
  SELECT     , // Выпадающий список
  CHECKBOX   , // Флажок

  NodeAND,
  NodeOR,
  NodeNOT
}
