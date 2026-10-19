package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.ui.localization.AppLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("crypto_radar_prefs", Context.MODE_PRIVATE)

    private val _languageFlow = MutableStateFlow(loadLanguage())
    val languageFlow: StateFlow<AppLanguage> = _languageFlow.asStateFlow()

    private val _refreshIntervalFlow = MutableStateFlow(loadRefreshInterval())
    val refreshIntervalFlow: StateFlow<Long> = _refreshIntervalFlow.asStateFlow()

    private fun loadLanguage(): AppLanguage {
        val langStr = prefs.getString("app_language", AppLanguage.PERSIAN.name) ?: AppLanguage.PERSIAN.name
        return try {
            AppLanguage.valueOf(langStr)
        } catch (e: Exception) {
            AppLanguage.PERSIAN
        }
    }

    fun setLanguage(language: AppLanguage) {
        prefs.edit().putString("app_language", language.name).apply()
        _languageFlow.value = language
    }

    private fun loadRefreshInterval(): Long {
        return prefs.getLong("refresh_interval_ms", 60_000L)
    }

    fun setRefreshInterval(intervalMs: Long) {
        prefs.edit().putLong("refresh_interval_ms", intervalMs).apply()
        _refreshIntervalFlow.value = intervalMs
    }
}
