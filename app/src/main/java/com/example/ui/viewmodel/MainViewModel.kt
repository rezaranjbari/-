package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.PreferencesManager
import com.example.data.local.WatchlistEntity
import com.example.data.model.Candle
import com.example.data.model.CryptoAsset
import com.example.data.model.CryptoNewsItem
import com.example.data.model.MarketOverviewData
import com.example.data.model.MultiSourcePrice
import com.example.data.model.RadarOpportunity
import com.example.data.repository.CryptoRepository
import com.example.ui.localization.AppLanguage
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val repository = CryptoRepository(database)
    private val prefs = PreferencesManager(application)

    val language: StateFlow<AppLanguage> = prefs.languageFlow

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _marketOverview = MutableStateFlow(MarketOverviewData())
    val marketOverview: StateFlow<MarketOverviewData> = _marketOverview.asStateFlow()

    private val _marketAssets = MutableStateFlow<List<CryptoAsset>>(emptyList())
    val marketAssets: StateFlow<List<CryptoAsset>> = _marketAssets.asStateFlow()

    private val _radarOpportunities = MutableStateFlow<List<RadarOpportunity>>(emptyList())
    val radarOpportunities: StateFlow<List<RadarOpportunity>> = _radarOpportunities.asStateFlow()

    private val _newsList = MutableStateFlow<List<CryptoNewsItem>>(emptyList())
    val newsList: StateFlow<List<CryptoNewsItem>> = _newsList.asStateFlow()

    val watchlist: StateFlow<List<WatchlistEntity>> = repository.watchlistFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Scanner filters
    private val _selectedTimeframe = MutableStateFlow("4h")
    val selectedTimeframe: StateFlow<String> = _selectedTimeframe.asStateFlow()

    private val _selectedStrategy = MutableStateFlow("ALL")
    val selectedStrategy: StateFlow<String> = _selectedStrategy.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Coin Detail state
    private val _selectedAsset = MutableStateFlow<CryptoAsset?>(null)
    val selectedAsset: StateFlow<CryptoAsset?> = _selectedAsset.asStateFlow()

    private val _selectedMultiSource = MutableStateFlow<MultiSourcePrice?>(null)
    val selectedMultiSource: StateFlow<MultiSourcePrice?> = _selectedMultiSource.asStateFlow()

    private val _selectedCandles = MutableStateFlow<List<Candle>>(emptyList())
    val selectedCandles: StateFlow<List<Candle>> = _selectedCandles.asStateFlow()

    private val _selectedOpportunity = MutableStateFlow<RadarOpportunity?>(null)
    val selectedOpportunity: StateFlow<RadarOpportunity?> = _selectedOpportunity.asStateFlow()

    private val _selectedSubChart = MutableStateFlow("RSI")
    val selectedSubChart: StateFlow<String> = _selectedSubChart.asStateFlow()

    private val _isDetailLoading = MutableStateFlow(false)
    val isDetailLoading: StateFlow<Boolean> = _isDetailLoading.asStateFlow()

    private var autoRefreshJob: Job? = null

    init {
        loadInitialData()
        startAutoRefresh()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                val overview = repository.getMarketOverview()
                _marketOverview.value = overview

                val assets = repository.fetchMarketAssets()
                _marketAssets.value = assets

                val news = repository.fetchNews()
                _newsList.value = news

                runRadarScan()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    private fun startAutoRefresh() {
        autoRefreshJob?.cancel()
        autoRefreshJob = viewModelScope.launch {
            while (true) {
                delay(60_000L) // 60 seconds interval
                refreshDataSilently()
            }
        }
    }

    private suspend fun refreshDataSilently() {
        try {
            val overview = repository.getMarketOverview()
            _marketOverview.value = overview

            val assets = repository.fetchMarketAssets()
            _marketAssets.value = assets

            val opps = repository.runRadarScan(
                timeframe = _selectedTimeframe.value,
                strategyFilter = _selectedStrategy.value,
                searchQuery = _searchQuery.value
            )
            _radarOpportunities.value = opps
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun refreshAll() {
        loadInitialData()
    }

    fun setLanguage(lang: AppLanguage) {
        prefs.setLanguage(lang)
    }

    fun selectTimeframe(tf: String) {
        _selectedTimeframe.value = tf
        runRadarScan()
    }

    fun selectStrategy(strategy: String) {
        _selectedStrategy.value = strategy
        runRadarScan()
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        runRadarScan()
    }

    fun setSubChart(chartType: String) {
        _selectedSubChart.value = chartType
    }

    fun runRadarScan() {
        viewModelScope.launch {
            val opps = repository.runRadarScan(
                timeframe = _selectedTimeframe.value,
                strategyFilter = _selectedStrategy.value,
                searchQuery = _searchQuery.value
            )
            _radarOpportunities.value = opps
        }
    }

    fun openAssetDetail(asset: CryptoAsset) {
        _selectedAsset.value = asset
        _isDetailLoading.value = true

        viewModelScope.launch {
            try {
                val multiSource = repository.getMultiSourcePrice(asset)
                _selectedMultiSource.value = multiSource

                val candles = repository.getCandles(asset.symbol, _selectedTimeframe.value)
                _selectedCandles.value = candles

                // Find or evaluate opportunity
                val opp = _radarOpportunities.value.firstOrNull { it.asset.symbol == asset.symbol }
                    ?: com.example.data.analysis.TechnicalAnalysisEngine.evaluateOpportunity(
                        asset = asset,
                        candles = candles,
                        btcChange24h = _marketOverview.value.btcChange24h,
                        timeframe = _selectedTimeframe.value
                    )
                _selectedOpportunity.value = opp
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isDetailLoading.value = false
            }
        }
    }

    fun closeAssetDetail() {
        _selectedAsset.value = null
        _selectedMultiSource.value = null
        _selectedCandles.value = emptyList()
        _selectedOpportunity.value = null
    }

    fun toggleWatchlist(asset: CryptoAsset, note: String = "") {
        viewModelScope.launch {
            val isPresent = watchlist.value.any { it.symbol == asset.symbol }
            if (isPresent) {
                repository.removeFromWatchlist(asset.symbol)
            } else {
                repository.addToWatchlist(asset.symbol, asset.name, asset.nameFa, note)
            }
        }
    }
}
