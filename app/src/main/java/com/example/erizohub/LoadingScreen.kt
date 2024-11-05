package com.example.erizohub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.erizohub.ui.theme.ErizoHubTheme
import kotlinx.coroutines.delay

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ErizoHubTheme {
                AppContent()
            }
        }
    }
}

object ErizoHubTheme {
    object Colors {
        val background = Color(4285465817)
        val primary = Color(4294092619)
        val textField = Color(0xFFE5E3E3)
        val textFieldText = Color(4291545802)
    }

    object Fonts {
        val customFontFamily = FontFamily(
            Font(R.font.rubik, FontWeight.Normal)
        )
    }
}

@Composable
fun AppContent() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "loading"
    ) {
        composable("loading") {
            LoadingScreenContent(navController)
        }
        composable("prelogin") {
            PreLogin(
                navController = navController,
                onButtonClickIniciar = { navController.navigate("login") },
                onButtonClickRegistrarse = { navController.navigate("register") }
            )
        }
        composable("login") {
            IniciarSesion(navController)
        }
        composable("register") {
            Registrarse(navController)
        }
    }
}

@Composable
fun LoadingScreenContent(navController: NavController) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
        delay(2000)
        navController.navigate("prelogin") {
            popUpTo("loading") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomEnd,
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
            exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.End
            ) {
                Image(
                    painter = painterResource(id = R.drawable.rectangle_15),
                    modifier = Modifier
                        .width(208.dp)
                        .height(169.dp),
                    contentDescription = "Rectangle 15"
                )
            }
        }

        Image(
            painter = painterResource(id = R.drawable.erizo),
            contentDescription = "Logo",
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize()
        )

        AnimatedVisibility(
            visible = visible,
            enter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn(),
            exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = R.drawable.rectangle_16),
                    modifier = Modifier,
                    contentDescription = "Rectangle 16"
                )
            }
        }
    }
}
