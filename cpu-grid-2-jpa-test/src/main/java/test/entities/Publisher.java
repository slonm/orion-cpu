package test.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Издатель
 * @author dobro
 */
@Entity
public class Publisher implements Serializable {

    private int id;
    private String title;

    public Publisher(){
    }
    
    public Publisher(String _title){
        title=_title;
    }
    
    @Id
    @GeneratedValue
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
}
