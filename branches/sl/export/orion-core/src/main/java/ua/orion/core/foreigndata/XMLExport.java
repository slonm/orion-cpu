package ua.orion.core.foreigndata;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.hibernate.converter.*;
import com.thoughtworks.xstream.hibernate.mapper.HibernateMapper;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.MapperWrapper;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import ua.orion.core.services.EntityService;

/**
 * Экспорт хранимых данных в XML
 *
 * @author slobodyanuk
 */
public final class XMLExport implements Runnable {

    private class CalendarConverter implements Converter {

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
    private EntityService es;
    private EntityManager em;

    public XMLExport(EntityService es, EntityManager em) {
        this.es = es;
        this.em = em;
        run();
    }

    @Override
    public void run() {
        final XStream xstream = new XStream() {

            protected MapperWrapper wrapMapper(final MapperWrapper next) {
                return new HibernateMapper(next);
            }
        };
        xstream.registerConverter(new HibernateProxyConverter());
        xstream.registerConverter(new HibernatePersistentCollectionConverter(xstream.getMapper()));
        xstream.registerConverter(new HibernatePersistentMapConverter(xstream.getMapper()));
        xstream.registerConverter(new HibernatePersistentSortedMapConverter(xstream.getMapper()));
        xstream.registerConverter(new HibernatePersistentSortedSetConverter(xstream.getMapper()));
        xstream.registerConverter(new SingleValueConverter() {

            @Override
            public String toString(Object obj) {
                return ((Calendar)obj).getTime().toString();
            }

            @Override
            public Object fromString(String str) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean canConvert(Class type) {
                return type.equals(Calendar.class);
            }
        });
        xstream.useAttributeFor(Integer.class);
        xstream.useAttributeFor(Float.class);
        xstream.useAttributeFor(Double.class);
        xstream.useAttributeFor(Long.class);
        xstream.useAttributeFor(Short.class);
        xstream.useAttributeFor(Character.class);
        xstream.useAttributeFor(Byte.class);
        xstream.useAttributeFor(Boolean.class);
        xstream.useAttributeFor(Number.class);
        xstream.useAttributeFor(Date.class);
        xstream.useAttributeFor(Calendar.class);
        xstream.useAttributeFor(java.sql.Timestamp.class);
        //xstream.setMode(XStream.NO_REFERENCES);
        xstream.setMarshallingStrategy(new EntityMarshallingStrategy(es));

        Set<Class<?>> eclasses = es.getManagedEntities();
        List<?> list = new ArrayList();
        for (Class<?> type : eclasses) {
            xstream.alias(type.getSimpleName(), type);
//            for(javax.persistence.metamodel.Attribute a:em.getMetamodel().entity(type).getAttributes()){
//                if(es.getManagedEntities().contains(a.getJavaType())){
//                    xstream.registerLocalConverter(type, a.getName(), cnv);
//                }
//            }
            list.addAll(getEntities(type));
        }
        String xml = xstream.toXML(list);
        try {
            try (BufferedWriter out = new BufferedWriter(new FileWriter("d:\\test.xml"))) {
                out.write(xml);
            }
        } catch (IOException e) {
            System.out.println("Exception ");
        }
    }

    private List getEntities(Class<?> type) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<?> query = cb.createQuery(type);
        query.from(type);
        return em.createQuery(query).getResultList();
    }
}
