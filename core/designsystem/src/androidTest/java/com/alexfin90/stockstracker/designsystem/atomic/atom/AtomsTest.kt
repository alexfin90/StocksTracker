package com.alexfin90.stockstracker.designsystem.atomic.atom

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.alexfin90.stockstracker.designsystem.atomic.atoms.ConnectionStatusIndicator
import com.alexfin90.stockstracker.designsystem.atomic.atoms.StocksTrackerToggleButton
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AtomsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // region ConnectionStatusIndicator

    @Test
    fun connectionStatusIndicator_showsConnectedLabel_whenConnected() {
        composeTestRule.setContent {
            ConnectionStatusIndicator(isConnected = true)
        }

        composeTestRule.onNodeWithText("Connected").assertIsDisplayed()
    }

    @Test
    fun connectionStatusIndicator_showsDisconnectedLabel_whenNotConnected() {
        composeTestRule.setContent {
            ConnectionStatusIndicator(isConnected = false)
        }

        composeTestRule.onNodeWithText("Disconnected").assertIsDisplayed()
    }

    // endregion

    // region StocksTrackerToggleButton

    @Test
    fun stocksTrackerToggleButton_showsStart_whenNotConnected() {
        composeTestRule.setContent {
            StocksTrackerToggleButton(
                isConnected = false,
                onClick = {},
            )
        }

        composeTestRule.onNodeWithText("Start")
            .assertIsDisplayed()
            .assertHasClickAction()
            .assertIsEnabled()
    }

    @Test
    fun stocksTrackerToggleButton_showsStop_whenConnected() {
        composeTestRule.setContent {
            StocksTrackerToggleButton(
                isConnected = true,
                onClick = {},
            )
        }

        composeTestRule.onNodeWithText("Stop").assertIsDisplayed()
    }

    @Test
    fun stocksTrackerToggleButton_invokesOnClick_whenTapped() {
        var clicks = 0
        composeTestRule.setContent {
            StocksTrackerToggleButton(
                isConnected = false,
                onClick = { clicks++ },
            )
        }

        composeTestRule.onNodeWithText("Start").performClick()

        assertEquals(1, clicks)
    }

    @Test
    fun stocksTrackerToggleButton_togglesLabel_onClick() {
        composeTestRule.setContent {
            var isConnected by remember { mutableStateOf(false) }
            StocksTrackerToggleButton(
                isConnected = isConnected,
                onClick = { isConnected = !isConnected },
            )
        }

        composeTestRule.onNodeWithText("Start").assertIsDisplayed()
        composeTestRule.onNodeWithText("Start").performClick()
        composeTestRule.onNodeWithText("Stop").assertIsDisplayed()
        composeTestRule.onNodeWithText("Stop").performClick()
        composeTestRule.onNodeWithText("Start").assertIsDisplayed()
    }

    @Test
    fun stocksTrackerToggleButton_doesNotInvokeOnClick_whenDisabled() {
        var clicks = 0
        composeTestRule.setContent {
            StocksTrackerToggleButton(
                isConnected = false,
                enabled = false,
                onClick = { clicks++ },
            )
        }

        composeTestRule.onNodeWithText("Start")
            .assertIsNotEnabled()
            .performClick()

        assertEquals(0, clicks)
    }

    // endregion
}