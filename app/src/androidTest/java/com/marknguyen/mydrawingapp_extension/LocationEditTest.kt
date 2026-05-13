package com.marknguyen.mydrawingapp_extension

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocationEditTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun clickImage_changeName_verifyInList() {
        onView(withId(R.id.rvLocations))
            .perform(RecyclerViewActions.actionOnItemAtPosition<LocationAdapter.LocationViewHolder>(0, click()))

        onView(withId(R.id.editTextName))
            .perform(clearText(), typeText("Test Location Name"), closeSoftKeyboard())

        pressBack()

        onView(withText("Test Location Name"))
            .check(matches(isDisplayed()))
    }
}
