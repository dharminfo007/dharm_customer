package in.app.dharm.info.online.shopping.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import in.app.dharm.info.online.shopping.R;
import in.app.dharm.info.online.shopping.common.DataProcessor;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvSkip, tvLogin;
    DataProcessor dataProcessor;
    EditText etMoNo, etAddress;
    FirebaseDatabase firebaseDatabase;

    // instance for firebase storage and StorageReference
    FirebaseFirestore db;
    ProgressDialog pd;
    public static String TAG = "LoginActivity";
    boolean isExist = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    public void init() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        db = FirebaseFirestore.getInstance();
        dataProcessor = new DataProcessor(this);
        tvSkip = findViewById(R.id.tvSkip);
        tvLogin = findViewById(R.id.tvLogin);
        etMoNo = findViewById(R.id.etMoNo);
        etAddress = findViewById(R.id.etAddress);
        tvSkip.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        pd = new ProgressDialog(LoginActivity.this);
        pd.setMessage("loading...");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSkip:
                dataProcessor.setBool("isSkip", true);
                startActivity(new Intent(LoginActivity.this, HomePageActivity.class));
                finishAffinity();
                break;

            case R.id.tvLogin:
                if (etMoNo.getText().toString().length() == 0) {
                    Toast.makeText(LoginActivity.this, "Please enter mobile number", Toast.LENGTH_LONG).show();
                } else if (etMoNo.getText().toString().length() < 10) {
                    Toast.makeText(LoginActivity.this, "Please enter valid mobile number", Toast.LENGTH_LONG).show();
                } else if (etAddress.getText().toString().length() == 0) {
                    Toast.makeText(LoginActivity.this, "Please enter address", Toast.LENGTH_LONG).show();
                } else {
                    addUserToFirebase();
                }

                break;

            default:
                break;
        }
    }

    public void addUserToFirebase() {

        if (!checkExistProduct(etMoNo.getText().toString())) {
            pd.show();

            Calendar c = Calendar.getInstance();
            System.out.println("Current time => " + c.getTime());

            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String currentDate = df.format(c.getTime());
            Map<String, Object> data = new HashMap<>();
            data.put("phone", "" + etMoNo.getText().toString());
            data.put("address", "" + etAddress.getText().toString());
            data.put("in_date", "" + currentDate);

            db.collection("userlist").document(etMoNo.getText().toString() + "")
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void documentReference) {
                            pd.dismiss();
                            Toast.makeText(LoginActivity.this, "User login successfully...", Toast.LENGTH_LONG).show();
                            dataProcessor.setBool("isLogin", true);
                            dataProcessor.setStr("phone", etMoNo.getText().toString());
                            dataProcessor.setStr("address", etAddress.getText().toString());
                            startActivity(new Intent(LoginActivity.this, HomePageActivity.class));
                            finishAffinity();
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

    public boolean checkExistProduct(String phoneNo) {

        db.collection("userlist")
                .whereEqualTo("phone", "" + phoneNo)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                isExist = true;
                                Log.d(TAG, "User is avail");
                            }
                        } else {
                            Log.d(TAG, "Error getting user: ", task.getException());
                            isExist = false;
                        }
                    }
                });
        return isExist;
    }

}