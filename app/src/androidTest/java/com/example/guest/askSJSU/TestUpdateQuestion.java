package com.example.guest.askSJSU;

import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Assert;

import java.util.LinkedHashMap;
import java.util.concurrent.ExecutionException;

@RunWith(AndroidJUnit4.class)
@SmallTest
//White-Box
public class TestUpdateQuestion {

    //Enter valid question ID and question body for new question.
    public static String Q_ID = "";
    public static String NEWBODY = "";


    @Test
    public void testUpdateUser() throws ExecutionException, InterruptedException, JSONException {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("questionid", Q_ID);
        params.put("questionbody", NEWBODY);

        PerformNetworkRequest request = new PerformNetworkRequest(
                Api.URL_UPDATE_QUESTION_BODY, params, Api.CODE_POST_REQUEST);
        request.execute();

        String s = request.get();

        JSONObject response = new JSONObject(s);
        Assert.assertTrue("Response should have question", response.has("question"));

        if (response.has("question")) {
            JSONObject question = response.getJSONObject("question");

            Assert.assertTrue(NEWBODY.equals(question.getString("questionbody")));
        }
    }
}