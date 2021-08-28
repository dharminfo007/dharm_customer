package in.app.dharm.info.online.shopping.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
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
import in.app.dharm.info.online.shopping.fragment.OfferFragment;
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
    TextView txtProductCount, tvUserIdentify, tvTotalOrders, tvAddress;
    AutoScrollViewPager viewPager;
    VideoView videoOnLaunch;
    CardView llContactUs;
    BottomNavigationView bottomNavigationView;
    Menu menu;
    DrawerLayout navDrawer;
    NavigationView navigationView;
    Button btn_sign_out;
    int k = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        dataProcessor = new DataProcessor(this);

        txtLogin = findViewById(R.id.txtLogin);
        imgWallet = findViewById(R.id.imgWallet);
        navDrawer = findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.main_sidebar);
        View headerview = navigationView.getHeaderView(0);
        tvUserIdentify = headerview.findViewById(R.id.tvUserIdentify);
        tvAddress = headerview.findViewById(R.id.tvAddress);
        tvTotalOrders = headerview.findViewById(R.id.tvTotalOrders);
        btn_sign_out = navigationView.findViewById(R.id.btn_sign_out);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setItemIconTintList(null);
        menu = bottomNavigationView.getMenu();
//        tvAppName.setText(getString(R.string.app_name));

        txtLogin.setOnClickListener(this);
        imgWallet.setOnClickListener(this);
        checkLoginStatus();
//        initProductDataAvailability();
//        initBannerDataAvailability();
        initBottomNavigation();

        btn_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataProcessor.setBool("isLogin", false);
                dataProcessor.setBool("isSkip", false);
                dataProcessor.setStr("phone", "");
                dataProcessor.setStr("address", "");
                dataProcessor.saveFavoriteArrayList(null, "favorite");
                dataProcessor.saveArrayList(null, "cart");
                startActivity(new Intent(HomePageActivity.this, LoginActivity.class));
                finishAffinity();
            }
        });

    }

    private void initBottomNavigation() {

        HomeFragment homeFragment = new HomeFragment();
        CartFragment cartFragment = new CartFragment();
        FavoriteFragment favoriteFragment = new FavoriteFragment();
        OfferFragment offerFragment = new OfferFragment();
        bottomNavigationView.getMenu().getItem(0).setIcon(R.drawable.home_select);
        setCurrentFragment(homeFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                menu.findItem(R.id.home).setIcon(R.drawable.home_unselect);
                menu.findItem(R.id.cart).setIcon(R.drawable.cart_unselect);
                menu.findItem(R.id.favorite).setIcon(R.drawable.fav_unselect);
                menu.findItem(R.id.coupan).setIcon(R.drawable.coupon_unselect);

                switch (item.getItemId()) {

                    case R.id.home:
                        item.setIcon(R.drawable.home_select);
                        setCurrentFragment(homeFragment);
                        break;

                    case R.id.cart:
                        item.setIcon(R.drawable.cart_select);
                        setCurrentFragment(cartFragment);
                        break;

                    case R.id.favorite:
                        item.setIcon(R.drawable.fav_select);
//                        item.setIcon(R.drawable.fav_select);
                        setCurrentFragment(favoriteFragment);
                        break;

                    case R.id.coupan:
                        item.setIcon(R.drawable.coupon_select);
                        setCurrentFragment(offerFragment);
                        break;
                }
                return false;
            }
        });

    }

    private void setCurrentFragment(Fragment fragment) {

        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, fragment).commit();
        /*supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit();*/
    }

    private void checkLoginStatus() {
        if (dataProcessor.getBool("isLogin") == true) {
            txtLogin.setText("ORDER");
            tvUserIdentify.setText(dataProcessor.getStr("phone"));
            tvAddress.setText(dataProcessor.getStr("address"));
            btn_sign_out.setVisibility(View.VISIBLE);
        } else {
            txtLogin.setText("LOGIN");
            tvUserIdentify.setVisibility(View.INVISIBLE);
            tvTotalOrders.setVisibility(View.INVISIBLE);
//            tvUserIdentify.setText(dataProcessor.getStr("phone"));
            tvAddress.setVisibility(View.INVISIBLE);
            btn_sign_out.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtLogin:
                if (dataProcessor.getBool("isLogin") == true) {
                    startActivity(new Intent(HomePageActivity.this, OrdersListingActivity.class));
                } else {
                    startActivity(new Intent(HomePageActivity.this, LoginActivity.class));
                    finishAffinity();
                }

                break;

            case R.id.imgWallet:

                // If the navigation drawer is not open then open it, if its already open then close it.
                if (!navDrawer.isDrawerOpen(GravityCompat.START)) {
                    navDrawer.openDrawer(GravityCompat.START);
                } else {
                    navDrawer.closeDrawer(GravityCompat.END);
                }
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

    @Override
    public void onBackPressed() {
        if (navDrawer.isDrawerOpen(GravityCompat.START)) {
            navDrawer.closeDrawer(GravityCompat.START);
        } else {
            k++;
            if (k == 1) {
                Toast.makeText(HomePageActivity.this, "Please press again to exit application..", Toast.LENGTH_SHORT).show();
            } else {
                finish();
            }
        }
    }


}
