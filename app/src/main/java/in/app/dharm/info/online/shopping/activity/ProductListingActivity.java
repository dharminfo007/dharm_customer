package in.app.dharm.info.online.shopping.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import in.app.dharm.info.online.shopping.R;
import in.app.dharm.info.online.shopping.adapter.FilterAdapter;
import in.app.dharm.info.online.shopping.adapter.ProductAdapter;
import in.app.dharm.info.online.shopping.common.Common;
import in.app.dharm.info.online.shopping.common.DataProcessor;
import in.app.dharm.info.online.shopping.model.CartProductListPojo;
import in.app.dharm.info.online.shopping.model.FilterListPojo;
import in.app.dharm.info.online.shopping.model.ProductListPojo;

public class ProductListingActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView rvProducts, rvProductsFilter;
    private ProductAdapter listAdapter;
    private FilterAdapter filterAdapter;
    ArrayList<ProductListPojo> productArrayList;
    ArrayList<ProductListPojo> productFavArrayList;
    ArrayList<String> filterListPojoArrayList;
//    ImageView imgBack;
    FirebaseFirestore db;
    public String TAG = "ProductListingActivity";
    ProgressDialog pd;
    MaterialTextView txtNoDataFound;
    EditText etSearch;
    ArrayList<String> images = new ArrayList<>();
    ImageView imgCart;
    TextView tvAllProductsTitle;
    DataProcessor dataProcessor;
    ProductListPojo productListPojo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_listing);

        pd = new ProgressDialog(ProductListingActivity.this);
        pd.setMessage("loading...");

        dataProcessor = new DataProcessor(this);

        db = FirebaseFirestore.getInstance();
        productArrayList = new ArrayList<>();
        images = new ArrayList<>();
        filterListPojoArrayList = new ArrayList<>();
        productFavArrayList = new ArrayList<>();
        rvProducts = (RecyclerView) findViewById(R.id.rvProducts);
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
        listAdapter = new ProductAdapter(productArrayList, this);
        rvProducts.setAdapter(listAdapter);

        rvProductsFilter.setHasFixedSize(true);
        LinearLayoutManager layoutManagerFilter = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvProductsFilter.setLayoutManager(layoutManagerFilter);
        filterAdapter = new FilterAdapter(filterListPojoArrayList, this);
        rvProductsFilter.setAdapter(filterAdapter);

        filterAdapter.notifyDataSetChanged();

        onClickListenersInit();
        filterAdapter.selectedPos = -1;
        filterAdapter.notifyDataSetChanged();
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
                if(s.length() > 0){
                    filter(s.toString());
                }

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

                                if(dataProcessor.getFavoriteArrayList("favorite") != null){
                                    for(int i = 0; i< dataProcessor.getFavoriteArrayList("favorite").size(); i++){
                                        if(dataProcessor.getFavoriteArrayList("favorite").get(i).getId().equals(document.getString("id"))){
                                            productListPojo.setFav(true);
                                        }
                                    }
                                }


                                productArrayList.add(productListPojo);
                            }
                            Log.d(TAG, " => " + productArrayList.size());
                            if (productArrayList.size() > 0) {
                                txtNoDataFound.setVisibility(View.GONE);
                                rvProducts.setVisibility(View.VISIBLE);
                                listAdapter = new ProductAdapter(productArrayList, ProductListingActivity.this);
                                rvProducts.setAdapter(listAdapter);
                                listAdapter.notifyDataSetChanged();
                            } else {
                                txtNoDataFound.setVisibility(View.VISIBLE);
                                rvProducts.setVisibility(View.GONE);
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
                            filterListPojoArrayList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String productFilterListPojo = document.getId();
                                filterListPojoArrayList.add(productFilterListPojo);
                            }
                            Log.d(TAG, " => " + filterListPojoArrayList.size());
                            if (filterListPojoArrayList.size() > 0) {
                                filterAdapter = new FilterAdapter(filterListPojoArrayList, ProductListingActivity.this);
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
                                    listAdapter = new ProductAdapter(productArrayList, ProductListingActivity.this);
                                    rvProducts.setAdapter(listAdapter);
                                    listAdapter.notifyDataSetChanged();
                                    filterAdapter.notifyDataSetChanged();
                                } else {
                                    txtNoDataFound.setVisibility(View.VISIBLE);
                                    rvProducts.setVisibility(View.GONE);
                                    listAdapter = new ProductAdapter(productArrayList, ProductListingActivity.this);
                                    rvProducts.setAdapter(listAdapter);
                                    listAdapter.notifyDataSetChanged();
                                    filterAdapter.notifyDataSetChanged();
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
                startActivity(new Intent(ProductListingActivity.this, CartProductsActivity.class));
                break;

            case R.id.tvAllProductsTitle:

                filterAdapter.selectedPos = -1;
                filterAdapter.notifyDataSetChanged();
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

    public void addProductToFavorite(int position){
        if (dataProcessor.getFavoriteArrayList("favorite") != null) {
            if (!Common.containsFavoriteProduct(dataProcessor.getFavoriteArrayList("favorite"),
                    productArrayList.get(position).getName())) {
                productFavArrayList = dataProcessor.getFavoriteArrayList("favorite");
                productListPojo = new ProductListPojo(productArrayList.get(position).getName(),
                        productArrayList.get(position).getTvDesc(), productArrayList.get(position).getTvPiecesPerCartoon(),
                        productArrayList.get(position).getTvStock(),
                        productArrayList.get(position).getTvPrice(), productArrayList.get(position).getIn_date(),
                        productArrayList.get(position).getType(), productArrayList.get(position).getId(),
                        productArrayList.get(position).getListProductImages());
                productListPojo.setFav(true);
                productArrayList.get(position).setFav(true);
                productFavArrayList.add(productListPojo);
                dataProcessor.saveFavoriteArrayList(productFavArrayList, "favorite");
                listAdapter.notifyDataSetChanged();
                Toast.makeText(ProductListingActivity.this, "Your product added to favorite", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Product already added to favorite", Toast.LENGTH_SHORT).show();
            }
        } else {
            productFavArrayList = dataProcessor.getFavoriteArrayList("favorite");
            if(productFavArrayList==null){
                productFavArrayList = new ArrayList<>();
            }
            Log.e(TAG, "addProductToFavorite: "+ position);
            if(productArrayList.size() > 0){
                productListPojo = new ProductListPojo(productArrayList.get(position).getName(),
                        productArrayList.get(position).getTvDesc(),
                        productArrayList.get(position).getTvPiecesPerCartoon(),
                        productArrayList.get(position).getTvStock(),
                        productArrayList.get(position).getTvPrice(),
                        productArrayList.get(position).getIn_date(),
                        productArrayList.get(position).getType(),
                        productArrayList.get(position).getId(),
                        productArrayList.get(position).getListProductImages());
                productListPojo.setFav(true);
                productArrayList.get(position).setFav(true);
                productFavArrayList.add(productListPojo);
                dataProcessor.saveFavoriteArrayList(productFavArrayList, "favorite");
                listAdapter.notifyDataSetChanged();
                Toast.makeText(ProductListingActivity.this, "Your product added to favorite", Toast.LENGTH_LONG).show();

            }else {
                Toast.makeText(ProductListingActivity.this, "No product found", Toast.LENGTH_LONG).show();

            }

        }
    }


}