package com.alexfin90.stockstracker.designsystem.atomic.molecoles

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.alexfin90.stockstracker.designsystem.atomic.molecules.StockRow
import com.alexfin90.stockstracker.designsystem.atomic.molecules.UiStockRow
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StockRowTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val appleStock = UiStockRow(
        symbol = "AAPL",
        name = "Apple Inc.",
        priceUsd = 150.0,
        isUp = true,
    )

    @Test
    fun stockRow_displaysSymbolAndName() {
        composeTestRule.setContent {
            StockRow(stock = appleStock, onClick = {})
        }

        composeTestRule.onNodeWithText("AAPL").assertIsDisplayed()
        composeTestRule.onNodeWithText("Apple Inc.").assertIsDisplayed()
    }

    @Test
    fun stockRow_formatsPriceWithTwoDecimals() {
        composeTestRule.setContent {
            StockRow(stock = appleStock.copy(priceUsd = 150.0), onClick = {})
        }

        composeTestRule.onNodeWithText("$150.00").assertIsDisplayed()
    }

    @Test
    fun stockRow_roundsPriceToTwoDecimals() {
        composeTestRule.setContent {
            StockRow(stock = appleStock.copy(priceUsd = 1234.5678), onClick = {})
        }

        composeTestRule.onNodeWithText("$1234.57").assertIsDisplayed()
    }

    @Test
    fun stockRow_isClickable_andInvokesOnClick() {
        var clicks = 0
        composeTestRule.setContent {
            StockRow(stock = appleStock, onClick = { clicks++ })
        }

        composeTestRule.onNodeWithText("AAPL")
            .assertHasClickAction()

        composeTestRule.onNodeWithText("AAPL").performClick()

        assertEquals(1, clicks)
    }

    @Test
    fun stockRow_rendersForDecreasingStock() {
        val decreasing = appleStock.copy(
            symbol = "MSFT",
            name = "Microsoft Corp.",
            priceUsd = 299.99,
            isUp = false,
        )
        composeTestRule.setContent {
            StockRow(stock = decreasing, onClick = {})
        }

        composeTestRule.onNodeWithText("MSFT").assertIsDisplayed()
        composeTestRule.onNodeWithText("Microsoft Corp.").assertIsDisplayed()
        composeTestRule.onNodeWithText("$299.99").assertIsDisplayed()
    }
}