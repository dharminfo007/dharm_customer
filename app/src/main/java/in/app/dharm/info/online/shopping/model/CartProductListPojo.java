package in.app.dharm.info.online.shopping.model;

public class CartProductListPojo {

    String name;
    String tvDesc;
    String tvQty;
    String tvStock;
    String tvPrice;
    String in_date;
    String tvOfferDisc;
    String type;
    String id;
    String unit;
    String piecesPerCartoon;

    public CartProductListPojo(String name, String tvDesc, String tvQty, String tvStock,
                           String tvPrice,
                           String in_date, String type, String id, String unit, String piecesPerCartoon) {
        this.name = name;
        this.tvDesc = tvDesc;
        this.tvQty = tvQty;
        this.tvStock = tvStock;
        this.tvPrice = tvPrice;
        this.in_date = in_date;
        this.type = type;
        this.id = id;
        this.unit = unit;
        this.piecesPerCartoon = piecesPerCartoon;
    }

    public String getTvDesc() {
        return tvDesc;
    }

    public void setTvDesc(String tvDesc) {
        this.tvDesc = tvDesc;
    }

    public String getTvQty() {
        return tvQty;
    }

    public void setTvQty(String tvQty) {
        this.tvQty = tvQty;
    }

    public String getTvStock() {
        return tvStock;
    }

    public void setTvStock(String tvStock) {
        this.tvStock = tvStock;
    }

    public String getTvPrice() {
        return tvPrice;
    }

    public void setTvPrice(String tvPrice) {
        this.tvPrice = tvPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIn_date() {
        return in_date;
    }

    public void setIn_date(String in_date) {
        this.in_date = in_date;
    }

    public String getTvOfferDisc() {
        return tvOfferDisc;
    }

    public void setTvOfferDisc(String tvOfferDisc) {
        this.tvOfferDisc = tvOfferDisc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPiecesPerCartoon() {
        return piecesPerCartoon;
    }

    public void setPiecesPerCartoon(String piecesPerCartoon) {
        this.piecesPerCartoon = piecesPerCartoon;
    }
}
