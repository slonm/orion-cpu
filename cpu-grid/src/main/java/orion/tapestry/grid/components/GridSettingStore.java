package orion.tapestry.grid.components;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.ajax.MultiZoneUpdate;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import orion.tapestry.grid.lib.savedsettings.IGridSettingStore;

/**
 * Интерфейс для сохранения/загрузки разных настроек таблицы
 * @author dobro
 */
@Import(stylesheet = {"gridsettingstore.css"}, library = {"gridsettingstore.js"})
public class GridSettingStore {

    /**
     * обьект для доступа к сохранённым наборам настроек
     */
    @Parameter
    @Property
    private IGridSettingStore gridSettingStore;
    /**
     * Блок, который содержит управляющие элементы,
     * необходимые для сохранения/загрузки свойств компоненты Grid
     */
    @Inject
    private Block optionalBlockSavedGridProperties;
    /**
     * Данные из формы
     */
    @Inject
    private Request request;

    /**
     * Проверяет, был ли предоставлен компоненте объект,
     * ответственный за сохранение и загрузку настроек компоненты
     */
    public Block getBlockSavedGridProperties() {
        if (this.gridSettingStore == null) {
            return null;
        } else {
            return optionalBlockSavedGridProperties;
        }
    }
    // ========= загрузка/удаление сохранённых настроек = начало ===============
    /**
     * форма для загрузки сохранённых настроек
     */
    @Component(id = "loadGridPropertiesForm")
    private Form _loadGridPropertiesForm;
    /**
     * Зона для формы загрузки свойств
     */
    @InjectComponent
    private Zone loadSettingZone;
    /**
     * Идентификатор загружаемого набора настроек
     */
    private Long selectedGridPropertiesId = null;
    /**
     * Флаг, который показыает, была ли нажата кнопка "удалить"
     */
    private boolean _deleteSetting = false;

    void onSelectedFromDelete() {
        _deleteSetting = true;
    }

    /**
     * Выдача сохранённых настроек с заданным номером
     */
    Object onSuccessFromLoadGridPropertiesForm() {
        //searchHits = searchService.performSearch(query);
        String postedId = request.getParameter("block-saved-properties-selector");
        //System.out.println("postedId="+postedId);
        try {
            this.selectedGridPropertiesId = new Long(postedId);
            if (_deleteSetting) {
                this.gridSettingStore.deleteSavedSetting(selectedGridPropertiesId);
                this.selectedGridPropertiesId = null;
            }
        } catch (NumberFormatException e) {
        }
        return loadSettingZone.getBody();
    }

    public String getSelectedGridProperties() {
        if (this.selectedGridPropertiesId == null) {
            return "null";
        } else {
            //return selectedGridPropertiesId.toString();//gridSettingStore.getSavedSetting(selectedGridPropertiesId);
            return gridSettingStore.getSavedSetting(selectedGridPropertiesId);
        }
    }
    // ========= загрузка/удаление сохранённых настроек = конец ================
    // =================== сохранение настроек = начало ========================
    /**
     * Зона для формы сохранения свойств
     */
    @InjectComponent
    private Zone saveSettingZone;
    /**
     * форма для загрузки сохранённых настроек
     */
    @Component(id = "saveGridPropertiesForm")
    private Form _saveGridPropertiesForm;
    /**
     * Название нового набора свойств
     */
    @Property
    private String newGridPropertiesName;
    /**
     * Значение нового набора свойств
     */
    @Property
    private String newGridPropertiesValue;

    /**
     * Сохранение настроек с заданным номером
     */
    Object onSuccessFromSaveGridPropertiesForm() {
        gridSettingStore.saveSetting(newGridPropertiesName, newGridPropertiesValue);
        System.out.println("onSuccessFromSaveGridPropertiesForm:"+gridSettingStore.getSavedSettingListJSON());
        return new MultiZoneUpdate("loadSettingZone", loadSettingZone.getBody()).add("saveSettingZone", saveSettingZone.getBody());
    }
    // =================== сохранение настроек = конец =========================
}
