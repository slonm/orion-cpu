package orion.tapestry.grid.lib.field.filter;

/**
 * Возможные типы елементов GUI для формы фильтрации
 * @author root
 */
public enum FieldFilterElementGUIType {
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
