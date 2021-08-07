package in.app.dharm.info.online.shopping.model;

import java.util.ArrayList;
import java.util.HashMap;

public class OrdersListPojo {

    String id, name, qty, price, user, in_date, total_price, unit, orderId;
    ArrayList<HashMap<String, String>> hashMaps;

    public OrdersListPojo() {
    }

    public OrdersListPojo(String orderId, String id, String name, String qty, String price,
                          String user, String in_date, String total_price, String unit, ArrayList<HashMap<String, String>> hashMaps) {
        this.orderId = orderId;
        this.id = id;
        this.name = name;
        this.qty = qty;
        this.price = price;
        this.user = user;
        this.in_date = in_date;
        this.total_price = total_price;
        this.unit = unit;
        this.hashMaps = hashMaps;
    }

    public OrdersListPojo(String orderId, String total_price, ArrayList<HashMap<String, String>> hashMaps) {
        this.orderId = orderId;
        this.total_price = total_price;
        this.hashMaps = hashMaps;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getIn_date() {
        return in_date;
    }

    public void setIn_date(String in_date) {
        this.in_date = in_date;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public ArrayList<HashMap<String, String>> getHashMaps() {
        return hashMaps;
    }

    public void setHashMaps(ArrayList<HashMap<String, String>> hashMaps) {
        this.hashMaps = hashMaps;
    }
}
