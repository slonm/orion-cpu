package orion.cpu.entities.pub;

import orion.cpu.baseentities.*;
import javax.persistence.*;

/**
 * Изображения документа
 * @author sl
 */
@Entity
@Table(schema="pub", uniqueConstraints = @UniqueConstraint(columnNames = {"document", "name"}))
public class DocumentImage extends NamedEntity<DocumentImage> {

    private Document document;
    private Byte[] image = null;

    @ManyToOne
    @JoinColumn(insertable = false, updatable = false)
    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    @Lob
    public Byte[] getImage() {
        return image;
    }

    public void setImage(Byte[] images) {
        this.image = images;
    }
}
