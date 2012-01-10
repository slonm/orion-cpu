package ua.orion.cpu.core.students.entities;

import java.util.Calendar;
import javax.persistence.*;
import org.apache.tapestry5.beaneditor.DataType;
import ua.orion.core.persistence.AbstractEntity;

/**
 * Сущность хранит информацию о договоре студента
 *
 * @author molodec
 */
@Entity
public class ContractStudent extends AbstractEntity<ContractStudent> {

    private static final long serialVersionUID = 1L;
    /**
     * Номер договора.
     */
    private String numberContract;
    /**
     * Дата заключения.
     */
    @Temporal(javax.persistence.TemporalType.DATE)
    private Calendar dateContract;
    /**
     * Вид платильщица. Пример: физическое или юридическое лицо.
     */
    @ManyToOne
    private KindPayer kindPayer;
    /**
     * Реквизиты платильщика.
     */
    @DataType("longText")
    private String payerRequisition;

    /**
     * Создание договора студента на основе:
     *
     * @param numberContract - номер договора
     * @param dateContract - дата заключения договора
     * @param kindPayer - вид платильщика
     * @param payerRequisition - реквизиты платильщика
     */
    public ContractStudent(String numberContract, Calendar dateContract,
            KindPayer kindPayer, String payerRequisition) {
        this.numberContract = numberContract;
        this.dateContract = dateContract;
        this.kindPayer = kindPayer;
        this.payerRequisition = payerRequisition;
    }

    /**
     * Конструктор по умолчанию для создания договора.
     */
    public ContractStudent() {
    }

    /**
     * Получить номер договора
     *
     * @return номер договора
     */
    public String getNumberContract() {
        return numberContract;
    }

    /**
     * Установить номер договора
     *
     * @param numberContract - номер договора
     */
    public void setNumberContract(String numberContract) {
        this.numberContract = numberContract;
    }

    /**
     * Получить дату заключения договора
     *
     * @return дата заключения договора(календарь)
     */
    public Calendar getDateContract() {
        return dateContract;
    }

    /**
     * Установит дату заключения договора
     *
     * @param dateContract - дата заключения договора
     */
    public void setDateContract(Calendar dateContract) {
        this.dateContract = dateContract;
    }

    /**
     * Получить вид платилищика(справочник)
     *
     * @return вид платильщика
     */
    public KindPayer getKindPayer() {
        return kindPayer;
    }

    /**
     * Установить вид платильщика
     *
     * @param kindPayer - вид платильщика
     */
    public void setKindPayer(KindPayer kindPayer) {
        this.kindPayer = kindPayer;
    }

    /**
     * Получить реквизиты платильщика
     *
     * @return - реквизиты платильщика
     */
    public String getPayerRequisition() {
        return payerRequisition;
    }

    /**
     * Установить реквизиты платильщика
     *
     * @param payerRequisition - реквизиты платильщика
     */
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
}
