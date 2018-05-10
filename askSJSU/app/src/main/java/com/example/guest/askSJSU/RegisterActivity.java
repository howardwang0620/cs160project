package com.example.guest.askSJSU;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;

import java.util.LinkedHashMap;

public class RegisterActivity extends AppCompatActivity {

    private TextView usernameText;
    private TextView emailText;
    private TextView passwordText;
    private TextView sjsuIDText;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameText = findViewById(R.id.usernameRegEditText);
        emailText = findViewById(R.id.emailRegEditText);
        passwordText = findViewById(R.id.pwRegEditText);
        sjsuIDText = findViewById(R.id.sjsuIDEditText);

        auth = FirebaseAuth.getInstance();

    }

    public void registerButton(View view) throws java.lang.InterruptedException,
            java.util.concurrent.ExecutionException, org.json.JSONException {
        final String username = usernameText.getText().toString().trim();
        final String email = emailText.getText().toString().trim();
        final String password = passwordText.getText().toString().trim();
        final String sID = sjsuIDText.getText().toString().trim();

        if (!username.isEmpty()) {
            if (!email.isEmpty()) {
                if (email.endsWith("@sjsu.edu")) {
                    if (!sID.isEmpty()) {
                        if (sID.length() == 9) {
                            if (!password.isEmpty()) {
                                if (password.length() > 5) {

                                    final LinkedHashMap<String, String> params = new LinkedHashMap<>();

                                    params.put("username", username);
                                    params.put("userpassword", password);
                                    params.put("useremail", email);
                                    params.put("sjsuid", sID);

                                    PerformNetworkRequest request = new PerformNetworkRequest(
                                            Api.URL_GET_USER_BY_USERNAME + username, null, Api.CODE_GET_REQUEST);
                                    request.execute();

                                    String s = request.get();
                                    JSONObject response = new JSONObject(s);
                                    //username not found, create the account (will add email verification later)
                                    if (response.getBoolean("error")) {
                                        auth = FirebaseAuth.getInstance();
                                        auth.createUserWithEmailAndPassword(email, password).
                                                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if(task.isSuccessful()) {

                                                            //place user into  mysql database
                                                            params.put("username", username);
                                                            params.put("userpassword", password);
                                                            params.put("useremail", email);
                                                            params.put("sjsuid", sID);
                                                            PerformNetworkRequest request = new PerformNetworkRequest(
                                                                    Api.URL_CREATE_USER, params, Api.CODE_POST_REQUEST);
                                                            request.execute();

                                                            //send verification email with firebase
                                                            sendVerificationEmail();
                                                            FirebaseAuth.getInstance().signOut();
                                                            finish();
                                                        } else {
                                                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                                                Toast.makeText(getApplicationContext(), "E-mail already in use",
                                                                        Toast.LENGTH_LONG).show();
                                                            } else {
                                                                Toast.makeText(getApplicationContext(), "Error sending e-mail verification",
                                                                        Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    }
                                                });
                                    } else {
                                        usernameText.setText("");
                                        usernameText.setHint("Username already taken");
                                        usernameText.setHintTextColor(Color.parseColor("#f66c6c"));
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            "Password must be at least 6 characters",
                                            Toast.LENGTH_LONG).show();
                                }
                            } else {
                                passwordText.setText("");
                                passwordText.setHint("Enter a Password");
                                passwordText.setHintTextColor(Color.parseColor("#f66c6c"));
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "SJSU ID must be exactly 9 digits",
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {
                        sjsuIDText.setText("");
                        sjsuIDText.setHint("Enter an SJSU ID");
                        sjsuIDText.setHintTextColor(Color.parseColor("#f66c6c"));
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Not a Valid SJSU Email Address",
                            Toast.LENGTH_LONG).show();
                }
            } else {
                emailText.setText("");
                emailText.setHint("Enter an Email Address");
                emailText.setHintTextColor(Color.parseColor("#f66c6c"));
            }
        } else {
            usernameText.setText("");
            usernameText.setHint("Enter a Username");
            usernameText.setHintTextColor(Color.parseColor("#f66c6c"));
        }

    }
    public void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this,
                                        "E-mail verification sent", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    public void cancelRegButton(View view) {
        finish();
    }
}
