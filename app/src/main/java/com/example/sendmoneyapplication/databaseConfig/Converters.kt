package com.example.sendmoneyapplication.databaseConfig

import androidx.room.TypeConverter
import com.example.sendmoneyapplication.SendMonyScreen.domain.model.DatabaseRowItem
import com.example.sendmoneyapplication.SendMonyScreen.domain.model.Provider
import com.example.sendmoneyapplication.SendMonyScreen.domain.model.Service
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class Converters {

    @TypeConverter
    fun databaseRowItemToString(databaseRowItem: DatabaseRowItem ): String? = Gson().toJson(databaseRowItem)

    @TypeConverter
    fun stringToDatabaseRowItem(json: String?): DatabaseRowItem? = Gson().fromJson(json, DatabaseRowItem::class.java)

    @TypeConverter
    fun serviceToString(service: Service ): String? = Gson().toJson(service)

    @TypeConverter
    fun stringToService(json: String?): Service? = Gson().fromJson(json, Service::class.java)


    @TypeConverter
    fun providerToString(provider: Provider ): String? = Gson().toJson(provider)

    @TypeConverter
    fun stringToProvider(json: String?): Provider? = Gson().fromJson(json, Provider::class.java)
}