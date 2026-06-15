package com.buildstack.nutriscan.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.buildstack.nutriscan.data.local.entity.ScanEntity

@Database(entities = [ScanEntity::class], version = 1, exportSchema = false)
abstract class ScanDatabase : RoomDatabase() {
    abstract val scanDao: ScanDao
}
