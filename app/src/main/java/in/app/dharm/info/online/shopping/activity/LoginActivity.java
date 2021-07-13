package in.app.dharm.info.online.shopping.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import in.app.dharm.info.online.shopping.R;
import in.app.dharm.info.online.shopping.common.DataProcessor;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvSkip, tvLogin;
    DataProcessor dataProcessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    public void init(){
        dataProcessor = new DataProcessor(this);
        tvSkip = findViewById(R.id.tvSkip);
        tvLogin = findViewById(R.id.tvLogin);
        tvSkip.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvSkip :
                dataProcessor.setBool("isSkip", true);
                startActivity(new Intent(LoginActivity.this, HomePageActivity.class));
                finishAffinity();
                break;

                case R.id.tvLogin :
                dataProcessor.setBool("isLogin", true);
                startActivity(new Intent(LoginActivity.this, HomePageActivity.class));
                finishAffinity();
                break;

            default:
                break;
        }
    }
}