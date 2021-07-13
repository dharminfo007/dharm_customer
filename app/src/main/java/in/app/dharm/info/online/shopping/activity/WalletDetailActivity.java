package in.app.dharm.info.online.shopping.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import in.app.dharm.info.online.shopping.R;
import in.app.dharm.info.online.shopping.adapter.OfferListAdapter;
import in.app.dharm.info.online.shopping.adapter.ProductAdapter;
import in.app.dharm.info.online.shopping.model.CouponListPojo;
import in.app.dharm.info.online.shopping.model.ProductListPojo;

public class WalletDetailActivity extends AppCompatActivity implements View.OnClickListener{

    RecyclerView rvOfferLists;
    private OfferListAdapter listAdapter;
    ArrayList<CouponListPojo> offerList;
    ImageView imgBack;
    FirebaseFirestore db;
    public String TAG = "WalletDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_detail);

        init();
    }

    public void init(){
        offerList = new ArrayList<>();
        rvOfferLists  = (RecyclerView)findViewById(R.id.rvOfferLists);
        imgBack = findViewById(R.id.imgBack);
        db = FirebaseFirestore.getInstance();
        rvOfferLists.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvOfferLists.setLayoutManager(layoutManager);
        listAdapter = new OfferListAdapter(offerList, this);
        rvOfferLists.setAdapter(listAdapter);
        imgBack.setOnClickListener(this);

        initCouponDataAvailability();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                onBackPressed();
                break;

            default:
                break;
        }
    }
    private void initCouponDataAvailability() {
        db.collection("couponlist")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            offerList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                CouponListPojo productListPojo = new CouponListPojo(
                                        document.getString("coupon_title"),
                                        document.getString("coupon_desc"));
                                offerList.add(productListPojo);
                            }
                            Log.d(TAG, " => " + offerList.size());
                            if (offerList.size() > 0) {
                                listAdapter = new OfferListAdapter(offerList, WalletDetailActivity.this);
                                rvOfferLists.setAdapter(listAdapter);
                                listAdapter.notifyDataSetChanged();
                            } else {
                                listAdapter.notifyDataSetChanged();
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


}