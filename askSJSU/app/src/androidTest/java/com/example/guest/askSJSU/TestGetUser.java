package com.example.guest.askSJSU;

import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.json.JSONArray;
import org.junit.Assert;
import org.junit.Test;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.runner.RunWith;
import java.util.concurrent.ExecutionException;


@RunWith(AndroidJUnit4.class)
@SmallTest
//White-Box
public class TestGetUser {

    //Enter valid username to search for
    public static String USER = "";
    public static String userID = "";


    @Test
    public void testGetUserByUsername() throws ExecutionException, InterruptedException, JSONException {
        PerformNetworkRequest request = new PerformNetworkRequest(
                Api.URL_GET_USER_BY_USERNAME + USER, null, Api.CODE_GET_REQUEST);
        request.execute();

        String s = request.get();
        JSONObject response = new JSONObject(s);
        Assert.assertFalse(response.getBoolean("error"));
    }

    @Test
    public void testGetUserByUserID() throws ExecutionException, InterruptedException, JSONException {
        PerformNetworkRequest request = new PerformNetworkRequest(
                Api.URL_GET_USER_BY_USERID + userID, null, Api.CODE_GET_REQUEST);
        request.execute();

        String s = request.get();
        JSONObject response = new JSONObject(s);
        JSONArray jsonArray = response.getJSONArray("userdata");
        JSONObject jsonObject = jsonArray.getJSONObject(0);

        Assert.assertFalse(jsonObject.getString("username") == "");

    }
}