package com.example.sendmoneyapplication.loginScreen.ui


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.sendmoneyapplication.SendMonyScreen.ui.SavedRequestsScreen
import com.example.sendmoneyapplication.SendMonyScreen.ui.SendMoneyScreen
import com.example.sendmoneyapplication.databaseConfig.AppDatabase
import com.example.sendmoneyapplication.starter.SendMoneyApplicationTheme

enum class Screen {
    loginScreen ,
    SendMoneyScreen ,
    savedRequestsScreen ,
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val db = Room.databaseBuilder(
             applicationContext,
            AppDatabase::class.java, "app-database"
        ).build()


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            SendMoneyApplicationTheme {

                NavHost(navController = navController, startDestination = Screen.loginScreen.name) {
                    composable(Screen.loginScreen.toString()) {
                        LoginScreen(navController = navController)
                    }
                    composable(Screen.SendMoneyScreen.toString()) {
                        SendMoneyScreen(db = db , navController = navController)
                    }
                    composable(Screen.savedRequestsScreen.toString()) {
                        SavedRequestsScreen(requestSendDataDao = db.requestInfo())
                    }
                }

            }
        }
    }
}







