package com.example.guest.askSJSU;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

//Contains utility functions for application
public class Utilities {


    @Test
    public static JSONArray getUserByUserId(int userID) throws ExecutionException, InterruptedException, JSONException {
        PerformNetworkRequest request = new PerformNetworkRequest(
                Api.URL_GET_USER_BY_USERID + userID, null, Api.CODE_GET_REQUEST);
        request.execute();

        String s = request.get();
        JSONObject response = new JSONObject(s);
        JSONArray jsonArray = response.getJSONArray("userdata");
        return jsonArray;
    }
}
