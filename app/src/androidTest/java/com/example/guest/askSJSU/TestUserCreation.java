package com.example.guest.askSJSU;

import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

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
public class TestUserCreation {

    //Enter new Username, valid email, password, and SJSU ID for new account.
    public static String USER = "";
    public static String EMAIL = "";
    public static String PW = "";
    public static String ID = "";

    @Rule
    public ActivityTestRule<InitialLoginActivity> Activity = new ActivityTestRule<>(InitialLoginActivity.class);

    @Test
    public void testUserCreation() throws ExecutionException, InterruptedException, JSONException {
        onView(withId(R.id.regBtn)).perform(click());
        onView(withId(R.id.usernameRegEditText)).perform(typeText(USER));
        onView(withId(R.id.emailRegEditText)).perform(typeText(EMAIL));
        onView(withId(R.id.pwRegEditText)).perform(typeText(PW));
        onView(withId(R.id.sjsuIDEditText)).perform(typeText(ID));
        onView(withId(R.id.sjsuIDEditText)).perform(closeSoftKeyboard());
        onView(withId(R.id.regBtn)).perform(click());
        PerformNetworkRequest request = new PerformNetworkRequest(
                Api.URL_GET_USER_BY_USERNAME + USER, null, Api.CODE_GET_REQUEST);
        request.execute();
        
        String s = request.get();
        JSONObject response = new JSONObject(s);
        Assert.assertFalse(response.getBoolean("error")); //Assert False to show that there is no error.
    }
}
