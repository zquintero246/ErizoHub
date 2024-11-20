package com.example.erizohub.InicioApp

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
import com.example.erizohub.InicioApp.ErizoHubTheme.Fonts.customFontFamily
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.example.erizohub.MainActivity
import com.example.erizohub.R


@Composable
fun DividerLogin(modifier: Modifier) {
    // Componente que crea un divisor horizontal en la pantalla de inicio de sesión.
    HorizontalDivider(
        modifier = Modifier.fillMaxWidth(), // El divisor ocupa todo el ancho disponible.
        thickness = 21.dp, // Grosor del divisor.
        color = Color(0xFFF7F7F7) // Color gris claro del divisor.
    )
}

@Composable
fun ButtonGoogle(
    text: String, // Texto que se mostrará en el botón.
    logoResId: Int, // ID del recurso de la imagen del logo (Google).
    onClickAction: () -> Unit // Acción a ejecutar cuando se presione el botón.
) {
    Button(
        onClick = { onClickAction() }, // Llama a la acción proporcionada al hacer clic.
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White // Fondo blanco para el botón.
        ),
        modifier = Modifier
            .width(367.dp) // Ancho del botón.
            .height(85.dp) // Altura del botón.
            .padding(bottom = 26.dp), // Margen inferior.
        shape = RoundedCornerShape(50.dp), // Bordes redondeados.
        elevation = ButtonDefaults.buttonElevation(2.dp), // Elevación del botón para efecto de sombra.
    ) {
        // Imagen del logo (por ejemplo, Google) al inicio del botón.
        Image(
            painter = painterResource(id = logoResId), // Carga el recurso de imagen del logo.
            contentDescription = "Logo", // Descripción para accesibilidad.
            modifier = Modifier
                .size(34.dp) // Tamaño del logo.
                .padding(start = 8.dp) // Margen izquierdo.
        )

        Spacer(modifier = Modifier.width(8.dp)) // Espacio entre el logo y el texto.

        // Texto que muestra el mensaje en el botón (por ejemplo, "Continuar con Google").
        Text(
            text = text, // Texto dinámico que se muestra en el botón.
            color = Color.Black, // Color negro para el texto.
            fontSize = 15.sp, // Tamaño de la fuente.
            modifier = Modifier
                .weight(1f) // El texto ocupa todo el espacio restante disponible.
                .padding(start = 40.dp) // Margen izquierdo para centrar el texto.
        )

        // Flecha al final del botón para indicar acción (navegación o continuación).
        Image(
            painter = painterResource(id = R.drawable.arrow_icon), // Imagen de la flecha.
            contentDescription = "Flecha derecha", // Descripción para accesibilidad.
            modifier = Modifier
                .size(24.02.dp) // Tamaño de la flecha.
                .padding(end = 8.dp) // Margen derecho.
        )
    }
}





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IniciarSesion(navController: NavController, onGoogleSignInClick: () -> Unit) {
    // Variables de estado para los campos de entrada de email y contraseña.
    var emailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }

    // Obtención del contexto actual y la instancia de autenticación de Firebase.
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val scrollState = rememberScrollState() // Estado para habilitar desplazamiento vertical.

    // Contenedor principal.
    Column(
        modifier = Modifier
            .background(color = ErizoHubTheme.Colors.background) // Fondo del tema.
            .fillMaxSize() // Ocupa todo el tamaño disponible.
            .verticalScroll(scrollState), // Habilita el desplazamiento.
        horizontalAlignment = Alignment.CenterHorizontally // Alineación horizontal centrada.
    ) {
        // Encabezado con título "Iniciar Sesión".
        Column(
            modifier = Modifier
                .background(color = ErizoHubTheme.Colors.background)
                .fillMaxWidth()
                .height(250.dp), // Altura del encabezado.
            horizontalAlignment = Alignment.CenterHorizontally, // Centrado horizontal.
            verticalArrangement = Arrangement.Bottom // Posiciona el contenido en la parte inferior.
        ) {
            Text(
                text = "Iniciar Sesión", // Título.
                fontSize = 40.sp, // Tamaño de fuente grande.
                fontFamily = customFontFamily, // Fuente personalizada.
                color = ErizoHubTheme.Colors.primary // Color primario del tema.
            )
        }

        // Formulario de inicio de sesión.
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .width(648.dp)
                .background(
                    Color.White,
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                ), // Fondo blanco con bordes redondeados.
            horizontalAlignment = Alignment.CenterHorizontally, // Centrado horizontal.
            verticalArrangement = Arrangement.spacedBy(23.dp) // Espaciado entre elementos.
        ) {
            Spacer(Modifier.height(30.dp)) // Espaciador superior.

            // Campo de entrada para el email.
            TextField(
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = ErizoHubTheme.Colors.textField // Color del campo.
                ),
                value = emailInput, // Texto del campo.
                onValueChange = { emailInput = it }, // Actualiza el estado al escribir.
                label = {
                    Text(
                        "email", // Etiqueta del campo.
                        color = ErizoHubTheme.Colors.textFieldText, // Color del texto.
                        fontFamily = customFontFamily, // Fuente personalizada.
                        fontSize = 10.sp // Tamaño de fuente pequeño.
                    )
                },
                modifier = Modifier
                    .height(61.dp) // Altura del campo.
                    .width(367.dp) // Ancho del campo.
                    .clip(RoundedCornerShape(50.dp)) // Bordes redondeados.
                    .background(color = ErizoHubTheme.Colors.textField) // Fondo.
                    .border(10.dp, ErizoHubTheme.Colors.textField, RoundedCornerShape(50.dp)) // Borde.
            )

            // Campo de entrada para la contraseña.
            TextField(
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = ErizoHubTheme.Colors.textField
                ),
                value = passwordInput,
                onValueChange = { passwordInput = it },
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

        // Botón para recuperar contraseña.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            horizontalArrangement = Arrangement.End // Posicionado a la derecha.
        ) {
            Button(
                onClick = {}, // Acción pendiente.
                modifier = Modifier
                    .width(250.dp)
                    .height(40.dp)
                    .padding(top = 10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Text(
                    text = "Olvidaste la contraseña?",
                    fontSize = 12.sp,
                    fontFamily = customFontFamily,
                    color = ErizoHubTheme.Colors.primary
                )
            }
        }

        // Botones principales y alternativas de inicio de sesión.
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween, // Distribución vertical.
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Botón para iniciar sesión con email y contraseña.
            Button(
                onClick = {
                    if (emailInput.isEmpty() || passwordInput.isEmpty()) {
                        Toast.makeText(context, "Llene todos los campos", Toast.LENGTH_SHORT).show()
                    } else {
                        // Intenta iniciar sesión con Firebase Authentication.
                        auth.signInWithEmailAndPassword(emailInput, passwordInput)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val intent = Intent(context, MainActivity::class.java)
                                    context.startActivity(intent) // Navega al MainActivity.
                                    (context as Activity).finish() // Cierra la actividad actual.
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Error al iniciar sesión",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }
                },
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 20.dp)
                    .width(367.dp)
                    .height(61.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text(
                    text = "Iniciar Sesión",
                    color = ErizoHubTheme.Colors.primary,
                    fontFamily = customFontFamily,
                    fontSize = 20.sp
                )
            }

            // Botón para registrarse.
            Button(
                onClick = { navController.navigate("register") }, // Navega a la pantalla de registro.
                modifier = Modifier
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

            Spacer(modifier = Modifier.height(60.dp)) // Espaciador.

            // Divisor para separar secciones.
            DividerLogin(Modifier)

            Spacer(modifier = Modifier.height(50.dp)) // Espaciador.

            // Botón para iniciar sesión con Google.
            Column {
                ButtonGoogle(
                    text = "Iniciar sesión con Google",
                    logoResId = R.drawable.googleicon, // Ícono de Google.
                    onClickAction = { onGoogleSignInClick() } // Acción al presionar.
                )
            }
        }
    }
}
