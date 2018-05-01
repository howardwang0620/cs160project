package com.example.guest.askSJSU;

import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.concurrent.ExecutionException;


@RunWith(AndroidJUnit4.class)
@SmallTest
//White-Box
public class TestGetRecentQuestions {

    @Test
    public void testGetRecentQs() throws ExecutionException, InterruptedException, JSONException {
        PerformNetworkRequest request = new PerformNetworkRequest(
                Api.URL_GET_RECENT_QUESTIONS, null, Api.CODE_GET_REQUEST);
        request.execute();
        String s = request.get();
        JSONObject response = new JSONObject(s);
        //Log.d("SIZE OF ARRAY", response.getJSONArray("question") + "");
        assert (response.getJSONArray("question").length() == 5);
    }
}