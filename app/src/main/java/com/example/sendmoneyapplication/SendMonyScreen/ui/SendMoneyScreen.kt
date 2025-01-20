package com.example.sendmoneyapplication.SendMonyScreen.ui

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.sendmoneyapplication.SendMonyScreen.domain.model.Provider
import com.example.sendmoneyapplication.SendMonyScreen.domain.model.SendMoneyData
import com.example.sendmoneyapplication.SendMonyScreen.domain.model.Service
import com.example.sendmoneyapplication.SendMonyScreen.ui.row.BuildProviderItem
import com.example.sendmoneyapplication.SendMonyScreen.ui.row.BuildRequiredFieldItem
import com.example.sendmoneyapplication.SendMonyScreen.ui.row.BuildServiceIem
import com.example.sendmoneyapplication.databaseConfig.AppDatabase
import com.example.sendmoneyapplication.loginScreen.ui.Screen
import com.example.sendmoneyapplication.starter.bgColor
import kotlinx.serialization.json.Json

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendMoneyScreen(db: AppDatabase, navController: NavHostController) {

    val viewModel = viewModel<SaveDataViewModel>()

    var language by remember { mutableStateOf("en") }
    var amount by remember { mutableStateOf("0") }

    var selectedService by remember { mutableStateOf<Service?>(null) }
    var selectedServiceIndex by remember { mutableStateOf(0) }

    var selectedProvider by remember { mutableStateOf<Provider?>(null) }
    var selectedProviderIndex by remember { mutableStateOf(0) }

    val saveDataResult by viewModel.saveStatus.collectAsState()
    var clearItemAfterSavingData by remember { mutableStateOf(false) }
    var isAnyValidationFailed by remember { mutableStateOf(false) }
    var isAnyValidationSuccess by remember { mutableStateOf(false) }

    val sendMoneyData = getData()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = "Send Money App",
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                        language= if (language == "en") "ar" else "en"
                    }) {
                        Icon(
                            Icons.Filled.Build,
                            contentDescription = "Change Language",
                            tint = Color.White
                        )
                    }

                    IconButton(onClick = {
                        navController.navigate(Screen.savedRequestsScreen.name)
                    }) {
                        Icon(
                            Icons.Filled.List,
                            contentDescription = "Transaction List",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        content = {

            Column(
                modifier = Modifier
                    .background(color = bgColor)
                    .fillMaxSize()
                    .padding(16.dp)
                ,

            ) {

                Space()
                Space()

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)) {
                    Text(
                        text = if (language == "en") sendMoneyData.title.en.toString() else sendMoneyData.title.ar.toString(),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Space()

                    TextTitle(title = "Service")
                    Space(space = 7)
                    BuildServiceIem(
                        listOfService = sendMoneyData.services ,
                        oldSelectedService = selectedService ,
                        onServiceItemSelected =  { serviceItem , index ->
                            selectedService = serviceItem
                            selectedServiceIndex = index
                        })

                    Space()


                    TextTitle(title = "Provider")
                    Space(space = 7)
                    val providerList = getAvailableProvider ( listOfService = sendMoneyData.services ,selectedServiceIndex = selectedServiceIndex)

                    BuildProviderItem(
                        providerList = providerList ,
                        selectedServiceIndex = selectedServiceIndex ,
                        selectedProviderItem = selectedProvider ,
                        selectedProviderIndex = selectedProviderIndex ,
                        onProviderSelected = { provider , index ->
                            selectedProvider = provider
                            selectedProviderIndex = index
                        })


                    BuildRequiredFieldItem(
                        clearItemAfterSavingData = clearItemAfterSavingData ,
                        providerItem = selectedProvider ,
                        language = language ,
                        onAmountEntered = {
                            amount = it
                        },
                        onValidationFailed = { isFieldError ->
                            isAnyValidationFailed = isFieldError
                        },
                        onValidationSucceeded = {
                            isAnyValidationSuccess = it
                        }
                    )


                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            modifier = Modifier
                                .padding(30.dp) ,
                            // .padding(start = 30.dp , end = 30.dp , bottom = 10.dp , top = 10.dp),

                            onClick = {

                                if (isAnyValidationSuccess && isAnyValidationFailed.not()) {

                                    isAnyValidationSuccess= false

                                    viewModel.sendMoneyRequest(
                                        db,
                                        selectedService = selectedService ,
                                        selectedProvider = selectedProvider ,
                                        amount =amount
                                    )
                                }else{
                                    Toast.makeText(navController.context, "Please Enter the Failed Data ", Toast.LENGTH_SHORT).show()
                                }


                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Blue,
                                contentColor = Color.White
                            ) ,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(text = if (language == "en") "Send" else "إرسال")
                        }
                    }


                }

                    Spacer(modifier = Modifier.height(16.dp) )

                saveDataResult?.let { status ->

                    if (status.contains("successfully")){
                        clearItemAfterSavingData = true
                    }
                    Toast.makeText(LocalContext.current, status, Toast.LENGTH_SHORT).show()
                }
            }
        }
    )

}


@Composable
fun TextTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.bodyMedium ,
        fontWeight = FontWeight.Bold
    )
}
@Composable
fun Space( space: Int = 20) = Spacer(modifier = Modifier.height(space.dp))

fun getAvailableProvider(listOfService : List<Service>, selectedServiceIndex: Int): List<Provider> {
    return listOfService[selectedServiceIndex].providers
}


fun getData(): SendMoneyData {

    val information =  """
        {
          "title": {
            "en": "Send Money",
            "ar": "إرسال المال"
          },
          "services": [
            {
              "label": {
                "en": "Bank Transfer"
              },
              "name": "bank_transfer",
              "providers": [
                {
                  "name": "ABC Bank",
                  "id": "101",
                  "required_fields": [
                    {
                      "label": {
                        "en": "Amount (AED)",
                        "ar": "المبلغ (بالدرهم)"
                      },
                      "name": "amount",
                      "placeholder": "0.00",
                      "type": "number",
                      "re": "",
                      "max_length": 0,
                      "validation_error_message": "Amount is required"
                    },
                    {
                      "label": {
                        "en": "Bank Account Number",
                        "ar": "رقم حساب البنك"
                      },
                      "name": "bank_account_number",
                      "placeholder": null,
                      "type": "text",
                      "validation": "^[A-Za-z0-9 ]{1,64}${'$'}",
                      "max_length": "64",
                      "validation_error_message": "Bank account number is required"
                    },
                    {
                      "label": {
                        "en": "First name",
                        "ar": "الاسم الأول"
                      },
                      "name": "firstname",
                      "placeholder": "Please enter first name",
                      "type": "text",
                      "validation": "^[\\s\\S]*",
                      "max_length": 250,
                      "validation_error_message": "First name is required"
                    },
                    {
                      "label": {
                        "en": "Last name",
                        "ar": "اسم العائلة"
                      },
                      "name": "lastname",
                      "placeholder": "Please enter last name",
                      "type": "text",
                      "validation": "^[\\s\\S]*",
                      "max_length": 250,
                      "validation_error_message": "Last name is required"
                    }
                  ]
                },
                {
                  "name": "XYZ Bank",
                  "id": "102",
                  "required_fields": [
                    {
                      "label": {
                        "en": "Amount (AED)",
                        "ar": "المبلغ (بالدرهم)"
                      },
                      "name": "amount",
                      "placeholder": "0.00",
                      "type": "number",
                      "validation": "",
                      "max_length": 0,
                      "validation_error_message": "Amount is required"
                    },
                    {
                      "label": {
                        "en": "Bank Account Number",
                        "ar": "رقم حساب البنك"
                      },
                      "name": "bank_account_number",
                      "placeholder": null,
                      "type": "text",
                      "validation": "^[A-Za-z0-9 ]{1,64}${'$'}",
                      "max_length": "64",
                      "validation_error_message": "Bank account number is required"
                    },
                    {
                      "label": {
                        "en": "Full Name",
                        "ar": "الاسم الكامل"
                      },
                      "name": "full_name",
                      "placeholder": {
                        "en": "Enter full name",
                        "ar": "أدخل الاسم الكامل"
                      },
                      "type": "text",
                      "validation": "^[\\s\\S]*",
                      "max_length": 250,
                      "validation_error_message": {
                        "en": "Full name is required",
                        "ar": "الاسم الكامل مطلوب"
                      }
                    },
                    {
                      "label": {
                        "en": "Province State",
                        "ar": "المقاطعة / الولاية"
                      },
                      "name": "province_state",
                      "placeholder": null,
                      "options": [
                        {
                          "label": "Abu Dhabi",
                          "name": "abu_dhabi"
                        },
                        {
                          "label": "Dubai",
                          "name": "dubai"
                        }
                      ],
                      "type": "option",
                      "validation": "^[\\s\\S]*",
                      "max_length": "",
                      "validation_error_message": ""
                    }
                  ]
                },
                {
                  "name": "Global Bank",
                  "id": "103",
                  "required_fields": [
                    {
                      "label": {
                        "en": "Amount (AED)",
                        "ar": "المبلغ (بالدرهم)"
                      },
                      "name": "amount",
                      "placeholder": "0.00",
                      "type": "number",
                      "validation": "",
                      "max_length": 0,
                      "validation_error_message": "Amount is required"
                    },
                    {
                      "label": {
                        "en": "Bank Account Number",
                        "ar": "رقم حساب البنك"
                      },
                      "name": "bank_account_number",
                      "placeholder": null,
                      "type": "text",
                      "validation": "^[A-Za-z0-9 ]{1,64}${'$'}",
                      "max_length": "64",
                      "validation_error_message": "Bank account number is required"
                    },
                    {
                      "label": {
                        "en": "Date of Birth (YYYY-MM-DD)",
                        "ar": "تاريخ الميلاد (سنة - شهر - يوم)"
                      },
                      "name": "date_of_birth",
                      "placeholder": "YYYY-MM-DD",
                      "type": "text",
                      "validation": "^(?:19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])${'$'}",
                      "max_length": "",
                      "validation_error_message": "Date of birth is required"
                    },
                    {
                      "label": {
                        "en": "Gender",
                        "ar": "الجنس"
                      },
                      "name": "gender",
                      "placeholder": null,
                      "type": "option",
                      "options": [
                        {
                          "label": "Male",
                          "name": "M"
                        },
                        {
                          "label": "Female",
                          "name": "F"
                        }
                      ],
                      "validation": "^[\\s\\S]*",
                      "max_length": "",
                      "validation_error_message": ""
                    }
                  ]
                },
                {
                  "name": "First Bank",
                  "id": "104",
                  "required_fields": [
                    {
                      "label": {
                        "en": "Amount (AED)",
                        "ar": "المبلغ (بالدرهم)"
                      },
                      "name": "amount",
                      "placeholder": "0.00",
                      "type": "number",
                      "validation": "",
                      "max_length": 0,
                      "validation_error_message": "Amount is required"
                    },
                    {
                      "label": {
                        "en": "Bank Account Number",
                        "ar": "رقم حساب البنك"
                      },
                      "name": "bank_account_number",
                      "placeholder": null,
                      "type": "text",
                      "validation": "^[A-Za-z0-9 ]{1,64}${'$'}",
                      "max_length": "64",
                      "validation_error_message": "Bank account number is required"
                    },
                    {
                      "label": {
                        "en": "Last name",
                        "ar": "اسم العائلة"
                      },
                      "name": "lastname",
                      "placeholder": "Please enter last name",
                      "type": "text",
                      "validation": "^[\\s\\S]*",
                      "max_length": 250,
                      "validation_error_message": "Last name is required"
                    },
                    {
                      "label": {
                        "en": "Gender",
                        "ar": "الجنس"
                      },
                      "name": "gender",
                      "placeholder": null,
                      "type": "option",
                      "options": [
                        {
                          "label": "Male",
                          "name": "M"
                        },
                        {
                          "label": "Female",
                          "name": "F"
                        }
                      ],
                      "validation": "^[\\s\\S]*",
                      "max_length": "",
                      "validation_error_message": ""
                    }
                  ]
                },
                {
                  "name": "United Bank",
                  "id": "105",
                  "required_fields": [
                    {
                      "label": {
                        "en": "Amount (AED)",
                        "ar": "المبلغ (بالدرهم)"
                      },
                      "name": "amount",
                      "placeholder": "0.00",
                      "type": "number",
                      "validation": "",
                      "max_length": 0,
                      "validation_error_message": "Amount is required"
                    },
                    {
                      "label": {
                        "en": "Bank Account Number",
                        "ar": "رقم حساب البنك"
                      },
                      "name": "bank_account_number",
                      "placeholder": null,
                      "type": "text",
                      "validation": "^[A-Za-z0-9 ]{1,64}${'$'}",
                      "max_length": "64",
                      "validation_error_message": "Bank account number is required"
                    },
                    {
                      "label": {
                        "en": "Last name",
                        "ar": "اسم العائلة"
                      },
                      "name": "lastname",
                      "placeholder": "Please enter last name",
                      "type": "text",
                      "validation": "^[\\s\\S]*",
                      "max_length": 250,
                      "validation_error_message": "Last name is required"
                    }
                  ]
                }
              ]
            },
            {
              "label": {
                "en": "Wallet Transfer"
              },
              "name": "wallet_transfer",
              "providers": [
                {
                  "name": "ABC Wallet",
                  "id": "501",
                  "required_fields": [
                    {
                      "label": {
                        "en": "Amount (AED)",
                        "ar": "المبلغ (بالدرهم)"
                      },
                      "name": "amount",
                      "placeholder": "0.00",
                      "type": "number",
                      "validation": "",
                      "max_length": 0,
                      "validation_error_message": "Amount is required"
                    },
                    {
                      "label": {
                        "en": "Mobile Number",
                        "ar": "رقم الهاتف المحمول"
                      },
                      "name": "msisdn",
                      "placeholder": null,
                      "type": "msisdn",
                      "validation": "^\\+?[1-9][0-9]{6,14}${'$'}",
                      "max_length": "14",
                      "validation_error_message": "Mobile number is required"
                    },
                    {
                      "label": {
                        "en": "First name",
                        "ar": "الاسم الأول"
                      },
                      "name": "firstname",
                      "placeholder": "Please enter first name",
                      "type": "text",
                      "validation": "^[\\s\\S]*",
                      "max_length": 250,
                      "validation_error_message": "First name is required"
                    },
                    {
                      "label": {
                        "en": "Last name",
                        "ar": "اسم العائلة"
                      },
                      "name": "lastname",
                      "placeholder": "Please enter last name",
                      "type": "text",
                      "validation": "^[\\s\\S]*",
                      "max_length": 250,
                      "validation_error_message": "Last name is required"
                    }
                  ]
                },
                {
                  "name": "Quick Pay",
                  "id": "502",
                  "required_fields": [
                    {
                      "label": {
                        "en": "Amount (AED)",
                        "ar": "المبلغ (بالدرهم)"
                      },
                      "name": "amount",
                      "placeholder": "0.00",
                      "type": "number",
                      "validation": "",
                      "max_length": 0,
                      "validation_error_message": "Amount is required"
                    },
                    {
                      "label": {
                        "en": "Mobile Number",
                        "ar": "رقم الهاتف المحمول"
                      },
                      "name": "msisdn",
                      "placeholder": null,
                      "type": "msisdn",
                      "validation": "^\\+?[1-9][0-9]{6,14}${'$'}",
                      "max_length": "14",
                      "validation_error_message": "Mobile number is required"
                    },
                    {
                      "label": {
                        "en": "Full Name",
                        "ar": "الاسم الكامل"
                      },
                      "name": "full_name",
                      "placeholder": {
                        "en": "Enter full name",
                        "ar": "أدخل الاسم الكامل"
                      },
                      "type": "text",
                      "validation": "^[\\s\\S]*",
                      "max_length": 250,
                      "validation_error_message": {
                        "en": "Full name is required",
                        "ar": "الاسم الكامل مطلوب"
                      }
                    },
                    {
                      "label": {
                        "en": "Gender",
                        "ar": "الجنس"
                      },
                      "name": "gender",
                      "placeholder": null,
                      "type": "option",
                      "options": [
                        {
                          "label": "Male",
                          "name": "M"
                        },
                        {
                          "label": "Female",
                          "name": "F"
                        }
                      ],
                      "validation": "^[\\s\\S]*",
                      "max_length": "",
                      "validation_error_message": ""
                    }
                  ]
                },
                {
                  "name": "Flash Wallet",
                  "id": "503",
                  "required_fields": [
                    {
                      "label": {
                        "en": "Amount (AED)",
                        "ar": "المبلغ (بالدرهم)"
                      },
                      "name": "amount",
                      "placeholder": "0.00",
                      "type": "number",
                      "validation": "",
                      "max_length": 0,
                      "validation_error_message": "Amount is required"
                    },
                    {
                      "label": {
                        "en": "Mobile Number",
                        "ar": "رقم الهاتف المحمول"
                      },
                      "name": "msisdn",
                      "placeholder": null,
                      "type": "msisdn",
                      "validation": "^\\+?[1-9][0-9]{6,14}${'$'}",
                      "max_length": "14",
                      "validation_error_message": "Mobile number is required"
                    },
                    {
                      "label": {
                        "en": "Province State",
                        "ar": "المقاطعة / الولاية"
                      },
                      "name": "province_state",
                      "placeholder": null,
                      "options": [
                        {
                          "label": "Abu Dhabi",
                          "name": "abu_dhabi"
                        },
                        {
                          "label": "Dubai",
                          "name": "dubai"
                        },
                        {
                          "label": "Sharjah",
                          "name": "sharjah"
                        }
                      ],
                      "type": "option",
                      "validation": "^[\\s\\S]*",
                      "max_length": "",
                      "validation_error_message": ""
                    },
                    {
                      "label": {
                        "en": "Full Name",
                        "ar": "الاسم الكامل"
                      },
                      "name": "full_name",
                      "placeholder": {
                        "en": "Enter full name",
                        "ar": "أدخل الاسم الكامل"
                      },
                      "type": "text",
                      "validation": "^[\\s\\S]*",
                      "max_length": 250,
                      "validation_error_message": {
                        "en": "Full name is required",
                        "ar": "الاسم الكامل مطلوب"
                      }
                    }
                  ]
                },
                {
                  "name": "Smart Pay",
                  "id": "504",
                  "required_fields": [
                    {
                      "label": {
                        "en": "Amount (AED)",
                        "ar": "المبلغ (بالدرهم)"
                      },
                      "name": "amount",
                      "placeholder": "0.00",
                      "type": "number",
                      "validation": "",
                      "max_length": 0,
                      "validation_error_message": "Amount is required"
                    },
                    {
                      "label": {
                        "en": "Mobile Number",
                        "ar": "رقم الهاتف المحمول"
                      },
                      "name": "msisdn",
                      "placeholder": null,
                      "type": "msisdn",
                      "validation": "^\\+?[1-9][0-9]{6,14}${'$'}",
                      "max_length": "14",
                      "validation_error_message": "Mobile number is required"
                    },
                    {
                      "label": {
                        "en": "Full Name",
                        "ar": "الاسم الكامل"
                      },
                      "name": "full_name",
                      "placeholder": {
                        "en": "Enter full name",
                        "ar": "أدخل الاسم الكامل"
                      },
                      "type": "text",
                      "validation": "^[\\s\\S]*",
                      "max_length": 250,
                      "validation_error_message": {
                        "en": "Full name is required",
                        "ar": "الاسم الكامل مطلوب"
                      }
                    }
                  ]
                }
              ]
            }
          ]
        }

    """.trimIndent()
    val jsonParser = Json { ignoreUnknownKeys = true }
    val sendMoneyData = jsonParser.decodeFromString<SendMoneyData>(information)

    return  sendMoneyData ;

}
