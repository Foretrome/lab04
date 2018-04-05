package com.example.foretrome.lab04;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    Button suggestBen;
    Button loginBtn;
    TextView uNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        uNameView = findViewById(R.id.usernameView);
        loginBtn = findViewById(R.id.loginBtn);
        suggestBen = findViewById(R.id.sugestNameBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uName = uNameView.getText().toString();
                if(uName.isEmpty()){
                    //Error
                }else{
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("usernameFromIntent", uName);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });
    }
}
