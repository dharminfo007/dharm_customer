package in.app.dharm.info.online.shopping.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import in.app.dharm.info.online.shopping.R;
import in.app.dharm.info.online.shopping.activity.DharmDealListingActivity;
import in.app.dharm.info.online.shopping.activity.LoginActivity;
import in.app.dharm.info.online.shopping.adapter.CartAdapter;
import in.app.dharm.info.online.shopping.adapter.CartFragmentAdapter;
import in.app.dharm.info.online.shopping.adapter.FavListAdapter;
import in.app.dharm.info.online.shopping.common.DataProcessor;
import in.app.dharm.info.online.shopping.model.CartProductListPojo;
import in.app.dharm.info.online.shopping.model.GenerateOrderPojo;
import in.app.dharm.info.online.shopping.model.ProductListPojo;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView rvCartProducts;
    private CartFragmentAdapter listAdapter;
    ArrayList<CartProductListPojo> productCartList;
    ImageView imgBack;
    DataProcessor dataProcessor;
    TextView txtNoCartData, tvOrderPrice, tvOrderTotal, txtBuyNow;
    public String TAG = "FavoriteFragment";
    ProgressDialog pd;
    View mView;
    FirebaseFirestore db;
    ArrayList<GenerateOrderPojo> orderList;
    long orderSize = 0;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
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
        mView = inflater.inflate(R.layout.fragment_cart, container, false);
        pd = new ProgressDialog(getActivity());
        pd.setMessage("loading...");

        productCartList = new ArrayList<>();
        rvCartProducts = mView.findViewById(R.id.rvCartProducts);
        txtNoCartData = mView.findViewById(R.id.txtNoCartData);
        tvOrderPrice = mView.findViewById(R.id.tvOrderPrice);
        tvOrderTotal = mView.findViewById(R.id.tvOrderTotal);
        txtBuyNow = mView.findViewById(R.id.txtBuyNow);

        dataProcessor = new DataProcessor(getActivity());
        orderList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();

        rvCartProducts.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvCartProducts.setLayoutManager(layoutManager);
        listAdapter = new CartFragmentAdapter(productCartList, getActivity(),
                CartFragment.this);
        rvCartProducts.setAdapter(listAdapter);

        //Load the date from the network or other resources
        //into the array list asynchronously

        if (dataProcessor.getArrayList("cart") != null
                && dataProcessor.getArrayList("cart").size() > 0) {
            pd.show();
            productCartList = dataProcessor.getArrayList("cart");
            listAdapter = new CartFragmentAdapter(productCartList, getActivity(),
                    CartFragment.this);
            rvCartProducts.setAdapter(listAdapter);
            listAdapter.notifyDataSetChanged();
            grandTotal(productCartList);
            pd.dismiss();
        } else {
            checkCartList();
        }
        tvOrderTotal.setText("Order Total ");

        txtBuyNow.setOnClickListener(this);
        return mView;
    }

    private void checkCartList() {
        if (productCartList.size() > 0) {
            txtNoCartData.setVisibility(View.GONE);
            rvCartProducts.setVisibility(View.VISIBLE);
        } else {
            txtNoCartData.setText("No data found");
            txtNoCartData.setVisibility(View.VISIBLE);
            rvCartProducts.setVisibility(View.GONE);
        }
    }

    public void removeAt(int position) {
        productCartList.remove(position);
        dataProcessor.getArrayList("cart").remove(position);
        dataProcessor.saveArrayList(productCartList, "cart");
        grandTotal(productCartList);
        listAdapter.notifyItemRemoved(position);
        listAdapter.notifyItemRangeChanged(position, productCartList.size());

        checkCartList();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtBuyNow:
                if (dataProcessor.getBool("isLogin") == true) {
                    addOrderToFireStore();
                } else {
                    Toast.makeText(getActivity(), "You need to login first..", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finishAffinity();
                }

                break;
        }
    }

    private void addOrderToFireStore() {
        pd.show();
        GenerateOrderPojo ordersListPojo = new GenerateOrderPojo();
        Map<String, Object> docData = new HashMap<>();
        docData.put("user", dataProcessor.getStr("phone"));
        docData.put("order_total", grandTotal(productCartList));
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String currentDate = df.format(c.getTime());
        for (int i = 0; i < productCartList.size(); i++) {
            ordersListPojo = new GenerateOrderPojo(productCartList.get(i).getId(), productCartList.get(i).getTvQty(),
                    productCartList.get(i).getTvPrice(), currentDate, productCartList.get(i).getUnit());
            orderList.add(ordersListPojo);
        }
        docData.put("products", orderList);


        db.collection("orderlist").document("DIO_" + String.valueOf(getOrderListSize() + 1))
                .set(docData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pd.dismiss();
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