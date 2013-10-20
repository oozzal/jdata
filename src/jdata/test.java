
package jdata;

/**
 *
 * @author Uzzal Devkota
 */
public class test {

    public static void main(String args[]) {
        String str = "or 1=1#";
        System.out.println(str.contains(" "));
        //first letter capital [A-Z]
        //other letters can be capital or small [a-zA-Z]
        //those other letters can be repeated *
        //System.out.println(str.matches("[A-Z][a-zA-Z]*"));
    }
}
