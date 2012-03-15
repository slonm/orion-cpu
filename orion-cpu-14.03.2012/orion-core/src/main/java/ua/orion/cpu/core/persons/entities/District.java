package ua.orion.cpu.core.persons.entities;

import javax.persistence.*;
import org.apache.tapestry5.beaneditor.DataType;
import ua.orion.core.persistence.*;

/**
 * Справочный класс - область Хранит список областей
 *
 * @author molodec
 */
@Entity
@ReferenceBook
public class District extends AbstractEnumerationEntity<District> {

    private static final long serialVersionUID = 1L;
    @ManyToOne
    private Region region;
    @DataType("longtext")
    private String addinitionalData;

    public District() {
    }

    public District(String name) {
        setName(name);
    }

    public District(String name, Region region, String addinitionalData) {
        setName(name);
        this.region = region;
        this.addinitionalData = addinitionalData;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    /**
     * Дополнительная информация о области Данное поле хранит все то, что
     * относится к области, но не относится ни к одному другому полю данной
     * сущности
     *
     * @return
     */
    public String getAddinitionalData() {
        return addinitionalData;
    }

    public void setAddinitionalData(String addinitionalData) {
        this.addinitionalData = addinitionalData;
    }
}
