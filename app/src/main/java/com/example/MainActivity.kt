package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.RadarTopBar
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.Strings
import com.example.ui.screens.CoinDetailScreen
import com.example.ui.screens.MarketDashboardScreen
import com.example.ui.screens.NewsScreen
import com.example.ui.screens.OpportunityScannerScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.WatchlistScreen
import com.example.ui.theme.CryptoRadarTheme
import com.example.ui.theme.RadarBg
import com.example.ui.theme.RadarCyan
import com.example.ui.theme.RadarSurface
import com.example.ui.theme.RadarSurfaceVariant
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.MainViewModel

enum class NavigationTab {
    MARKET,
    SCANNER,
    WATCHLIST,
    NEWS,
    SETTINGS
}

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val language by viewModel.language.collectAsStateWithLifecycle()
            val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
            val marketOverview by viewModel.marketOverview.collectAsStateWithLifecycle()
            val marketAssets by viewModel.marketAssets.collectAsStateWithLifecycle()
            val radarOpportunities by viewModel.radarOpportunities.collectAsStateWithLifecycle()
            val newsList by viewModel.newsList.collectAsStateWithLifecycle()
            val watchlist by viewModel.watchlist.collectAsStateWithLifecycle()

            val selectedTimeframe by viewModel.selectedTimeframe.collectAsStateWithLifecycle()
            val selectedStrategy by viewModel.selectedStrategy.collectAsStateWithLifecycle()
            val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

            val selectedAsset by viewModel.selectedAsset.collectAsStateWithLifecycle()
            val selectedMultiSource by viewModel.selectedMultiSource.collectAsStateWithLifecycle()
            val selectedCandles by viewModel.selectedCandles.collectAsStateWithLifecycle()
            val selectedOpportunity by viewModel.selectedOpportunity.collectAsStateWithLifecycle()
            val selectedSubChart by viewModel.selectedSubChart.collectAsStateWithLifecycle()
            val isDetailLoading by viewModel.isDetailLoading.collectAsStateWithLifecycle()

            var currentTab by rememberSaveable { mutableStateOf(NavigationTab.MARKET) }

            // Layout Direction: RTL for Persian (default), LTR for English
            val layoutDirection = if (language == AppLanguage.PERSIAN) {
                LayoutDirection.Rtl
            } else {
                LayoutDirection.Ltr
            }

            CryptoRadarTheme {
                CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(RadarBg)
                    ) {
                        if (selectedAsset != null) {
                            // Coin Detail Screen with BackHandler
                            val isAssetInWatchlist = watchlist.any { it.symbol == selectedAsset?.symbol }
                            CoinDetailScreen(
                                asset = selectedAsset!!,
                                multiSourcePrice = selectedMultiSource,
                                candles = selectedCandles,
                                opportunity = selectedOpportunity,
                                selectedSubChart = selectedSubChart,
                                isWatchlisted = isAssetInWatchlist,
                                isLoading = isDetailLoading,
                                language = language,
                                onBack = { viewModel.closeAssetDetail() },
                                onToggleWatchlist = { selectedAsset?.let { viewModel.toggleWatchlist(it) } },
                                onSubChartSelected = { viewModel.setSubChart(it) }
                            )
                        } else {
                            // Main Navigation Shell with TopBar and BottomBar
                            Scaffold(
                                modifier = Modifier.fillMaxSize(),
                                containerColor = RadarBg,
                                topBar = {
                                    RadarTopBar(
                                        language = language,
                                        isRefreshing = isRefreshing,
                                        onRefresh = { viewModel.refreshAll() },
                                        onToggleLanguage = {
                                            val nextLang = if (language == AppLanguage.PERSIAN) AppLanguage.ENGLISH else AppLanguage.PERSIAN
                                            viewModel.setLanguage(nextLang)
                                        }
                                    )
                                },
                                bottomBar = {
                                    RadarBottomNavigation(
                                        currentTab = currentTab,
                                        language = language,
                                        onTabSelected = { currentTab = it }
                                    )
                                }
                            ) { innerPadding ->
                                AnimatedContent(
                                    targetState = currentTab,
                                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                                    label = "tab_transition",
                                    modifier = Modifier.padding(innerPadding)
                                ) { tab ->
                                    when (tab) {
                                        NavigationTab.MARKET -> {
                                            MarketDashboardScreen(
                                                overview = marketOverview,
                                                assets = marketAssets,
                                                radarPicks = radarOpportunities,
                                                language = language,
                                                onAssetClick = { viewModel.openAssetDetail(it) },
                                                onNavigateToScanner = { currentTab = NavigationTab.SCANNER }
                                            )
                                        }

                                        NavigationTab.SCANNER -> {
                                            OpportunityScannerScreen(
                                                opportunities = radarOpportunities,
                                                selectedTimeframe = selectedTimeframe,
                                                selectedStrategy = selectedStrategy,
                                                searchQuery = searchQuery,
                                                language = language,
                                                onTimeframeChange = { viewModel.selectTimeframe(it) },
                                                onStrategyChange = { viewModel.selectStrategy(it) },
                                                onSearchChange = { viewModel.setSearchQuery(it) },
                                                onAssetClick = { viewModel.openAssetDetail(it) }
                                            )
                                        }

                                        NavigationTab.WATCHLIST -> {
                                            WatchlistScreen(
                                                watchlist = watchlist,
                                                assets = marketAssets,
                                                language = language,
                                                onAssetClick = { viewModel.openAssetDetail(it) },
                                                onRemoveFromWatchlist = { viewModel.toggleWatchlist(it) }
                                            )
                                        }

                                        NavigationTab.NEWS -> {
                                            NewsScreen(
                                                news = newsList,
                                                language = language
                                            )
                                        }

                                        NavigationTab.SETTINGS -> {
                                            SettingsScreen(
                                                currentLanguage = language,
                                                onLanguageChange = { viewModel.setLanguage(it) }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RadarBottomNavigation(
    currentTab: NavigationTab,
    language: AppLanguage,
    onTabSelected: (NavigationTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.testTag("bottom_navigation_bar"),
        containerColor = RadarSurface,
        contentColor = TextPrimary
    ) {
        val items = listOf(
            Triple(NavigationTab.MARKET, Strings.tabMarket(language), Icons.Default.Dashboard),
            Triple(NavigationTab.SCANNER, Strings.tabScanner(language), Icons.Default.Radar),
            Triple(NavigationTab.WATCHLIST, Strings.tabWatchlist(language), Icons.Default.Star),
            Triple(NavigationTab.NEWS, Strings.tabNews(language), Icons.Default.Newspaper),
            Triple(NavigationTab.SETTINGS, Strings.tabSettings(language), Icons.Default.Settings)
        )

        items.forEach { (tab, title, icon) ->
            val isSelected = currentTab == tab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = title
                    )
                },
                label = {
                    Text(
                        text = title,
                        fontSize = 10.sp,
                        maxLines = 1
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = RadarCyan,
                    selectedTextColor = RadarCyan,
                    indicatorColor = RadarSurfaceVariant,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary
                ),
                modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
            )
        }
    }
}
