package orion.cpu.entities.org;

import javax.persistence.*;
import orion.cpu.baseentities.BaseEntity;

/**
 *
 * @author sl
 */
@Entity
@Table(schema = "org")
public class FuncStructureUnit extends BaseEntity<FuncStructureUnit> {
private OrgUnit orgUnit;

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
