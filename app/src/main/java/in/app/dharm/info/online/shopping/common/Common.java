package in.app.dharm.info.online.shopping.common;

import java.util.ArrayList;

import in.app.dharm.info.online.shopping.model.CartProductListPojo;

public class Common {

    public static boolean containsProduct(ArrayList<CartProductListPojo> list, String name) {
        for (CartProductListPojo item : list) {
            if (item.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

}
