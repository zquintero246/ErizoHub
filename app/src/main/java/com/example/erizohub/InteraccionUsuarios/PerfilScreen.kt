package com.example.erizohub.InteraccionUsuarios

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.erizohub.InicioApp.ErizoHubTheme
import com.example.erizohub.R

@Composable
fun Perfil(navController: NavController) {
    // Columna principal que contiene todo el contenido de la pantalla
    Column(
        modifier = Modifier
            .fillMaxWidth() // Ocupa todo el ancho de la pantalla
            .background(color = Color.White) // Fondo blanco
    ) {
        // Sección superior: Imagen de perfil y nombre de usuario
        Column(
            modifier = Modifier
                .height(350.dp) // Altura fija para esta sección
                .fillMaxWidth() // Ancho completo
                .background(color = Color.White), // Fondo blanco
            horizontalAlignment = Alignment.CenterHorizontally // Centra el contenido horizontalmente
        ) {
            // Imagen de perfil
            Image(
                painter = painterResource(id = R.drawable.profile), // Recurso de la imagen
                contentDescription = "profile", // Descripción para accesibilidad
                modifier = Modifier
                    .size(250.dp) // Tamaño de la imagen
                    .align(Alignment.CenterHorizontally) // Centra horizontalmente
                    .padding(0.dp, 100.dp, 0.dp, 20.dp) // Margen interno
            )
            // Texto del nombre de usuario
            Text(
                modifier = Modifier,
                fontSize = 20.sp, // Tamaño de fuente
                fontFamily = ErizoHubTheme.Fonts.customFontFamily, // Fuente personalizada
                color = Color(0xFFB8B8B8), // Color gris
                text = "Nombre usuario" // Texto predeterminado
            )
        }

        // Sección inferior: Opciones y botones
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, // Centra horizontalmente
            modifier = Modifier
                .fillMaxSize() // Ocupa todo el espacio restante
                .background( // Fondo personalizado con bordes redondeados
                    color = ErizoHubTheme.Colors.background,
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                )
        ) {
            // Botón de "Seguir"
            Button(
                onClick = { /*TODO*/ }, // Acción al hacer clic
                modifier = Modifier
                    .align(Alignment.CenterHorizontally) // Centra horizontalmente
                    .width(225.dp) // Ancho del botón
                    .height(59.dp) // Altura del botón
                    .offset(y = -25.dp) // Desplazamiento vertical
                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(30.dp)) // Borde gris
                    .shadow(20.dp, shape = RoundedCornerShape(30.dp)), // Sombra
                elevation = ButtonDefaults.buttonElevation(20.dp), // Elevación del botón
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White, // Fondo blanco
                    contentColor = Color.Black // Texto negro
                )
            ) {
                Text(
                    modifier = Modifier.alpha(0.8f), // Transparencia del texto
                    fontWeight = FontWeight.Normal, // Peso normal
                    color = Color(0xFF656262), // Color gris oscuro
                    fontSize = 20.sp, // Tamaño de texto
                    fontFamily = ErizoHubTheme.Fonts.customFontFamily, // Fuente personalizada
                    text = "Seguir" // Texto del botón
                )
            }

            // Línea divisoria
            HorizontalDivider(
                modifier = Modifier
                    .width(325.dp) // Ancho de la línea
                    .padding(top = 40.dp) // Margen superior
                    .alpha(0.5f) // Transparencia
            )

            // Botón "Emprendimientos Activos"
            Button(
                onClick = { /*TODO*/ }, // Acción al hacer clic
                modifier = Modifier.padding(top = 11.dp, bottom = 11.dp), // Márgenes
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6F04D9)) // Color de fondo
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween, // Espaciado entre elementos
                    modifier = Modifier.width(280.dp) // Ancho del botón
                ) {
                    Text(
                        modifier = Modifier.alpha(0.5f), // Transparencia del texto
                        fontWeight = FontWeight.Normal, // Peso normal
                        fontFamily = ErizoHubTheme.Fonts.customFontFamily, // Fuente personalizada
                        fontSize = 15.sp, // Tamaño de texto
                        text = "Emprendimientos Activos" // Texto del botón
                    )
                    Icon(
                        modifier = Modifier.alpha(0.5f), // Transparencia del ícono
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, // Ícono de flecha
                        contentDescription = "Menu"
                    )
                }
            }

            // Línea divisoria
            HorizontalDivider(
                modifier = Modifier
                    .width(325.dp)
                    .alpha(0.5f) // Transparencia
            )

            // Botón "Servicios Pagados"
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.padding(top = 11.dp, bottom = 11.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6F04D9))
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.width(280.dp)
                ) {
                    Text(
                        modifier = Modifier.alpha(0.5f),
                        fontWeight = FontWeight.Normal,
                        fontFamily = ErizoHubTheme.Fonts.customFontFamily,
                        fontSize = 15.sp,
                        text = "Servicios Pagados"
                    )
                    Icon(
                        modifier = Modifier.alpha(0.5f),
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Menu"
                    )
                }
            }

            // Línea divisoria
            HorizontalDivider(
                modifier = Modifier
                    .width(325.dp)
                    .alpha(0.5f)
            )

            // Botón "Historial"
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.padding(top = 11.dp, bottom = 11.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6F04D9))
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.width(280.dp)
                ) {
                    Text(
                        modifier = Modifier.alpha(0.5f),
                        fontWeight = FontWeight.Normal,
                        fontFamily = ErizoHubTheme.Fonts.customFontFamily,
                        fontSize = 15.sp,
                        text = "Historial"
                    )
                    Icon(
                        modifier = Modifier.alpha(0.5f),
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Menu"
                    )
                }
            }

            // Texto "Promedio de Calificación"
            Text(
                modifier = Modifier
                    .alpha(0.5f) // Transparencia
                    .padding(top = 24.dp), // Margen superior
                color = Color.White, // Texto blanco
                fontFamily = ErizoHubTheme.Fonts.customFontFamily, // Fuente personalizada
                text = "Promedio de Calificación"
            )

            // Texto para el promedio (valor fijo 0.0)
            Text(
                modifier = Modifier.alpha(0.5f),
                color = Color.White,
                fontFamily = ErizoHubTheme.Fonts.customFontFamily,
                text = "0.0"
            )

            // Fila con botones de calificación (estrellas)
            Row(
                horizontalArrangement = Arrangement.SpaceBetween, // Espaciado entre botones
                modifier = Modifier
                    .width(325.dp) // Ancho
                    .padding(top = 56.dp), // Margen superior
                verticalAlignment = Alignment.CenterVertically // Alinea verticalmente al centro
            ) {
                Text(
                    modifier = Modifier.alpha(0.5f), // Transparencia
                    fontFamily = ErizoHubTheme.Fonts.customFontFamily, // Fuente personalizada
                    fontSize = 20.sp, // Tamaño de texto
                    text = "CALIFICAR:", // Texto
                    color = Color.White // Color blanco
                )

                // Botones para las estrellas
                repeat(5) { // Crea 5 botones de estrellas
                    Button(
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6F04D9), // Fondo
                            contentColor = Color(0xFFF2784B) // Color de contenido (estrella)
                        ),
                        modifier = Modifier.size(30.dp), // Tamaño del botón
                        contentPadding = PaddingValues(0.dp) // Sin padding
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star, // Ícono de estrella
                            contentDescription = "Star",
                            modifier = Modifier.fillMaxSize() // Llena el botón
                        )
                    }
                }
            }
        }
    }
}





