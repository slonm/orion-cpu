package orion.cpu.controllers.event;

import br.com.arsmachina.dao.SortCriterion;

/**
 * Событие после поиска всех записей с указанием требуемого части набора
 * @param <T> тип возвращаемого значения метода
 * @author sl
 */
public class AfterPartialFindAllEv<T> extends AfterFindAllEv<T>{

    private final int firstResult;
    private final int maxResults;
    private final SortCriterion[] sortCriteria;

    public AfterPartialFindAllEv(T returnValue, int firstResult, int maxResults, SortCriterion[] sortCriteria) {
        super(returnValue);
        this.firstResult = firstResult;
        this.maxResults = maxResults;
        this.sortCriteria = sortCriteria;
    }

    public int getFirstResult() {
        return firstResult;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public SortCriterion[] getSortCriteria() {
        return sortCriteria;
    }
}
