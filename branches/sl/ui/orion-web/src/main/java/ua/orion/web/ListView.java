package ua.orion.web;

import java.util.List;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Grid;
import ua.orion.web.components.ListViewMode;

/**
 * Компонент аналогичный GUI ListView 
 * @author slobodyanuk
 */
public class ListView {

    @Property
    private ListViewMode mode;
    @Parameter(required = true, autoconnect = true)
    private List source;
    @Parameter(principal = true)
    private Object object;
    @Parameter
    private BeanModel model;
    @Parameter(value = "block:empty",
            defaultPrefix = BindingConstants.LITERAL)
    private Block empty;
    @Component(parameters={
    "source=inherit:source"})
    private Grid grid;
}
