package com.example.sendmoneyapplication.SendMonyScreen.ui.row

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.sendmoneyapplication.SendMonyScreen.domain.model.FieldLabel
import com.example.sendmoneyapplication.SendMonyScreen.domain.model.FieldType
import com.example.sendmoneyapplication.SendMonyScreen.domain.model.RequiredField
import com.example.sendmoneyapplication.SendMonyScreen.domain.model.Provider
import com.example.sendmoneyapplication.SendMonyScreen.domain.model.getKeyBoardType
import com.example.sendmoneyapplication.SendMonyScreen.domain.model.getTextLength
import com.example.sendmoneyapplication.SendMonyScreen.domain.model.getValidationMessage
import com.example.sendmoneyapplication.SendMonyScreen.ui.Space
import com.example.sendmoneyapplication.SendMonyScreen.ui.TextTitle

@Composable
fun BuildRequiredFieldItem(
    clearItemAfterSavingData: Boolean,
    providerItem: Provider?,
    language: String,
    onAmountEntered: (String) -> Unit  ,
    onValidationFailed: (Boolean) -> Unit  ,
   onValidationSucceeded: (Boolean) -> Unit

   ){

    var isAnyValidationFailed by remember { mutableStateOf(false) }

    providerItem?.requiredRequiredFields?.forEach { requiredRequiredField ->
        Space()
        when (requiredRequiredField.type) {

            FieldType.option -> {
                BuildOptionIem(language = language,
                    requiredField = requiredRequiredField,
                    onOptionSelectedIndex = { index ->
                    },
                    onOptionSelected = { options ->
                    })
            }

            else -> {
                InputField(
                    clearItemAfterSavingData = clearItemAfterSavingData,
                    requiredField = requiredRequiredField,
                    language = language,
                    onAmountEntered = onAmountEntered ,
                    onValidationFailed = { isFieldError ->
                        isAnyValidationFailed = isFieldError
                        onValidationFailed(isAnyValidationFailed)
                    },
                    onValidationSucceeded = { isFieldValid ->
                        if (isFieldValid) {
                            isAnyValidationFailed = false
                        }
                        onValidationSucceeded(!isAnyValidationFailed)
                    }
                )
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    clearItemAfterSavingData: Boolean,
    requiredField: RequiredField,
    language: String,
    onAmountEntered: (String) -> Unit ,
    onValidationFailed: (Boolean) -> Unit ,
    onValidationSucceeded: (Boolean) -> Unit
){


    var value by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    fun validateInput(input: String) {

        if (requiredField.name.toString() == "amount") {
            onAmountEntered(input)
        }

       if (input.isEmpty()) {
                isError = true
                onValidationFailed(true)
                errorMessage = requiredField.getValidationMessage(language)
                return
        } else {
            if (requiredField.validation?.length != 0 && requiredField.validation != null && requiredField.validation?.toRegex()?.matches(input) == false) {
                isError = true
                errorMessage = requiredField.getValidationMessage(language)
                onValidationFailed(true)
            } else {
                isError = false
                errorMessage = ""
                onValidationSucceeded(true)
            }
        }


    }

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        (if (language == "en") requiredField.label?.en else requiredField.label?.ar)?.let {
            TextTitle(title = it)
        }
        Space(space = 7)
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = {
                if (it.length <= requiredField.getTextLength()) {
                    value = it
                    validateInput(input = it)
                }
            },
            placeholder = {
                Text(
                    text = if (language == "en") requiredField.label?.en.toString() else requiredField.label?.ar.toString()
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = requiredField.getKeyBoardType()),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            isError = isError
        )

        if (isError) {
            Text(
                text = errorMessage,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

    }


    LaunchedEffect(clearItemAfterSavingData) {
        if (clearItemAfterSavingData) {
            value = ""
            isError = false
            errorMessage = ""
        }
    }
}




