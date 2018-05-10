package com.example.andrea.ratingq;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RatingQuestionCreation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_question);
    }

    private void buttonClick(View v){
        Button button = (Button)v;
        createRatingQ();
    }

    private void createRatingQ(){
        EditText title = (EditText) findViewById(R.id.title);
        EditText rateQ = (EditText) findViewById(R.id.question);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String dateCreated = dateFormat.format(date);
        RatingQuestion post = new RatingQuestion(title.getText().toString(), rateQ.getText().toString(), dateCreated);
        //add post to db
    }
}
