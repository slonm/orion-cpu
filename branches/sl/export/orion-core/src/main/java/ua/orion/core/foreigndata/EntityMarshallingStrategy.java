/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.core.foreigndata;

import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.core.AbstractTreeMarshallingStrategy;
import com.thoughtworks.xstream.core.TreeMarshaller;
import com.thoughtworks.xstream.core.TreeUnmarshaller;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;
import ua.orion.core.services.EntityService;

/**
 *
 * @author slobodyanuk
 */
public class EntityMarshallingStrategy extends AbstractTreeMarshallingStrategy {

    private final EntityService es;

    public EntityMarshallingStrategy(EntityService es) {
        this.es = es;
    }
    @Override
    protected TreeUnmarshaller createUnmarshallingContext(Object root,
        HierarchicalStreamReader reader, ConverterLookup converterLookup, Mapper mapper) {
        return new TreeUnmarshaller(root, reader, converterLookup, mapper);
    }

    @Override
    protected TreeMarshaller createMarshallingContext(
        HierarchicalStreamWriter writer, ConverterLookup converterLookup, Mapper mapper) {
        return new EntityMarshaller(writer, converterLookup, mapper, es);
    }
}
