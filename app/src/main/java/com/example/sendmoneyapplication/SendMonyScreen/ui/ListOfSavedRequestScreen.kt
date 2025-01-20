package com.example.sendmoneyapplication.SendMonyScreen.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sendmoneyapplication.SendMonyScreen.ui.row.BuildSendRequestRow
import com.example.sendmoneyapplication.databaseConfig.RequestSendDataDao
import com.example.sendmoneyapplication.starter.bgColor

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedRequestsScreen(requestSendDataDao: RequestSendDataDao) {

    val viewModel = viewModel<SaveDataViewModel>()

    val savedRequestsList by viewModel.savedRequests.collectAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.fetchSavedRequests(requestInfoDao =  requestSendDataDao)
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("List of Saved Requests [ "+savedRequestsList.size+" ]", color = Color.White)
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp , bottom = 50.dp , end = 16.dp)
                    .verticalScroll(rememberScrollState()),

                verticalArrangement = if (savedRequestsList.isEmpty()) Arrangement.Center else Arrangement.Top,

            ) {


                Space(50)
                Space(50)

                if (savedRequestsList.isEmpty()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()

                    ) {
                        Text(
                            text = "No saved requests",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }else {
                    savedRequestsList.forEach { request ->
                        Column(modifier = Modifier.padding(bottom = 8.dp)) {

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                                        RoundedCornerShape(8.dp)
                                    )
                            ) {
                                BuildSendRequestRow (
                                    requestId = request.id,
                                    serviceName = request.services,
                                    providerName = request.provider,
                                    amount = request.amount ,
                                    onShowDetailsClick = {

                                    }
                                )
                            }


                        }
                    }
                }

            }
        }
    )
}
