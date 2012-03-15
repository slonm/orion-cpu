package test.foo.entities;

import java.io.Serializable;
import java.util.*;
import javax.persistence.*;
import ua.orion.core.validation.Unique;

/**
 *
 * @author sl
 */
@Entity
@Unique
public class Person implements Serializable{

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique=true)
    private String fio;
    
    @ManyToOne(cascade=CascadeType.PERSIST)
    private City addressCity;
    
    @OneToMany(cascade=CascadeType.ALL)
    private List<Document> documents=new ArrayList();

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public City getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(City addressCity) {
        this.addressCity = addressCity;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Person other = (Person) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.fio == null) ? (other.fio != null) : !this.fio.equals(other.fio)) {
            return false;
        }
        if (this.addressCity != other.addressCity && (this.addressCity == null || !this.addressCity.equals(other.addressCity))) {
            return false;
        }
        if (this.documents != other.documents && (this.documents == null || !this.documents.equals(other.documents))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 11 * hash + (this.fio != null ? this.fio.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Person{" + "fio=" + fio + '}';
    }
  
}
