package deskcomm_restapi.support;

/**
 * Created by Jay Rathod on 05-02-2017.
 */
public class L {
    public static void P(String... s) {
        System.out.println("_________________________________________________");
        for (String s1 :
                s) {
            System.out.print(s1 + " ");
        }
        System.out.println();
        System.out.println("___________________________________________________");

    }
}
