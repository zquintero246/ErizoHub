package com.example.erizohub

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.erizohub.ErizoHubTheme.Fonts.customFontFamily
import com.google.firebase.auth.FirebaseAuth


@Composable
fun DividerLogin(modifier: Modifier) {
    HorizontalDivider(
        modifier = Modifier.fillMaxWidth(),
        thickness = 21.dp,
        color = Color(0xFFF7F7F7)
    )
}


@Composable
fun ButtonGoogleFacebook(
    text: String,
    logoResId: Int,
    onClickAction: () -> Unit
) {
    Button(
        onClick = { onClickAction() },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White
        ),
        modifier = Modifier
            .width(367.dp)
            .height(85.dp)
            .padding(bottom = 26.dp),
        shape = RoundedCornerShape(50.dp),
        elevation = ButtonDefaults.buttonElevation(2.dp),
    ) {
        Image(
            painter = painterResource(id = logoResId),
            contentDescription = "Logo",
            modifier = Modifier
                .size(34.dp)
                .padding(start = 8.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            color = Color.Black,
            fontSize = 15.sp,
            modifier = Modifier
                .weight(1f)
                .padding(start = 40.dp),
        )

        Image(
            painter = painterResource(id = R.drawable.arrow_icon),
            contentDescription = "Flecha derecha",
            modifier = Modifier
                .size(24.02.dp)
                .padding(end = 8.dp)
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IniciarSesion(navController: NavController){
    var emailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }

    val context = LocalContext.current

    val auth = FirebaseAuth.getInstance()


    Column (modifier = Modifier
        .background(color = ErizoHubTheme.Colors.background)
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
            .background(Color.White, shape = RoundedCornerShape(topStart=30.dp,topEnd=30.dp))
            .padding(top = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(23.dp)
        ) {
            TextField(
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = ErizoHubTheme.Colors.textField,
                ),
                value = emailInput,
                onValueChange = {emailInput = it},
                label = { Text("email",
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
                value = passwordInput,
                onValueChange = {passwordInput = it},
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
        Row(modifier = Modifier.fillMaxWidth().background(Color.White), horizontalArrangement = Arrangement.End){
            Button(onClick = {}, modifier = Modifier
                .width(250.dp)
                .height(40.dp)
                .padding(top = 10.dp),
                colors= ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Text(
                    text = "Olvidaste la contraseña?",
                    fontSize = 12.sp,
                    fontFamily = customFontFamily,
                    color = ErizoHubTheme.Colors.primary,
                    modifier = Modifier.background(color = Color.Transparent)
                )
            }
        }
        Column(modifier = Modifier
            .background(Color.White)
            .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,){

            Button(onClick = {
                if (emailInput.isEmpty() || passwordInput.isEmpty()) {
                    Toast.makeText(context, "Llene todos los campos", Toast.LENGTH_SHORT).show()
                } else {
                    auth.signInWithEmailAndPassword(emailInput, passwordInput)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Iniciar MainActivity tras inicio de sesión exitoso
                                val intent = Intent(context, MainActivity::class.java)
                                context.startActivity(intent)
                                (context as Activity).finish() // Cierra AuthActivity
                            } else {
                                Toast.makeText(context, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
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
                    text = "Iniciar Sesión",
                    color = ErizoHubTheme.Colors.primary,
                    fontFamily = customFontFamily,
                    fontSize = 20.sp
                )
            }
            DividerLogin(Modifier)
            Column{
//                ButtonGoogleFacebook( "Continuar con Google", R.drawable.arrow_icon)
//                ButtonGoogleFacebook( "Continuar con Facebook", R.drawable.arrow_icon)
            }
        }
    }
}