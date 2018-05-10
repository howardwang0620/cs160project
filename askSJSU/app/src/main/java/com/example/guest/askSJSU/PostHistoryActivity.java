package com.example.guest.askSJSU;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;

public class PostHistoryActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<Question> questionList;
    private int userID;
    private long lastClickTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_history);

        Bundle extras = this.getIntent().getExtras();

        userID = extras.getInt("USERID", 0);
        listView = findViewById(R.id.postHistoryFeed);
        questionList = new ArrayList<>();
        readUserPosts(userID);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (SystemClock.elapsedRealtime() - lastClickTime < 500) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();

                Object obj = listView.getItemAtPosition(position);
                Question q = (Question) obj;
                int qID = q.getQuestionId();

                Intent intent = new Intent(PostHistoryActivity.this, VoteActivity.class);
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

    public void readUserPosts(int userID) {
        try {
            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_GET_QUESTIONS_BY_USER_ID + userID, null, Api.CODE_GET_REQUEST);
            request.execute();

            String s = request.get();
            JSONObject response = new JSONObject(s);

            if (response.has("question")) {
                refreshQuestionList(response.getJSONArray("question"));
            }
        } catch (java.lang.InterruptedException ie) {
            ie.printStackTrace();
        } catch (java.util.concurrent.ExecutionException ee) {
            ee.printStackTrace();
        } catch (org.json.JSONException je) {
            je.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        listView = findViewById(R.id.postHistoryFeed);
        readUserPosts(userID);
    }

    public void refreshQuestionList(JSONArray questions) throws org.json.JSONException {
        questionList.clear();

        for (int i = 0; i < questions.length(); i++) {
            JSONObject obj = questions.getJSONObject(i);

            questionList.add(new Question(
                    obj.getInt("questionid"),
                    obj.getString("questionbody"),
                    obj.getString("questioncategory"),
                    obj.getString("questiontype"),
                    Date.valueOf(obj.getString("datecreated")),
                    Date.valueOf(obj.getString("expirationdate")),
                    obj.getInt("usefulcount"),
                    obj.getInt("visible"),
                    obj.getInt("userid")
            ));
        }

        QuestionAdapter adapter = new QuestionAdapter(PostHistoryActivity.this, questionList);
        if(adapter != null) {
            Log.d("ADAPTER NOT NULL", "NOT NULL");
            listView.setAdapter(adapter);
        }
        else
            Log.d("ADAPTER NULL", adapter.toString());
    }
}
