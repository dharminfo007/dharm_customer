package in.app.dharm.info.online.shopping.model;

public class GenerateOrderPojo {

    String id, qty, price, in_date, unit;

    public GenerateOrderPojo() {
    }

    public GenerateOrderPojo(String id, String qty, String price, String in_date, String unit) {
        this.id = id;
        this.qty = qty;
        this.price = price;
        this.in_date = in_date;
        this.unit = unit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getIn_date() {
        return in_date;
    }

    public void setIn_date(String in_date) {
        this.in_date = in_date;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
