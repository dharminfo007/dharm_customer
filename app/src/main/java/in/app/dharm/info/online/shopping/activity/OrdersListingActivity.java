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
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import in.app.dharm.info.online.shopping.R;
import in.app.dharm.info.online.shopping.adapter.OrdersAdapter;
import in.app.dharm.info.online.shopping.common.DataProcessor;
import in.app.dharm.info.online.shopping.model.OrdersListPojo;

public class OrdersListingActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView rvOrderList;
    private OrdersAdapter listAdapter;
    ArrayList<OrdersListPojo> orderList;
    ImageView imgBack;
    FirebaseFirestore db;
    public String TAG = "OrderListingActivity";
    ArrayList<HashMap<String, String>> arrayListProduct;
    DataProcessor dataProcessor;
    MaterialTextView txtNoData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_detail);
        init();
    }

    public void init() {
        orderList = new ArrayList<>();
        dataProcessor = new DataProcessor(this);
        rvOrderList = (RecyclerView) findViewById(R.id.rvOrdersLists);
        imgBack = findViewById(R.id.imgBack);
        txtNoData = findViewById(R.id.txtNoData);
        db = FirebaseFirestore.getInstance();
        rvOrderList.setHasFixedSize(true);
        arrayListProduct = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvOrderList.setLayoutManager(layoutManager);
        listAdapter = new OrdersAdapter(orderList, this);
        rvOrderList.setAdapter(listAdapter);
        imgBack.setOnClickListener(this);
        initOrderDataAvailability();
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
    
    private void initOrderDataAvailability() {
        db.collection("orderlist")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            orderList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                OrdersListPojo productListPojo = null;
                                arrayListProduct = (ArrayList<HashMap<String, String>>) document.get("products");

//                                for(int i = 0; i < arrayListProduct.size(); i++){
                                    if(document.getString("user").equals(dataProcessor.getStr("phone"))){
                                        productListPojo = new OrdersListPojo(
                                                document.getId(),
                                                document.getString("order_total"),
                                                arrayListProduct);
                                        orderList.add(productListPojo);
                                    }
//                                }

                            }

                            if (orderList.size() > 0) {
                                txtNoData.setVisibility(View.GONE);
                                rvOrderList.setVisibility(View.VISIBLE);
                                listAdapter = new OrdersAdapter(orderList, OrdersListingActivity.this);
                                rvOrderList.setAdapter(listAdapter);
                                listAdapter.notifyDataSetChanged();
                            } else {
                                txtNoData.setVisibility(View.VISIBLE);
                                rvOrderList.setVisibility(View.GONE);
                                listAdapter.notifyDataSetChanged();
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


}