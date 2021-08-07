package in.app.dharm.info.online.shopping.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import in.app.dharm.info.online.shopping.R;
import in.app.dharm.info.online.shopping.adapter.SlidingImageAdapter;
import in.app.dharm.info.online.shopping.common.AutoScrollViewPager;
import in.app.dharm.info.online.shopping.common.DataProcessor;
import in.app.dharm.info.online.shopping.model.BannerListPojo;
import in.app.dharm.info.online.shopping.model.ProductListPojo;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    VideoView videoOnLaunch;
    View mView;
    RelativeLayout llProducts;
    TextView txtProductCount;
    CardView llContactUs;
    AutoScrollViewPager viewPager;
    ArrayList<BannerListPojo> bannerArrayList = new ArrayList<>();
    private static final int AUTO_SCROLL_THRESHOLD_IN_MILLI = 5000;
    DataProcessor dataProcessor;
    FirebaseFirestore db;
    public String TAG = "HomeFragment";
    List<ProductListPojo> productArrayList = new ArrayList<>();
    ArrayList<String> images = new ArrayList<>();
    ExtendedFloatingActionButton floatingButtonToDeal;
    NestedScrollView nestedScrollView;


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        bannerArrayList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        llProducts = mView.findViewById(R.id.llProducts);
        txtProductCount = mView.findViewById(R.id.txtProductCount);
        videoOnLaunch = mView.findViewById(R.id.videoOnLaunch);
        llContactUs = mView.findViewById(R.id.llContactUs);
        viewPager = mView.findViewById(R.id.view_pager);
        floatingButtonToDeal = mView.findViewById(R.id.floatingButtonToDeal);
        nestedScrollView = mView.findViewById(R.id.nestedScrollView);
//        floatingButtonToDeal.shrink();
        viewPager.setAdapter(new SlidingImageAdapter(getActivity(),
                bannerArrayList));

//        viewPager.setAdapter(autoScrollPagerAdapter);
        TabLayout tabs = mView.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        // start auto scroll
        viewPager.startAutoScroll();
        // set auto scroll time in mili
        viewPager.setInterval(AUTO_SCROLL_THRESHOLD_IN_MILLI);
        // enable recycling using true
        viewPager.setCycle(true);

        llProducts.setOnClickListener(this);
//        txtLogin.setOnClickListener(this);
//        imgWallet.setOnClickListener(this);
        llContactUs.setOnClickListener(this);
        floatingButtonToDeal.setOnClickListener(this);
        initProductDataAvailability();
        initBannerDataAvailability();
        nestedWithFloatingButton();
        return mView;
    }

    private void nestedWithFloatingButton() {
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // the delay of the extension of the FAB is set for 12 items
                if (scrollY > oldScrollY + 12 && floatingButtonToDeal.isExtended()) {
                    floatingButtonToDeal.shrink();
                }

                // the delay of the extension of the FAB is set for 12 items
                if (scrollY < oldScrollY - 12 && !floatingButtonToDeal.isExtended()) {
                    floatingButtonToDeal.extend();
                }

                // if the nestedScrollView is at the first item of the list then the
                // extended floating action should be in extended state
                if (scrollY == 0) {
                    floatingButtonToDeal.extend();
                }
            }
        });
    }

    @Override
    public void onResume() {
        createVideoThumbnail();
        super.onResume();
    }

    private void createVideoThumbnail() {
        Uri videoURI = Uri.parse("android.resource://" + getActivity().getPackageName() + "/raw/grab_the_best_offer_now");
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(getActivity().getApplicationContext(), videoURI);
        Bitmap thumb = retriever.getFrameAtTime(10, MediaMetadataRetriever.OPTION_PREVIOUS_SYNC);
//        videoOnLaunch.setBackground(new BitmapDrawable(getResources(), thumb));
        Uri uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.grab_the_best_offer_now);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
//videoview.setLayoutParams(new FrameLayout.LayoutParams(550,550));
        videoOnLaunch.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        videoOnLaunch.setVideoURI(uri);
        videoOnLaunch.start();

        videoOnLaunch.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.llProducts:
                Intent intentProducts = new Intent(getActivity(), ProductListingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("productList", (Serializable)productArrayList);
                intentProducts.putExtra("BUNDLE", bundle);
                startActivity(intentProducts);
                break;

            case R.id.llContactUs:
                startActivity(new Intent(getActivity(), ContactUsActivity.class));
                break;

            case R.id.floatingButtonToDeal:
                goToDealPageListing();
                break;

            default:
                break;
        }
    }

    private void goToDealPageListing() {
        startActivity(new Intent(getActivity(), DharmDealListingActivity.class));
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
                                viewPager.setAdapter(new SlidingImageAdapter(getActivity(), bannerArrayList));
                            }
                            Log.d(TAG, " => " + bannerArrayList.size());

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}