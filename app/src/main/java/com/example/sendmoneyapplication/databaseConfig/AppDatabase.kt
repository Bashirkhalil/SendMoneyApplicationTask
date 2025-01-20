package com.example.sendmoneyapplication.databaseConfig

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.sendmoneyapplication.SendMonyScreen.domain.model.DatabaseRowItem
import com.example.sendmoneyapplication.SendMonyScreen.domain.model.Provider
import com.example.sendmoneyapplication.SendMonyScreen.domain.model.Service

@Database(entities = [DatabaseRowItem::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun requestInfo(): RequestSendDataDao
}