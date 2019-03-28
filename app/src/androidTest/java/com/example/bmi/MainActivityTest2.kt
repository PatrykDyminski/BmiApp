package com.example.bmi


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
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
class MainActivityTest2 {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun mainActivityTest2() {
        val appCompatImageButton = onView(withId(R.id.infoBtn))
        appCompatImageButton.perform(click())

        val textView = onView(withId(R.id.textWynik))
        textView.check(matches(isDisplayed()))

        pressBack()

        val textView2 = onView(withId(R.id.mass_label))
        textView2.check(matches(isDisplayed()))

        val textView3 = onView(withId(R.id.height_label))
        textView3.check(matches(isDisplayed()))

        val imageButton = onView(withId(R.id.infoBtn))
        imageButton.check(matches(isDisplayed()))
    }
}
