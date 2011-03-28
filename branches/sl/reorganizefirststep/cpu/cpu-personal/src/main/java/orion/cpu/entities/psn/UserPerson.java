package orion.cpu.entities.psn;

import br.com.arsmachina.authentication.entity.User;
import javax.persistence.*;

/**
 * @author sl
 */
@Entity
public class UserPerson extends User {

    private static final long serialVersionUID = 1L;

    private Person person;

    @ManyToOne
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

}
