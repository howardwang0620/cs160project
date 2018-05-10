package com.example.guest.askSJSU;

import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SearchActivity extends AppCompatActivity {

    private TextView searchEditText;
    private Spinner searchFilterSpinner;
    private ListView searchListView;

    private QuestionAdapter adapter;

    private int userID;
    private ArrayList<Question> questions;
    private long lastClickTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Bundle extras = this.getIntent().getExtras();
        userID = extras.getInt("USERID", 0);

        searchEditText = (TextView) findViewById(R.id.searchEditText);
        searchFilterSpinner = (Spinner) findViewById(R.id.searchFilterSpinner);
        searchListView = (ListView) findViewById(R.id.searchListView);

        adapter = new QuestionAdapter(SearchActivity.this, questions);

        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (SystemClock.elapsedRealtime() - lastClickTime < 500) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();

                Object obj = searchListView.getItemAtPosition(position);
                Question q = (Question) obj;
                int qID = q.getQuestionId();

                Intent intent = new Intent(SearchActivity.this, VoteActivity.class);
                intent.putExtra("USERID", userID);
                intent.putExtra("QUESTIONID", qID);
                startActivity(intent);
            }
        });
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

    public void searchButton(View view) {
        String searchString = searchEditText.getText().toString().trim();
        String searchFilter = searchFilterSpinner.getSelectedItem().toString();

        if (!searchString.isEmpty()) {
            switch (searchFilter) {
                case "Question ID":
                    questions = searchByQuestionID(searchString);
                    adapter = new QuestionAdapter(SearchActivity.this, questions);
                    searchListView.setAdapter(adapter);
                    break;
                case "Question Body":
                    questions = searchByBody(searchString);
                    adapter = new QuestionAdapter(SearchActivity.this, questions);
                    searchListView.setAdapter(adapter);
                    break;
                case "Question Type":
                    questions = searchByType(searchString);
                    adapter = new QuestionAdapter(SearchActivity.this, questions);
                    searchListView.setAdapter(adapter);
                    break;
                case "Question Category":
                    questions = searchByCategory(searchString);
                    adapter = new QuestionAdapter(SearchActivity.this, questions);
                    searchListView.setAdapter(adapter);
                    break;
                case "Question Usefulness":
                    questions = searchByUsefulCount(searchString);
                    adapter = new QuestionAdapter(SearchActivity.this, questions);
                    searchListView.setAdapter(adapter);
                    break;
            }
        } else {
            searchEditText.setText("");
            searchEditText.setHint("Enter something!");
            searchEditText.setHintTextColor(Color.parseColor("#f66c6c"));
        }
    }

    public ArrayList<Question> searchByQuestionID (String qID) {
        ArrayList<Question> searchResults = new ArrayList<>();
        try {
            PerformNetworkRequest getQuestion = new PerformNetworkRequest(Api.URL_GET_QUESTION + qID, null, Api.CODE_GET_REQUEST);
            getQuestion.execute();

            String gq = getQuestion.get();
            JSONObject response = new JSONObject(gq);
            if (!response.getBoolean("error")) {
                JSONObject question = response.getJSONObject("question");

                searchResults.add(new Question(
                        question.getInt("questionid"),
                        question.getString("questionbody"),
                        question.getString("questioncategory"),
                        question.getString("questiontype"),
                        Date.valueOf(question.getString("datecreated")),
                        Date.valueOf(question.getString("expirationdate")),
                        question.getInt("usefulcount"),
                        question.getInt("visible"),
                        question.getInt("userid")
                ));
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        } catch (ExecutionException ee) {
            ee.printStackTrace();
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return searchResults;
    }

    public ArrayList<Question> searchByBody (String qBody) {
        ArrayList<Question> searchResults = new ArrayList<>();
        try {
            PerformNetworkRequest getQuestion = new PerformNetworkRequest(Api.URL_GET_QUESTIONS_BY_BODY + qBody, null, Api.CODE_GET_REQUEST);
            getQuestion.execute();

            String gq = getQuestion.get();
            JSONObject response = new JSONObject(gq);
            if (!response.getBoolean("error")) {
                JSONArray questionArray = response.getJSONArray("question");

                for (int i = 0; i < questionArray.length(); i++) {
                    JSONObject question = questionArray.getJSONObject(i);

                    searchResults.add(new Question(
                            question.getInt("questionid"),
                            question.getString("questionbody"),
                            question.getString("questioncategory"),
                            question.getString("questiontype"),
                            Date.valueOf(question.getString("datecreated")),
                            Date.valueOf(question.getString("expirationdate")),
                            question.getInt("usefulcount"),
                            question.getInt("visible"),
                            question.getInt("userid")
                    ));
                }
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        } catch (ExecutionException ee) {
            ee.printStackTrace();
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return searchResults;
    }

    public ArrayList<Question> searchByCategory (String qCategory) {
        ArrayList<Question> searchResults = new ArrayList<>();
        try {
            PerformNetworkRequest getQuestion = new PerformNetworkRequest(Api.URL_GET_QUESTIONS_BY_CATEGORY + qCategory, null, Api.CODE_GET_REQUEST);
            getQuestion.execute();

            String gq = getQuestion.get();
            JSONObject response = new JSONObject(gq);
            if (!response.getBoolean("error")) {
                JSONArray questionArray = response.getJSONArray("question");

                for (int i = 0; i < questionArray.length(); i++) {
                    JSONObject question = questionArray.getJSONObject(i);

                    searchResults.add(new Question(
                            question.getInt("questionid"),
                            question.getString("questionbody"),
                            question.getString("questioncategory"),
                            question.getString("questiontype"),
                            Date.valueOf(question.getString("datecreated")),
                            Date.valueOf(question.getString("expirationdate")),
                            question.getInt("usefulcount"),
                            question.getInt("visible"),
                            question.getInt("userid")
                    ));
                }
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        } catch (ExecutionException ee) {
            ee.printStackTrace();
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return searchResults;
    }

    public ArrayList<Question> searchByType (String qType) {
        ArrayList<Question> searchResults = new ArrayList<>();
        try {
            PerformNetworkRequest getQuestion = new PerformNetworkRequest(Api.URL_GET_QUESTIONS_BY_TYPE + qType, null, Api.CODE_GET_REQUEST);
            getQuestion.execute();

            String gq = getQuestion.get();
            JSONObject response = new JSONObject(gq);
            if (!response.getBoolean("error")) {
                JSONArray questionArray = response.getJSONArray("question");

                for (int i = 0; i < questionArray.length(); i++) {
                    JSONObject question = questionArray.getJSONObject(i);

                    searchResults.add(new Question(
                            question.getInt("questionid"),
                            question.getString("questionbody"),
                            question.getString("questioncategory"),
                            question.getString("questiontype"),
                            Date.valueOf(question.getString("datecreated")),
                            Date.valueOf(question.getString("expirationdate")),
                            question.getInt("usefulcount"),
                            question.getInt("visible"),
                            question.getInt("userid")
                    ));
                }
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        } catch (ExecutionException ee) {
            ee.printStackTrace();
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return searchResults;
    }

    public ArrayList<Question> searchByUsefulCount (String usefulCount) {
        ArrayList<Question> searchResults = new ArrayList<>();
        try {
            PerformNetworkRequest getQuestion = new PerformNetworkRequest(Api.URL_GET_QUESTIONS_BY_USEFUL_COUNT + usefulCount, null, Api.CODE_GET_REQUEST);
            getQuestion.execute();

            String gq = getQuestion.get();
            JSONObject response = new JSONObject(gq);
            if (!response.getBoolean("error")) {
                JSONArray questionArray = response.getJSONArray("question");

                for (int i = 0; i < questionArray.length(); i++) {
                    JSONObject question = questionArray.getJSONObject(i);

                    searchResults.add(new Question(
                            question.getInt("questionid"),
                            question.getString("questionbody"),
                            question.getString("questioncategory"),
                            question.getString("questiontype"),
                            Date.valueOf(question.getString("datecreated")),
                            Date.valueOf(question.getString("expirationdate")),
                            question.getInt("usefulcount"),
                            question.getInt("visible"),
                            question.getInt("userid")
                    ));
                }
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        } catch (ExecutionException ee) {
            ee.printStackTrace();
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return searchResults;
    }
}
