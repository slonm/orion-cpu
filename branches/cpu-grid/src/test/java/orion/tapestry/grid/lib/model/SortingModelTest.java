package orion.tapestry.grid.lib.model;

import java.util.TreeSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Тестируем orion.tapestry.grid.lib.model.SortingModel
 * @author Gennadiy Dobrovolsky
 */
public class SortingModelTest {

    SortingModel model;

    @Before
    public void setUp() {
        model = new SortingModel();
    }

    @Test
    public void testSortingModel() {

        // add one entry
        model.add(new SortingModelEntry("lastname", SortingModelEntry.ASC, 1));

        // add multiple entries
        model.add(new SortingModelEntry("age", SortingModelEntry.DESC, 2),
                new SortingModelEntry("address", SortingModelEntry.DESC, 3),
                new SortingModelEntry("amount", SortingModelEntry.DESC, 4));

        
        SortingModelEntry o1,o2,o3,o4;
        // extract existing entry
        o1 = model.getSortingModelEntry("lastname");
        Assert.assertEquals("lastname", o1.fieldId);

        // extract non-existing entry
        o1 = model.getSortingModelEntry("someotherfield");
        Assert.assertEquals(o1, null);

        // add and remove
        model.add(new SortingModelEntry("deleteme", SortingModelEntry.ASC, 5));
        o1 = model.getSortingModelEntry("deleteme");
        Assert.assertNotNull(o1);
        model.remove(o1);
        Assert.assertNull(model.getSortingModelEntry("deleteme"));

        model.add(new SortingModelEntry("deleteme", SortingModelEntry.ASC, 5));
        Assert.assertNotNull(model.getSortingModelEntry("deleteme"));
        model.remove("deleteme");
        Assert.assertNull(model.getSortingModelEntry("deleteme"));


        // test ordering
           TreeSet<SortingModelEntry> allEntries=model.getSortingModelEntry();
           o1 = allEntries.first();
           o2 = allEntries.higher(o1);
           o3 = allEntries.higher(o2);
           o4 = allEntries.higher(o3);

           Assert.assertEquals("amount"  , o1.fieldId);
           Assert.assertEquals("address" , o2.fieldId);
           Assert.assertEquals("age"     , o3.fieldId);
           Assert.assertEquals("lastname", o4.fieldId);
    }
}
