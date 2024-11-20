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
    // Variable de estado para controlar la visibilidad de la animación.
    var visible by remember { mutableStateOf(false) }

    // Efecto que se ejecuta cuando el componente se monta, activando la visibilidad.
    LaunchedEffect(Unit) {
        visible = true
    }

    // Contenedor principal de tipo columna para organizar el contenido verticalmente.
    Column(
        modifier = Modifier.fillMaxSize(), // Ocupa todo el tamaño disponible.
        verticalArrangement = Arrangement.Bottom // Posiciona los elementos en la parte inferior.
    ) {
        // Imagen del logo.
        Image(
            painter = painterResource(id = R.drawable.erizo), // Recurso de la imagen del logo.
            contentDescription = "Logo", // Descripción para accesibilidad.
            modifier = Modifier
                .width(400.dp) // Ancho de la imagen.
                .height(400.dp) // Altura de la imagen.
                .padding(bottom = 50.dp) // Margen inferior.
        )

        // Visibilidad animada para mostrar la sección de bienvenida.
        AnimatedVisibility(
            visible = visible, // Controla si el contenido es visible.
            enter = slideInVertically( // Animación de entrada deslizándose verticalmente.
                initialOffsetY = { it } // Comienza desde su posición inicial.
            ) + fadeIn(), // También incluye un efecto de desvanecimiento al aparecer.
            exit = fadeOut() // Efecto de desvanecimiento al desaparecer.
        ) {
            // Contenedor para la sección de bienvenida.
            Box(
                modifier = Modifier
                    .height(247.dp) // Altura del contenedor.
                    .width(412.dp) // Ancho del contenedor.
                    .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)) // Bordes redondeados en la parte superior.
                    .background(color = ErizoHubTheme.Colors.background) // Fondo con el color del tema.
            ) {
                // Texto de bienvenida centrado en la parte superior del contenedor.
                Text(
                    text = "Bienvenido", // Texto de bienvenida.
                    modifier = Modifier
                        .align(Alignment.TopCenter) // Alineado en el centro superior.
                        .padding(top = 70.dp), // Margen superior.
                    fontSize = 30.sp, // Tamaño del texto.
                    fontFamily = ErizoHubTheme.Fonts.customFontFamily, // Fuente personalizada.
                    color = ErizoHubTheme.Colors.primary // Color basado en el tema.
                )

                // Botones para iniciar sesión o registrarse, alineados en la parte inferior.
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter) // Alineado en el centro inferior.
                        .padding(bottom = 50.dp), // Margen inferior.
                    horizontalArrangement = Arrangement.spacedBy(16.dp) // Espaciado entre los botones.
                ) {
                    // Botón de "Iniciar sesión".
                    Button(
                        onClick = onButtonClickIniciar, // Acción al presionar el botón.
                        modifier = Modifier
                            .width(160.dp) // Ancho del botón.
                            .height(54.dp), // Altura del botón.
                        colors = ButtonDefaults.buttonColors(Color.Black) // Fondo negro para el botón.
                    ) {
                        Text(
                            text = "Iniciar sesión", // Texto del botón.
                            fontSize = 15.sp, // Tamaño de la fuente.
                            fontFamily = ErizoHubTheme.Fonts.customFontFamily, // Fuente personalizada.
                            color = ErizoHubTheme.Colors.primary // Color del texto basado en el tema.
                        )
                    }

                    // Botón de "Registrarse".
                    Button(
                        onClick = onButtonClickRegistrarse, // Acción al presionar el botón.
                        modifier = Modifier
                            .width(160.dp) // Ancho del botón.
                            .height(54.dp), // Altura del botón.
                        colors = ButtonDefaults.buttonColors(Color.Black) // Fondo negro para el botón.
                    ) {
                        Text(
                            text = "Registrarse", // Texto del botón.
                            fontSize = 15.sp, // Tamaño de la fuente.
                            fontFamily = ErizoHubTheme.Fonts.customFontFamily, // Fuente personalizada.
                            color = ErizoHubTheme.Colors.primary // Color del texto basado en el tema.
                        )
                    }
                }
            }
        }
    }
}