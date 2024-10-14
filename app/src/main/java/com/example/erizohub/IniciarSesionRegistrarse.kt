package com.example.erizohub

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.erizohub.ErizoHubTheme.Fonts.customFontFamily


@Preview(showBackground = true)
@Composable
fun IniciarSesion(){
    Column (modifier = Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Column(modifier = Modifier
            .background(color = ErizoHubTheme.Colors.background)
            .fillMaxWidth()
            .height(300.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = "Iniciar Sesión",
                modifier = Modifier,
                fontSize = 40.sp,
                fontFamily = customFontFamily,
                color = ErizoHubTheme.Colors.primary
            )
        }
        Column (modifier = Modifier
                .fillMaxWidth()
                .width(648.dp)
                .clip(RoundedCornerShape(topEndPercent = 40, topStartPercent = 40))
                .padding(top = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(23.dp)
        ) {
                TextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("Nombre de usuario",
                        color = ErizoHubTheme.Colors.textFieldText,
                        fontFamily = customFontFamily,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        fontSize = 10.sp
                    )

                    },
                    modifier = Modifier
                        .height(61.dp)
                        .width(367.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(color = ErizoHubTheme.Colors.textField)
                        .border(10.dp, ErizoHubTheme.Colors.textField, RoundedCornerShape(50.dp))

                )
                TextField(
                    value = "",
                    onValueChange = {},
                    label = {
                        Text("Contraseña",
                            color = ErizoHubTheme.Colors.textFieldText,
                            fontFamily = customFontFamily,
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            fontSize = 10.sp
                            )
                            },
                    modifier = Modifier
                        .height(61.dp)
                        .width(367.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(color = ErizoHubTheme.Colors.textField)
                        .border(10.dp, ErizoHubTheme.Colors.textField, RoundedCornerShape(50.dp))
                )
        }
        Row {
            Button(onClick = {}, modifier = Modifier
                .background(color = Color.White)
            ) {
                Text(
                    text = "Olvidaste la contraseña?",
                    fontSize = 12.sp,
                    fontFamily = customFontFamily,
                    color = ErizoHubTheme.Colors.primary,
                    modifier = Modifier.background(color = Color.White)
                )
            }
        }
    }
}

@Composable
fun Registrarse(){
    Column (modifier = Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Column {
            Text(
                text = "Iniciar Sesión",
                modifier = Modifier,
                fontSize = 20.sp
            )
        }
        Column (modifier = Modifier
            .fillMaxWidth()
            .width(648.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TextField(
                value = "",
                onValueChange = {},
                label = { Text("Nombre de usuario") },
                modifier = Modifier
                    .height(61.dp)
                    .width(367.dp)
                    .padding(bottom = 23.dp)
            )
            TextField(
                value = "",
                onValueChange = {},
                label = { Text("Correo unab") },
                modifier = Modifier
                    .height(61.dp)
                    .width(367.dp)
                    .padding(bottom = 23.dp)
            )
            TextField(
                value = "",
                onValueChange = {},
                label = { Text("Contraseña") },
                modifier = Modifier.height(61.dp).width(367.dp)
            )
        }
    }
}