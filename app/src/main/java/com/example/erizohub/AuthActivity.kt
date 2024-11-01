package com.example.erizohub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val myController = rememberNavController()

            NavHost(
                navController = myController ,
                startDestination = "login" ) {
                composable("login") {
                    IniciarSesion(myController)
                }
                composable("register") {
                    Registrarse(myController)
                }

            }

        }
    }
}