package com.example.erizohub

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.erizohub.ErizoHubTheme.Fonts.customFontFamily


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Registrarse(navController: NavController){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }

    Column (modifier = Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Box(modifier = Modifier
            .background(color = ErizoHubTheme.Colors.background)
            .fillMaxWidth()
            .zIndex(1f)
            .height(300.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = "Registrarse",
                modifier = Modifier,
                fontSize = 40.sp,
                fontFamily = customFontFamily,
                color = ErizoHubTheme.Colors.primary
            )
        }


        Column (modifier = Modifier
            .fillMaxWidth()
            .width(648.dp)
            .padding(top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(23.dp)
        ) {
            TextField(
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = ErizoHubTheme.Colors.textField,
                ),
                value = username,
                onValueChange = {username = it},
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
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = ErizoHubTheme.Colors.textField,
                ),
                value = email,
                onValueChange = {email = it},
                label = {
                    Text("Correo unab",
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
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = ErizoHubTheme.Colors.textField,
                ),
                value = password,
                onValueChange = {password = it},
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
        Column(modifier = Modifier
            .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,){

            Button( onClick = {

            },
                modifier = Modifier
                    .padding(top = 29.dp)
                    .width(367.dp)
                    .height(61.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                ),
            ){
                Text(
                    text = "Registrarse",
                    color = ErizoHubTheme.Colors.primary,
                    fontFamily = customFontFamily,
                    fontSize = 20.sp
                )
            }
            DividerLogin(modifier = Modifier.padding(top = 20.dp, bottom = 20.dp))
            Column{
                ButtonGoogleFacebook( "Continuar con Google", R.drawable.arrow_icon)
                ButtonGoogleFacebook( "Continuar con Facebook", R.drawable.arrow_icon)
            }
        }
    }
}