package com.example.guest.askSJSU;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
//Black-Box
public class CreatePostMenuTest{

    public static String REAL_USER = "testuser";
    public static String REAL_PW = "testpass";

    @Rule
    public IntentsTestRule<InitialLoginActivity> Activity = new IntentsTestRule<>(InitialLoginActivity.class);

    @Test
    public void CreatePostPage() {
        Espresso.onView(withId(R.id.usernameEditText)).perform(typeText(REAL_USER));
        Espresso.onView(withId(R.id.pwEditText)).perform(typeText(REAL_PW));
        Espresso.onView(withId(R.id.pwEditText)).perform(closeSoftKeyboard());
        Espresso.onView(withId(R.id.loginBtn)).perform(click());
        Espresso.onView(withId(R.id.createBtn)).perform(click());
        intended(hasComponent(QuestionCreationActivity.class.getName()));
    }

    @Test
    public void ChangeToRatingQ() {
        Espresso.onView(withId(R.id.usernameEditText)).perform(typeText(REAL_USER));
        Espresso.onView(withId(R.id.pwEditText)).perform(typeText(REAL_PW));
        Espresso.onView(withId(R.id.pwEditText)).perform(closeSoftKeyboard());
        Espresso.onView(withId(R.id.loginBtn)).perform(click());
        Espresso.onView(withId(R.id.createBtn)).perform(click());
        Espresso.onView(withId(R.id.questionTypeSpinner)).perform(click());
        Espresso.onData(allOf(is(instanceOf(String.class)), is("Rating"))).perform(click());
        Espresso.onView(withId(R.id.questionOptionNameEditText)).check(matches(not(isDisplayed())));
        Espresso.onView(withId(R.id.questionOptionAddButton)).check(matches(not(isDisplayed())));
        Espresso.onView(withId(R.id.questionOptionsListView)).check(matches(not(isDisplayed())));
    }

    @Test
    public void CancelPost() {
        Espresso.onView(withId(R.id.usernameEditText)).perform(typeText(REAL_USER));
        Espresso.onView(withId(R.id.pwEditText)).perform(typeText(REAL_PW));
        Espresso.onView(withId(R.id.pwEditText)).perform(closeSoftKeyboard());
        Espresso.onView(withId(R.id.loginBtn)).perform(click());
        Espresso.onView(withId(R.id.createBtn)).perform(click());
        Espresso.onView(withId(R.id.questionOptionNameEditText)).perform(closeSoftKeyboard());
        Espresso.onView(withId(R.id.cancelPostBtn)).perform(click());
        intended(hasComponent(HomePageActivity.class.getName()));
    }
}
