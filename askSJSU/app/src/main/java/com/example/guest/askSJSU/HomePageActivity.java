package com.example.guest.askSJSU;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;

public class HomePageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView listView;

    private int userID;
    private ArrayList<Question> questionList;
    private long lastClickTime;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = this.getIntent().getExtras();
        userID = extras.getInt("USERID", 0);

        this.setTitle("askSJSU");

        setContentView(R.layout.activity_home_page_nav_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView userNameTxt = (TextView) headerView.findViewById(R.id.homePageUserNameTxtView);
        userNameTxt.setText("buffer");

        TextView emailTxt = (TextView) headerView.findViewById(R.id.homePageEmailTxtView);
        emailTxt.setText("buffer@email.com");

        try {
            JSONArray jsonArray = Utilities.getUserByUserId(userID);
            JSONObject userData = jsonArray.getJSONObject(0);
            userNameTxt.setText(userData.getString("username"));
            emailTxt.setText(userData.getString("useremail"));
        } catch (Exception e) {
            Log.d("Utilities Exception", "JSON Exception");
        }

        listView = findViewById(R.id.topQuestionsListView);
        questionList = new ArrayList<>();
        readQuestions();

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

                Intent intent = new Intent(HomePageActivity.this, VoteActivity.class);
                intent.putExtra("USERID", userID);
                intent.putExtra("QUESTIONID", qID);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home_page_nav_drawer, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Home) {
            // Handle the home action
        } else if (id == R.id.nav_myPosts) {
            Intent intent = new Intent(HomePageActivity.this, PostHistoryActivity.class);
            intent.putExtra("USERID", userID);
            startActivity(intent);
        } else if (id == R.id.nav_Settings) {
            Intent intent = new Intent(HomePageActivity.this, SettingsPageActivity.class);
            intent.putExtra("USERID", userID);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            // Handle the logout action
            logoutFunction();
        }

        item.setChecked(false);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();

        listView = findViewById(R.id.topQuestionsListView);
        readQuestions();
    }

    public void readQuestions() {
        try {
            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_GET_RECENT_QUESTIONS, null, Api.CODE_GET_REQUEST);
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

        QuestionAdapter adapter = new QuestionAdapter(HomePageActivity.this, questionList);
        listView.setAdapter(adapter);
    }

    public void createPostButton(View view) {
        Intent intent = new Intent(this, QuestionCreationActivity.class);
        intent.putExtra("USERID", userID);
        startActivity(intent);
    }

    public void searchPostsButton(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("USERID", userID);
        startActivity(intent);
    }

    public void logoutFunction() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, InitialLoginActivity.class);
        finish();
        startActivity(intent);
    }
}
