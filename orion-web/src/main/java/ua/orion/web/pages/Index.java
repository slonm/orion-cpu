package ua.orion.web.pages;

import java.util.List;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Tree;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.tree.DefaultTreeModel;
import org.apache.tapestry5.tree.TreeModel;
import ua.orion.core.services.EntityService;
import ua.orion.core.utils.ValueWrapper;
import ua.orion.core.utils.ValueWrapperTreeModelAdapter;
import ua.orion.cpu.core.orgunits.entities.OrgUnit;

/**
 * Стартовая страница информационной системы КПУ. Навигация по другим страницам
 * описана в ./components/Layout.java
 */
public class Index {

    @InjectComponent
    private Tree tree;
    @SuppressWarnings("unused")
    @Persist(PersistenceConstants.FLASH)
    @Property
    private ValueWrapper valueWrapper;
    private final ValueWrapper rootNode = new ValueWrapper("Root", null);
    @Inject
    private EntityService entityService;

    public ValueWrapper createSubTree(List<OrgUnit> orgUnits, OrgUnit o, ValueWrapper UnitNode) {
        for (OrgUnit co : orgUnits) {
            if (co.getParent() != null) {
                if (co.getParent().getId() == o.getId()) {
                    ValueWrapper subUnitNode = new ValueWrapper(co.getName(), "");
                    UnitNode.addChild(subUnitNode);
                    createSubTree(orgUnits, co, subUnitNode);
                }
            }
        }
        return UnitNode;
    }

    @BeginRender
    public Object beginRender() {
        List<OrgUnit> orgUnits = entityService.getEntityManager().createQuery("FROM OrgUnit").getResultList();
        for (OrgUnit o : orgUnits) {
            ValueWrapper orgUnitNode = new ValueWrapper(o.getName(), "");
            if (o.getParent() == null) {
                rootNode.addChild(createSubTree(orgUnits, o, orgUnitNode));
            }
        }
        return null;
    }

    public TreeModel<ValueWrapper> getValueWrapperModel() {
        ValueEncoder<ValueWrapper> encoder = new ValueEncoder<ValueWrapper>() {

            public String toClient(ValueWrapper value) {
                return value.uuid;
            }

            public ValueWrapper toValue(String clientValue) {
                return rootNode.seek(clientValue);
            }
        };

        return new DefaultTreeModel<ValueWrapper>(encoder,
                new ValueWrapperTreeModelAdapter(), rootNode.children);
    }
//    @Inject
//    private PersistentLocale persistentLocale;
//
//    {
//        persistentLocale.set(new Locale("uk"));
//    }
}
