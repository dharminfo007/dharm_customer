package in.app.dharm.info.online.shopping.common;

import java.util.ArrayList;

import in.app.dharm.info.online.shopping.model.CartProductListPojo;
import in.app.dharm.info.online.shopping.model.ProductListPojo;

public class Common {

    public static boolean containsProduct(ArrayList<CartProductListPojo> list, String name) {
        for (CartProductListPojo item : list) {
            if (item.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsFavoriteProduct(ArrayList<ProductListPojo> list, String name) {
        for (ProductListPojo item : list) {
            if (item.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

}
