package in.app.dharm.info.online.shopping.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import in.app.dharm.info.online.shopping.R;
import in.app.dharm.info.online.shopping.common.DataProcessor;
import in.app.dharm.info.online.shopping.model.GenerateOrderPojo;

public class ContactUsActivity extends AppCompatActivity implements View.OnClickListener {

    MaterialTextView tvSend;
    DataProcessor dataProcessor;
    ProgressDialog pd;
    FirebaseFirestore db;
    public String TAG = "ContactUsActivity";
    EditText etFeedBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        initi();
    }

    private void initi() {
        tvSend = findViewById(R.id.tvSend);
        etFeedBack = findViewById(R.id.etFeedBack);
        tvSend.setOnClickListener(this);
        dataProcessor = new DataProcessor(this);
        db = FirebaseFirestore.getInstance();
        pd = new ProgressDialog(ContactUsActivity.this);
        pd.setMessage("loading...");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSend:

                if(etFeedBack.getText().toString().length() > 0){
                    if (dataProcessor.getBool("isLogin") == true) {
                        submitFeedbackToAdmin();
                    } else {
                        Toast.makeText(ContactUsActivity.this, "You need to login first..", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ContactUsActivity.this, LoginActivity.class));
                        finishAffinity();
                    }
                }else {
                    Toast.makeText(ContactUsActivity.this, "Please enter feedback first..", Toast.LENGTH_LONG).show();
                }

                break;

            default:
                break;
        }
    }

    private void submitFeedbackToAdmin() {
        pd.show();

        Map<String, Object> docData = new HashMap<>();
        docData.put("user", dataProcessor.getStr("phone"));

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String currentDate = df.format(c.getTime());
        docData.put("feedback_add_date", currentDate);
        docData.put("feedback", etFeedBack.getText().toString());
//        docData.put("products", orderList);


        db.collection("feedbacklist").document("FOD_" + dataProcessor.getStr("phone"))
                .set(docData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pd.dismiss();
                        Log.d(TAG, "Feedback successfully written!");
                        Toast.makeText(ContactUsActivity.this, "Feedback successfully added", Toast.LENGTH_LONG).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Log.w(TAG, "Error writing feedback", e);
                    }
                });
    }


}