package ua.orion.cpu.core.students.entities;

import java.util.Calendar;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.apache.tapestry5.beaneditor.DataType;
import ua.orion.core.persistence.AbstractEntity;

/**
 * Сущность хранит информацию о договоре студента
 *
 * @author molodec
 */
@Entity
@Table(schema = "stu")
public class ContractStudent extends AbstractEntity<ContractStudent> {

    private static final long serialVersionUID = 1L;
    private String numberContract;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Calendar dateContract;
    @ManyToOne
    private KindPayer kindPayer;
    @DataType("longText")
    private String payerRequisition;

    public ContractStudent(String numberContract, Calendar dateContract, KindPayer kindPayer, String payerRequisition) {
        this.numberContract = numberContract;
        this.dateContract = dateContract;
        this.kindPayer = kindPayer;
        this.payerRequisition = payerRequisition;
    }

    public ContractStudent() {
    }

    public String getNumberContract() {
        return numberContract;
    }

    public void setNumberContract(String numberContract) {
        this.numberContract = numberContract;
    }

    public Calendar getDateContract() {
        return dateContract;
    }

    public void setDateContract(Calendar dateContract) {
        this.dateContract = dateContract;
    }

    public KindPayer getKindPayer() {
        return kindPayer;
    }

    public void setKindPayer(KindPayer kindPayer) {
        this.kindPayer = kindPayer;
    }

    public String getPayerRequisition() {
        return payerRequisition;
    }

    public void setPayerRequisition(String payerRequisition) {
        this.payerRequisition = payerRequisition;
    }

    @Override
    protected boolean entityEquals(ContractStudent obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int compareTo(ContractStudent o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        return numberContract;
    }
}
