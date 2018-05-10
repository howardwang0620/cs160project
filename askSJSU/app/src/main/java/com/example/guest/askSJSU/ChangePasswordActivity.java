package com.example.guest.askSJSU;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.LinkedHashMap;

public class ChangePasswordActivity extends AppCompatActivity {
    private int userID;
    private String newPassword;
    private String newPasswordCheck;
    private TextView newPasswordText;
    private TextView newPasswordCheckText;

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Bundle extras = this.getIntent().getExtras();
        userID = extras.getInt("USERID", 0);

        newPasswordText = findViewById(R.id.newPassword);
        newPasswordCheckText = findViewById(R.id.newPasswordCheck);

        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    //toolbar back button press handler
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void changePasswordButton (View view) {
        newPassword = newPasswordText.getText().toString().trim();
        newPasswordCheck = newPasswordCheckText.getText().toString().trim();
        if (TextUtils.isEmpty(newPassword)) {
            newPasswordText.setText("");
            newPasswordText.setHint("Enter new password");
            newPasswordText.setHintTextColor(Color.parseColor("#f66c6c"));
        } else {
            if (TextUtils.isEmpty(newPasswordCheck)) {
                newPasswordCheckText.setText("");
                newPasswordCheckText.setHint("Re-enter password");
                newPasswordCheckText.setHintTextColor(Color.parseColor("#f66c6c"));
            } else if (!newPassword.equals(newPasswordCheck)) {
                newPasswordCheckText.setText("");
                newPasswordCheckText.setHint("Passwords do not match");
                newPasswordCheckText.setHintTextColor(Color.parseColor("#f66c6c"));
            } else {
                user.updatePassword(newPassword)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    LinkedHashMap<String, String> passwordUpdate = new LinkedHashMap<>();
                                    passwordUpdate.put("userid", String.valueOf(userID));
                                    passwordUpdate.put("userpassword", newPassword);
                                    PerformNetworkRequest request = new PerformNetworkRequest(
                                            Api.URL_UPDATE_USER_PASSWORD, passwordUpdate, Api.CODE_POST_REQUEST);
                                    request.execute();
                                    Toast.makeText(getApplicationContext(), "Password changed", Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Error changing password", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

            }
        }
    }
}
