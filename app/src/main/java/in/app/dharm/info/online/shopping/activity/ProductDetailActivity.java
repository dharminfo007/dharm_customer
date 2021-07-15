package in.app.dharm.info.online.shopping.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.app.dharm.info.online.shopping.R;
import in.app.dharm.info.online.shopping.adapter.ProductAdapter;
import in.app.dharm.info.online.shopping.adapter.ProductImageAdapter;
import in.app.dharm.info.online.shopping.adapter.SlidingImageAdapter;
import in.app.dharm.info.online.shopping.common.AutoScrollViewPager;
import in.app.dharm.info.online.shopping.common.Common;
import in.app.dharm.info.online.shopping.common.DataProcessor;
import in.app.dharm.info.online.shopping.model.BannerListPojo;
import in.app.dharm.info.online.shopping.model.CartProductListPojo;
import in.app.dharm.info.online.shopping.model.ImageListPojo;
import in.app.dharm.info.online.shopping.model.OrdersListPojo;
import in.app.dharm.info.online.shopping.model.ProductListPojo;

public class ProductDetailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    RecyclerView rvProducts;
    private ProductImageAdapter listAdapter;
    ArrayList<ImageListPojo> productImageList;
    ArrayList<OrdersListPojo> productOrderList;
    private ArrayList<BannerListPojo> productImages;
    Spinner spinnerCartoon, spinnerUnit;
    ImageView imgCart, imgBack;
    TextView txtAddToCart, txtBuyNow;
    FirebaseFirestore db;
    public String TAG = "ProductDetailActivity";
    String id = "";
    TextView txtProdName, txtProdDesc, tvStockStatus;
    ArrayList<String> groupImages;
    DataProcessor dataProcessor;
    ArrayList<CartProductListPojo> cartProductList;
    CartProductListPojo cartProductListPojo;
    String name, desc, selUnit, price, in_date, type, qty = "1", stock, piecesPerCartoon;
    ProgressDialog pd;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    long orderSize = 0;
    AutoScrollViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        init();
    }

    public void init() {
        id = getIntent().getStringExtra("id");
        db = FirebaseFirestore.getInstance();
        dataProcessor = new DataProcessor(this);

        pd = new ProgressDialog(ProductDetailActivity.this);
        pd.setMessage("loading...");

        cartProductList = new ArrayList<>();
        groupImages = new ArrayList<>();
        productImageList = new ArrayList<>();
        productOrderList = new ArrayList<>();
        productImages = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("orderlist");
//        cartProductList = dataProcessor.getArrayList("cart");
        rvProducts = findViewById(R.id.rvProductsImage);
        txtAddToCart = findViewById(R.id.txtAddToCart);
        viewPager = findViewById(R.id.vpProductImages);
        imgCart = findViewById(R.id.imgCart);
        imgBack = findViewById(R.id.imgBack);
        txtProdName = findViewById(R.id.txtProdName);
        txtProdDesc = findViewById(R.id.txtProdDesc);
        tvStockStatus = findViewById(R.id.tvStockStatus);
        txtBuyNow = findViewById(R.id.txtBuyNow);

        rvProducts.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvProducts.setLayoutManager(layoutManager);
        listAdapter = new ProductImageAdapter(groupImages, this);
        rvProducts.setAdapter(listAdapter);

        initDynamicListSpinner();
        initSelectionSpinner();

        onClickListenersInit();

        getProductById();
        getOrderListSize();
    }

    private long getOrderListSize() {

        db.collection("orderlist")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            orderSize = task.getResult().size();
                            Log.d("order_list_size: ", task.getResult().size() + "");
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return orderSize;
    }

    private void getProductById() {
        db.collection("productlist").whereEqualTo("id", "" + id)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        if (document.getData() != null || document.getData().size() > 0) {
                            txtProdName.setText(document.getString("name"));
                            txtProdDesc.setText(document.getString("description"));
                            if (Integer.parseInt(document.getString("stock")) > 0) {
                                tvStockStatus.setText(document.getString("stock") + " in stocks");
                            } else {
                                tvStockStatus.setText("OUT OF STOCK");
                            }
                            groupImages = (ArrayList<String>) document.get("images");
                            productImages = (ArrayList<BannerListPojo>) document.get("images");
                            if (groupImages.size() > 0) {
                                listAdapter = new ProductImageAdapter(groupImages, ProductDetailActivity.this);
                                rvProducts.setAdapter(listAdapter);
                                listAdapter.notifyDataSetChanged();
                            }
                            if(productImages.size() > 0){
//                                viewPager.setAdapter(new SlidingImageAdapter(ProductDetailActivity.this,
//                                        productImages));
                            }


                            name = document.getString("name");
                            desc = document.getString("description");
                            stock = document.getString("stock");
                            price = document.getString("price");
                            in_date = document.getString("in_date");
                            type = document.getString("type");
                            id = document.getString("id");
                            piecesPerCartoon = document.getString("pieces per cartoon");
                        }
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void onClickListenersInit() {
        imgCart.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        txtAddToCart.setOnClickListener(this);
//        txtBuyNow.setOnClickListener(this);
    }

    private void initDynamicListSpinner() {
        spinnerCartoon = findViewById(R.id.spinnerCartoon);
        spinnerUnit = findViewById(R.id.spinnerPiece);

        // Custom choices
        List<CharSequence> choices = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            choices.add(i + "");
        }

        // Unit choices
        List<CharSequence> unitArray = new ArrayList<>();
        unitArray.add("Cartoon");
        unitArray.add("Piece");

        // Create an ArrayAdapter with custom choices
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, choices);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter to th spinner
        spinnerCartoon.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapterUnit = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, unitArray);

        // Specify the layout to use when the list of choices appears
        adapterUnit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter to th spinner
        spinnerUnit.setAdapter(adapterUnit);
//        spinnerPiece.setAdapter(adapter);
    }

    private void initSelectionSpinner() {
        spinnerCartoon = findViewById(R.id.spinnerCartoon);
        // Set SpinnerActivity as the item selected listener
        spinnerCartoon.setOnItemSelectedListener(this);
        spinnerUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(ProductDetailActivity.this, spinnerUnit.getSelectedItem() + " selected", Toast.LENGTH_SHORT).show();
                selUnit = spinnerUnit.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        qty = spinnerCartoon.getSelectedItem().toString();
    }

    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgCart:
                startActivity(new Intent(ProductDetailActivity.this, CartProductsActivity.class));
                break;

            case R.id.imgBack:
                onBackPressed();
                break;

            case R.id.txtAddToCart:
                if (dataProcessor.getArrayList("cart") != null) {
                    if (!Common.containsProduct(dataProcessor.getArrayList("cart"),
                            txtProdName.getText().toString())) {
                        cartProductList = dataProcessor.getArrayList("cart");
                        cartProductListPojo = new CartProductListPojo(
                                name,
                                desc, qty, stock, price, in_date, type, id, selUnit, piecesPerCartoon);
                        cartProductList.add(cartProductListPojo);
                        dataProcessor.saveArrayList(cartProductList, "cart");
                        Toast.makeText(ProductDetailActivity.this, "Your product added to cart", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Product already added to cart", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    cartProductList = dataProcessor.getArrayList("cart");
                    cartProductListPojo = new CartProductListPojo(
                            name,
                            desc, qty, stock, price, in_date, type, id, selUnit, piecesPerCartoon);
                    cartProductList.add(cartProductListPojo);
                    dataProcessor.saveArrayList(cartProductList, "cart");
                    Toast.makeText(ProductDetailActivity.this, "Your product added to cart", Toast.LENGTH_LONG).show();

                }


                break;

           /* case R.id.txtBuyNow:
                addProductForOrdering();
                break;*/

            default:
                break;
        }
    }

    private void addProductForOrdering() {
        pd.show();
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String currentDate = df.format(c.getTime());
        Map<String, Object> data = new HashMap<>();
        data.put("id", id + "");
        data.put("description", "" + desc);
        data.put("name", "" + name);
        data.put("in_date", "" + currentDate);
        data.put("price", price);
        data.put("qty", "" + qty);
        data.put("total_price", "₹ " + (Long.parseLong(price.trim().toString()) * Integer.parseInt(qty)) + "");
        data.put("user", "");
        data.put("order_no", "DIO_"+String.valueOf(getOrderListSize()+1));


        db.collection("orderlist")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        pd.dismiss();
                        Toast.makeText(ProductDetailActivity.this, "Thank you...\\n Your order generated successfully", Toast.LENGTH_LONG).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }
}