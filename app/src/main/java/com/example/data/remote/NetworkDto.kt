package com.example.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// Binance DTOs
@JsonClass(generateAdapter = true)
data class BinanceTicker24hDto(
    @Json(name = "symbol") val symbol: String,
    @Json(name = "priceChangePercent") val priceChangePercent: String?,
    @Json(name = "lastPrice") val lastPrice: String?,
    @Json(name = "highPrice") val highPrice: String?,
    @Json(name = "lowPrice") val lowPrice: String?,
    @Json(name = "volume") val volume: String?,
    @Json(name = "quoteVolume") val quoteVolume: String?,
    @Json(name = "closeTime") val closeTime: Long?
)

// CoinGecko DTOs
@JsonClass(generateAdapter = true)
data class CoinGeckoSparklineDto(
    @Json(name = "price") val price: List<Double>?
)

@JsonClass(generateAdapter = true)
data class CoinGeckoMarketDto(
    @Json(name = "id") val id: String,
    @Json(name = "symbol") val symbol: String,
    @Json(name = "name") val name: String,
    @Json(name = "image") val image: String?,
    @Json(name = "current_price") val currentPrice: Double?,
    @Json(name = "market_cap") val marketCap: Double?,
    @Json(name = "market_cap_rank") val marketCapRank: Int?,
    @Json(name = "total_volume") val totalVolume: Double?,
    @Json(name = "high_24h") val high24h: Double?,
    @Json(name = "low_24h") val low24h: Double?,
    @Json(name = "price_change_percentage_24h") val priceChangePercentage24h: Double?,
    @Json(name = "sparkline_in_7d") val sparklineIn7d: CoinGeckoSparklineDto?,
    @Json(name = "last_updated") val lastUpdated: String?
)

// CoinCap DTOs
@JsonClass(generateAdapter = true)
data class CoinCapAssetDto(
    @Json(name = "id") val id: String,
    @Json(name = "rank") val rank: String?,
    @Json(name = "symbol") val symbol: String,
    @Json(name = "name") val name: String,
    @Json(name = "priceUsd") val priceUsd: String?,
    @Json(name = "changePercent24Hr") val changePercent24Hr: String?,
    @Json(name = "volumeUsd24Hr") val volumeUsd24Hr: String?,
    @Json(name = "marketCapUsd") val marketCapUsd: String?
)

@JsonClass(generateAdapter = true)
data class CoinCapResponseDto(
    @Json(name = "data") val data: List<CoinCapAssetDto>?
)

// CryptoCompare News DTOs
@JsonClass(generateAdapter = true)
data class CryptoCompareNewsItemDto(
    @Json(name = "id") val id: String?,
    @Json(name = "published_on") val publishedOn: Long?,
    @Json(name = "title") val title: String?,
    @Json(name = "url") val url: String?,
    @Json(name = "source") val source: String?,
    @Json(name = "body") val body: String?,
    @Json(name = "imageurl") val imageurl: String?
)

@JsonClass(generateAdapter = true)
data class CryptoCompareNewsResponseDto(
    @Json(name = "Data") val data: List<CryptoCompareNewsItemDto>?
)
