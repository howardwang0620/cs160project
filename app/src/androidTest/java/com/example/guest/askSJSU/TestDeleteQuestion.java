package com.example.guest.askSJSU;

import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.ExecutionException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@SmallTest
//White-Box
public class TestDeleteQuestion {

    //Enter valid question ID to delete
    public static String Q_ID = "";

    @Test
    public void testUserDeletion() throws ExecutionException, InterruptedException, JSONException {
        PerformNetworkRequest request = new PerformNetworkRequest(
                Api.URL_GET_ALL_QUESTIONS, null, Api.CODE_GET_REQUEST);
        request.execute();
        String s = request.get();
        JSONObject response = new JSONObject(s);
        int lengthBefore = response.getJSONArray("question").length();


        request = new PerformNetworkRequest(
                Api.URL_DELETE_QUESTION + Q_ID, null, Api.CODE_GET_REQUEST);
        request.execute();

        request = new PerformNetworkRequest(Api.URL_GET_ALL_QUESTIONS, null, Api.CODE_GET_REQUEST);
        request.execute();
        s = request.get();
        response = new JSONObject(s);
        int lengthAfter = response.getJSONArray("question").length();

        Assert.assertTrue(lengthBefore - 1 == lengthAfter);
    }
}