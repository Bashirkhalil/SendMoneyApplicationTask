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
import com.example.sendmoneyapplication.SendMonyScreen.domain.model.Options
import com.example.sendmoneyapplication.SendMonyScreen.domain.model.RequiredField
import com.example.sendmoneyapplication.SendMonyScreen.ui.Space
import com.example.sendmoneyapplication.SendMonyScreen.ui.TextTitle

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BuildOptionIem(
    language: String ,
    requiredField: RequiredField,
    onOptionSelectedIndex: (Int) -> Unit,
    onOptionSelected: (Options) -> Unit  ) {

    val mList = requiredField.options!!
    var isExpandService by remember { mutableStateOf(false) }
    var optionsItem by remember { mutableStateOf(mList[0]) }

    (if (language == "en") requiredField.label?.en else requiredField.label?.ar)?.let {
        TextTitle(title = it)
    }

    Space(space = 7)

    ExposedDropdownMenuBox(
        expanded = isExpandService,
        onExpandedChange = {
            isExpandService = !isExpandService } ) {

        ExposedDropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            expanded = isExpandService,
            onDismissRequest = { isExpandService = false}) {
            mList.forEachIndexed { index, options ->
                DropdownMenuItem(
                    text = {
                        Text(text = options.labelName.toString())
                    },
                    onClick = {
                        isExpandService = false
                        onOptionSelectedIndex(index)
                        onOptionSelected(options)
                        optionsItem = options
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
            value =  optionsItem.labelName.toString(),
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