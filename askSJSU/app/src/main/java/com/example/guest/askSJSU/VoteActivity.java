package com.example.guest.askSJSU;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.sql.*;
import java.util.LinkedHashMap;

import org.json.*;

import android.util.Log;

public class VoteActivity extends AppCompatActivity {

    private TextView questionIDTextView;
    private TextView questionCategoryTextView;
    private TextView questionTypeTextView;
    private TextView questionBodyTextView;
    private TextView averageRatingTextView;
    private TextView dateCreatedTextView;
    private TextView expirationDateTextView;
    private TextView usefulCountTextView;
    private TextView upvoteClickTextView;
    private TextView downvoteClickTextView;

    private ListView optionsListView;
    private QuestionOptionAdapter adapter;

    private final int VOTED = 0;
    private final int UPVOTED = 1;

    private int userID;
    private int questionID;
    private String questionType;
    private ArrayList<QuestionOption> questionOptions;
    private double averageRating;

    private int hasVoted;
    private int positionVoted;
    private int hasUpvoted;

    private long upvoteLastClickTime = 0;
    private long voteLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        Bundle extras = this.getIntent().getExtras();
        userID = extras.getInt("USERID", 0);
        questionID = extras.getInt("QUESTIONID", 0);

        questionIDTextView = (TextView) findViewById(R.id.questionIDTextView);
        questionCategoryTextView = (TextView) findViewById(R.id.questionCategoryTextView);
        questionTypeTextView = (TextView) findViewById(R.id.questionTypeTextView);
        questionBodyTextView = (TextView) findViewById(R.id.questionBodyTextView);
        averageRatingTextView = (TextView) findViewById(R.id.averageRatingTextView);
        dateCreatedTextView = (TextView) findViewById(R.id.dateCreatedTextView);
        expirationDateTextView = (TextView) findViewById(R.id.expirationDateTextView);
        usefulCountTextView = (TextView) findViewById(R.id.usefulCountTextView);
        upvoteClickTextView = (TextView) findViewById(R.id.upvoteClickTextView);
        upvoteClickTextView.setVisibility(View.VISIBLE);
        downvoteClickTextView = (TextView) findViewById(R.id.downvoteClickTextView);
        downvoteClickTextView.setVisibility(View.VISIBLE);
        optionsListView = (ListView) findViewById(R.id.optionsListView);

        int[] voteHistArray = getVoteHistory(userID, questionID);
        hasVoted = voteHistArray[VOTED];
        positionVoted = -1;
        hasUpvoted = voteHistArray[UPVOTED];

        Question question = readQuestion(questionID);
        questionOptions = getOptionList(questionID);
        questionType = question.getQuestionType();

        adapter = new QuestionOptionAdapter(VoteActivity.this, questionOptions);
        optionsListView.setAdapter(adapter);

        if (questionType.equals("Rating")) {
            averageRating = getAverageRating(questionOptions);
            averageRatingTextView.setText(getResources().getString(R.string.average_rating, averageRating));
            averageRatingTextView.setVisibility(View.VISIBLE);
        }

        optionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (SystemClock.elapsedRealtime() - voteLastClickTime < 500) {
                    return;
                }
                voteLastClickTime = SystemClock.elapsedRealtime();

                TextView optionNameTextView = (TextView) view.findViewById(R.id.optionNameTextView);
                TextView voteCountTextView = (TextView) view.findViewById(R.id.optionVoteCountTextView);

                if (hasVoted == 0 && positionVoted == -1) {
                    QuestionOption option = adapter.getItem(position);
                    option.vote();
                    adapter.notifyDataSetChanged();

                    optionNameTextView.setTypeface(null, Typeface.BOLD);
                    voteCountTextView.setTypeface(null, Typeface.BOLD);
                    view.setBackgroundColor(Color.parseColor("#EAEAEA"));

                    int optionID = option.getOptionId();

                    vote(optionID, userID, questionID);

                    if (questionType.equals("Rating")) {
                        averageRating = getAverageRating(questionOptions);
                        averageRatingTextView.setText(getResources().getString(R.string.average_rating, averageRating));
                    }

                    hasVoted = 2;
                    positionVoted = position;

                } else if (hasVoted == 2 && positionVoted == position) {
                    QuestionOption option = adapter.getItem(position);
                    option.unvote();
                    adapter.notifyDataSetChanged();

                    optionNameTextView.setTypeface(null, Typeface.NORMAL);
                    voteCountTextView.setTypeface(null, Typeface.NORMAL);
                    view.setBackgroundColor(Color.parseColor("#fafafa"));

                    int optionID = option.getOptionId();

                    unvote(optionID, userID, questionID);

                    if (questionType.equals("Rating")) {
                        averageRating = getAverageRating(questionOptions);
                        averageRatingTextView.setText(getResources().getString(R.string.average_rating, averageRating));
                    }

                    hasVoted = 0;
                    positionVoted = -1;

                } else {
                    Toast.makeText(getApplicationContext(),
                            "You have already voted on this question",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        upvoteClickTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - upvoteLastClickTime < 500) {
                    return;
                }
                upvoteLastClickTime = SystemClock.elapsedRealtime();

                if (hasUpvoted == 0) {
                    upVote(questionID, userID);

                    upvoteClickTextView.setTypeface(null, Typeface.BOLD);
                    hasUpvoted = 2;
                } else if (hasUpvoted == 2) {
                    downVote(questionID, userID);

                    upvoteClickTextView.setTypeface(null, Typeface.NORMAL);
                    hasUpvoted = 0;
                } else {
                    Toast.makeText(getApplicationContext(),
                            "You have already voted on this question.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        downvoteClickTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - upvoteLastClickTime < 500) {
                    return;
                }
                upvoteLastClickTime = SystemClock.elapsedRealtime();

                if (hasUpvoted == 0) {
                    downVote(questionID, userID);

                    downvoteClickTextView.setTypeface(null, Typeface.BOLD);
                    hasUpvoted = -1;
                } else if (hasUpvoted == -1) {
                    upVote(questionID, userID);

                    downvoteClickTextView.setTypeface(null, Typeface.NORMAL);
                    hasUpvoted = 0;
                } else {
                    Toast.makeText(getApplicationContext(),
                            "You have already voted on this question.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        int[] voteHistArray = getVoteHistory(userID, questionID);
        hasVoted = voteHistArray[VOTED];
        hasUpvoted = voteHistArray[UPVOTED];

        Question question = readQuestion(questionID);
        questionOptions = getOptionList(questionID);

        adapter = new QuestionOptionAdapter(VoteActivity.this, questionOptions);
        optionsListView.setAdapter(adapter);

        if (question.getQuestionType().equals("Rating")) {
            averageRating = getAverageRating(questionOptions);
            averageRatingTextView.setText(getResources().getString(R.string.average_rating, averageRating));
            averageRatingTextView.setVisibility(View.VISIBLE);
        }
    }

    //toolbar back button press handler
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void vote (int oID, int uID, int qID) {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("optionid", String.valueOf(oID));
        PerformNetworkRequest vote = new PerformNetworkRequest(Api.URL_UPDATE_QUESTION_OPTION_VOTE, params, Api.CODE_POST_REQUEST);
        vote.execute();

        LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
        parameters.put("userid", String.valueOf(uID));
        parameters.put("questionid", String.valueOf(qID));
        PerformNetworkRequest updateVH = new PerformNetworkRequest(Api.URL_UPDATE_VOTE_HISTORY_HASVOTED, parameters, Api.CODE_POST_REQUEST);
        updateVH.execute();
    }

    public void unvote (int oID, int uID, int qID) {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("optionid", String.valueOf(oID));
        PerformNetworkRequest vote = new PerformNetworkRequest(Api.URL_UPDATE_QUESTION_OPTION_UNVOTE, params, Api.CODE_POST_REQUEST);
        vote.execute();

        LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
        parameters.put("userid", String.valueOf(uID));
        parameters.put("questionid", String.valueOf(qID));
        PerformNetworkRequest updateVH = new PerformNetworkRequest(Api.URL_UPDATE_VOTE_HISTORY_HASVOTED, parameters, Api.CODE_POST_REQUEST);
        updateVH.execute();
    }

    public double getAverageRating(ArrayList<QuestionOption> options) {
        double total = 0;
        double votes = 0;
        double avg = 0;

        for (int i = 0; i < options.size(); i++) {
            QuestionOption opt = options.get(i);
            int o = Integer.valueOf(opt.getOptionName());
            int v = opt.getVoteCount();
            //Log.d("o", String.valueOf(o));
            //Log.d("v", String.valueOf(v));
            votes += v;
            total += v * o;
            //Log.d("votes", String.valueOf(votes));
            //Log.d("total", String.valueOf(total));
        }

        if (votes > 0) {
            avg = total / votes;
        }
        //Log.d("avg", String.valueOf(avg));
        return avg;
    }

    public int[] getVoteHistory(int uID, int qID) {
        int[] votehist = new int[2];
        votehist[VOTED] = 0;
        votehist[UPVOTED] = 0;
        try {
            PerformNetworkRequest getVH =
                    new PerformNetworkRequest(Api.URL_GET_VOTE_HISTORY + "&userid=" + uID +
                            "&questionid=" + qID, null, Api.CODE_GET_REQUEST);
            getVH.execute();

            String vh = getVH.get();
            JSONObject response = new JSONObject(vh);
            if (!response.getBoolean("error")) {
                JSONObject votehistory = response.getJSONObject("votehistory");

                votehist[VOTED] = votehistory.getInt("hasvoted");
                votehist[UPVOTED] = votehistory.getInt("hasupvoted");

            } else {
                LinkedHashMap<String, String> params = new LinkedHashMap<>();
                params.put("userid", String.valueOf(uID));
                params.put("questionid", String.valueOf(qID));

                PerformNetworkRequest createVH = new PerformNetworkRequest(
                        Api.URL_CREATE_VOTE_HISTORY, params, Api.CODE_POST_REQUEST);
                createVH.execute();
            }
        } catch (java.lang.InterruptedException ie) {
            ie.printStackTrace();
        } catch (java.util.concurrent.ExecutionException ee) {
            ee.printStackTrace();
        } catch (org.json.JSONException je) {
            je.printStackTrace();
        }
        return votehist;
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
                    question.getString("questioncategory"),
                    question.getString("questiontype"),
                    Date.valueOf(question.getString("datecreated")),
                    Date.valueOf(question.getString("expirationdate")),
                    question.getInt("usefulcount"),
                    question.getInt("visible"),
                    question.getInt("userid")
            );

            questionIDTextView.setText(getResources().getString(R.string.question_id, q.getQuestionId()));
            questionBodyTextView.setText(q.getQuestionBody());
            questionCategoryTextView.setText(q.getQuestionCategory());
            questionTypeTextView.setText(q.getQuestionType());
            dateCreatedTextView.setText(getResources().getString(R.string.date_created, q.getDateCreated().toString()));
            if (q.getQuestionType().equals("Polling")) {
                expirationDateTextView.setVisibility(View.GONE);
            } else {
                expirationDateTextView.setText(getResources().getString(R.string.expiration_date, q.getExpirationDate().toString()));
            }
            usefulCountTextView.setText(getResources().getString(R.string.useful_count, q.getUsefulCount()));

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

    public Question upVote(int qID, int uID) {
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
                    question.getString("questioncategory"),
                    question.getString("questiontype"),
                    Date.valueOf(question.getString("datecreated")),
                    Date.valueOf(question.getString("expirationdate")),
                    question.getInt("usefulcount"),
                    question.getInt("visible"),
                    question.getInt("userid")
            );

            LinkedHashMap<String, String> upvoteparams = new LinkedHashMap<>();
            upvoteparams.put("userid", String.valueOf(uID));
            upvoteparams.put("questionid", String.valueOf(qID));
            PerformNetworkRequest updateUV = new PerformNetworkRequest(Api.URL_UPDATE_VOTE_HISTORY_HASUPVOTED, upvoteparams, Api.CODE_POST_REQUEST);
            updateUV.execute();

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

    public Question downVote(int qID, int uID) {
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
                    question.getString("questioncategory"),
                    question.getString("questiontype"),
                    Date.valueOf(question.getString("datecreated")),
                    Date.valueOf(question.getString("expirationdate")),
                    question.getInt("usefulcount"),
                    question.getInt("visible"),
                    question.getInt("userid")
            );

            LinkedHashMap<String, String> upvoteparams = new LinkedHashMap<>();
            upvoteparams.put("userid", String.valueOf(uID));
            upvoteparams.put("questionid", String.valueOf(qID));
            PerformNetworkRequest updateUV = new PerformNetworkRequest(Api.URL_UPDATE_VOTE_HISTORY_HASUPVOTED, upvoteparams, Api.CODE_POST_REQUEST);
            updateUV.execute();

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
