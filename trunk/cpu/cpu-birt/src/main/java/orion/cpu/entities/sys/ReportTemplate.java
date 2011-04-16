package orion.cpu.entities.sys;

import javax.persistence.*;
import org.apache.tapestry5.beaneditor.DataType;
import org.hibernate.annotations.Type;
import orion.cpu.baseentities.NamedEntity;

/**
 * Шаблон отчета. У отчета естьуникальное имя (Name) и xml шаблон (RptDesign).
 * @author sl
 */
@Entity
@Table(schema = "sys", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name"})})
public class ReportTemplate extends NamedEntity<ReportTemplate> {

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
