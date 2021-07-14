package in.app.dharm.info.online.shopping.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import in.app.dharm.info.online.shopping.R;
import in.app.dharm.info.online.shopping.adapter.SlidingImageAdapter;
import in.app.dharm.info.online.shopping.common.AutoScrollViewPager;
import in.app.dharm.info.online.shopping.common.DataProcessor;
import in.app.dharm.info.online.shopping.fragment.CartFragment;
import in.app.dharm.info.online.shopping.fragment.FavoriteFragment;
import in.app.dharm.info.online.shopping.model.BannerListPojo;
import in.app.dharm.info.online.shopping.model.ProductListPojo;

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int AUTO_SCROLL_THRESHOLD_IN_MILLI = 5000;
    LinearLayout llProducts;
    MaterialTextView txtLogin;
    ImageView imgWallet;
    DataProcessor dataProcessor;
    FirebaseFirestore db;
    public String TAG = "HomePageActivity";
    List<ProductListPojo> productArrayList = new ArrayList<>();
    ArrayList<String> images = new ArrayList<>();
    ArrayList<BannerListPojo> bannerArrayList = new ArrayList<>();
    TextView txtProductCount;
    AutoScrollViewPager viewPager;
    VideoView videoOnLaunch;
    CardView llContactUs;
    BottomNavigationView bottomNavigationView;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        dataProcessor = new DataProcessor(this);

        txtLogin = findViewById(R.id.txtLogin);
        imgWallet = findViewById(R.id.imgWallet);
        /*llProducts = findViewById(R.id.llProducts);
        txtProductCount = findViewById(R.id.txtProductCount);
        videoOnLaunch = findViewById(R.id.videoOnLaunch);
        llContactUs = findViewById(R.id.llContactUs);*/
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setItemIconTintList(null);
        menu = bottomNavigationView.getMenu();
        /*AutoScrollPagerAdapter autoScrollPagerAdapter =
                new AutoScrollPagerAdapter(getSupportFragmentManager());*/
        /*viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new SlidingImageAdapter(HomePageActivity.this,
                bannerArrayList));

//        viewPager.setAdapter(autoScrollPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        // start auto scroll
        viewPager.startAutoScroll();
        // set auto scroll time in mili
        viewPager.setInterval(AUTO_SCROLL_THRESHOLD_IN_MILLI);
        // enable recycling using true
        viewPager.setCycle(true);

        llProducts.setOnClickListener(this);
        txtLogin.setOnClickListener(this);
        imgWallet.setOnClickListener(this);
        llContactUs.setOnClickListener(this);*/
        checkLoginStatus();
//        initProductDataAvailability();
//        initBannerDataAvailability();
        initBottomNavigation();

    }

    private void initBottomNavigation() {

        HomeFragment homeFragment = new HomeFragment();
        CartFragment cartFragment = new CartFragment();
        FavoriteFragment favoriteFragment = new FavoriteFragment();

        setCurrentFragment(homeFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                menu.findItem(R.id.home).setIcon(R.drawable.home_unselect);
                menu.findItem(R.id.cart).setIcon(R.drawable.cart_unselect);
                menu.findItem(R.id.favorite).setIcon(R.drawable.fav_unselect);
                menu.findItem(R.id.coupan).setIcon(R.drawable.coupon_unselect);

                switch (item.getItemId()){

                    case R.id.home :
                        item.setIcon(R.drawable.home_select);
                        setCurrentFragment(homeFragment);
                        break;

                    case R.id.cart :
                        item.setIcon(R.drawable.cart_select);
                        setCurrentFragment(cartFragment);
                        break;

                    case R.id.favorite :
                        item.setIcon(R.drawable.fav_select);
//                        item.setIcon(R.drawable.fav_select);
                        setCurrentFragment(favoriteFragment);
                        break;

                    case R.id.coupan :
                        item.setIcon(R.drawable.coupon_select);
                        setCurrentFragment(cartFragment);
                        break;
                }
                return false;
            }
        });

    }
    private void setCurrentFragment(Fragment fragment){

        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, fragment).commit();
        /*supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit();*/
    }


    private void initProductDataAvailability() {
        db.collection("productlist")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            productArrayList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
//                                ProductListPojo productListModel = document.toObject(ProductListPojo.class);
//                                for(String image : productListModel.getListProductImages()){
//                                    images.add(image);
//                                }
                                ProductListPojo productListPojo = new ProductListPojo(document.getString("name"),
                                        document.getString("description"), document.getString("pieces per cartoon"),
                                        String.valueOf(document.getString("stock")),
                                        document.getString("price"), document.getString("in_date"),
                                        document.getString("type"), document.getString("id"), (ArrayList<String>) document.get("images"));
//                                , images);
                                productArrayList.add(productListPojo);
                            }
                            Log.d(TAG, " => " + productArrayList.size());
                            if (productArrayList.size() > 0) {
                                txtProductCount.setText(productArrayList.size() + "");
                            } else {
                                txtProductCount.setText("Not available");
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void initBannerDataAvailability() {
        db.collection("bannerlist")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            bannerArrayList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                BannerListPojo bannerListPojo = new BannerListPojo(document.getString("image"));
                                bannerArrayList.add(bannerListPojo);
                                viewPager.setAdapter(new SlidingImageAdapter(HomePageActivity.this, bannerArrayList));
                            }
                            Log.d(TAG, " => " + bannerArrayList.size());

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void checkLoginStatus() {
        if (dataProcessor.getBool("isLogin") == true) {
            txtLogin.setText("ORDER");
        } else {
            txtLogin.setText("LOGIN");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

           /* case R.id.llProducts:
                startActivity(new Intent(HomePageActivity.this, ProductListingActivity.class));
                break;

            case R.id.llContactUs:
                startActivity(new Intent(HomePageActivity.this, ContactUsActivity.class));
                break;
*/
            case R.id.txtLogin:
                if (dataProcessor.getBool("isLogin") == true) {
                    startActivity(new Intent(HomePageActivity.this, OrdersListingActivity.class));
                } else {
                    startActivity(new Intent(HomePageActivity.this, LoginActivity.class));
                    finishAffinity();
                }

                break;

            case R.id.imgWallet:
                startActivity(new Intent(HomePageActivity.this, WalletDetailActivity.class));
                break;

            default:
                break;
        }
    }

    @Override
    protected void onResume() {
//        createVideoThumbnail();
        super.onResume();
    }

}