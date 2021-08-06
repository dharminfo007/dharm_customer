package in.app.dharm.info.online.shopping.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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
import in.app.dharm.info.online.shopping.adapter.ProductDetailImageAdapter;
import in.app.dharm.info.online.shopping.adapter.ProductImageAdapter;
import in.app.dharm.info.online.shopping.common.AutoScrollViewPager;
import in.app.dharm.info.online.shopping.common.Common;
import in.app.dharm.info.online.shopping.common.DataProcessor;
import in.app.dharm.info.online.shopping.model.CartProductListPojo;
import in.app.dharm.info.online.shopping.model.ImageListPojo;
import in.app.dharm.info.online.shopping.model.OrdersListPojo;
import in.app.dharm.info.online.shopping.model.ProductListPojo;


public class ProductDetailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    RecyclerView rvProducts;
    private ProductImageAdapter listAdapter;
    ArrayList<ImageListPojo> productImageList;
    ArrayList<OrdersListPojo> productOrderList;
    private ArrayList<String> productImages;
    Spinner spinnerCartoon, spinnerUnit;
    ImageView imgCart, imgBack, imgLike;
    TextView txtAddToCart, txtDealNow, tvPrice;
    FirebaseFirestore db;
    public String TAG = "ProductDetailActivity";
    String id = "";
    TextView txtProdName, txtProdDesc, tvStockStatus, btnDecreaseQty, btnIncreaseQty, tvQty;
    ArrayList<String> groupImages;
    DataProcessor dataProcessor;
    ArrayList<CartProductListPojo> cartProductList;
    CartProductListPojo cartProductListPojo;
    String name, desc, selUnit, price, in_date, type, stock, piecesPerCartoon;
    int qty = 1;
    ProgressDialog pd;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    long orderSize = 0;
    AutoScrollViewPager viewPager;
    ProductListPojo product;
    CardView cardFav;
    ArrayList<ProductListPojo> productFavArrayList;
    boolean isFav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        init();
    }

    public void init() {
        id = getIntent().getStringExtra("id");
        isFav = getIntent().getBooleanExtra("favorite", false);
        db = FirebaseFirestore.getInstance();
        dataProcessor = new DataProcessor(this);

        pd = new ProgressDialog(ProductDetailActivity.this);
        pd.setMessage("loading...");

        product = new ProductListPojo();
        cartProductList = new ArrayList<>();
        groupImages = new ArrayList<>();
        productImageList = new ArrayList<>();
        productOrderList = new ArrayList<>();
        productImages = new ArrayList<>();
        productFavArrayList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("orderlist");
//        cartProductList = dataProcessor.getArrayList("cart");
        rvProducts = findViewById(R.id.rvProductsImage);
        txtAddToCart = findViewById(R.id.txtAddToCart);
        viewPager = findViewById(R.id.vpProductImages);
        btnDecreaseQty = findViewById(R.id.btnDecreaseQty);
        btnIncreaseQty = findViewById(R.id.btnIncreaseQty);
        tvQty = findViewById(R.id.tvQty);
        tvPrice = findViewById(R.id.tvPrice);
        cardFav = findViewById(R.id.cardFav);
        imgBack = findViewById(R.id.imgBack);
        txtProdName = findViewById(R.id.txtProdName);
        txtProdDesc = findViewById(R.id.txtProdDesc);
        tvStockStatus = findViewById(R.id.tvStockStatus);
        txtDealNow = findViewById(R.id.txtDealNow);
        imgLike = findViewById(R.id.imgLike);

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
                            product = new ProductListPojo(document.getString("name"),
                                    document.getString("description"),
                                    document.getString("pieces per cartoon"),
                                    document.getString("stock"),
                                    document.getString("price"),
                                    document.getString("in_date"),
                                    document.getString("type"),
                                    document.getString("id"),
                                    (ArrayList<String>) document.get("images"));
                            txtProdName.setText(document.getString("name"));
                            txtProdDesc.setText(document.getString("description"));
                            if (Integer.parseInt(document.getString("stock")) > 0) {
                                tvStockStatus.setText(document.getString("stock") + " in stocks");
                            } else {
                                tvStockStatus.setText("OUT OF STOCK");
                            }
                            if(isFav == true){
                                imgLike.setBackgroundResource(R.drawable.ic_fav_selected);
                            }else {
                                imgLike.setBackgroundResource(R.drawable.ic_fav);
                            }
                            tvPrice.setText("₹ " + document.getString("price"));
                            groupImages = (ArrayList<String>) document.get("images");
                            productImages = (ArrayList<String>) document.get("images");
                            if (groupImages.size() > 0) {
                                listAdapter = new ProductImageAdapter(groupImages, ProductDetailActivity.this);
                                rvProducts.setAdapter(listAdapter);
                                listAdapter.notifyDataSetChanged();
                            }

                            if (productImages.size() > 0) {
                                viewPager.setAdapter(new ProductDetailImageAdapter(ProductDetailActivity.this,
                                        productImages));
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
        cardFav.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        txtAddToCart.setOnClickListener(this);
        txtDealNow.setOnClickListener(this);
        btnIncreaseQty.setOnClickListener(this);
        btnDecreaseQty.setOnClickListener(this);
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
        qty = Integer.parseInt(spinnerCartoon.getSelectedItem().toString());
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

            case R.id.cardFav:
                if (product.isFav() == true) {
                    dataProcessor = new DataProcessor(this);
                    dataProcessor.removeFromFavArrayList("favorite", product);
                    product.setFav(false);
//                    notifyItemChanged(position);
//                    notifyDataSetChanged();
                } else {
                    addProductToFavorite();

                }
                break;

            case R.id.txtAddToCart:
                if (dataProcessor.getArrayList("cart") != null) {
                    if (!Common.containsProduct(dataProcessor.getArrayList("cart"),
                            txtProdName.getText().toString())) {
                        cartProductList = dataProcessor.getArrayList("cart");
                        cartProductListPojo = new CartProductListPojo(
                                name,
                                desc, qty + "", stock, price, in_date, type, id, selUnit, piecesPerCartoon,
                                productImages);
                        cartProductList.add(cartProductListPojo);
                        dataProcessor.saveArrayList(cartProductList, "cart");
                        Toast.makeText(ProductDetailActivity.this, "Your product added to cart", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ProductDetailActivity.this, CartProductsActivity.class));
                    } else {
                        Toast.makeText(this, "Product already added to cart", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    cartProductList = dataProcessor.getArrayList("cart");
                    if (cartProductList == null) {
                        cartProductList = new ArrayList<>();
                    }
                    cartProductListPojo = new CartProductListPojo(
                            name,
                            desc, qty + "", stock, price, in_date, type, id, selUnit, piecesPerCartoon,
                            productImages);
                    cartProductList.add(cartProductListPojo);
                    dataProcessor.saveArrayList(cartProductList, "cart");
                    Toast.makeText(ProductDetailActivity.this, "Your product added to cart", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ProductDetailActivity.this, CartProductsActivity.class));
                }


                break;

            case R.id.txtDealNow:
//                addProductForOrdering();
//                 goToDealPageListing();
                openDialog(product);
                break;

            case R.id.btnDecreaseQty:
                if (qty > 1) {
                    qty--;
                    tvQty.setText(String.valueOf(qty));
                }
                break;

            case R.id.btnIncreaseQty:
                if (qty < 20) {
                    qty++;
                    tvQty.setText(String.valueOf(qty));
                }
                break;

            default:
                break;
        }
    }

    private void goToDealPageListing() {
        startActivity(new Intent(ProductDetailActivity.this, DharmDealListingActivity.class));
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
        data.put("total_price", "₹ " + (Long.parseLong(price.trim().toString()) * qty) + "");
        data.put("user", "");
        data.put("order_no", "DIO_" + String.valueOf(getOrderListSize() + 1));


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

    private void openDialog(ProductListPojo product) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.dialog_bottom_sheet);

        ImageView imgProduct = bottomSheetDialog.findViewById(R.id.imgProduct);
        TextView tvTitle = bottomSheetDialog.findViewById(R.id.tvTitle);
        TextView tvPrice = bottomSheetDialog.findViewById(R.id.tvPrice);
        TextView tvCartoon = bottomSheetDialog.findViewById(R.id.tvCartoon);
        TextView tvStock = bottomSheetDialog.findViewById(R.id.tvStock);
        TextView txt_TotalCartoons = bottomSheetDialog.findViewById(R.id.txt_TotalCartoons);
        TextView txt_TotalAmt = bottomSheetDialog.findViewById(R.id.txt_TotalAmt);
        EditText etReqCartoon = bottomSheetDialog.findViewById(R.id.etReqCartoon);
        EditText etReqAmt = bottomSheetDialog.findViewById(R.id.etReqAmt);
        TextView tvAddDeal = bottomSheetDialog.findViewById(R.id.tvAddDeal);
        ImageView imgClose = bottomSheetDialog.findViewById(R.id.imgClose);

        tvTitle.setText(product.getName());
        tvCartoon.setText(product.getTvPiecesPerCartoon() + " /Cartoons");
        tvStock.setText(product.getTvStock() + " in stocks");
        tvPrice.setText("₹ " + product.getTvPrice());

        if (product.getListProductImages().size() > 0) {
            Glide
                    .with(this)
                    .load(product.getListProductImages().get(0))
                    .centerCrop()
//                .placeholder(R.drawable.loading_spinner)
                    .into(imgProduct);
        }
        etReqCartoon.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0) {
                    txt_TotalCartoons.setText(String.valueOf(Integer.parseInt(product.getTvPiecesPerCartoon()) *
                            Integer.parseInt(etReqCartoon.getText().toString())));
                }

            }
        });
        etReqAmt.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0) {
                    txt_TotalAmt.setText("₹ " + String.valueOf(Integer.parseInt(product.getTvPiecesPerCartoon()) *
                            Integer.parseInt(etReqAmt.getText().toString())
                            * Integer.parseInt(etReqCartoon.getText().toString())));
                }
            }
        });
        tvAddDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataProcessor = new DataProcessor(ProductDetailActivity.this);
                if (etReqCartoon.getText().toString().length() == 0) {
                    Toast.makeText(ProductDetailActivity.this, "Add cartoon first", Toast.LENGTH_LONG).show();
                } else if (etReqAmt.getText().toString().length() == 0) {
                    Toast.makeText(ProductDetailActivity.this, "Add amount to deal first", Toast.LENGTH_LONG).show();
                } else {
                    if (dataProcessor.getBool("isLogin") == true) {
                        addDealToFireStore(etReqCartoon.getText().toString(),
                                etReqAmt.getText().toString(), product.getId(), bottomSheetDialog);

                    } else {
                        bottomSheetDialog.dismiss();
                        Toast.makeText(ProductDetailActivity.this, "You need to login first..", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ProductDetailActivity.this, LoginActivity.class));
                        finishAffinity();
                    }
                }


            }
        });
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();

    }

    public void addDealToFireStore(String reqCartoon, String dealAmt, String product_id,
                                   BottomSheetDialog bottomSheetDialog) {
        pd.show();

        Map<String, Object> docData = new HashMap<>();
        docData.put("user", dataProcessor.getStr("phone"));
        docData.put("product_id", product_id);
        docData.put("cartoon", reqCartoon);
        docData.put("deal_amount", dealAmt);
        docData.put("status", "pending");

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String currentDate = df.format(c.getTime());

        docData.put("deal_in_date", currentDate);

        db.collection("deallist").document("DOD_" + dataProcessor.getStr("phone") + "_" + product_id)
                .set(docData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ProductDetailActivity.this, "Your deal request success..", Toast.LENGTH_LONG).show();
                        pd.dismiss();
                        bottomSheetDialog.dismiss();
                        Log.d(TAG, "Deal successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProductDetailActivity.this, "Failed to deal..", Toast.LENGTH_LONG).show();

                        pd.dismiss();
                        bottomSheetDialog.dismiss();
                        Log.w(TAG, "Error writing deal", e);
                    }
                });
    }

    public void addProductToFavorite() {
        if (dataProcessor.getFavoriteArrayList("favorite") != null) {
            if (!Common.containsFavoriteProduct(dataProcessor.getFavoriteArrayList("favorite"),
                    product.getName())) {
                productFavArrayList = dataProcessor.getFavoriteArrayList("favorite");

                product.setFav(true);
                productFavArrayList.add(product);
                imgLike.setBackgroundResource(R.drawable.ic_fav_selected);
                dataProcessor.saveFavoriteArrayList(productFavArrayList, "favorite");
                listAdapter.notifyDataSetChanged();
                Toast.makeText(ProductDetailActivity.this, "Your product added to favorite", Toast.LENGTH_LONG).show();
            } else {
                dataProcessor.removeFromFavArrayList("favorite", product);
                product.setFav(false);
                imgLike.setBackgroundResource(R.drawable.ic_fav);
//                Toast.makeText(this, "Product already added to favorite", Toast.LENGTH_SHORT).show();
            }
        } else {
            productFavArrayList = dataProcessor.getFavoriteArrayList("favorite");
            if (productFavArrayList == null) {
                productFavArrayList = new ArrayList<>();
            }
            product.setFav(true);
            productFavArrayList.add(product);
            dataProcessor.saveFavoriteArrayList(productFavArrayList, "favorite");
            listAdapter.notifyDataSetChanged();
            Toast.makeText(ProductDetailActivity.this, "Your product added to favorite", Toast.LENGTH_LONG).show();


        }
    }

}