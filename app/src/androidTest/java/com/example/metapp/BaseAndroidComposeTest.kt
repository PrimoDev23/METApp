package com.example.metapp

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Rule

abstract class BaseAndroidComposeTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    protected val context: Context
        get() = composeTestRule.activity

    @After
    fun after() {
        unmockkAll()
    }

}