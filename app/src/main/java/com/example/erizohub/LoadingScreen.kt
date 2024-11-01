package com.example.erizohub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.erizohub.ui.theme.ErizoHubTheme
import kotlinx.coroutines.delay

class LoadingScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ErizoHubTheme {
                AppContent()
            }
        }
    }
}


class ErizoHubTheme {
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
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(3000L)
        isLoading = false
    }

    if (!isLoading) {
        ManejarLogin()
    } else {
        LoadingScreenContent()
    }
}


@Preview(showBackground = true)
@Composable
fun LoadingScreenContent() {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(500L)
        visible = true
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
            Row(modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.End) {
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
            ) }

        }

    }
}



