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
import in.app.dharm.info.online.shopping.adapter.OrdersAdapter;
import in.app.dharm.info.online.shopping.adapter.ProductAdapter;
import in.app.dharm.info.online.shopping.model.OrdersListPojo;
import in.app.dharm.info.online.shopping.model.ProductListPojo;

public class OrdersListingActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView rvOrderList;
    private OrdersAdapter listAdapter;
    ArrayList<OrdersListPojo> orderList;
    ImageView imgBack;
    FirebaseFirestore db;
    public String TAG = "OrderListingActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_detail);
        init();
    }

    public void init() {
        orderList = new ArrayList<>();
        rvOrderList = (RecyclerView) findViewById(R.id.rvOrdersLists);
        imgBack = findViewById(R.id.imgBack);
        db = FirebaseFirestore.getInstance();
        rvOrderList.setHasFixedSize(true);
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
                                OrdersListPojo productListPojo = new OrdersListPojo(document.getString("id"),
                                        document.getString("name"),
                                        document.getString("qty"),
                                        document.getString("price"),
                                        document.getString("user"), 
                                        document.getString("in_date"),
                                        document.getString("total_price"));
                                orderList.add(productListPojo);
                            }
                            Log.d(TAG, " => " + orderList.size());
                            if (orderList.size() > 0) {
                                listAdapter = new OrdersAdapter(orderList, OrdersListingActivity.this);
                                rvOrderList.setAdapter(listAdapter);
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