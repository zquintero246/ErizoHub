package com.example.erizohub.InicioApp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.erizohub.R

@Composable
fun PreLogin(navController: NavController, onButtonClickIniciar: () -> Unit, onButtonClickRegistrarse: () -> Unit) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
    ) {
        Image(
            painter = painterResource(id = R.drawable.erizo),
            contentDescription = "Logo",
            modifier = Modifier
                .width(400.dp)
                .height(400.dp)
                .padding(bottom = 50.dp)
        )

        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(
                initialOffsetY = { it }
            ) + fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .height(247.dp)
                    .width(412.dp)
                    .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                    .background(color = ErizoHubTheme.Colors.background)
            ) {
                Text(
                    text = "Bienvenido",
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 70.dp),
                    fontSize = 30.sp,
                    fontFamily = ErizoHubTheme.Fonts.customFontFamily,
                    color = ErizoHubTheme.Colors.primary,
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 50.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = onButtonClickIniciar,
                        modifier = Modifier
                            .width(160.dp)
                            .height(54.dp),
                        colors = ButtonDefaults.buttonColors(Color.Black)
                    ) {
                        Text(
                            text = "Iniciar sesión",
                            fontSize = 15.sp,
                            fontFamily = ErizoHubTheme.Fonts.customFontFamily,
                            color = ErizoHubTheme.Colors.primary,
                        )
                    }

                    Button(
                        onClick = onButtonClickRegistrarse,
                        modifier = Modifier
                            .width(160.dp)
                            .height(54.dp),
                        colors = ButtonDefaults.buttonColors(Color.Black)
                    ) {
                        Text(
                            text = "Registrarse",
                            fontSize = 15.sp,
                            fontFamily = ErizoHubTheme.Fonts.customFontFamily,
                            color = ErizoHubTheme.Colors.primary,
                        )
                    }
                }
            }
        }
    }
}





