package in.app.dharm.info.online.shopping.model;

public class CouponListPojo {

    String coupon_title;
    String coupon_desc;

    public CouponListPojo(String coupon_title, String coupon_desc) {
        this.coupon_title = coupon_title;
        this.coupon_desc = coupon_desc;
    }

    public String getCoupon_title() {
        return coupon_title;
    }

    public void setCoupon_title(String coupon_title) {
        this.coupon_title = coupon_title;
    }

    public String getCoupon_desc() {
        return coupon_desc;
    }

    public void setCoupon_desc(String coupon_desc) {
        this.coupon_desc = coupon_desc;
    }
}
