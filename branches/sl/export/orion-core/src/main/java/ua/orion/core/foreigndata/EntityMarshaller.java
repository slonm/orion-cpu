/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.core.foreigndata;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.core.TreeMarshaller;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.path.Path;
import com.thoughtworks.xstream.io.path.PathTracker;
import com.thoughtworks.xstream.io.path.PathTrackingWriter;
import com.thoughtworks.xstream.mapper.Mapper;
import java.io.Serializable;
import ua.orion.core.services.EntityService;

/**
 *
 * @author slobodyanuk
 */
public class EntityMarshaller extends TreeMarshaller implements MarshallingContext {

    private final EntityService es;
    private PathTracker pathTracker = new PathTracker();
    private Converter entityConverter = new EntityConverter();

    private int depth(Path path) {
        return path.toString().split("/").length-1;
    }

    private class EntityConverter implements Converter {

        @Override
        public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
            writer.addAttribute("id", es.getPrimaryKey((Serializable) source).toString());
        }

        @Override
        public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean canConvert(Class type) {
            return es.getManagedEntities().contains(type);
        }
    }

    public EntityMarshaller(HierarchicalStreamWriter writer, ConverterLookup converterLookup, Mapper mapper,
            EntityService es) {
        super(writer, converterLookup, mapper);
        this.es = es;
        this.writer = new PathTrackingWriter(writer, pathTracker);
    }

    @Override
    public void convert(Object item, Converter converter) {
        if (depth(pathTracker.getPath()) > 2 && entityConverter.canConvert(item.getClass())) {
            entityConverter.marshal(item, writer, this);
        } else {
            converter.marshal(item, writer, this);
        }
    }
    
    
}
