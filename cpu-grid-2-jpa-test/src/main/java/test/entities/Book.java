package test.entities;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Книга
 * @author dobro
 */
@Entity
public class Book implements Serializable {

    /**
     * Идентификатор книги
     */
    @Id
    @GeneratedValue
    private int id;
    /**
     * Название книги
     */
    private String title;
    /**
     * Издатель книги
     */
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "PUBLISHER_ID")
    private Publisher publisher = new Publisher();

    public Book(){
    }

    public Book(String _title,Publisher _publisher){
        title=_title;
        publisher=_publisher;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String _title) {
        this.title = _title;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher _publisher) {
        this.publisher = _publisher;
    }
}
