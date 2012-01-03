package ua.orion.web.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.BeanEditForm;

public class BeanEditFormExt extends BeanEditForm{
    @Property
    @Parameter(defaultPrefix = BindingConstants.PROP)
    private Block buttons;
}
