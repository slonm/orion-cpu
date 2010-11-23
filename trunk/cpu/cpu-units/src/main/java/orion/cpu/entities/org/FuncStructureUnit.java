package orion.cpu.entities.org;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import orion.cpu.baseentities.BaseEntity;

/**
 *
 * @author sl
 */
//TODO проработать
@Entity
@Table(schema = "org")
public class FuncStructureUnit extends BaseEntity<FuncStructureUnit> {

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
