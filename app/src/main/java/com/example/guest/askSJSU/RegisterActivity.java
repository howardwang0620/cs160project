package com.example.guest.askSJSU;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.LinkedHashMap;

public class RegisterActivity extends AppCompatActivity {

    private TextView usernameText;
    private TextView emailText;
    private TextView passwordText;
    private TextView sjsuIDText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameText = findViewById(R.id.usernameRegEditText);
        emailText = findViewById(R.id.emailRegEditText);
        passwordText = findViewById(R.id.pwRegEditText);
        sjsuIDText = findViewById(R.id.sjsuIDEditText);
    }

    public void registerButton(View view) {
        String username = usernameText.getText().toString().trim();
        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();
        String sID = sjsuIDText.getText().toString().trim();
        LinkedHashMap<String, String> params = new LinkedHashMap<>();

        if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !sID.isEmpty()) {
            params.put("username", username);
            params.put("userpassword", password);
            params.put("useremail", email);
            params.put("sjsuid", sID);

            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREATE_USER, params, Api.CODE_POST_REQUEST);
            request.execute();
            finish(); //will add verification step in future build
        }
    }

    public void cancelRegButton(View view) {
        finish();
    }
}
