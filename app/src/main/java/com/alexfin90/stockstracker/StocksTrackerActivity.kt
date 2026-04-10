package com.alexfin90.stockstracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.alexfin90.stockstracker.designsystem.atomic.atoms.ConnectionStatusIndicator
import com.alexfin90.stockstracker.designsystem.atomic.atoms.StocksTrackerToggleButton
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
private fun StocksTrackerApp(
    viewModel: StocksTrackerActivityViewModelContract = hiltViewModel<MainViewModel>()
) {
    val isConnected by viewModel.connectionActive.collectAsStateWithLifecycle()
    StocksTrackerAppContent(isConnected, viewModel::onToggleConnection)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StocksTrackerAppContent(
    isConnected: Boolean,
    onToggleConnection: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.primary,
        topBar = {
            TopAppBar(
                title = {
                    ConnectionStatusIndicator(
                        isConnected = isConnected
                    )
                },
                actions = {
                    StocksTrackerToggleButton(
                        isConnected = isConnected,
                        onClick = onToggleConnection,
                    )
                },
            )
        }
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
        composable<Route.StockDetail>(
            deepLinks = listOf(
                navDeepLink<Route.StockDetail>(
                    basePath = BuildConfig.DEEPLINK_BASE_URL
                )
            )
        ) {
            StockDetailScreen(modifier = Modifier.fillMaxSize())
        }
    }
}
