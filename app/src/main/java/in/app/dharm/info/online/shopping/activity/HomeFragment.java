package in.app.dharm.info.online.shopping.activity;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import in.app.dharm.info.online.shopping.R;
import in.app.dharm.info.online.shopping.adapter.SlidingImageAdapter;
import in.app.dharm.info.online.shopping.common.AutoScrollViewPager;
import in.app.dharm.info.online.shopping.model.BannerListPojo;
import in.app.dharm.info.online.shopping.model.ProductListPojo;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
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
    FirebaseFirestore db;
    public String TAG = "HomeFragment";
    List<ProductListPojo> productArrayList = new ArrayList<>();
    ExtendedFloatingActionButton floatingButtonToDeal;
    NestedScrollView nestedScrollView;
    LinearLayout llServiceProvider;
    private StorageReference storageReference;
    private DatabaseReference mDatabase;
    Uri videoURI;
    ImageView imgSale;

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
        llServiceProvider = mView.findViewById(R.id.llServiceProvider);
        txtProductCount = mView.findViewById(R.id.txtProductCount);
        videoOnLaunch = mView.findViewById(R.id.videoOnLaunch);
        llContactUs = mView.findViewById(R.id.llContactUs);
        viewPager = mView.findViewById(R.id.view_pager);
        floatingButtonToDeal = mView.findViewById(R.id.floatingButtonToDeal);
        nestedScrollView = mView.findViewById(R.id.nestedScrollView);
        imgSale = mView.findViewById(R.id.imgSale);

        viewPager.setAdapter(new SlidingImageAdapter(getActivity(),
                bannerArrayList));

        storageReference = FirebaseStorage.getInstance().getReference();

//        mDatabase = FirebaseDatabase.getInstance().getReference("gs://dharm-fe376.appspot.com/Files/offer_video.mp4");

        TabLayout tabs = mView.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        viewPager.startAutoScroll();
        viewPager.setInterval(AUTO_SCROLL_THRESHOLD_IN_MILLI);
        viewPager.setCycle(true);

        llProducts.setOnClickListener(this);
        llContactUs.setOnClickListener(this);
        llServiceProvider.setOnClickListener(this);
        floatingButtonToDeal.setOnClickListener(this);
        initProductDataAvailability();
        initBannerDataAvailability();
        nestedWithFloatingButton();
        getSaleProductImage();
        return mView;
    }

    private void getSaleProductImage() {
        db.collection("onsaleproduct")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getString("sale_img"));
                                Glide
                                        .with(getActivity())
                                        .load(document.getString("sale_img"))
                                        .placeholder(R.drawable.image_sale_p)
                                        .dontAnimate()
                                        .into(imgSale);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
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

        storageReference.child("Files/offer_video.mp4")
                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d(TAG, uri + "");
                videoURI = uri;
                Display display = getActivity().getWindowManager().getDefaultDisplay();
                int width = display.getWidth();
                int height = display.getHeight();
                videoOnLaunch.setLayoutParams(new LinearLayout.LayoutParams(width, height));
                videoOnLaunch.setVideoURI(videoURI);
                videoOnLaunch.start();

                videoOnLaunch.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.setLooping(true);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.d(TAG, exception.getMessage() + "");
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.llProducts:
                Intent intentProducts = new Intent(getActivity(), ProductListingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("productList", (Serializable) productArrayList);
                intentProducts.putExtra("BUNDLE", bundle);
                startActivity(intentProducts);
                break;

            case R.id.llContactUs:
                startActivity(new Intent(getActivity(), ContactUsActivity.class));
                break;

            case R.id.floatingButtonToDeal:
                goToDealPageListing();
                break;

            case R.id.llServiceProvider:
                openServiceProvider();
                break;

            default:
                break;
        }
    }

    private void openServiceProvider() {
        startActivity(new Intent(getActivity(), ServiceProviderActivity.class));
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
                                ProductListPojo productListPojo = new ProductListPojo(document.getString("name"),
                                        document.getString("description"), document.getString("pieces per cartoon"),
                                        String.valueOf(document.getString("stock")),
                                        document.getString("price"), document.getString("in_date"),
                                        document.getString("type"), document.getString("id"), (ArrayList<String>) document.get("images"));
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