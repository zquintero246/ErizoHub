package com.example.erizohub

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.erizohub.ErizoHubTheme.Fonts.customFontFamily
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Registrarse(navController: NavController, onGoogleSignUpClick: () -> Unit) {
    // Variables de estado
    var emailinput by remember { mutableStateOf("") }
    var nameUserinput by remember { mutableStateOf("") }
    var profilepictureinput by remember { mutableStateOf("https://i.pinimg.com/736x/d7/49/10/d74910bede462ec2c81f40da876d6f1a.jpg") }
    var password by remember { mutableStateOf("") }
    var passwordConfirmation by remember { mutableStateOf("") }
    var registerError by remember { mutableStateOf<String?>(null) }
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current


    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = ErizoHubTheme.Colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        Box(modifier = Modifier
            .background(color = ErizoHubTheme.Colors.background)
            .fillMaxWidth()
            .height(250.dp),
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
                .background(Color.White, shape = RoundedCornerShape(topStart=30.dp,topEnd=30.dp)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(23.dp)
            ) {
                Spacer(Modifier.height(30.dp))
                TextField(
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = ErizoHubTheme.Colors.textField,
                    ),
                    value = nameUserinput,
                    onValueChange = {nameUserinput = it},
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
                    value = emailinput,
                    onValueChange = {emailinput = it},
                    label = {
                        Text("Correo",
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
                .fillMaxSize()
                .background(color = Color.White),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,){

                Button( onClick = {if (nameUserinput.isEmpty()
                    || emailinput.isEmpty() || profilepictureinput.isEmpty()|| password.isEmpty()|| passwordConfirmation.isEmpty()){
                    Toast.makeText(context, "Llene todos los campos", Toast.LENGTH_SHORT).show()
                }else{
                    if (password == passwordConfirmation) {
                        registerError = null

                        auth.createUserWithEmailAndPassword(emailinput, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Guardar datos en Firestore
                                    val user = auth.currentUser
                                    val newUser = User (
                                        userName = nameUserinput,
                                        emailc = emailinput,
                                        profilePictureUrl = profilepictureinput,
                                        )
                                    db.collection("users").document(user!!.uid).set(newUser)
                                    Toast.makeText(context, "Se guardo correctamente",Toast.LENGTH_SHORT).show()
                                    navController.navigate("login")
                                } else {
                                    Toast.makeText(context, "Error al registrar usuario",Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        registerError = "Las contraseñas no coinciden"
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
                        text = "Registrarse",
                        color = ErizoHubTheme.Colors.primary,
                        fontFamily = customFontFamily,
                        fontSize = 20.sp
                    )
                }
                DividerLogin(modifier = Modifier.padding(top = 20.dp, bottom = 20.dp))
                Column{
                    ButtonGoogle(
                        text = "Continuar con Google",
                        logoResId = R.drawable.googleicon,
                        onClickAction = { onGoogleSignUpClick() }
                    )
                }
            }
    }
}