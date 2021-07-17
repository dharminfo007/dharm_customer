package in.app.dharm.info.online.shopping.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import in.app.dharm.info.online.shopping.R;
import in.app.dharm.info.online.shopping.adapter.DharmDelListingAdapter;
import in.app.dharm.info.online.shopping.adapter.FilterAdapter;
import in.app.dharm.info.online.shopping.adapter.ProductAdapter;
import in.app.dharm.info.online.shopping.model.ProductListPojo;

public class DharmDealListingActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView rvProducts, rvProductsFilter;
    private DharmDelListingAdapter listAdapter;
    private FilterAdapter filterAdapter;
    ArrayList<ProductListPojo> productArrayList;
    ArrayList<String> filterListPojoArrayList;
    //    ImageView imgBack;
    FirebaseFirestore db;
    public String TAG = "DharmDealListingActivity";
    ProgressDialog pd;
    MaterialTextView txtNoDataFound;
    EditText etSearch;
    ArrayList<String> images = new ArrayList<>();
    ImageView imgCart;
    TextView tvAllProductsTitle, tvPageTitle;
    AppBarLayout appBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_listing);

        pd = new ProgressDialog(DharmDealListingActivity.this);
        pd.setMessage("loading...");

        db = FirebaseFirestore.getInstance();
        productArrayList = new ArrayList<>();
        images = new ArrayList<>();
        filterListPojoArrayList = new ArrayList<>();
        appBar = findViewById(R.id.appBar);
        appBar.setVisibility(View.GONE);
        rvProducts = (RecyclerView) findViewById(R.id.rvProducts);
        tvPageTitle =  findViewById(R.id.tvPageTitle);
        tvPageTitle.setText("Deal with products !");
        tvAllProductsTitle = findViewById(R.id.tvAllProductsTitle);
        etSearch = findViewById(R.id.etSearch);
        rvProductsFilter = (RecyclerView) findViewById(R.id.rvProductsFilter);
//        imgBack = findViewById(R.id.imgBack);
        txtNoDataFound = findViewById(R.id.txtNoDataMatch);
        imgCart = findViewById(R.id.imgCart);
        tvAllProductsTitle.setText("ALL PRODUCTS");
        rvProducts.setHasFixedSize(true);
//        GridLayoutManager layoutManager = new GridLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvProducts.setLayoutManager(layoutManager);
        listAdapter = new DharmDelListingAdapter(productArrayList, this);
        rvProducts.setAdapter(listAdapter);

        rvProductsFilter.setHasFixedSize(true);
        LinearLayoutManager layoutManagerFilter = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvProductsFilter.setLayoutManager(layoutManagerFilter);
        filterAdapter = new FilterAdapter(filterListPojoArrayList, this);
        rvProductsFilter.setAdapter(filterAdapter);

        filterAdapter.notifyDataSetChanged();

        onClickListenersInit();
        initProductDataAvailability();
        initFilterDataAvailability();
        findProducts();
        imgCart.setOnClickListener(this);
    }

    private void findProducts() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // filter your list from your input
                filter(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });
    }


    private void initProductDataAvailability() {
        pd.show();
        db.collection("productlist")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            pd.dismiss();
                            productArrayList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                ProductListPojo productListPojo = new ProductListPojo(document.getString("name"),
                                        document.getString("description"), document.getString("pieces per cartoon"),
                                        document.getString("stock"),
                                        document.getString("price"), document.getString("in_date"),
                                        document.getString("type"),document.getString("id"),(ArrayList<String>) document.get("images"));
                                productArrayList.add(productListPojo);
                            }
                            Log.d(TAG, " => " + productArrayList.size());
                            if (productArrayList.size() > 0) {
                                listAdapter = new DharmDelListingAdapter(productArrayList, DharmDealListingActivity.this);
                                rvProducts.setAdapter(listAdapter);
                                listAdapter.notifyDataSetChanged();
                            } else {
                                listAdapter.notifyDataSetChanged();
                            }

                        } else {
                            pd.dismiss();
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void initFilterDataAvailability() {
        db.collection("producttype")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            productArrayList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String productFilterListPojo = document.getId();
                                filterListPojoArrayList.add(productFilterListPojo);
                            }
                            Log.d(TAG, " => " + filterListPojoArrayList.size());
                            if (filterListPojoArrayList.size() > 0) {
                                filterAdapter = new FilterAdapter(filterListPojoArrayList, DharmDealListingActivity.this);
                                rvProductsFilter.setAdapter(filterAdapter);
                                filterAdapter.notifyDataSetChanged();
                            } else {
                                filterAdapter.notifyDataSetChanged();
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void filterProductsListing(String productType){

        if(!productType.equals("")){
            pd.show();
            db.collection("productlist")
                    .whereEqualTo("type", ""+productType)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                pd.dismiss();
                                productArrayList = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    ProductListPojo productListPojo = new ProductListPojo(document.getString("name"),
                                            document.getString("description"), document.getString("pieces per cartoon"),
                                            document.getString("stock"),
                                            document.getString("price"), document.getString("in_date"),
                                            document.getString("type"),document.getString("id"),(ArrayList<String>) document.get("images"));
                                    productArrayList.add(productListPojo);

                                }
                                Log.d(TAG, " => " + productArrayList.size());
                                if (productArrayList.size() > 0) {
                                    txtNoDataFound.setVisibility(View.GONE);
                                    rvProducts.setVisibility(View.VISIBLE);
                                    listAdapter = new DharmDelListingAdapter(productArrayList, DharmDealListingActivity.this);
                                    rvProducts.setAdapter(listAdapter);
                                    listAdapter.notifyDataSetChanged();
                                } else {
                                    txtNoDataFound.setVisibility(View.VISIBLE);
                                    rvProducts.setVisibility(View.GONE);
                                    listAdapter = new DharmDelListingAdapter(productArrayList, DharmDealListingActivity.this);
                                    rvProducts.setAdapter(listAdapter);
                                    listAdapter.notifyDataSetChanged();
                                }
                            } else {
                                pd.dismiss();
                                Log.d(TAG, "Error getting documents: ", task.getException());
                                txtNoDataFound.setVisibility(View.VISIBLE);
                                rvProducts.setVisibility(View.GONE);
                            }
                        }
                    });
        }else {
            initProductDataAvailability();
        }


    }

    private void onClickListenersInit() {
//        imgBack.setOnClickListener(this);
        tvAllProductsTitle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.imgBack:
                onBackPressed();
                break;*/
            case R.id.imgCart:
                startActivity(new Intent(DharmDealListingActivity.this, CartProductsActivity.class));
                break;

            case R.id.tvAllProductsTitle:
                initProductDataAvailability();
                break;
            default:
                break;
        }
    }

    void filter(String text){
        ArrayList<ProductListPojo> temp = new ArrayList();
        for(ProductListPojo d: productArrayList){
            //use .toLowerCase() for better matches
            if(d.getName().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }
        }
        //update recyclerview
        listAdapter.updateList(temp);
    }

}