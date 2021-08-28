package in.app.dharm.info.online.shopping.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import in.app.dharm.info.online.shopping.R;
import in.app.dharm.info.online.shopping.adapter.FilterAdapter;
import in.app.dharm.info.online.shopping.adapter.ProductAdapter;
import in.app.dharm.info.online.shopping.common.DataProcessor;
import in.app.dharm.info.online.shopping.model.ProductListPojo;

public class ServiceProviderActivity extends AppCompatActivity {

    RecyclerView rvServiceProviders;
    ProgressDialog pd;
    TextView txtNoDataFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider);

        init();
    }

    public  void init(){
        pd = new ProgressDialog(ServiceProviderActivity.this);
        pd.setMessage("loading...");

        rvServiceProviders = (RecyclerView) findViewById(R.id.rvServiceProviders);
        txtNoDataFound = findViewById(R.id.tvNoServices);
        txtNoDataFound.setVisibility(View.VISIBLE);
        rvServiceProviders.setVisibility(View.GONE);
    }

}