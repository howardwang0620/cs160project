package com.example.guest.askSJSU;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
//Black-Box
public class RatingQuestionTest {

    public static String REAL_USER = "testuser";
    public static String REAL_PW = "testpass";
    //Enter Question Body (QBODY)
    public static String QBODY = "";


    @Rule
    public IntentsTestRule<InitialLoginActivity> Activity = new IntentsTestRule<>(InitialLoginActivity.class);

    @Test
    public void ViewRatingQuestion() {
        Espresso.onView(withId(R.id.usernameEditText)).perform(typeText(REAL_USER));
        Espresso.onView(withId(R.id.pwEditText)).perform(typeText(REAL_PW));
        Espresso.onView(withId(R.id.pwEditText)).perform(closeSoftKeyboard());
        Espresso.onView(withId(R.id.loginBtn)).perform(click());
        Espresso.onView(withId(R.id.createBtn)).perform(click());
        Espresso.onView(withId(R.id.questionTypeSpinner)).perform(click());
        Espresso.onData(allOf(is(instanceOf(String.class)), is("Rating"))).perform(click());
        Espresso.onView(withId(R.id.questionBodyEditText)).perform(typeText(QBODY));
        Espresso.onView(withId(R.id.questionExpirationDateEditText)).perform(typeText("2018-08-24 01:32:36"));
        Espresso.onView(withId(R.id.questionExpirationDateEditText)).perform(closeSoftKeyboard());
        Espresso.onView(withId(R.id.createPostBtn)).perform(click());
        Espresso.onView(withText(QBODY)).perform(click());
        intended(hasComponent(VoteActivity.class.getName()));
    }
}

