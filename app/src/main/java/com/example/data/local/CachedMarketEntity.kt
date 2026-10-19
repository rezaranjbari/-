package com.example.data.local

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "cached_assets")
data class CachedMarketEntity(
    @PrimaryKey val symbol: String,
    val name: String,
    val nameFa: String,
    val priceUsd: Double,
    val change24h: Double,
    val high24h: Double,
    val low24h: Double,
    val volume24hUsd: Double,
    val marketCapUsd: Double,
    val marketCapRank: Int,
    val sparklineJson: String,
    val lastUpdated: Long
)

@Dao
interface CachedMarketDao {
    @Query("SELECT * FROM cached_assets ORDER BY marketCapRank ASC")
    fun getAllCachedAssets(): Flow<List<CachedMarketEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(assets: List<CachedMarketEntity>)

    @Query("DELETE FROM cached_assets")
    suspend fun clearAll()
}
