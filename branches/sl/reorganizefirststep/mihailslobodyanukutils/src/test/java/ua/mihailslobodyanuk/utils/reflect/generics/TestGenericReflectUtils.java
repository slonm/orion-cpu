package ua.mihailslobodyanuk.utils.reflect.generics;


import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.Assert;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author user
 */
public class TestGenericReflectUtils {

    @Test
    public void test1() {
        class A<T> {
        }
        int[] a=new int[1];
        Assert.assertEquals(GenericReflectUtils.actualParameterClass(A.class, A.class, 0), Object.class);
        Assert.assertEquals(GenericReflectUtils.actualParameterClass(A.class, A.class, 0, String.class), String.class);
        Assert.assertEquals(GenericReflectUtils.actualParameterClass(A.class, A.class, 0, List.class), List.class);
        Assert.assertEquals(GenericReflectUtils.actualParameterClass(A.class, A.class, 0, a.getClass()), a.getClass());
        Assert.assertEquals(GenericReflectUtils.actualParameterClassWithSubDefaults(A.class, A.class, 0, new Class[]{String.class}), String.class);
        Assert.assertEquals(GenericReflectUtils.actualParameterClassWithSubDefaults(A.class, A.class, 0, new Class[]{List.class}), List.class);
        Assert.assertEquals(GenericReflectUtils.actualParameterClassWithSubDefaults(A.class, A.class, 0, new Class[]{a.getClass()}), a.getClass());

    }

    @Test
    public void test2() {

        class A<T extends String> {
       }
        Assert.assertEquals(GenericReflectUtils.actualParameterClass(A.class, A.class, 0), String.class);
        Assert.assertEquals(GenericReflectUtils.actualParameterClassWithSubDefaults(A.class, A.class, 0, new Class[]{String.class}), String.class);

        class B<T extends List> {
        }
        Assert.assertEquals(GenericReflectUtils.actualParameterClass(B.class, B.class, 0), List.class);
        Assert.assertEquals(GenericReflectUtils.actualParameterClassWithSubDefaults(B.class, B.class, 0, new Class[]{List.class}), List.class);

        class C<T extends List<C>> {
        }
        Assert.assertEquals(GenericReflectUtils.actualParameterClass(C.class, C.class, 0), List.class);
        Assert.assertEquals(GenericReflectUtils.actualParameterClassWithSubDefaults(C.class, C.class, 0, new Class[]{List.class}), List.class);

        class D<T extends D> {
        }
        Assert.assertEquals(GenericReflectUtils.actualParameterClass(D.class, D.class, 0), D.class);
        Assert.assertEquals(GenericReflectUtils.actualParameterClassWithSubDefaults(D.class, D.class, 0, new Class[]{D.class}), D.class);
    }

    @Test
    public void test3() {
        class A<T extends String, V extends T> {
        }
        Assert.assertEquals(GenericReflectUtils.actualParameterClass(A.class, A.class, 1), String.class);

        class B<T extends List, V extends T> {
        }
        int[] a=new int[0];
        Assert.assertEquals(GenericReflectUtils.actualParameterClass(B.class, B.class, 1), List.class);
        Assert.assertEquals(GenericReflectUtils.actualParameterClass(B.class, B.class, 1, ArrayList.class), ArrayList.class);
        Assert.assertEquals(GenericReflectUtils.actualParameterClassWithSubDefaults(B.class, B.class, 1, new Class[]{List.class, ArrayList.class}), ArrayList.class);
        class C<T, V> {
        }
        Assert.assertEquals(GenericReflectUtils.actualParameterClassWithSubDefaults(C.class, C.class, 1, new Class[]{a.getClass(), a.getClass()}), a.getClass());
    }

    @Test
    public void test4() {
        class A<T extends List> {
        }
        class B<V extends ArrayList> extends A{}
        Assert.assertEquals(GenericReflectUtils.actualParameterClass(B.class, A.class, 0), List.class);

        class A1<T extends List> {
        }
        class B1<V extends ArrayList> extends A1<V>{}
        Assert.assertEquals(GenericReflectUtils.actualParameterClass(B1.class, A1.class, 0), ArrayList.class);

        class A2<T extends List> {
        }
        class B2 extends A2<ArrayList<Long>>{}
        Assert.assertEquals(GenericReflectUtils.actualParameterClass(B2.class, A2.class, 0), ArrayList.class);

        class A3<T> {
        }
        class B3 extends A3<int[]>{}
        int[]a=new int[0];
        Assert.assertEquals(GenericReflectUtils.actualParameterClass(B3.class, A3.class, 0), GenericArrayType.class);
        Assert.assertEquals(GenericReflectUtils.actualParameterClass(B3.class, A3.class, 0, a.getClass()), a.getClass());
    }

    @Test
    public void test5() {
        class A<T extends List> {
        }
        class B<V extends ArrayList> extends A{}
        Assert.assertEquals(GenericReflectUtils.actualParameterClass(B.class, A.class, 0), List.class);

        class A1<T extends List> {
        }
        class B1<V extends ArrayList> extends A1<V>{}
        Assert.assertEquals(GenericReflectUtils.actualParameterClass(B1.class, A1.class, 0), ArrayList.class);

        class A2<T extends List> {
        }
        class B2 extends A2<ArrayList<Long>>{}
        Assert.assertEquals(GenericReflectUtils.actualParameterClass(B2.class, A2.class, 0), ArrayList.class);

        class A3<T> {
        }
        class B3 extends A3<int[]>{}
        int[]a=new int[0];
        Assert.assertEquals(GenericReflectUtils.actualParameterClass(B3.class, A3.class, 0), GenericArrayType.class);
        Assert.assertEquals(GenericReflectUtils.actualParameterClass(B3.class, A3.class, 0, a.getClass()), a.getClass());
    }

    public static void main(String[] args) {
        TestGenericReflectUtils o = new TestGenericReflectUtils();
        o.test4();
    }
}
