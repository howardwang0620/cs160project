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
public class TestUpdateUser {

    //Enter new Username, valid email, password, and SJSU ID for new account.
    public static String USERID = "0";
    public static String NEWUSERNAME = "0";
    public static String NEWPASS = "0";
    public static String NEWEMAIL = "0";
    public static String NEWVERIFIED = "0";
    public static String NEWSJSUID = "999";

    @Test
    public void testUpdateUser() throws ExecutionException, InterruptedException, JSONException {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("userid", USERID);
        params.put("username", NEWUSERNAME);
        params.put("userpassword", NEWPASS);
        params.put("useremail", NEWEMAIL);
        params.put("verified", NEWVERIFIED);
        params.put("sjsuid", NEWSJSUID);

        PerformNetworkRequest request = new PerformNetworkRequest(
                Api.URL_UPDATE_USER, params, Api.CODE_POST_REQUEST);
        request.execute();

        String s = request.get();

        Log.d("response", s);

        JSONObject response = new JSONObject(s);
        Assert.assertTrue("Response should have userdata", response.has("userdata"));

        if (response.has("userdata")) {
            JSONArray userdata = response.getJSONArray("userdata");
            JSONObject user = userdata.getJSONObject(0);

            Assert.assertTrue("New username should equal username", NEWUSERNAME.equals(user.getString("username")));
            Assert.assertTrue("New password should equal userpassword", NEWPASS.equals(user.getString("userpassword")));
            Assert.assertTrue("New email should equal useremail", NEWEMAIL.equals(user.getString("useremail")));
            Assert.assertTrue("New verified should equal verified", NEWVERIFIED.equals(user.getString("verified")));
            Assert.assertTrue("New SJSU ID should equal sjsuid", NEWSJSUID.equals(user.getString("sjsuid")));
        }
    }
}
