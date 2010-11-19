package orion.cpu.entities.psn;

import br.com.arsmachina.authentication.entity.User;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author sl
 */
@Entity
@Table(schema = "psn")
public class UserPerson extends User {

    private static final long serialVersionUID = 1L;

    private Person person;

    @NotNull
    @ManyToOne
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

}
