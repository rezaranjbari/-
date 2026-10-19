package com.example.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface BinanceService {
    @GET("api/v3/ticker/24hr")
    suspend fun get24hTickers(): List<BinanceTicker24hDto>

    @GET("api/v3/klines")
    suspend fun getKlines(
        @Query("symbol") symbol: String,
        @Query("interval") interval: String,
        @Query("limit") limit: Int = 50
    ): List<List<Any>>
}

interface CoinGeckoService {
    @GET("api/v3/coins/markets")
    suspend fun getMarkets(
        @Query("vs_currency") vsCurrency: String = "usd",
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") perPage: Int = 50,
        @Query("page") page: Int = 1,
        @Query("sparkline") sparkline: Boolean = true,
        @Query("price_change_percentage") priceChangePercentage: String = "24h"
    ): List<CoinGeckoMarketDto>
}

interface CoinCapService {
    @GET("v2/assets")
    suspend fun getAssets(
        @Query("limit") limit: Int = 50
    ): CoinCapResponseDto
}

interface CryptoNewsService {
    @GET("data/v2/news/")
    suspend fun getNews(
        @Query("lang") lang: String = "EN"
    ): CryptoCompareNewsResponseDto
}

object NetworkClient {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(12, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("User-Agent", "CryptoRadarPro-Android/1.0")
                .header("Accept", "application/json")
                .build()
            chain.proceed(request)
        }
        .build()

    val binance: BinanceService = Retrofit.Builder()
        .baseUrl("https://data-api.binance.vision/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(BinanceService::class.java)

    val binancePrimary: BinanceService = Retrofit.Builder()
        .baseUrl("https://api.binance.com/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(BinanceService::class.java)

    val coinGecko: CoinGeckoService = Retrofit.Builder()
        .baseUrl("https://api.coingecko.com/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(CoinGeckoService::class.java)

    val coinCap: CoinCapService = Retrofit.Builder()
        .baseUrl("https://api.coincap.io/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(CoinCapService::class.java)

    val news: CryptoNewsService = Retrofit.Builder()
        .baseUrl("https://min-api.cryptocompare.com/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(CryptoNewsService::class.java)
}
