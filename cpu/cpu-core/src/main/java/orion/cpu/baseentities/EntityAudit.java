package orion.cpu.baseentities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.*;

/**
 * Встраиваемый объект для аудита работы с сущностями
 * @author sl
 */
@Embeddable
public class EntityAudit implements Serializable {

    private static final long serialVersionUID = 1L;
    private String filler;
    private Date fillDateTime;
    private Date modifyDateTime;
    private String currentUser;

    @Temporal(value = javax.persistence.TemporalType.DATE)
    public Date getFillDateTime() {
        return fillDateTime;
    }

    public String getFiller() {
        return filler;
    }

    @Temporal(value = TemporalType.DATE)
    public Date getModifyDateTime() {
        return modifyDateTime;
    }

    public void setFillDateTime(Date fillDateTime) {
        this.fillDateTime = fillDateTime;
    }

    public void setFiller(String filler) {
        this.filler = filler;
    }

    public void setModifyDateTime(Date modifyDateTime) {
        this.modifyDateTime = modifyDateTime;
    }

    public void record() {
        setModifyDateTime(Calendar.getInstance().getTime());
        setFiller(currentUser);
    }

    public static EntityAudit create() {
        EntityAudit ret = new EntityAudit();
        ret.setModifyDateTime(Calendar.getInstance().getTime());
        return ret;
    }

    @Transient
    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }
}
