package com.example.guest.askSJSU;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.sql.*;
import java.util.LinkedHashMap;

import org.json.*;

import android.util.Log;

public class VoteActivity extends AppCompatActivity {

    private TextView questionIDTextView;
    private TextView questionBodyTextView;
    private TextView dateCreatedTextView;
    private TextView expirationDateTextView;
    private TextView usefulCountTextView;
    private TextView upvoteClickTextView;
    private TextView downvoteClickTextView;

    private ListView optionsListView;

    private int userID;
    private int questionID;
    private ArrayList<QuestionOption> questionOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        Bundle extras = this.getIntent().getExtras();
        userID = extras.getInt("USERID", 0);
        questionID = extras.getInt("QUESTIONID", 0);

        questionIDTextView = (TextView) findViewById(R.id.questionIDTextView);
        questionBodyTextView = (TextView) findViewById(R.id.questionBodyTextView);
        dateCreatedTextView = (TextView) findViewById(R.id.dateCreatedTextView);
        expirationDateTextView = (TextView) findViewById(R.id.expirationDateTextView);
        usefulCountTextView = (TextView) findViewById(R.id.usefulCountTextView);
        upvoteClickTextView = (TextView) findViewById(R.id.upvoteClickTextView);
        upvoteClickTextView.setVisibility(View.VISIBLE);
        downvoteClickTextView = (TextView) findViewById(R.id.downvoteClickTextView);
        downvoteClickTextView.setVisibility(View.VISIBLE);
        optionsListView = (ListView) findViewById(R.id.optionsListView);

        upvoteClickTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upVote(questionID);
            }
        });

        downvoteClickTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downVote(questionID);
            }
        });

        readQuestion(questionID);
        questionOptions = getOptionList(questionID);
        QuestionOptionAdapter adapter = new QuestionOptionAdapter(VoteActivity.this, questionOptions);
        optionsListView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        readQuestion(questionID);
        questionOptions = getOptionList(questionID);
        QuestionOptionAdapter adapter = new QuestionOptionAdapter(VoteActivity.this, questionOptions);
        optionsListView.setAdapter(adapter);
    }

    public Question readQuestion(int qID) {
        Question q = new Question();
        try {
            PerformNetworkRequest getQuestion = new PerformNetworkRequest(Api.URL_GET_QUESTION + qID, null, Api.CODE_GET_REQUEST);
            getQuestion.execute();

            String gq = getQuestion.get();
            JSONObject response = new JSONObject(gq);
            JSONObject question = response.getJSONObject("question");

            q = new Question(
                    question.getInt("questionid"),
                    question.getString("questionbody"),
                    question.getString("questiontype"),
                    Timestamp.valueOf(question.getString("datecreated")),
                    Timestamp.valueOf(question.getString("expirationdate")),
                    question.getInt("usefulcount"),
                    question.getInt("visible"),
                    question.getInt("userid")
            );

            questionIDTextView.setText(getResources().getString(R.string.question_id, question.getInt("questionid")));
            questionBodyTextView.setText(question.getString("questionbody"));
            dateCreatedTextView.setText(getResources().getString(R.string.date_created, question.getString("datecreated")));
            expirationDateTextView.setText(getResources().getString(R.string.expiration_date, question.getString("expirationdate")));
            usefulCountTextView.setText(getResources().getString(R.string.useful_count, question.getInt("usefulcount")));

        } catch (java.lang.InterruptedException ie) {
            ie.printStackTrace();
        } catch (java.util.concurrent.ExecutionException ee) {
            ee.printStackTrace();
        } catch (org.json.JSONException je) {
            je.printStackTrace();
        }
        return q;
    }

    public ArrayList<QuestionOption> getOptionList(int qID) {
        ArrayList<QuestionOption> optionList = new ArrayList<>();
        try {
            PerformNetworkRequest getOptions = new PerformNetworkRequest(Api.URL_GET_QUESTION_OPTIONS + qID, null, Api.CODE_GET_REQUEST);
            getOptions.execute();

            String go = getOptions.get();
            JSONObject response = new JSONObject(go);
            JSONArray optionArray = response.getJSONArray("questionoption");

            for (int i = 0; i < optionArray.length(); i++) {
                JSONObject option = optionArray.getJSONObject(i);

                optionList.add(new QuestionOption(
                        option.getInt("optionid"),
                        option.getString("optionname"),
                        option.getInt("votecount"),
                        option.getInt("questionid")
                ));
            }
        } catch (java.lang.InterruptedException ie) {
            ie.printStackTrace();
        } catch (java.util.concurrent.ExecutionException ee) {
            ee.printStackTrace();
        } catch (org.json.JSONException je) {
            je.printStackTrace();
        }
        return optionList;
    }

    public Question upVote(int qID) {
        Question q = new Question();
        try {
            LinkedHashMap<String, String> params = new LinkedHashMap<>();
            params.put("questionid", String.valueOf(qID));

            PerformNetworkRequest upVote = new PerformNetworkRequest(Api.URL_UPDATE_QUESTION_UPVOTE, params, Api.CODE_POST_REQUEST);
            upVote.execute();

            String uv = upVote.get();
            JSONObject response = new JSONObject(uv);
            JSONObject question = response.getJSONObject("question");

            q = new Question(
                    question.getInt("questionid"),
                    question.getString("questionbody"),
                    question.getString("questiontype"),
                    Timestamp.valueOf(question.getString("datecreated")),
                    Timestamp.valueOf(question.getString("expirationdate")),
                    question.getInt("usefulcount"),
                    question.getInt("visible"),
                    question.getInt("userid")
            );

            usefulCountTextView.setText(getResources().getString(R.string.useful_count, question.getInt("usefulcount")));

        } catch (java.lang.InterruptedException ie) {
            ie.printStackTrace();
        } catch (java.util.concurrent.ExecutionException ee) {
            ee.printStackTrace();
        } catch (org.json.JSONException je) {
            je.printStackTrace();
        }
        return q;
    }

    public Question downVote(int qID) {
        Question q = new Question();
        try {
            LinkedHashMap<String, String> params = new LinkedHashMap<>();
            params.put("questionid", String.valueOf(qID));

            PerformNetworkRequest downVote = new PerformNetworkRequest(Api.URL_UPDATE_QUESTION_DOWNVOTE, params, Api.CODE_POST_REQUEST);
            downVote.execute();

            String dv = downVote.get();
            JSONObject response = new JSONObject(dv);
            JSONObject question = response.getJSONObject("question");

            q = new Question(
                    question.getInt("questionid"),
                    question.getString("questionbody"),
                    question.getString("questiontype"),
                    Timestamp.valueOf(question.getString("datecreated")),
                    Timestamp.valueOf(question.getString("expirationdate")),
                    question.getInt("usefulcount"),
                    question.getInt("visible"),
                    question.getInt("userid")
            );

            usefulCountTextView.setText(getResources().getString(R.string.useful_count, question.getInt("usefulcount")));

        } catch (java.lang.InterruptedException ie) {
            ie.printStackTrace();
        } catch (java.util.concurrent.ExecutionException ee) {
            ee.printStackTrace();
        } catch (org.json.JSONException je) {
            je.printStackTrace();
        }
        return q;
    }
}
