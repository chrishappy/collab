package com.themusicians.musiclms;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LanguageSwitchTest {

  @Rule
  public ActivityTestRule<MainActivity> mActivityTestRule =
      new ActivityTestRule<>(MainActivity.class);

  @Test
  public void userLanguageSwitchTest() {
    ViewInteraction bottomNavigationItemView =
        onView(
            allOf(
                withId(R.id.page_2),
                withContentDescription("User Profile"),
                childAtPosition(childAtPosition(withId(R.id.bottom_navigation), 0), 1),
                isDisplayed()));
    bottomNavigationItemView.perform(click());

    ViewInteraction appCompatButton2 =
        onView(
            allOf(
                withId(R.id.changeMyLang),
                withText("Language"),
                childAtPosition(
                    allOf(
                        withId(R.id.linearLayout),
                        childAtPosition(
                            withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                            1)),
                    0),
                isDisplayed()));
    appCompatButton2.perform(click());

    DataInteraction appCompatCheckedTextView =
        onData(anything())
            .inAdapterView(
                allOf(
                    withId(R.id.select_dialog_listview),
                    childAtPosition(withId(R.id.contentPanel), 0)))
            .atPosition(1);
    appCompatCheckedTextView.perform(click());

    ViewInteraction appCompatButton3 =
        onView(
            allOf(
                withId(R.id.changeMyLang),
                withText("语言"),
                childAtPosition(
                    allOf(
                        withId(R.id.linearLayout),
                        childAtPosition(
                            withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                            1)),
                    0),
                isDisplayed()));
    appCompatButton3.perform(click());

    DataInteraction appCompatCheckedTextView2 =
        onData(anything())
            .inAdapterView(
                allOf(
                    withId(R.id.select_dialog_listview),
                    childAtPosition(withId(R.id.contentPanel), 0)))
            .atPosition(0);
    appCompatCheckedTextView2.perform(click());
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
        return parent instanceof ViewGroup
            && parentMatcher.matches(parent)
            && view.equals(((ViewGroup) parent).getChildAt(position));
      }
    };
  }
}
