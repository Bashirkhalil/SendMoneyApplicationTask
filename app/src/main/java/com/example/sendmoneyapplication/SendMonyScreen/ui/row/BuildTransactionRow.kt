package com.example.sendmoneyapplication.SendMonyScreen.ui.row

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.sendmoneyapplication.SendMonyScreen.domain.model.Label
import com.example.sendmoneyapplication.SendMonyScreen.domain.model.Provider
import com.example.sendmoneyapplication.SendMonyScreen.domain.model.Service
import com.google.gson.Gson

@Composable
fun BuildSendRequestRow(
    requestId: Long,
    serviceName: Service,
    providerName: Provider,
    amount: String,
    onShowDetailsClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier =
            Modifier
                .fillMaxWidth() ,

        ) {
            Text(
                text = "Request ID: $requestId",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Amount: $amount",
                style = MaterialTheme.typography.labelSmall
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Service: ${serviceName.label?.en}",
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Provider: ${providerName.name}",
                style = MaterialTheme.typography.labelSmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

       Button(onClick = { expanded = !expanded }) {
            Text(text = if (expanded) "Hide Details" else "Show Details")
        }

        if (expanded) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Provider : ${Gson().toJson(providerName)} \n Service : ${Gson().toJson(serviceName)} \n Amount :  ${Gson().toJson(amount)}",
                style = MaterialTheme.typography.labelSmall
            )
            onShowDetailsClick()
        }
    }
}

