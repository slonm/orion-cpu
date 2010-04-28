/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.tapestry.grid.lib.model;

import java.util.TreeSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Testing orion.tapestry.grid.lib.model.SortingModelEntry
 * @author Gennadiy Dobrovolsky
 */
public class OrderingTest {

    TreeSet<SortingModelEntry> config;

    @Before
    public void setUp() {
        config = new TreeSet<SortingModelEntry>();
        config.add(new SortingModelEntry("lastname", SortingModelEntry.ASC, 1));
        config.add(new SortingModelEntry("age", SortingModelEntry.DESC, 2));
    }

    @Test
    public void testCompareTo() {
        SortingModelEntry o1=config.first();
        SortingModelEntry o2=config.higher(o1);
        Assert.assertEquals("age", o1.fieldId);
        Assert.assertEquals("lastname", o2.fieldId);
    }
}
