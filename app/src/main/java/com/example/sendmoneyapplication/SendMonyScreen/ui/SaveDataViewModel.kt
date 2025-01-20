package com.example.sendmoneyapplication.SendMonyScreen.ui

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sendmoneyapplication.SendMonyScreen.domain.model.DatabaseRowItem
import com.example.sendmoneyapplication.SendMonyScreen.domain.model.Provider
import com.example.sendmoneyapplication.SendMonyScreen.domain.model.Service
import com.example.sendmoneyapplication.databaseConfig.AppDatabase
import com.example.sendmoneyapplication.databaseConfig.RequestSendDataDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SaveDataViewModel : ViewModel() {

    private val _saveStatus = MutableStateFlow<String?>(null)
    val saveStatus: StateFlow<String?> = _saveStatus

    @SuppressLint("SuspiciousIndentation")
    fun sendMoneyRequest(
        db: AppDatabase,
        selectedService: Service?,
        selectedProvider: Provider?,
        amount: String
    ) {
        viewModelScope.launch {
            try {
                val dataItem = DatabaseRowItem(
                    amount = amount,
                    services = selectedService!!,
                    provider = selectedProvider!!
                )
                val requestInfoDao = db.requestInfo()
              val mRequestID =   requestInfoDao.insert(requestInformation = dataItem)
                _saveStatus.value = "Data saved successfully $mRequestID"
            } catch (e: Exception) {
                _saveStatus.value = "Failed to save data"
            }
        }

    }


    private val _savedRequests = MutableStateFlow<List<DatabaseRowItem>>(emptyList())
    val savedRequests: StateFlow<List<DatabaseRowItem>> = _savedRequests

    fun fetchSavedRequests(requestInfoDao: RequestSendDataDao) {
        viewModelScope.launch {
            try {
                val requests = requestInfoDao.getRequestSendMoneyData()
                _savedRequests.value = requests
            } catch (e: Exception) {
                _savedRequests.value = emptyList()
            }
        }
    }


}