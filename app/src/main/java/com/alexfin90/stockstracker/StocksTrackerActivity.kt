package com.alexfin90.stockstracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alexfin90.stockstracker.designsystem.theme.StocksTrackerTheme
import com.alexfin90.stockstracker.navigation.Route
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class StocksTrackerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate")
        enableEdgeToEdge()
        setContent {
            StocksTrackerTheme {
                StocksTrackerApp()
            }
        }
    }
}


@Composable
private fun StocksTrackerApp() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.primary
    ) { innerPadding ->
        StocksTrackerNavHost(modifier = Modifier.padding(paddingValues = innerPadding))
    }
}

@Composable
private fun StocksTrackerNavHost(modifier: Modifier = Modifier) {
    val navController: NavHostController = rememberNavController()
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Route.StocksFeed
    ) {
        composable<Route.StocksFeed> {
            StocksFeedScreen(
                modifier = Modifier.fillMaxSize(),
                onStockClick = { symbol ->
                    navController.navigate(Route.StockDetail(symbol = symbol))
                }
            )
        }
    }
}
