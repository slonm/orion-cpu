package orion.tapestry.grid.lib.model;

/**
 * String field
 * @author Gennadiy Dobrovolsky
 */
public class FieldString extends FieldAbstract<String> {

    /**
     * Constructor
     * @param newFieldId    field identifier
     * @param newLabel      visible field label
     * @param newIsSortable if the field is sortable
     */
    public FieldString(String newFieldId, String newLabel,boolean newIsSortable) {
        super(newFieldId,newLabel,newIsSortable);
    }

    // TODO разобраться с проверялкой данных
    @Override
    public boolean isValid(String checkme) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // TODO непонятно, где вставить извлекатель данных из строки
    @Override
    public String getValue() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Class<String> getDataType() {
        return String.class;
    }
}
