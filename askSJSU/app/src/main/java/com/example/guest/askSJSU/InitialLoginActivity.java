package com.example.guest.askSJSU;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;


public class InitialLoginActivity extends AppCompatActivity {

    private TextView usernameText;
    private TextView passwordText;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_login);


        usernameText = findViewById(R.id.usernameEditText);
        passwordText = findViewById(R.id.pwEditText);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onResume() {
        super.onResume();
        usernameText.setText("");
        passwordText.setText("");
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
                String email = user.getString("useremail");
                final int userID = user.getInt("userid");
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            final FirebaseUser fbUser = mAuth.getCurrentUser();


                            fbUser.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //if reload successful
                                    if(task.isSuccessful()) {
                                        if(fbUser.isEmailVerified()) {
                                            Intent intent = new Intent(InitialLoginActivity.this, HomePageActivity.class);
                                            intent.putExtra("USERID", userID);
                                            finish();
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(InitialLoginActivity.this, "E-mail not verified", Toast.LENGTH_LONG).show();

                                        }
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(InitialLoginActivity.this, "Account not found on Firebase", Toast.LENGTH_LONG).show();
                        }
                    }
                });
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
