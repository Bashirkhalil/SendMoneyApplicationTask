package com.example.sendmoneyapplication.SendMonyScreen.ui.row

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
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
import com.example.sendmoneyapplication.SendMonyScreen.domain.model.Provider


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BuildProviderItem(providerList: List<Provider>,
                      selectedServiceIndex: Int   ,
                      selectedProviderItem :Provider? ,
                      selectedProviderIndex :Int ,
                      onProviderSelected : (Provider ,Int ) -> Unit ) {

    var isExpandProvider by remember { mutableStateOf(false) }


    var selectedProvider by remember {
        if(selectedProviderItem == null) {
            mutableStateOf(providerList[0])
        }else{
            mutableStateOf(selectedProviderItem)
        }
    }

//
//    if(oldSelectedService == null) {
//        onServiceItemSelected(selectedService,0)
//    }



//    selectedProvider = providerList[0]
//    onProviderSelected(providerList[0],0)

    ExposedDropdownMenuBox(

        expanded = isExpandProvider,
        onExpandedChange = { isExpandProvider = !isExpandProvider } ) {
        ExposedDropdownMenu (

            expanded = isExpandProvider,
            onDismissRequest = { isExpandProvider = false}) {
            providerList.forEachIndexed { index, provider ->
                DropdownMenuItem(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = {
                        Text(text = provider.name.toString())
                    },
                    onClick = {
                        isExpandProvider = false
                        selectedProvider = providerList[index]
                        onProviderSelected(providerList[index],index)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
       Row {
           TextField(
               modifier = Modifier
                   .fillMaxWidth()
                   .menuAnchor()
                   .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                   .padding(0.dp) ,
               value =  selectedProvider?.name.toString(),
               onValueChange = {  },
               readOnly = true,
               colors = TextFieldDefaults.textFieldColors(
                   containerColor = Color.White, // Ensure container color is white
                   focusedIndicatorColor = Color.Transparent, // Remove focused indicator (underline)
                   unfocusedIndicatorColor = Color.Transparent // Remove unfocused indicator (underline)
               ),
               trailingIcon = {
                   ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpandProvider)
               }
           )
       }
    }



}