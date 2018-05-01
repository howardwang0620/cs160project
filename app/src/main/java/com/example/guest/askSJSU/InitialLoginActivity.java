package com.example.guest.askSJSU;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import org.json.JSONObject;
import org.json.JSONArray;


public class InitialLoginActivity extends AppCompatActivity {

    private TextView usernameText;
    private TextView passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_login);

        usernameText = findViewById(R.id.usernameEditText);
        passwordText = findViewById(R.id.pwEditText);
    }

    public void loginButton(View view) throws java.lang.InterruptedException,
            java.util.concurrent.ExecutionException, org.json.JSONException {
        String username = usernameText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();

        PerformNetworkRequest request = new PerformNetworkRequest(
                Api.URL_GET_USER_BY_USERNAME + username, null, Api.CODE_GET_REQUEST);
        request.execute();

        String s = request.get();
        JSONObject response = new JSONObject(s);
        JSONArray results = new JSONArray();
        JSONObject user = new JSONObject();

        if (response.has("userdata")) {
            results = response.getJSONArray("userdata");
            if (!results.isNull(0)) {
                user = results.getJSONObject(0);
            }
        }

        if((user.has("username"))) {
            //if user exists in db
            if(user.getString("userpassword").equals(password)) {   //successful login
                Intent intent = new Intent(this, HomePageActivity.class);
                intent.putExtra("USERID", user.getInt("userid"));
                finish();
                startActivity(intent);
            } else {
                passwordText.setText("");
                passwordText.setHint("Incorrect password");
                passwordText.setHintTextColor(Color.parseColor("#f66c6c"));
            }
        } else {
            usernameText.setText("");
            passwordText.setText("");
            usernameText.setHint("Username not found");
            usernameText.setHintTextColor(Color.parseColor("#f66c6c"));
        }
    }


    public void registerButton(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
