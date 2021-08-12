package in.app.dharm.info.online.shopping.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import in.app.dharm.info.online.shopping.R;
import in.app.dharm.info.online.shopping.adapter.CartAdapter;
import in.app.dharm.info.online.shopping.common.DataProcessor;
import in.app.dharm.info.online.shopping.model.CartProductListPojo;
import in.app.dharm.info.online.shopping.model.GenerateOrderPojo;

public class CartProductsActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView rvProducts;
    private CartAdapter listAdapter;
    ArrayList<CartProductListPojo> cartList;
    ArrayList<GenerateOrderPojo> orderList;
    ImageView imgBack;
    DataProcessor dataProcessor;
    TextView tvOrderPrice, tvOrderTotal, tvNoCart, txtBuyNow;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseFirestore db;
    public String TAG = "CartProductsActivity";
    long orderSize = 0;
    ProgressDialog pd;
    LinearLayout llBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_products);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("orderlist");
        db = FirebaseFirestore.getInstance();

        pd = new ProgressDialog(CartProductsActivity.this);
        pd.setMessage("loading...");

        cartList = new ArrayList<>();
        orderList = new ArrayList<>();
        rvProducts = findViewById(R.id.rvCartProducts);
        imgBack = findViewById(R.id.imgBack);
        tvOrderPrice = findViewById(R.id.tvOrderPrice);
        tvOrderTotal = findViewById(R.id.tvOrderTotal);
        tvNoCart = findViewById(R.id.tvNoCart);
        txtBuyNow = findViewById(R.id.txtBuyNow);
        llBottom = findViewById(R.id.llBottom);

        dataProcessor = new DataProcessor(this);

        rvProducts.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvProducts.setLayoutManager(layoutManager);
        listAdapter = new CartAdapter(cartList, this);
        rvProducts.setAdapter(listAdapter);

        //Load the date from the network or other resources
        //into the array list asynchronously

        if (dataProcessor.getArrayList("cart") != null && dataProcessor.getArrayList("cart").size() > 0) {
            cartList = dataProcessor.getArrayList("cart");
            listAdapter = new CartAdapter(cartList, this);
            rvProducts.setAdapter(listAdapter);
            listAdapter.notifyDataSetChanged();
        } else {
            checkCartList();
        }

        imgBack.setOnClickListener(this);
        txtBuyNow.setOnClickListener(this);
        grandTotal(cartList);

        tvOrderTotal.setText("Order Total ");
        getOrderListSize();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                onBackPressed();
                break;

            case R.id.txtBuyNow:
                if (dataProcessor.getBool("isLogin") == true) {
                    addOrderToFireStore();
                } else {
                    Toast.makeText(CartProductsActivity.this, "You need to login first..", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(CartProductsActivity.this, LoginActivity.class));
                    finishAffinity();
                }
                break;
            default:
                break;
        }
    }

    private void addOrderToFireStore() {
        pd.show();
        GenerateOrderPojo ordersListPojo = new GenerateOrderPojo();
        Map<String, Object> docData = new HashMap<>();
        docData.put("user", dataProcessor.getStr("phone"));
        docData.put("order_accepted", "false");
        docData.put("order_total", grandTotal(cartList) + "");
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        String orderID = "DIO_" + String.valueOf(orderSize + 1) + "";

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String currentDate = df.format(c.getTime());
        for (int i = 0; i < cartList.size(); i++) {
            ordersListPojo = new GenerateOrderPojo(cartList.get(i).getId(), cartList.get(i).getName(),
                    cartList.get(i).getTvQty(),
                    cartList.get(i).getTvPrice(), currentDate, cartList.get(i).getUnit());
            orderList.add(ordersListPojo);
        }
        docData.put("products", orderList);


        db.collection("orderlist").document("DIO_" + String.valueOf(orderSize + 1) + "")
                .set(docData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pd.dismiss();
                        cartList.clear();
                        dataProcessor.getArrayList("cart").clear();
                        dataProcessor.saveArrayList(cartList, "cart");
                        grandTotal(cartList);
                        listAdapter.notifyDataSetChanged();
                        checkCartList();
                        Toast.makeText(CartProductsActivity.this, "Order generated successfully..", Toast.LENGTH_LONG).show();
                        openSuccessDialog(orderID);
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    private void openSuccessDialog(String orderId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View view = layoutInflaterAndroid.inflate(R.layout.order_success_dialog, null);
        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        TextView tvOrderId = view.findViewById(R.id.tvOrderId);
        view.findViewById(R.id.tvDone).setOnClickListener(v -> alertDialog.dismiss());
        tvOrderId.setText(orderId);
    }

    public int grandTotal(ArrayList<CartProductListPojo> items) {

        int totalPrice = 0;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getUnit().toLowerCase().equals("cartoon")) {
                totalPrice += Integer.parseInt(items.get(i).getTvQty()) *
                        Integer.parseInt(items.get(i).getPiecesPerCartoon()) *
                        Integer.parseInt(items.get(i).getTvPrice());
            } else {
                totalPrice += Integer.parseInt(items.get(i).getTvQty()) *
                        Integer.parseInt(items.get(i).getTvPrice());
            }

        }

        tvOrderPrice.setText("₹ " + totalPrice + "");
        return totalPrice;
    }

    public void removeAt(int position) {
        cartList.remove(position);
        dataProcessor.getArrayList("cart").remove(position);
        dataProcessor.saveArrayList(cartList, "cart");
        grandTotal(cartList);
        listAdapter.notifyItemRemoved(position);
        listAdapter.notifyItemRangeChanged(position, cartList.size());

        checkCartList();

    }

    private void checkCartList() {
        if (cartList.size() > 0) {
            tvNoCart.setVisibility(View.GONE);
            rvProducts.setVisibility(View.VISIBLE);
            llBottom.setVisibility(View.VISIBLE);
        } else {
            tvNoCart.setText("Cart is empty");
            tvNoCart.setVisibility(View.VISIBLE);
            rvProducts.setVisibility(View.GONE);
            llBottom.setVisibility(View.GONE);
        }
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

}