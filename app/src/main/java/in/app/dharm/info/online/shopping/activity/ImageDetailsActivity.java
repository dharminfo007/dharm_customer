package in.app.dharm.info.online.shopping.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import in.app.dharm.info.online.shopping.R;
import in.app.dharm.info.online.shopping.adapter.AutoScrollPagerAdapter;
import in.app.dharm.info.online.shopping.adapter.ImageDetailAdapter;
import in.app.dharm.info.online.shopping.adapter.SlidingImageAdapter;
import in.app.dharm.info.online.shopping.common.AutoScrollViewPager;
import in.app.dharm.info.online.shopping.model.ProductListPojo;

public class ImageDetailsActivity extends AppCompatActivity {

    private static final int AUTO_SCROLL_THRESHOLD_IN_MILLI = 1000;
    ArrayList<String> imageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_details);

        imageList = (ArrayList<String>) getIntent().getSerializableExtra("imageList");

        /*AutoScrollPagerAdapter autoScrollPagerAdapter =
                new AutoScrollPagerAdapter(getSupportFragmentManager(), imageList);*/
        AutoScrollViewPager viewPager = findViewById(R.id.vpImages);
        viewPager.setAdapter(new ImageDetailAdapter(ImageDetailsActivity.this,
                imageList));
        TabLayout tabs = findViewById(R.id.tabImage);
        tabs.setupWithViewPager(viewPager);


        // start auto scroll
//        viewPager.startAutoScroll();
        // set auto scroll time in mili
//        viewPager.setInterval(AUTO_SCROLL_THRESHOLD_IN_MILLI);
        // enable recycling using true
//        viewPager.setCycle(true);
    }

}