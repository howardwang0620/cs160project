package com.example.guest.askSJSU;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
//Black-Box
public class InvalidPollingQTest {

    public static String REAL_USER = "testuser";
    public static String REAL_PW = "testpass";
    public static String QUESTION = "question?";

    @Rule
    public ActivityTestRule<InitialLoginActivity> Activity = new ActivityTestRule<>(InitialLoginActivity.class);

    @Test
    public void TestEmptyQuestion() {
        Espresso.onView(withId(R.id.usernameEditText)).perform(typeText(REAL_USER));
        Espresso.onView(withId(R.id.pwEditText)).perform(typeText(REAL_PW));
        Espresso.onView(withId(R.id.pwEditText)).perform(closeSoftKeyboard());
        Espresso.onView(withId(R.id.loginBtn)).perform(click());
        Espresso.onView(withId(R.id.createBtn)).perform(click());
        Espresso.onView(withId(R.id.questionBodyEditText)).perform(closeSoftKeyboard());
        Espresso.onView(withId(R.id.createPostBtn)).perform(click());
        Espresso.onView(withId(R.id.questionBodyEditText)).check(matches(withHint("Add a Question Body")));
    }

    @Test
    public void TestEmptyQuestionOption() {
        Espresso.onView(withId(R.id.usernameEditText)).perform(typeText(REAL_USER));
        Espresso.onView(withId(R.id.pwEditText)).perform(typeText(REAL_PW));
        Espresso.onView(withId(R.id.pwEditText)).perform(closeSoftKeyboard());
        Espresso.onView(withId(R.id.loginBtn)).perform(click());
        Espresso.onView(withId(R.id.createBtn)).perform(click());
        Espresso.onView(withId(R.id.questionBodyEditText)).perform(typeText(QUESTION));
        Espresso.onView(withId(R.id.questionBodyEditText)).perform(closeSoftKeyboard());
        Espresso.onView(withId(R.id.createPostBtn)).perform(click());
        Espresso.onView(withId(R.id.questionOptionNameEditText)).check(matches(withHint("Not Enough Options")));
    }
}