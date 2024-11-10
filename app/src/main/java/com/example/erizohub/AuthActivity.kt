package com.example.erizohub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp


class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase
        initializeFirebase {
            setContent {
                val myController = rememberNavController()

                NavHost(
                    navController = myController,
                    startDestination = "loading"
                ) {
                    composable("loading") {
                        LoadingScreenContent(myController)
                    }
                    composable("prelogin") {
                        PreLogin(
                            navController = myController,
                            onButtonClickIniciar = { myController.navigate("login") },
                            onButtonClickRegistrarse = {
                                myController.navigate("register")
                            }
                        )
                    }
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

    private fun initializeFirebase(onInitialized: () -> Unit) {
        FirebaseApp.initializeApp(this).also {
            onInitialized()
        }
    }
}

