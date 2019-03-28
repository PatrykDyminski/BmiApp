package com.example.bmi


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun mainActivityTest() {
        val appCompatEditText = onView(withId(R.id.mass_edit))
        appCompatEditText.perform(click())

        val appCompatEditText2 = onView(withId(R.id.mass_edit))
        appCompatEditText2.perform(replaceText("80"), closeSoftKeyboard())

        val appCompatEditText3 = onView(withId(R.id.mass_edit))
        appCompatEditText3.perform(pressImeActionButton())

        val appCompatEditText4 = onView(withId(R.id.height_edit))
        appCompatEditText4.perform(replaceText("180"), closeSoftKeyboard())

        val appCompatEditText5 = onView(withId(R.id.height_edit))
        appCompatEditText5.perform(pressImeActionButton())

        val appCompatButton = onView(withId(R.id.countBtn))
        appCompatButton.perform(click())

        val textView = onView(withId(R.id.result_label))
        textView.check(matches(withText("24,69")))

        val textView2 = onView(withId(R.id.category_label))
        textView2.check(matches(withText("HEALTHY")))
    }

}
