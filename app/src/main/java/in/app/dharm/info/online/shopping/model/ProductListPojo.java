package in.app.dharm.info.online.shopping.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductListPojo implements Serializable {
    //Add the type indicators here
    public static final int TEXT_TYPE = 0;
    public static final int IMAGE_TYPE = 0;

    String name;
    String tvDesc;
    String tvPiecesPerCartoon;
    String tvStock;
    String tvPrice;
    String in_date;
    String tvOfferDisc;
    String type;
    String id;
    boolean isFav ;
    public ArrayList<String> listProductImages;

    public ProductListPojo() {
    }

    public ProductListPojo(String name, String tvDesc, String tvPiecesPerCartoon, String tvStock,
                           String tvPrice,
                           String in_date, String type, String id, ArrayList<String> listProductImages) {
        this.name = name;
        this.tvDesc = tvDesc;
        this.tvPiecesPerCartoon = tvPiecesPerCartoon;
        this.tvStock = tvStock;
        this.tvPrice = tvPrice;
        this.in_date = in_date;
        this.type = type;
        this.id = id;
        this.listProductImages = listProductImages;
    }

    public String getTvDesc() {
        return tvDesc;
    }

    public void setTvDesc(String tvDesc) {
        this.tvDesc = tvDesc;
    }

    public String getTvPiecesPerCartoon() {
        return tvPiecesPerCartoon;
    }

    public void setTvPiecesPerCartoon(String tvPiecesPerCartoon) {
        this.tvPiecesPerCartoon = tvPiecesPerCartoon;
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

    public ArrayList<String> getListProductImages() {
        return listProductImages;
    }

    public void setListProductImages(ArrayList<String> listProductImages) {
        this.listProductImages = listProductImages;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

}

