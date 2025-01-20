package com.example.sendmoneyapplication.loginScreen.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.sendmoneyapplication.SendMonyScreen.ui.Space
import com.example.sendmoneyapplication.starter.SendMoneyApplicationTheme
import com.example.sendmoneyapplication.starter.bgColor


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {


    val hardcodedUsername = "testuser"
    val hardcodedPassword = "password123"

    var username by remember { mutableStateOf(TextFieldValue(hardcodedUsername)) }
    var password by remember { mutableStateOf(TextFieldValue(hardcodedPassword)) }
    var errorMessage by remember { mutableStateOf("") }


    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
                //    modifier = Modifier.background(color =  MaterialTheme.colorScheme.primary),
                title = {
                    Text("Sign In",
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center) },
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .background(color = bgColor)
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text("SEND MONEY APP", fontSize = 24.sp, modifier = Modifier.padding(bottom = 24.dp))

                Text("Welcome to Send Money App!", fontSize = 24.sp, modifier = Modifier.padding(bottom = 24.dp))


                // name
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = username,
                    onValueChange = { username = it },
                    placeholder = {
                        Text(
                            text = "Email*"
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White, // Ensure container color is white
                        focusedIndicatorColor = Color.Transparent, // Remove focused indicator (underline)
                        unfocusedIndicatorColor = Color.Transparent // Remove unfocused indicator (underline)
                    ),
                )


                Space()

                // password
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = password,
                    onValueChange = { password = it },
                    placeholder = {
                        Text(
                            text = "Email*"
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White, // Ensure container color is white
                        focusedIndicatorColor = Color.Transparent, // Remove focused indicator (underline)
                        unfocusedIndicatorColor = Color.Transparent // Remove unfocused indicator (underline)
                    ),
                )


                Space()

                if (errorMessage.isNotEmpty()) {
                    Text(errorMessage, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
                }

                // Sign In Button
                Button(
                    onClick = {
                        if (username.text == hardcodedUsername && password.text == hardcodedPassword) {
                            navController.navigate("SendMoneyScreen")
                        } else {
                            errorMessage = "Invalid username or password"
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 50.dp),
                    shape = MaterialTheme.shapes.extraLarge
                ) {
                    Text("Sign In")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("By proceeding you also agree to the terms of service and Privacy policy.",
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    )
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SendMoneyApplicationTheme {
        LoginScreen(navController = rememberNavController())
    }
}