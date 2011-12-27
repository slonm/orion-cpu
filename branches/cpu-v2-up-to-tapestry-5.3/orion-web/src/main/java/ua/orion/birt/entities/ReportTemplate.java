package ua.orion.birt.entities;

import javax.persistence.*;
import org.apache.tapestry5.beaneditor.DataType;
import org.hibernate.annotations.Type;
import ua.orion.core.persistence.AbstractEnumerationEntity;

/**
 * Шаблон отчета. У отчета есть уникальное имя (Name) и xml шаблон (RptDesign).
 * @author sl
 */
@Entity
public class ReportTemplate extends AbstractEnumerationEntity<ReportTemplate> {

    /**
     * Содержимое файла .rptdesign
     */
    private String body;

    @Lob
    @Type(type="org.hibernate.type.StringClobType")
    @DataType("longtext")
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
