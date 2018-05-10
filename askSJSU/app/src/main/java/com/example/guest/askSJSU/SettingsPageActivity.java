package com.example.guest.askSJSU;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Set;

public class SettingsPageActivity extends AppCompatActivity {
    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);

        Bundle extras = this.getIntent().getExtras();

        userID = extras.getInt("USERID", 0);
        TextView userNameTxt = (TextView) findViewById(R.id.settingsUsername);
        userNameTxt.setText("buffer");
        TextView emailTxt = (TextView) findViewById(R.id.settingsEmail);
        emailTxt.setText("buffer@email.com");

        try {
            JSONArray jsonArray = Utilities.getUserByUserId(userID);
            JSONObject userData = jsonArray.getJSONObject(0);
            userNameTxt.setText(userData.getString("username"));
            emailTxt.setText(userData.getString("useremail"));
        } catch (Exception e) {
            Log.d("Utilities Exception", "JSON Exception");
        }

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

    public void changePwButton(View view){
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        intent.putExtra("USERID", userID);
        startActivity(intent);
        finish();
    }

    public void deleteAccButton(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Delete Account");
        builder.setMessage("Are you sure you want to delete this account?");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            PerformNetworkRequest deleteAcc = new PerformNetworkRequest(
                                                    Api.URL_DELETE_USER + userID, null, Api.CODE_GET_REQUEST);
                                            deleteAcc.execute();
                                            Toast.makeText(SettingsPageActivity.this, "Successfully deleted account", Toast.LENGTH_LONG).show();
                                            FirebaseAuth.getInstance().signOut();
                                            Intent intent = new Intent(SettingsPageActivity.this, InitialLoginActivity.class);
                                            finish();
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(SettingsPageActivity.this, "Error deleting account", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
