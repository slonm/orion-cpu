package ua.orion.cpu.core.orgunits.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import ua.orion.core.persistence.AbstractEntity;

/**
 * Штатные должности подразделения с указанием доли ставки на должность
 * (Штатное расписание)
 * это модель - если несколько ставок доцентов, в модели они будут храниться 
 * как отдельные объекты
 * @author kgp
 */
@Entity
@Table(schema = "org")
public class OrgUnitPost extends AbstractEntity<OrgUnitPost> {

    private static final long serialVersionUID = 1L;
    private OrgUnit orgUnit;
    private Post post;
    private Double postPercentageRate;

    public OrgUnitPost(OrgUnit orgUnit, Post post, Double postPercentageRate) {
        this.orgUnit = orgUnit;
        this.post = post;
        this.postPercentageRate = postPercentageRate;
    }

    public OrgUnitPost() {
    }

    @ManyToOne
    public OrgUnit getOrgUnit() {
        return orgUnit;
    }

    public void setOrgUnit(OrgUnit orgUnit) {
        this.orgUnit = orgUnit;
    }

    @ManyToOne
    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Double getPostPercentageRate() {
        return postPercentageRate;
    }

    public void setPostPercentageRate(Double postPercentageRate) {
        this.postPercentageRate = postPercentageRate;
    }

    @Override
    protected boolean entityEquals(OrgUnitPost obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int compareTo(OrgUnitPost o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
