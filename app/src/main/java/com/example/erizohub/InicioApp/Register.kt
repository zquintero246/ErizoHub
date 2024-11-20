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
    // Variables de estado para los campos de entrada.
    var emailinput by remember { mutableStateOf("") } // Campo de entrada para el email.
    var nameUserinput by remember { mutableStateOf("") } // Campo de entrada para el nombre de usuario.
    var profilepictureinput by remember { mutableStateOf("") } // Campo de entrada para la URL de la foto de perfil (opcional).
    var password by remember { mutableStateOf("") } // Campo de entrada para la contraseña.
    val auth = FirebaseAuth.getInstance() // Instancia de Firebase Auth.
    val db = FirebaseFirestore.getInstance() // Instancia de Firestore.
    val context = LocalContext.current // Contexto actual para mostrar mensajes y navegar.

    // Contenedor principal.
    Column(
        modifier = Modifier
            .fillMaxSize() // Ocupa toda la pantalla.
            .background(color = ErizoHubTheme.Colors.background), // Fondo con color del tema.
        horizontalAlignment = Alignment.CenterHorizontally, // Contenido centrado horizontalmente.
        verticalArrangement = Arrangement.Top // Contenido comienza desde la parte superior.
    ) {
        // Encabezado.
        Box(
            modifier = Modifier
                .background(color = ErizoHubTheme.Colors.background) // Fondo con color del tema.
                .fillMaxWidth()
                .height(250.dp), // Altura del encabezado.
            contentAlignment = Alignment.BottomCenter // Contenido centrado en la parte inferior.
        ) {
            Text(
                text = "Registrarse", // Título.
                fontSize = 40.sp, // Tamaño de la fuente grande.
                fontFamily = customFontFamily, // Fuente personalizada.
                color = ErizoHubTheme.Colors.primary // Color primario del tema.
            )
        }

        // Formulario para el registro.
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .width(648.dp)
                .background(Color.White, shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)), // Fondo blanco con bordes redondeados.
            horizontalAlignment = Alignment.CenterHorizontally, // Contenido centrado horizontalmente.
            verticalArrangement = Arrangement.spacedBy(23.dp) // Espaciado entre los elementos.
        ) {
            Spacer(Modifier.height(30.dp)) // Espaciador inicial.

            // Campo para el nombre de usuario.
            TextField(
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = ErizoHubTheme.Colors.textField // Color del campo.
                ),
                value = nameUserinput, // Valor actual del campo.
                onValueChange = { nameUserinput = it }, // Actualiza el valor al escribir.
                label = {
                    Text(
                        "Nombre de usuario", // Etiqueta del campo.
                        color = ErizoHubTheme.Colors.textFieldText, // Color del texto.
                        fontFamily = customFontFamily, // Fuente personalizada.
                        fontSize = 10.sp // Tamaño de la fuente.
                    )
                },
                modifier = Modifier
                    .height(61.dp)
                    .width(367.dp)
                    .clip(RoundedCornerShape(50.dp)) // Bordes redondeados.
                    .background(color = ErizoHubTheme.Colors.textField) // Fondo.
                    .border(10.dp, ErizoHubTheme.Colors.textField, RoundedCornerShape(50.dp)) // Borde.
            )

            // Campo para el email.
            TextField(
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = ErizoHubTheme.Colors.textField
                ),
                value = emailinput,
                onValueChange = { emailinput = it },
                label = {
                    Text(
                        "Correo", // Etiqueta.
                        color = ErizoHubTheme.Colors.textFieldText,
                        fontFamily = customFontFamily,
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

            // Campo para la contraseña.
            TextField(
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = ErizoHubTheme.Colors.textField
                ),
                value = password,
                onValueChange = { password = it },
                label = {
                    Text(
                        "Contraseña",
                        color = ErizoHubTheme.Colors.textFieldText,
                        fontFamily = customFontFamily,
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

        // Botón y opciones de registro.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White), // Fondo blanco.
            verticalArrangement = Arrangement.SpaceBetween, // Espaciado automático.
            horizontalAlignment = Alignment.CenterHorizontally // Contenido centrado horizontalmente.
        ) {
            // Botón para registrarse.
            Button(
                onClick = {
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
                                                navController.navigate("login")
                                            }
                                            .addOnFailureListener { e ->
                                                Toast.makeText(context, "Error al guardar datos: ${e.message}", Toast.LENGTH_SHORT).show()
                                            }
                                    }
                                } else {
                                    val errorMessage = task.exception?.message ?: "Error desconocido"
                                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                                }
                            }
                    }
                },
                modifier = Modifier
                    .padding(top = 29.dp)
                    .width(367.dp)
                    .height(61.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text(
                    text = "Registrarse",
                    color = ErizoHubTheme.Colors.primary,
                    fontFamily = customFontFamily,
                    fontSize = 20.sp
                )
            }

            // Divisor y botón de Google.
            DividerLogin(modifier = Modifier.padding(top = 20.dp, bottom = 20.dp))
            Column {
                ButtonGoogle(
                    text = "Continuar con Google",
                    logoResId = R.drawable.googleicon,
                    onClickAction = { onGoogleSignUpClick() }
                )
            }
        }
    }
}
