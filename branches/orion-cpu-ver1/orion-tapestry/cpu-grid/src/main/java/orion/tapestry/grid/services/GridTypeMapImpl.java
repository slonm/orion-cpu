package orion.tapestry.grid.services;

import java.util.Map;

/**
 *
 * @author Gennadiy Dobrovolsky
 */
public class GridTypeMapImpl implements GridTypeMap {

    private Map<String, Class> configuration;

    @Override
    public Class getField(String attribyteType) {
        //System.out.println(attribyteType);
        if (this.configuration.containsKey(attribyteType)) {
            return this.configuration.get(attribyteType);
        } else {
            return null;
        }
    }

    public GridTypeMapImpl(Map<String, Class> _configuration) {
        this.configuration = _configuration;
    }

    @Override
    public Map<String, Class> getConfiguration() {
        return this.configuration;
    }
}
