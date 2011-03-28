package orion.cpu.entities.sys;

import javax.persistence.*;
import org.apache.tapestry5.beaneditor.DataType;
import orion.cpu.baseentities.NamedEntity;

/**
 * Хранимые в базе данных tml шаблоны страниц
 * @author sl
 */
@Entity
@Table(schema = "sys", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name"})})
public class PageTemplate extends NamedEntity<PageTemplate> {

    private String body;

    /**
     * Get the value of body
     *
     * @return the value of body
     */
    @Lob
    @DataType("longtext")
    public String getBody() {
        return body;
    }

    /**
     * Set the value of body
     *
     * @param body new value of body
     */
    public void setBody(String body) {
        this.body = body;
    }
}
