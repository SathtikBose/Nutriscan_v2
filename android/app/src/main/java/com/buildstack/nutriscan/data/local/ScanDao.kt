package com.buildstack.nutriscan.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.buildstack.nutriscan.data.local.entity.ScanEntity
import kotlinx.coroutines.flow.Flow

@Dao
@JvmSuppressWildcards
interface ScanDao {
    @Query("SELECT * FROM scans ORDER BY date DESC")
    fun getAllScans(): Flow<List<ScanEntity>>

    @Query("SELECT * FROM scans WHERE id = :id")
    suspend fun getScanById(id: String): ScanEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScan(scan: ScanEntity): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    suspend fun insertScans(scans: List<ScanEntity>): List<Long>?

    @Query("DELETE FROM scans")
    suspend fun clearAll(): Int?
}
