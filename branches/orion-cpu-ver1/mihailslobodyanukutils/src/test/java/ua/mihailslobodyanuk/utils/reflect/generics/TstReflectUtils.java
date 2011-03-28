/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ua.mihailslobodyanuk.utils.reflect.generics;

import java.io.Serializable;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sl
 */
public class TstReflectUtils {
//    static class Printer{
//        public void print(Class cls){
//            TypeVariable[] params=cls.getTypeParameters();
//            System.out.println("Class:"+this.getClass().getName());
//            System.out.println("    Type parameters for: "+cls.getName());
//            for(int i=0;i<params.length;i++){
//                System.out.println("        "+params[i].getName()+": "+ReflectUtils.getClassOfArgument(this.getClass(), cls, i));
//            }
//        }
//    }
//
//    //class B extends A<Long>
//    public void testBextendsA_Long_(){
//        class A<T> extends Printer{}
//        class Z extends A<Long>{}
//        Z z=new Z();
//        System.out.println("testB_T_extendsA_T_");
//        z.print(A.class);
//        System.out.println();
//    }
//
//    //class B<T> extends A<T>
//    public void testB_T_extendsA_T_(){
//        class A<T> extends Printer{}
//        class B<T> extends A<T>{}
//        class Z extends B<Long>{}
//        Z z=new Z();
//        System.out.println("testB_T_extendsA_T_");
//        z.print(A.class);
//        System.out.println();
//    }
//
//    public void testDeepInheritance(){
//        class A<T> extends Printer{}
//        class B<V,T> extends A<T>{}
//        class C<V,T> extends B<T,V>{}
//        class D<V,T> extends C<T,V>{}
//        class Z extends D<Long, String>{}
//        Z z=new Z();
//        System.out.println("testB_T_extendsA_T_");
//        z.print(A.class);
//        z.print(B.class);
//        z.print(C.class);
//        z.print(D.class);
//        System.out.println();
//    }
//
//    //class B extends A
//    public void testZextendsA(){
//        class A<T> extends Printer{}
//        class Z extends A{}
//        Z z=new Z();
//        System.out.println("testZextendsA");
//        z.print(A.class);
//        System.out.println();
//    }
//
//    //class B extends A<Long>
//    public void testZextendsA_Long_(){
//        class A<T> extends Printer{}
//        class Z extends A<Long>{}
//        Z z=new Z();
//        System.out.println("testZextendsA_Long_");
//        z.print(A.class);
//        System.out.println();
//    }
//
//    //class B extends A<List>
//    public void testZextendsA_List_(){
//        class A<T> extends Printer{}
//        class Z extends A<List>{}
//        Z z=new Z();
//        System.out.println("testZextendsA_List_");
//        z.print(A.class);
//        System.out.println();
//    }
//
//    //class B extends A<List<Long>>
//    public void testZextendsA_List_Long__(){
//        class A<T> extends Printer{}
//        class Z extends A<List<Long>>{}
//        Z z=new Z();
//        System.out.println("testZextendsA_List_Long__");
//        z.print(A.class);
//        System.out.println();
//    }
//
//    //class A<T extends List>
//    public void testA_TextendsList_(){
//        class A<T extends List> extends Printer{}
//        class Z extends A{}
//        Z z=new Z();
//        System.out.println("testA_TextendsList_");
//        z.print(A.class);
//        System.out.println();
//    }
//
//    //class A<T extends List<Long>>
//    public void testA_TextendsList_Long__(){
//        class A<T extends List<Long>> extends Printer{}
//        class Z extends A{}
//        Z z=new Z();
//        System.out.println("testA_TextendsList_Long__");
//        z.print(A.class);
//        System.out.println();
//    }
//
//    public void testA_TextendsLongVextendsT_(){
//        class A<T extends Long, V extends T> extends Printer{}
//        class Z extends A{}
//        Z z=new Z();
//        System.out.println("testA_TextendsLongVextendsT_");
//        z.print(A.class);
//        System.out.println();
//    }
//
//    //class A<T extends List>
//    public void testA_TextendsList_2(){
//        class A<T extends List> extends Printer{}
//        class B<T extends List> extends A<T>{}
//        class Z extends B{}
//        Z z=new Z();
//        System.out.println("testA_TextendsList_2");
//        z.print(A.class);
//        System.out.println();
//    }
//
//    //class A<T extends List<Long>>
//    public void testA_TextendsList_Long__2(){
//        class A<T extends List<Long>> extends Printer{}
//        class B<T extends List<Long>> extends A<T>{}
//        class Z extends B{}
//        Z z=new Z();
//        System.out.println("testA_TextendsList_Long__");
//        z.print(A.class);
//        System.out.println();
//    }
//
//    public void testA_TextendsLongVextendsT_2(){
//        class A<T extends Long, V extends T> extends Printer{}
//        class B<T extends Long, V extends T> extends A<T,V>{}
//        class Z extends B{}
//        Z z=new Z();
//        System.out.println("testA_TextendsLongVextendsT_2");
//        z.print(A.class);
//        System.out.println();
//    }
//    public void testFacade(){
//        class GenericEntity<K extends Serializable>  implements Serializable{}
//        class GenericReference extends GenericEntity<Long> {}
//        class Z extends GenericReference {}
//
//        class GenericView<K extends Serializable> {}
//        class GenericReferenceView<R extends GenericReference> extends GenericView<Long> {}
//        class ZView extends GenericReferenceView<Z> {}
//
//        class GenericFacadeImpl<T extends GenericView<K>, R extends GenericEntity<K>,K extends Serializable> extends Printer{}
//        class GenericReferenceFacadeImpl<T extends GenericReferenceView<R>, R extends GenericReference> extends GenericFacadeImpl<T, R, Long>{}
//        class ZFacadeImpl extends GenericReferenceFacadeImpl<ZView, Z>{}
//
//        ZFacadeImpl z=new ZFacadeImpl();
//        System.out.println("testFacade");
//        z.print(GenericFacadeImpl.class);
//        System.out.println();
//
//    }
//
//    public static void main(String[] args) {
//        TstReflectUtils tst=new TstReflectUtils();
//        tst.testBextendsA_Long_();
//        tst.testB_T_extendsA_T_();
//        tst.testDeepInheritance();
//        tst.testZextendsA();
//        tst.testZextendsA_Long_();
//        tst.testZextendsA_List_();
//        tst.testZextendsA_List_Long__();
//        tst.testA_TextendsList_();
//        tst.testA_TextendsList_Long__();
//        tst.testA_TextendsLongVextendsT_();
//        tst.testA_TextendsList_2();
//        tst.testA_TextendsList_Long__2();
//        tst.testA_TextendsLongVextendsT_2();
//        tst.testFacade();
//    }
}
