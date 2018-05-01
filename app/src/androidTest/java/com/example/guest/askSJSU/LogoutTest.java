package com.example.guest.askSJSU;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
//Black-Box
public class LogoutTest {

    public static String REAL_USER = "testuser";
    public static String REAL_PW = "testpass";

    @Rule
    public ActivityTestRule<InitialLoginActivity> Activity = new ActivityTestRule<>(InitialLoginActivity.class);

    @Test
    public void TestLogout() {
        Espresso.onView(withId(R.id.usernameEditText)).perform(typeText(REAL_USER));
        Espresso.onView(withId(R.id.pwEditText)).perform(typeText(REAL_PW));
        Espresso.onView(withId(R.id.pwEditText)).perform(closeSoftKeyboard());
        Espresso.onView(withId(R.id.loginBtn)).perform(click());
        Intents.init();
        Espresso.onView(withId(R.id.logoutHomeBtn)).perform(click());
        intended(hasComponent(InitialLoginActivity.class.getName()));
        Intents.release();
    }
}
