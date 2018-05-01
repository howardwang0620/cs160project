package com.example.guest.askSJSU;

import android.app.DownloadManager;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class QuestionCreationActivity extends AppCompatActivity {

    private TextView questionBody;
    private TextView questionExpirationDate;
    private Spinner questionTypeSpinner;
    private Spinner questionPointScaleSpinner;
    private ListView questionOptionEditorListView;
    private TextView questionOptionName;
    private Button addQuestionOption;
    private QuestionOptionEditorAdapter adapter;

    private final int POLLING = 0;
    private final int RATING = 1;
    private final int FIVEPOINT = 0;
    private final int TENPOINT = 1;

    private int userID;
    private int questionType;
    private int pointScale;
    private ArrayList<String> questionOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_creation);

        Bundle extras = this.getIntent().getExtras();
        userID = extras.getInt("USERID", 0);

        questionType = POLLING;
        pointScale = 5;
        questionOptions = new ArrayList<>();

        questionBody = (TextView) findViewById(R.id.questionBodyEditText);
        questionExpirationDate = (TextView) findViewById(R.id.questionExpirationDateEditText);
        questionTypeSpinner = (Spinner) findViewById(R.id.questionTypeSpinner);
        questionPointScaleSpinner = (Spinner) findViewById(R.id.questionPointScaleSpinner);
        questionOptionName = (TextView) findViewById(R.id.questionOptionNameEditText);
        addQuestionOption = (Button) findViewById(R.id.questionOptionAddButton);
        questionOptionEditorListView = (ListView) findViewById(R.id.questionOptionsListView);

        questionTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == POLLING) {
                    questionOptionName.setVisibility(View.VISIBLE);
                    addQuestionOption.setVisibility(View.VISIBLE);
                    questionOptionEditorListView.setVisibility(View.VISIBLE);
                    questionExpirationDate.setVisibility(View.GONE);
                    questionPointScaleSpinner.setVisibility(View.GONE);
                    questionType = POLLING;
                }
                if (position == RATING) {
                    questionOptionName.setVisibility(View.GONE);
                    addQuestionOption.setVisibility(View.GONE);
                    questionOptionEditorListView.setVisibility(View.GONE);
                    questionExpirationDate.setVisibility(View.VISIBLE);
                    questionPointScaleSpinner.setVisibility(View.VISIBLE);
                    questionType = RATING;
                }
                else {
                    questionOptionName.setVisibility(View.VISIBLE);
                    addQuestionOption.setVisibility(View.VISIBLE);
                    questionOptionEditorListView.setVisibility(View.VISIBLE);
                    questionExpirationDate.setVisibility(View.GONE);
                    questionPointScaleSpinner.setVisibility(View.GONE);
                    questionType = POLLING;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        questionPointScaleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == FIVEPOINT) {
                    pointScale = 5;
                }
                if (position == TENPOINT) {
                    pointScale = 10;
                }
                else {
                    pointScale = 5;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapter = new QuestionOptionEditorAdapter(QuestionCreationActivity.this, questionOptions);
        questionOptionEditorListView.setAdapter(adapter);
    }

    public void questionOptionAddButton(View view) {
        String optionName = questionOptionName.getText().toString().trim();
        questionOptions.add(optionName);
        adapter.notifyDataSetChanged();
        questionOptionName.setText("");
        questionOptionName.setHint(R.string.option_name);
        questionOptionName.setHintTextColor(Color.parseColor("#a8a8a8"));
    }

    public void createPostButton(View view) {
        String body = questionBody.getText().toString().trim();
        String expDate = questionExpirationDate.getText().toString().trim();

        if (!body.isEmpty()) {
            if (questionType == POLLING) {
                if (questionOptions.size() > 1) {
                    createPollingQuestion(body, userID, questionOptions);

                    finish();

                } else {
                    questionOptionName.setText("");
                    questionOptionName.setHint("Not Enough Options");
                    questionOptionName.setHintTextColor(Color.parseColor("#f66c6c"));
                }

            }
            if (questionType == RATING) {
                if (!expDate.isEmpty()) {
                    createRatingQuestion(body, expDate, userID, pointScale);

                    finish();

                } else {
                    questionExpirationDate.setText("");
                    questionExpirationDate.setHint("Add an Expiration Date");
                    questionExpirationDate.setHintTextColor(Color.parseColor("#f66c6c"));
                }
            }
        } else {
            questionBody.setText("");
            questionBody.setHint("Add a Question Body");
            questionBody.setHintTextColor(Color.parseColor("#f66c6c"));
        }
    }

    public void cancelPostButton(View view) {
        finish();
    }

    public Question createPollingQuestion(String qBody, int uid, ArrayList<String> qOptions) {
        Question q = new Question();
        try {
            LinkedHashMap<String, String> questionParams = new LinkedHashMap<>();
            questionParams.put("questionbody", qBody);
            questionParams.put("questiontype", "Polling");
            questionParams.put("expirationdate", "1970-01-01 00:00:01");
            questionParams.put("userid", String.valueOf(uid));

            PerformNetworkRequest createQuestion = new PerformNetworkRequest(
                    Api.URL_CREATE_QUESTION, questionParams, Api.CODE_POST_REQUEST);
            createQuestion.execute();

            String cq = createQuestion.get();
            JSONObject response = new JSONObject(cq);
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

            String qid = question.getString("questionid");

            for (int i = 0; i < qOptions.size(); i++) {
                LinkedHashMap<String, String> questionOptionParams = new LinkedHashMap<>();
                questionOptionParams.put("optionname", qOptions.get(i));
                questionOptionParams.put("questionid", qid);
                PerformNetworkRequest createQuestionOption = new PerformNetworkRequest(
                        Api.URL_CREATE_QUESTION_OPTION_ID, questionOptionParams,
                        Api.CODE_POST_REQUEST);
                createQuestionOption.execute();
            }
        } catch (java.lang.InterruptedException ie) {
            ie.printStackTrace();
        } catch (java.util.concurrent.ExecutionException ee) {
            ee.printStackTrace();
        } catch (org.json.JSONException je) {
            je.printStackTrace();
        }
        return q;
    }

    public Question createRatingQuestion (String qBody, String expDate, int uid, int scale) {
        Question q = new Question();
        try {
            LinkedHashMap<String, String> questionParams = new LinkedHashMap<>();
            questionParams.put("questionbody", qBody);
            questionParams.put("questiontype", "Rating");
            questionParams.put("expirationdate", expDate);
            questionParams.put("userid", String.valueOf(uid));

            PerformNetworkRequest createQuestion = new PerformNetworkRequest(
                    Api.URL_CREATE_QUESTION, questionParams, Api.CODE_POST_REQUEST);
            createQuestion.execute();

            String cq = createQuestion.get();
            JSONObject response = new JSONObject(cq);
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

            String qid = question.getString("questionid");

            for (int i = 1; i <= scale; i++) {
                LinkedHashMap<String, String> questionOptionParams = new LinkedHashMap<>();
                questionOptionParams.put("optionname", String.valueOf(i));
                questionOptionParams.put("questionid", qid);
                PerformNetworkRequest createQuestionOption = new PerformNetworkRequest(
                        Api.URL_CREATE_QUESTION_OPTION_ID, questionOptionParams,
                        Api.CODE_POST_REQUEST);
                createQuestionOption.execute();
            }
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
