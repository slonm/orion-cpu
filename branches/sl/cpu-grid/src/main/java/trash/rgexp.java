/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trash;

/**
 *
 * @author dobro
 */
public class rgexp {

    public static void main(String[] a) {
        String value="aaaaaaaaaa.bbbbbbbb";
        System.out.println(value);
        String fld=value.replaceAll("\\.\\w+$", "");
        System.out.println(fld);
    }
}
