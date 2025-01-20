package com.example.sendmoneyapplication.databaseConfig

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.sendmoneyapplication.SendMonyScreen.domain.model.DatabaseRowItem

@Dao
interface RequestSendDataDao {
    @Insert
    suspend fun insert(requestInformation: DatabaseRowItem) : Long

    @Query("SELECT * FROM requestInformation ")
    suspend fun getRequestSendMoneyData(): List<DatabaseRowItem>
}