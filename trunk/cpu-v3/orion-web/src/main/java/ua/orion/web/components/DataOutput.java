/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.web.components;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.services.ClassPropertyAdapter;
import org.apache.tapestry5.ioc.services.PropertyAdapter;
import org.apache.tapestry5.services.BeanBlockSource;
import org.apache.tapestry5.services.DataTypeAnalyzer;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.PropertyOutputContext;

/**
 * Отрисовывает параметр object на используя DisplayBeanBlock зарегистрированный для 
 * типа datatype. Если datatype не указан, то он определяется с помощью DefaultDataTypeAnalyzer,
 * т.е. по типу данных object. Дополнительно можно (если требуется) указать подпись в параметре 
 * name.
 * @author slobodyanuk
 */
public class DataOutput {

    @Parameter(required = true, allowNull = false, defaultPrefix = "literal")
    private String datatype;
    @Parameter(required = true)
    private Object object;
    @Parameter(value = "", defaultPrefix = "literal")
    private String name;
    @Inject
    private BeanBlockSource beanBlockSource;
    @InjectService("DefaultDataTypeAnalyzer")
    private DataTypeAnalyzer analyzer;
    @Inject
    private ComponentResources cr;
    @Inject
    private Environment environment;
    private boolean mustPopEnvironment;

    String defaultDatatype() {
        return analyzer.identifyDataType(new PropertyAdapter() {

            @Override
            public Class getType() {
                return cr.getBoundType("object");
            }

            @Override
            public String getName() {
                return "";
            }

            @Override
            public boolean isRead() {
                return true;
            }

            @Override
            public Method getReadMethod() {
                return null;
            }

            @Override
            public boolean isUpdate() {
                return false;
            }

            @Override
            public Method getWriteMethod() {
                return null;
            }

            @Override
            public Object get(Object instance) {
                return null;
            }

            @Override
            public void set(Object instance, Object value) {
            }

            @Override
            public boolean isCastRequired() {
                return true;
            }

            @Override
            public ClassPropertyAdapter getClassAdapter() {
                return null;
            }

            @Override
            public Class getBeanType() {
                return null;
            }

            @Override
            public boolean isField() {
                return false;
            }

            @Override
            public Field getField() {
                return null;
            }

            @Override
            public Class getDeclaringClass() {
                return null;
            }

            @Override
            public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
                return null;
            }
        });
    }

    Object beginRender(MarkupWriter writer) {
        if (beanBlockSource.hasDisplayBlock(datatype)) {
            PropertyOutputContext context = new PropertyOutputContext() {

                public Messages getMessages() {
                    return cr.getContainerMessages();
                }

                public Object getPropertyValue() {
                    return object;
                }

                public String getPropertyId() {
                    return "";
                }

                public String getPropertyName() {
                    return name;
                }
            };

            environment.push(PropertyOutputContext.class, context);
            mustPopEnvironment = true;

            return beanBlockSource.getDisplayBlock(datatype);
        }

        if (object != null) {
            writer.write(object.toString());
        }

        // Don't render anything else

        return false;
    }

    void afterRender() {
        if (mustPopEnvironment) {
            environment.pop(PropertyOutputContext.class);
            mustPopEnvironment = false;
        }
    }
}
