package com.example.erizohub.Home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.erizohub.ClasesBD.Comentario
import com.example.erizohub.ClasesBD.Emprendimiento
import com.example.erizohub.ClasesBD.Producto
import com.example.erizohub.InicioApp.ErizoHubTheme
import com.example.erizohub.InicioApp.ErizoHubTheme.Fonts.customFontFamily
import com.example.erizohub.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// Función para obtener la primera palabra de un texto.
fun getFirstWord(text: String): String {
    // Divide el texto en palabras y retorna la primera palabra, o una cadena vacía si no hay palabras.
    return text.split(" ").firstOrNull() ?: ""
}

@OptIn(ExperimentalMaterial3Api::class) // Habilita el uso de funciones experimentales de Material Design 3.
@Composable
fun SearchField(onSearchTextChanged: (String) -> Unit, placeHolder: String = "") {
    // Variable de estado para almacenar el texto ingresado en el campo de búsqueda.
    var searchText by remember { mutableStateOf("") }

    // Campo de texto para la búsqueda.
    TextField(
        value = searchText, // Texto actual en el campo.
        onValueChange = {
            searchText = it // Actualiza el texto ingresado.
            onSearchTextChanged(it) // Llama a la función proporcionada para manejar el cambio.
        },
        label = {
            // Etiqueta del campo de búsqueda (placeholder).
            Text(
                text = placeHolder, // Texto del placeholder.
                color = ErizoHubTheme.Colors.textFieldText, // Color del texto.
                fontFamily = customFontFamily, // Fuente personalizada.
                fontSize = 10.sp // Tamaño del texto.
            )
        },
        leadingIcon = {
            // Icono de lupa al inicio del campo de búsqueda.
            Image(
                painter = painterResource(id = R.drawable.lupa), // Recurso de la imagen de lupa.
                contentDescription = null, // Sin descripción para accesibilidad (decorativo).
                modifier = Modifier.size(24.dp) // Tamaño del icono.
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = ErizoHubTheme.Colors.textField // Color de fondo del campo de texto.
        ),
        modifier = Modifier
            .height(61.dp) // Altura del campo de búsqueda.
            .width(367.dp) // Ancho del campo de búsqueda.
            .clip(RoundedCornerShape(50.dp)) // Bordes redondeados del campo.
            .background(color = ErizoHubTheme.Colors.textField) // Fondo del campo.
            .border(1.dp, ErizoHubTheme.Colors.textField, RoundedCornerShape(50.dp)) // Borde del campo.
            .padding(horizontal = 16.dp) // Espaciado interno horizontal.
    )
}


@Composable
fun HomeScreen(navController: NavController) {
    // Variables de estado para almacenar el nombre de usuario y los emprendimientos.
    var userName by remember { mutableStateOf("") } // Nombre completo del usuario.
    val primerNombre by remember { derivedStateOf { getFirstWord(userName) } } // Obtiene solo el primer nombre del usuario.
    val listEmprendimientos = remember { mutableStateOf<List<Emprendimiento>>(emptyList()) } // Lista completa de emprendimientos.
    val filteredEmprendimientos = remember { mutableStateOf<List<Emprendimiento>>(emptyList()) } // Lista filtrada según la búsqueda.
    val db = Firebase.firestore // Instancia de Firestore para las operaciones de base de datos.
    val user = FirebaseAuth.getInstance().currentUser // Usuario autenticado en Firebase.

    Log.d("HomeScreen", "Primer nombre: $primerNombre") // Mensaje de depuración.

    // Efecto secundario que se ejecuta al iniciar la pantalla para cargar los emprendimientos.
    LaunchedEffect(Unit) {
        db.collectionGroup("emprendimientos").get()
            .addOnSuccessListener { response ->
                // Convierte cada documento de la colección en un objeto `Emprendimiento`.
                val emprendimientos = response.documents.mapNotNull { document ->
                    try {
                        Log.d("HomeScreen", "Documento encontrado: ${document.id}")
                        Emprendimiento(
                            idEmprendimiento = document.id,
                            nombre_emprendimiento = document.getString("nombre_emprendimiento")?.lowercase() ?: "Sin nombre",
                            descripcion = document.getString("descripcion") ?: "Sin descripción",
                            imagenEmprendimiento = document.getString("imagenEmprendimiento") ?: "",
                            listaProductos = (document.get("listaproductos") as? List<Map<String, Any>>)?.mapNotNull { productoMap ->
                                try {
                                    Producto(
                                        id_producto = productoMap["id_producto"] as? String ?: "",
                                        nombre_producto = productoMap["nombre_producto"] as? String ?: "",
                                        descripcionProducto = productoMap["descripcionProducto"] as? String ?: "",
                                        imagen_producto = productoMap["imagen_producto"] as? String ?: ""
                                    )
                                } catch (e: Exception) {
                                    Log.e("HomeScreen", "Error al parsear producto: $productoMap", e)
                                    null
                                }
                            }?.toMutableList() ?: mutableListOf(),
                            comentarios = (document.get("comentarios") as? List<Map<String, Any>>)?.mapNotNull { comentarioMap ->
                                try {
                                    Comentario(
                                        usuario = comentarioMap["usuario"] as? String ?: "",
                                        contenido = comentarioMap["contenido"] as? String ?: ""
                                    )
                                } catch (e: Exception) {
                                    Log.e("HomeScreen", "Error al parsear comentario: $comentarioMap", e)
                                    null
                                }
                            }?.toMutableList() ?: mutableListOf()
                        )
                    } catch (e: Exception) {
                        Log.e("HomeScreen", "Error al parsear emprendimiento: ${document.id}", e)
                        null
                    }
                }
                Log.d("HomeScreen", "Total emprendimientos cargados: ${emprendimientos.size}")
                listEmprendimientos.value = emprendimientos // Actualiza la lista completa.
                filteredEmprendimientos.value = emprendimientos // Actualiza la lista filtrada.
            }
            .addOnFailureListener { e ->
                Log.e("HomeScreen", "Error al obtener emprendimientos", e) // Error al cargar los emprendimientos.
            }
    }

    // Efecto secundario para cargar el nombre del usuario al iniciar la pantalla.
    LaunchedEffect(user) {
        user?.let { currentUser ->
            db.collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        userName = document.getString("userName") ?: "" // Obtiene el nombre del usuario.
                        Log.d("HomeScreen", "User name cargado: $userName")
                    } else {
                        Log.d("HomeScreen", "No se encontró el usuario en Firestore")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("HomeScreen", "Error al obtener el usuario", e)
                }
        }
    }

    // Interfaz de la pantalla.
    Column(
        modifier = Modifier
            .fillMaxSize() // La columna ocupa todo el tamaño de la pantalla.
            .background(color = Color.White), // Fondo blanco.
        horizontalAlignment = Alignment.CenterHorizontally // Centra el contenido horizontalmente.
    ) {
        // Sección superior: saludo y campo de búsqueda.
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(top = 20.dp, bottom = 40.dp), // Espaciado superior e inferior.
            horizontalAlignment = Alignment.CenterHorizontally // Centra el contenido horizontalmente.
        ) {
            Text(
                color = ErizoHubTheme.Colors.primary, // Color principal del tema.
                text = "Bienvenido $primerNombre!", // Mensaje de bienvenida con el primer nombre.
                fontSize = 24.sp, // Tamaño del texto.
                fontFamily = customFontFamily, // Fuente personalizada.
                textAlign = TextAlign.Start // Alineación del texto a la izquierda.
            )
            Spacer(modifier = Modifier.height(10.dp)) // Espacio entre el saludo y el campo de búsqueda.

            // Campo de búsqueda para filtrar los emprendimientos.
            SearchField(
                onSearchTextChanged = { query ->
                    val queryLowercase = query.lowercase() // Convierte la consulta a minúsculas.
                    filteredEmprendimientos.value = if (queryLowercase.isEmpty()) {
                        listEmprendimientos.value // Muestra todos los emprendimientos si no hay búsqueda.
                    } else {
                        // Filtra los emprendimientos cuyo nombre contiene la consulta.
                        listEmprendimientos.value.filter {
                            it.nombre_emprendimiento.contains(queryLowercase, ignoreCase = true)
                        }
                    }
                },
                placeHolder = "Buscar emprendimiento" // Texto de ayuda en el campo.
            )
        }

        // Sección inferior: lista de emprendimientos recientes.
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), // Margen horizontal.
            horizontalAlignment = Alignment.CenterHorizontally // Centra el contenido horizontalmente.
        ) {
            Text(
                text = "Emprendimientos recientes", // Título de la sección.
                fontSize = 20.sp, // Tamaño del texto.
                fontFamily = customFontFamily, // Fuente personalizada.
                color = ErizoHubTheme.Colors.primary, // Color principal del tema.
            )
            Spacer(modifier = Modifier.height(29.dp)) // Espacio entre el título y la lista.

            // Lista de emprendimientos con disposición vertical.
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp) // Espaciado entre los elementos.
            ) {
                items(filteredEmprendimientos.value) { emprendimiento ->
                    // Cada emprendimiento se muestra como un `EmprendimientoItem`.
                    EmprendimientoItem(myEmprendimiento = emprendimiento) {
                        // Navega a la pantalla de detalles del emprendimiento.
                        navController.navigate("emprendimientoScreen/${emprendimiento.idEmprendimiento}")
                    }
                }
            }
        }
    }
}


@Composable
fun EmprendimientoItem(myEmprendimiento: Emprendimiento, onClick: () -> Unit) {
    // Contenedor principal: una tarjeta con bordes redondeados y sombra.
    Card(
        modifier = Modifier
            .fillMaxWidth() // La tarjeta ocupa todo el ancho disponible.
            .padding(8.dp) // Margen externo de 8dp alrededor de la tarjeta.
            .shadow(5.dp, shape = RoundedCornerShape(20.dp)) // Sombra para darle efecto de elevación.
            .border(1.dp, Color.Transparent, RoundedCornerShape(20.dp)) // Borde con esquinas redondeadas.
            .clickable { onClick() }, // Permite hacer clic en toda la tarjeta.
        shape = RoundedCornerShape(20.dp) // Forma de la tarjeta: esquinas redondeadas.
    ) {
        // Disposición horizontal para los elementos: imagen a la izquierda, texto a la derecha.
        Row(
            modifier = Modifier
                .fillMaxWidth() // La fila ocupa todo el ancho de la tarjeta.
                .background(Color(0xFFF6F6F6)) // Fondo gris claro.
                .border(3.dp, Color.Transparent, RoundedCornerShape(20.dp)), // Borde transparente para estética.
            verticalAlignment = Alignment.CenterVertically // Alinea los elementos verticalmente al centro.
        ) {
            // Imagen del emprendimiento.
            AsyncImage(
                model = myEmprendimiento.imagenEmprendimiento, // URL de la imagen del emprendimiento.
                contentDescription = null, // No se proporciona descripción (decorativo).
                modifier = Modifier
                    .size(150.dp) // Tamaño fijo de 150x150 dp.
                    .padding(end = 12.dp) // Margen derecho de 12dp para separar la imagen del texto.
                    .clip(RoundedCornerShape(20.dp)), // Esquinas redondeadas para la imagen.
                contentScale = ContentScale.Crop // Recorta la imagen para llenar el área disponible.
            )
            // Columna para el contenido textual del emprendimiento.
            Column(
                modifier = Modifier
                    .fillMaxWidth() // La columna ocupa todo el ancho restante.
                    .padding(5.dp) // Espaciado interno de 5dp.
            ) {
                // Texto para el nombre del emprendimiento.
                Text(
                    text = myEmprendimiento.nombre_emprendimiento, // Nombre del emprendimiento.
                    fontSize = 13.sp, // Tamaño de la fuente.
                    fontFamily = ErizoHubTheme.Fonts.customFontFamily, // Fuente personalizada.
                    color = ErizoHubTheme.Colors.background, // Color del texto basado en el tema.
                    modifier = Modifier
                        .padding(bottom = 4.dp) // Margen inferior de 4dp.
                        .clickable { onClick() } // Hacer clic en el texto también activa `onClick`.
                )
                // Calcula una descripción abreviada si es demasiado larga.
                val shortenedDescription = if (myEmprendimiento.descripcion.length > 50) {
                    "${myEmprendimiento.descripcion.take(100)}..." // Muestra los primeros 100 caracteres.
                } else {
                    myEmprendimiento.descripcion // Muestra la descripción completa si es corta.
                }
                // Texto para la descripción abreviada del emprendimiento.
                Text(
                    text = shortenedDescription, // Descripción del emprendimiento.
                    fontSize = 10.sp, // Tamaño de la fuente más pequeño.
                    fontFamily = customFontFamily, // Fuente personalizada.
                    color = Color.Gray // Color gris para menor énfasis.
                )
            }
        }
    }
}


@Composable
fun EmprendimientoItemNoClick(myEmprendimiento: Emprendimiento) {
    // Contenedor principal: una tarjeta estilizada con sombra y bordes redondeados.
    Card(
        modifier = Modifier
            .fillMaxWidth() // La tarjeta ocupa todo el ancho disponible.
            .padding(8.dp) // Margen externo alrededor de la tarjeta.
            .shadow(5.dp, shape = RoundedCornerShape(20.dp)) // Sombra para darle un efecto de elevación.
            .border(1.dp, Color.Transparent, RoundedCornerShape(20.dp)), // Borde transparente con esquinas redondeadas.
        shape = RoundedCornerShape(20.dp) // Bordes redondeados de la tarjeta.
    ) {
        // Disposición horizontal: imagen a la izquierda, texto a la derecha.
        Row(
            modifier = Modifier
                .fillMaxWidth() // La fila ocupa todo el ancho de la tarjeta.
                .background(Color(0xFFF6F6F6)) // Fondo gris claro para la fila.
                .border(3.dp, Color.Transparent, RoundedCornerShape(20.dp)), // Borde transparente para mejorar la estética.
            verticalAlignment = Alignment.CenterVertically // Alinea los elementos verticalmente al centro.
        ) {
            // Imagen del emprendimiento.
            AsyncImage(
                model = myEmprendimiento.imagenEmprendimiento, // URL de la imagen del emprendimiento.
                contentDescription = null, // Sin descripción (decorativo).
                modifier = Modifier
                    .size(150.dp) // Tamaño fijo de la imagen.
                    .padding(end = 12.dp) // Margen derecho entre la imagen y el texto.
                    .clip(RoundedCornerShape(20.dp)), // Esquinas redondeadas para la imagen.
                contentScale = ContentScale.Crop // Recorta la imagen para ajustarse al contenedor.
            )
            // Columna para el contenido textual del emprendimiento.
            Column(
                modifier = Modifier
                    .fillMaxWidth() // La columna ocupa todo el ancho restante.
                    .padding(5.dp) // Espaciado interno de 5dp.
            ) {
                // Texto que muestra el nombre del emprendimiento.
                Text(
                    text = myEmprendimiento.nombre_emprendimiento, // Nombre del emprendimiento.
                    fontSize = 13.sp, // Tamaño de fuente intermedio.
                    fontFamily = ErizoHubTheme.Fonts.customFontFamily, // Fuente personalizada.
                    color = ErizoHubTheme.Colors.background, // Color basado en el tema.
                    modifier = Modifier.padding(bottom = 4.dp) // Margen inferior para separar del texto siguiente.
                )
                // Calcula una descripción abreviada si es demasiado larga.
                val shortenedDescription = if (myEmprendimiento.descripcion.length > 50) {
                    "${myEmprendimiento.descripcion.take(100)}..." // Toma los primeros 100 caracteres.
                } else {
                    myEmprendimiento.descripcion // Muestra la descripción completa si es corta.
                }
                // Texto que muestra la descripción abreviada del emprendimiento.
                Text(
                    text = shortenedDescription, // Texto de la descripción.
                    fontSize = 10.sp, // Tamaño de fuente más pequeño.
                    fontFamily = customFontFamily, // Fuente personalizada.
                    color = Color.Gray // Color gris para menor énfasis.
                )
            }
        }
    }
}
