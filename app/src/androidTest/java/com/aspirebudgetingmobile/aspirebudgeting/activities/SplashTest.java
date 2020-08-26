package com.aspirebudgetingmobile.aspirebudgeting.activities;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.aspirebudgetingmobile.aspirebudgeting.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SplashTest {

    @Rule
    public ActivityTestRule<Splash> mActivityTestRule = new ActivityTestRule<>(Splash.class);

    @Test
    public void splashTest() {
        ViewInteraction relativeLayout = onView(
                allOf(withId(R.id.googleLoginRelativeLayout_login),
                        childAtPosition(
                                allOf(withId(R.id.googleLoginCard_login),
                                        childAtPosition(
                                                withId(R.id.loginElementsLayout),
                                                3)),
                                0),
                        isDisplayed()));
        relativeLayout.perform(click());

        ViewInteraction materialCardView = onView(
                allOf(withId(R.id.sheetsNameCard),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.sheetsListRecyclerView_LinkSheets),
                                        6),
                                0),
                        isDisplayed()));
        materialCardView.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
