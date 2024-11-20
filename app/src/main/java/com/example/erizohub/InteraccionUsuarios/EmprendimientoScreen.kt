package com.example.erizohub.InteraccionUsuarios

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.erizohub.ClasesBD.Comentario
import com.example.erizohub.ClasesBD.Emprendimiento
import com.example.erizohub.ClasesBD.Producto
import com.example.erizohub.Home.ProductoItem
import com.example.erizohub.InicioApp.ErizoHubTheme
import com.example.erizohub.InicioApp.ErizoHubTheme.Fonts.customFontFamily
import com.example.erizohub.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore


@Composable
fun EmprendimientoScreen(navController: NavController, idEmprendimiento: String) {
    // Inicialización de Firestore
    val db = Firebase.firestore

    // Variables de estado que representan el emprendimiento, comentarios, nuevo comentario y likes
    var emprendimiento by remember { mutableStateOf<Emprendimiento?>(null) } // Estado para almacenar los datos del emprendimiento
    var comentarios by remember { mutableStateOf(mutableListOf<Comentario>()) } // Lista de comentarios
    var nuevoComentario by remember { mutableStateOf("") } // Texto para el nuevo comentario
    var likes by remember { mutableStateOf(0) } // Número de "likes" del emprendimiento

    // Control de desplazamiento para el contenido de la pantalla
    val scrollState = rememberScrollState()

    // Obtiene al usuario autenticado actualmente
    val user = FirebaseAuth.getInstance().currentUser

    // Efecto secundario que se ejecuta cuando cambia el ID del emprendimiento
    LaunchedEffect(idEmprendimiento) {
        // Obtiene los datos del emprendimiento desde Firestore
        db.collection("emprendimientos").document(idEmprendimiento).get()
            .addOnSuccessListener { document ->
                if (document.exists()) { // Verifica que el documento exista
                    // Convierte el documento a un objeto "Emprendimiento"
                    emprendimiento = document.toObject(Emprendimiento::class.java)
                    // Obtiene el número de "likes" desde Firestore
                    likes = document.getLong("likes")?.toInt() ?: 0
                    // Limpia y actualiza la lista de comentarios
                    comentarios.clear()
                    comentarios.addAll(
                        (document.get("comentarios") as? List<Map<String, String>>)?.map {
                            // Convierte cada mapa en un objeto "Comentario"
                            Comentario(it["usuario"] ?: "", it["contenido"] ?: "")
                        } ?: emptyList()
                    )
                }
            }
    }

    // Estructura principal de la pantalla
    Column(
        modifier = Modifier
            .fillMaxSize() // Ocupa todo el espacio disponible
            .background(color = Color.White) // Fondo blanco
            .verticalScroll(scrollState) // Permite desplazamiento vertical
    ) {
        // Sección superior de la pantalla (imagen y detalles del emprendimiento)
        Column(
            modifier = Modifier
                .height(450.dp) // Altura fija para esta sección
                .background(Color.White) // Fondo blanco
                .fillMaxWidth() // Ancho completo
        ) {
            // Imagen del emprendimiento
            AsyncImage(
                model = emprendimiento?.imagenEmprendimiento ?: R.drawable.polygon_2, // Carga la imagen del emprendimiento o un recurso predeterminado
                contentDescription = "Imagen del emprendimiento", // Descripción de la imagen para accesibilidad
                modifier = Modifier.fillMaxSize(), // Ocupa todo el espacio disponible
                contentScale = ContentScale.Crop // Escala la imagen para llenar el espacio sin distorsión
            )

            // Detalles del emprendimiento (nombre y descripción)
            Column(
                modifier = Modifier
                    .fillMaxWidth() // Ocupa todo el ancho
                    .padding(16.dp), // Padding interno de 16dp
                verticalArrangement = Arrangement.Bottom // Alineación inferior
            ) {
                // Texto del nombre del emprendimiento
                Text(
                    text = emprendimiento?.nombre_emprendimiento ?: "Nombre del Emprendimiento", // Muestra el nombre o un texto predeterminado
                    fontSize = 25.sp, // Tamaño de fuente
                    fontWeight = FontWeight.Bold, // Fuente en negrita
                    color = Color.Black // Color negro
                )
                // Texto de la descripción del emprendimiento
                Text(
                    text = emprendimiento?.descripcion ?: "Descripción del Emprendimiento", // Muestra la descripción o un texto predeterminado
                    fontSize = 15.sp, // Tamaño de fuente
                    color = Color.Gray // Color gris
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth() // Ocupa todo el ancho disponible
                .padding(16.dp), // Margen externo de 16dp
            horizontalArrangement = Arrangement.SpaceBetween // Distribuye los elementos con espacio entre ellos
        ) {
            // Subsección para mostrar y manejar los "likes"
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Botón con ícono para dar "like"
                IconButton(onClick = {
                    likes += 1 // Incrementa el número de likes
                    // Actualiza el número de likes en Firestore
                    db.collection("emprendimientos").document(idEmprendimiento).update("likes", likes)
                }) {
                    Icon(Icons.Filled.Favorite, contentDescription = null, tint = Color.Red) // Ícono de corazón en rojo
                }
                // Muestra el número actual de likes
                Text(text = "$likes")
            }

            // Botón para navegar a la pantalla de selección de productos
            Button(
                onClick = { navController.navigate("producto_selection/$idEmprendimiento") }, // Navega a la pantalla correspondiente
                colors = ButtonDefaults.buttonColors(
                    containerColor = ErizoHubTheme.Colors.primary // Color personalizado para el botón
                )
            ) {
                Text("Productos", color = Color.White) // Texto del botón con color blanco
            }
        }

// Título para la sección de comentarios
        Text(
            text = "Comentarios", // Texto del título
            fontSize = 20.sp, // Tamaño de fuente
            fontWeight = FontWeight.Bold, // Fuente en negrita
            color = Color.Black, // Color del texto
            modifier = Modifier.padding(horizontal = 16.dp) // Margen horizontal de 16dp
        )

// Lista de comentarios con desplazamiento
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            // Itera sobre la lista de comentarios y crea elementos para cada uno
            items(comentarios) { comentario ->
                Row(
                    modifier = Modifier.padding(16.dp), // Margen externo de 16dp
                    verticalAlignment = Alignment.CenterVertically // Alinea verticalmente al centro
                ) {
                    // Ícono para representar al usuario
                    Icon(Icons.Default.AccountCircle, contentDescription = null, tint = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp)) // Espaciado entre el ícono y el texto
                    Column {
                        // Nombre del usuario en negrita
                        Text(text = comentario.usuario, fontWeight = FontWeight.Bold)
                        // Contenido del comentario en color gris
                        Text(text = comentario.contenido, color = Color.Gray)
                    }
                }
            }
        }

// Campo de texto para agregar un nuevo comentario
        OutlinedTextField(
            value = nuevoComentario, // Texto del nuevo comentario
            onValueChange = { nuevoComentario = it }, // Actualiza el estado al cambiar el texto
            label = { Text("Añade un comentario...") }, // Etiqueta dentro del campo de texto
            modifier = Modifier
                .fillMaxWidth() // Ocupa todo el ancho disponible
                .padding(16.dp) // Margen externo de 16dp
        )

// Botón para enviar el nuevo comentario
        Button(
            onClick = {
                // Verifica que el comentario no esté vacío y que el usuario esté autenticado
                if (nuevoComentario.isNotBlank() && user != null) {
                    val comentario = Comentario(user.displayName ?: "Usuario", nuevoComentario) // Crea un objeto Comentario
                    db.collection("emprendimientos").document(idEmprendimiento)
                        .update("comentarios", FieldValue.arrayUnion(comentario)) // Añade el comentario a Firestore
                    comentarios.add(comentario) // Actualiza la lista de comentarios localmente
                    nuevoComentario = "" // Limpia el campo de texto
                }
            },
            modifier = Modifier.padding(16.dp) // Margen externo de 16dp
        ) {
            Text("Comentar") // Texto del botón
        }
    }
}


@Composable
fun ProductoSelectionScreenEmprendimiento(navController: NavController, idEmprendimiento: String) {
    val db = FirebaseFirestore.getInstance() // Referencia a la base de datos Firestore
    val productos = remember { mutableStateListOf<Producto>() } // Lista de productos observables
    val context = LocalContext.current // Contexto actual para mostrar toasts o usar recursos

    // Efecto lanzado al montar el Composable o cuando cambia `idEmprendimiento`
    LaunchedEffect(idEmprendimiento) {
        db.collectionGroup("productos") // Consulta en el grupo de colecciones "productos"
            .get()
            .addOnSuccessListener { result ->
                // Filtra productos pertenecientes al `idEmprendimiento`
                val productosFiltrados = result.documents.mapNotNull { document ->
                    document.toObject(Producto::class.java)
                }.filter { it.idEmprendimiento == idEmprendimiento }

                productos.clear() // Limpia la lista actual
                productos.addAll(productosFiltrados) // Añade los productos filtrados

                Log.d("ProductoSelectionScreen", "Productos filtrados: ${productos.size}")
            }
            .addOnFailureListener {
                // Muestra un mensaje en caso de error
                Toast.makeText(context, "Error al obtener los productos", Toast.LENGTH_SHORT).show()
                Log.e("ProductoSelectionScreenEmprendimiento", "Error al obtener los productos", it)
            }
    }

    // Layout principal
    Column(
        Modifier
            .fillMaxSize() // Ocupa todo el espacio disponible
            .background(color = ErizoHubTheme.Colors.background), // Fondo personalizado
        horizontalAlignment = Alignment.CenterHorizontally // Centra horizontalmente los elementos
    ) {
        // Barra superior con título
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(color = ErizoHubTheme.Colors.background), // Fondo personalizado
            horizontalArrangement = Arrangement.Center, // Alinea los elementos al centro
            verticalAlignment = Alignment.CenterVertically // Alineación vertical al centro
        ) {
            Text(
                text = "Productos", // Título de la pantalla
                fontSize = 24.sp, // Tamaño de texto
                fontFamily = customFontFamily, // Fuente personalizada
                color = Color.White // Texto en blanco
            )
        }
        Spacer(modifier = Modifier.height(10.dp)) // Espaciado entre elementos

        // Lista de productos con desplazamiento
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight() // Ocupa todo el alto disponible
                .background(color = Color.White, RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)) // Fondo con bordes redondeados
        ) {
            // Genera elementos dinámicamente a partir de la lista de productos
            items(productos) { producto ->
                ProductoItem(producto) {
                    // Navega a la pantalla de visualización de producto
                    navController.navigate("visualizar_producto/${producto.id_producto}")
                    }
                }
            }

    }
}

@Composable
fun VisualizarProductoScreen(navController: NavController, idProducto: String) {
    val context = LocalContext.current // Contexto actual
    var producto by remember { mutableStateOf<Producto?>(null) } // Estado para almacenar los datos del producto
    val db = FirebaseFirestore.getInstance() // Referencia a Firestore
    val scrollState = rememberScrollState() // Controla el desplazamiento vertical

    // Efecto lanzado para cargar el producto al montar el Composable o al cambiar `idProducto`
    LaunchedEffect(idProducto) {
        db.collectionGroup("productos") // Consulta en el grupo de colecciones "productos"
            .whereEqualTo("id_producto", idProducto) // Filtro por ID del producto
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) { // Verifica si se encontraron resultados
                    val productoObtenido = result.documents[0].toObject(Producto::class.java) // Convierte el documento en objeto Producto
                    producto = productoObtenido
                } else {
                    // Muestra un mensaje si no se encontró el producto
                    Toast.makeText(context, "Producto no encontrado", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                // Muestra un mensaje en caso de error
                Toast.makeText(context, "Error al cargar el producto", Toast.LENGTH_SHORT).show()
                Log.e("VisualizarProductoScreen", "Error en la consulta: ${exception.message}", exception)
            }
    }

    // Layout principal
    Column(
        modifier = Modifier
            .fillMaxSize() // Ocupa todo el espacio disponible
            .background(color = Color.White) // Fondo blanco
            .verticalScroll(scrollState) // Habilita desplazamiento vertical
    ) {
        // Sección superior con la imagen del producto
        Column(
            modifier = Modifier
                .height(450.dp) // Altura fija
                .background(Color.White) // Fondo blanco
                .fillMaxWidth() // Ocupa todo el ancho disponible
        ) {
            Box(
                modifier = Modifier.fillMaxSize(), // Ocupa todo el espacio disponible
                contentAlignment = Alignment.TopCenter // Alinea el contenido al centro superior
            ) {
                // Imagen del producto
                AsyncImage(
                    model = producto?.imagen_producto ?: R.drawable.polygon_2, // Carga la imagen del producto o una predeterminada
                    contentDescription = "Imagen del producto", // Descripción para accesibilidad
                    modifier = Modifier
                        .fillMaxSize() // Ocupa todo el espacio disponible
                        .border(1.dp, Color.Transparent), // Borde transparente
                    contentScale = ContentScale.Crop // Escala la imagen para llenar el espacio sin distorsión
                )

                // Información del producto
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(bottom = 50.dp, start = 10.dp) // Espaciado inferior y a la izquierda
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Bottom // Alinea los elementos al fondo
                ) {
                    // Nombre del producto
                    Text(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .fillMaxWidth(),
                        text = producto?.nombre_producto ?: "Nombre del Producto", // Muestra el nombre o un texto predeterminado
                        color = Color.Black, // Color del texto
                        fontFamily = customFontFamily, // Fuente personalizada
                        fontSize = 25.sp // Tamaño de texto
                    )
                    // Descripción del producto
                    Text(
                        modifier = Modifier
                            .padding(start = 15.dp)
                            .fillMaxWidth(),
                        text = producto?.descripcionProducto ?: "Descripción del Producto", // Muestra la descripción o un texto predeterminado
                        color = Color.Black, // Color del texto
                        fontFamily = customFontFamily, // Fuente personalizada
                        fontSize = 15.sp, // Tamaño de texto
                        textAlign = TextAlign.Start // Alineación a la izquierda
                    )
                }
            }
        }

        // Sección inferior con diseño adicional (pendiente de contenido)
        Column(
            Modifier
                .fillMaxSize() // Ocupa todo el espacio restante
                .offset(y = (-50).dp) // Desplaza el contenido hacia arriba
                .background(color = ErizoHubTheme.Colors.background, RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)) // Fondo con bordes redondeados
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Column(
                Modifier
                    .fillMaxSize()
                    .background(color = Color.White, RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {


            }
        }
    }
}
