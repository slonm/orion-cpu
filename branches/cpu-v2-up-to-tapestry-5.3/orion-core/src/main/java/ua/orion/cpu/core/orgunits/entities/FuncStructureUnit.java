package ua.orion.cpu.core.orgunits.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import ua.orion.core.persistence.AbstractEntity;

/**
 *
 * @author sl
 */
//TODO проработать
@Entity
public class FuncStructureUnit extends AbstractEntity<FuncStructureUnit> {

    private OrgUnit orgUnit;

    @OneToOne
    @NotNull
    public OrgUnit getOrgUnit() {
        return orgUnit;
    }

    public void setOrgUnit(OrgUnit orgUnit) {
        this.orgUnit = orgUnit;
    }

    @Override
    protected boolean entityEquals(FuncStructureUnit obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int compareTo(FuncStructureUnit o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
