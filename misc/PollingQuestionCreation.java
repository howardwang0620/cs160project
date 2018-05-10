package com.example.andrea.pollingq;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PollingQuestionCreation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polling_q);
    }

    public void buttonClick(View v){
        Button button = (Button)v;
        createPollingQ();
    }

    public void createPollingQ(){
        EditText title = (EditText) findViewById(R.id.title);
        EditText pollQ = (EditText) findViewById(R.id.question);
        EditText a = (EditText)findViewById(R.id.option1);
        EditText b = (EditText)findViewById(R.id.option2);
        EditText c = (EditText)findViewById(R.id.option3);
        EditText d = (EditText)findViewById(R.id.option4);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String dateCreated = dateFormat.format(date);
        PollingQuestion post = new PollingQuestion(title.getText().toString(), pollQ.getText().toString(), a.getText().toString(), b.getText().toString(), c.getText().toString(), d.getText().toString(), dateCreated);
        //add pollQ to db
    }
}
