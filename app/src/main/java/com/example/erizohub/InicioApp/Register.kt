package com.example.erizohub.InicioApp

import android.util.Log
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
import com.example.erizohub.InicioApp.ErizoHubTheme.Fonts.customFontFamily
import com.example.erizohub.R
import com.example.erizohub.ClasesBD.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Registrarse(navController: NavController, onGoogleSignUpClick: () -> Unit) {
    // Variables de estado
    var emailinput by remember { mutableStateOf("") }
    var nameUserinput by remember { mutableStateOf("") }
    var profilepictureinput by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
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

                Button(onClick = {
                    if (nameUserinput.isEmpty() || emailinput.isEmpty() || password.isEmpty()) {
                        Toast.makeText(context, "Llene todos los campos", Toast.LENGTH_SHORT).show()
                    } else {
                        auth.createUserWithEmailAndPassword(emailinput, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val user = auth.currentUser
                                    if (user != null) {
                                        val newUser = User(
                                            userId = user.uid,
                                            userName = nameUserinput,
                                            emailc = emailinput,
                                            profilePictureUrl = profilepictureinput

                                        )
                                        db.collection("users").document(user.uid).set(newUser)
                                            .addOnSuccessListener {
                                                Toast.makeText(context, "Se guardó correctamente", Toast.LENGTH_SHORT).show()
                                                nameUserinput = ""
                                                emailinput = ""
                                                password = ""
                                                profilepictureinput = ""
                                                navController.navigate("login")
                                            }
                                            .addOnFailureListener { e ->
                                                Toast.makeText(context, "Error al guardar datos: ${e.message}", Toast.LENGTH_SHORT).show()
                                            }
                                    }
                                } else {
                                    if (task.exception is FirebaseAuthUserCollisionException) {
                                        Toast.makeText(context, "Este correo ya está registrado. Intente iniciar sesión.", Toast.LENGTH_LONG).show()
                                        navController.navigate("login")
                                    } else {
                                        val errorMessage = task.exception?.message ?: "Error desconocido"
                                        Toast.makeText(context, "Error al registrar usuario: $errorMessage", Toast.LENGTH_LONG).show()
                                        Log.e("AuthError", "Error al registrar usuario", task.exception)
                                    }
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
                ) {
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