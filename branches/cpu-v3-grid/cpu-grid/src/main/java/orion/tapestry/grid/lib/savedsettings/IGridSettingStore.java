package orion.tapestry.grid.lib.savedsettings;

/**
 * Сохранение и загрузка свойств компоненты
 * @author dobro
 */
public interface IGridSettingStore {
    /**
     * Возвращает строку, представляющую в формате JSON,
     * сохранённые ранее свойства формы
     * @param id уникальный идентификатор набора свойств
     */
    public String getSavedSetting(Long id);
    /**
     * удаляет сохранённый набор настроек
     * @param id уникальный идентификатор набора настроек
     */
    public boolean deleteSavedSetting(Long id);

    /**
     * @return сохранённые наборы настроек в формате JSON
     */
    public String getSavedSettingListJSON();

    /**
     * Сохранение набора свойств
     * @param id идентификатор
     * @param label название набора
     * @param setting набор свойств
     * @return true, если сохранение произошло успешно
     */
    public boolean saveSetting(String label, String setting);
}
