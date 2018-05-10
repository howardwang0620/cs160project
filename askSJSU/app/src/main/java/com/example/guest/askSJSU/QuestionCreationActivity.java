package com.example.guest.askSJSU;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.DownloadManager;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Locale;

public class QuestionCreationActivity extends AppCompatActivity {

    private TextView questionBody;
    private EditText questionExpirationDate;
    private Spinner questionCategorySpinner;
    private Spinner questionTypeSpinner;
    private Spinner questionPointScaleSpinner;
    private ListView questionOptionEditorListView;
    private TextView questionOptionName;
    private Button addQuestionOption;
    private QuestionOptionEditorAdapter adapter;

    private final String POLLING = "Polling";
    private final String RATING = "Rating";
    private final String FIVEPOINT = "5-Point Scale";
    private final String TENPOINT = "10-Point Scale";

    private int userID;
    private String questionCategory;
    private String questionType;
    private String pointScale;
    private ArrayList<String> questionOptions;

    private long lastClickTime;

    Calendar calendar = Calendar.getInstance();
    private DatePickerDialog dp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_creation);

        Bundle extras = this.getIntent().getExtras();
        userID = extras.getInt("USERID", 0);

        questionBody = (TextView) findViewById(R.id.questionBodyEditText);
        questionExpirationDate = (EditText) findViewById(R.id.questionExpirationDateEditText);
        questionCategorySpinner = (Spinner) findViewById(R.id.questionCategorySpinner);
        questionTypeSpinner = (Spinner) findViewById(R.id.questionTypeSpinner);
        questionPointScaleSpinner = (Spinner) findViewById(R.id.questionPointScaleSpinner);
        questionOptionName = (TextView) findViewById(R.id.questionOptionNameEditText);
        addQuestionOption = (Button) findViewById(R.id.questionOptionAddButton);
        questionOptionEditorListView = (ListView) findViewById(R.id.questionOptionsListView);

        questionCategory = questionCategorySpinner.getSelectedItem().toString();
        questionType = questionTypeSpinner.getSelectedItem().toString();
        pointScale = questionPointScaleSpinner.getSelectedItem().toString();

        questionOptions = new ArrayList<>();

        questionCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                questionCategory = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        questionTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                questionType = parent.getItemAtPosition(position).toString();
                if (questionType.equals(POLLING)) {
                    questionOptionName.setVisibility(View.VISIBLE);
                    addQuestionOption.setVisibility(View.VISIBLE);
                    questionOptionEditorListView.setVisibility(View.VISIBLE);
                    questionExpirationDate.setVisibility(View.INVISIBLE);
                    questionPointScaleSpinner.setVisibility(View.INVISIBLE);
                }
                if (questionType.equals(RATING)) {
                    questionOptionName.setVisibility(View.INVISIBLE);
                    addQuestionOption.setVisibility(View.INVISIBLE);
                    questionOptionEditorListView.setVisibility(View.INVISIBLE);
                    questionExpirationDate.setVisibility(View.VISIBLE);
                    questionPointScaleSpinner.setVisibility(View.VISIBLE);
                }
                else {
                    questionOptionName.setVisibility(View.VISIBLE);
                    addQuestionOption.setVisibility(View.VISIBLE);
                    questionOptionEditorListView.setVisibility(View.VISIBLE);
                    questionExpirationDate.setVisibility(View.INVISIBLE);
                    questionPointScaleSpinner.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        questionPointScaleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pointScale = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        questionExpirationDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dp = new DatePickerDialog(QuestionCreationActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                dp.getDatePicker().setMinDate(calendar.getTimeInMillis());
                dp.show();
            }
        });

        adapter = new QuestionOptionEditorAdapter(QuestionCreationActivity.this, questionOptions);
        questionOptionEditorListView.setAdapter(adapter);
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private void updateLabel() { //enters selected date into expiration date text field
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        questionExpirationDate.setText(sdf.format(calendar.getTime()));
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

    public void questionOptionAddButton(View view) {
        if (SystemClock.elapsedRealtime() - lastClickTime < 500) {
            return;
        }
        lastClickTime = SystemClock.elapsedRealtime();

        String optionName = questionOptionName.getText().toString().trim();
        if (!optionName.isEmpty()) {
            questionOptions.add(optionName);
            adapter.notifyDataSetChanged();
            questionOptionName.setText("");
            questionOptionName.setHint(R.string.option_name);
            questionOptionName.setHintTextColor(Color.parseColor("#a8a8a8"));
        } else {
            questionOptionName.setText("");
            questionOptionName.setHint("Enter an Option");
            questionOptionName.setHintTextColor(Color.parseColor("#f66c6c"));
        }
    }

    public void createPostButton(View view) {
        if (SystemClock.elapsedRealtime() - lastClickTime < 500) {
            return;
        }
        lastClickTime = SystemClock.elapsedRealtime();

        String body = questionBody.getText().toString().trim();
        String expDate = questionExpirationDate.getText().toString().trim();
        questionCategory = questionCategorySpinner.getSelectedItem().toString();
        questionType = questionTypeSpinner.getSelectedItem().toString();
        pointScale = questionPointScaleSpinner.getSelectedItem().toString();

        if (!body.isEmpty()) {
            if (questionType.equals(POLLING)) {
                if (questionOptions.size() > 1) {
                    createPollingQuestion(body, questionCategory, userID, questionOptions);

                    finish();

                } else {
                    questionOptionName.setText("");
                    questionOptionName.setHint("Not Enough Options");
                    questionOptionName.setHintTextColor(Color.parseColor("#f66c6c"));
                }

            }
            if (questionType.equals(RATING)) {
                if (!expDate.isEmpty()) {
                    createRatingQuestion(body, questionCategory, expDate, userID, pointScale);

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

    public Question createPollingQuestion(String qBody, String qCategory, int uid, ArrayList<String> qOptions) {
        Question q = new Question();
        try {
            LinkedHashMap<String, String> questionParams = new LinkedHashMap<>();
            questionParams.put("questionbody", qBody);
            questionParams.put("questioncategory", qCategory);
            questionParams.put("questiontype", "Polling");
            questionParams.put("expirationdate", "2999-01-01");
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
                    question.getString("questioncategory"),
                    question.getString("questiontype"),
                    Date.valueOf(question.getString("datecreated")),
                    Date.valueOf(question.getString("expirationdate")),
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

    public Question createRatingQuestion (String qBody, String qCategory, String expDate, int uid, String scale) {
        Question q = new Question();
        try {
            LinkedHashMap<String, String> questionParams = new LinkedHashMap<>();
            questionParams.put("questionbody", qBody);
            questionParams.put("questioncategory", qCategory);
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
                    question.getString("questioncategory"),
                    question.getString("questiontype"),
                    Date.valueOf(question.getString("datecreated")),
                    Date.valueOf(question.getString("expirationdate")),
                    question.getInt("usefulcount"),
                    question.getInt("visible"),
                    question.getInt("userid")
            );

            String qid = question.getString("questionid");

            int s = 0;
            if (scale.equals(FIVEPOINT)) { s = 5;}
            if (scale.equals(TENPOINT)) { s = 10; }

            for (int i = s; i > 0; i--) {
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
