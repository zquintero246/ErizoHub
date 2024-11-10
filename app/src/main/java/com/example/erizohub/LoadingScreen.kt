package com.example.erizohub


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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.delay



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
fun LoadingScreenContent(navController: NavController) {
    var visible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    FirebaseApp.initializeApp(context)


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
