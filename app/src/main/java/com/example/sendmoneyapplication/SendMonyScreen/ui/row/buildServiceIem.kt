package com.example.sendmoneyapplication.SendMonyScreen.ui.row

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.sendmoneyapplication.SendMonyScreen.domain.model.Service

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BuildServiceIem(listOfService : List<Service>,
                    oldSelectedService  : Service?,
                    onServiceItemSelected : (Service , Int) -> Unit,
                    ) {

    var isExpandService by remember { mutableStateOf(false) }

    var selectedService by remember {
        if(oldSelectedService == null) {
            mutableStateOf(listOfService[0])
        }else{
            mutableStateOf(oldSelectedService)
        }
    }

    if(oldSelectedService == null) {
        onServiceItemSelected(selectedService,0)
    }



    // build Service
    ExposedDropdownMenuBox(
        expanded = isExpandService,
        onExpandedChange = {
            isExpandService = !isExpandService } ) {

        ExposedDropdownMenu(
            expanded = isExpandService,
            onDismissRequest = { isExpandService = false}) {
            listOfService.forEachIndexed { index, service ->
                DropdownMenuItem(
                    modifier = Modifier.fillMaxWidth(),
                    text = {
                        Text(text = service.label?.en.toString())
                    },
                    onClick = {
                        selectedService = service
                        isExpandService = false
                        onServiceItemSelected(service,index)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(0.dp) ,
            value =  selectedService?.label?.en.toString(),
            onValueChange = {  },
            readOnly = true,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White, // Ensure container color is white
                focusedIndicatorColor = Color.Transparent, // Remove focused indicator (underline)
                unfocusedIndicatorColor = Color.Transparent // Remove unfocused indicator (underline)
            ),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpandService)
            }
        )
    }

}