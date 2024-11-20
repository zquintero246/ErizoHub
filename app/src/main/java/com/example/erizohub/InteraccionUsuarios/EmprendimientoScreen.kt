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
    val db = Firebase.firestore
    var emprendimiento by remember { mutableStateOf<Emprendimiento?>(null) }
    var comentarios by remember { mutableStateOf(mutableListOf<Comentario>()) }
    var nuevoComentario by remember { mutableStateOf("") }
    var likes by remember { mutableStateOf(0) }
    val scrollState = rememberScrollState()
    val user = FirebaseAuth.getInstance().currentUser

    LaunchedEffect(idEmprendimiento) {
        db.collection("emprendimientos").document(idEmprendimiento).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    emprendimiento = document.toObject(Emprendimiento::class.java)
                    likes = document.getLong("likes")?.toInt() ?: 0
                    comentarios.clear()
                    comentarios.addAll((document.get("comentarios") as? List<Map<String, String>>)?.map {
                        Comentario(it["usuario"] ?: "", it["contenido"] ?: "")
                    } ?: emptyList())
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .verticalScroll(scrollState)
    ) {
        Column(
            modifier = Modifier
                .height(450.dp)
                .background(Color.White)
                .fillMaxWidth(),
        ) {
            AsyncImage(
                model = emprendimiento?.imagenEmprendimiento ?: R.drawable.polygon_2,
                contentDescription = "Imagen del emprendimiento",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.Bottom,
            ) {
                Text(
                    text = emprendimiento?.nombre_emprendimiento ?: "Nombre del Emprendimiento",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = emprendimiento?.descripcion ?: "Descripción del Emprendimiento",
                    fontSize = 15.sp,
                    color = Color.Gray
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    likes += 1
                    db.collection("emprendimientos").document(idEmprendimiento).update("likes", likes)
                }) {
                    Icon(Icons.Filled.Favorite, contentDescription = null, tint = Color.Red)
                }
                Text(text = "$likes")
            }
            Button(
                onClick = { navController.navigate("producto_selection/$idEmprendimiento") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = ErizoHubTheme.Colors.primary
                )
            ) {
                Text("Productos", color = Color.White)
            }
        }

        Text(
            text = "Comentarios",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(comentarios) { comentario ->
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.AccountCircle, contentDescription = null, tint = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = comentario.usuario, fontWeight = FontWeight.Bold)
                        Text(text = comentario.contenido, color = Color.Gray)
                    }
                }
            }
        }

        // Campo para agregar un nuevo comentario
        OutlinedTextField(
            value = nuevoComentario,
            onValueChange = { nuevoComentario = it },
            label = { Text("Añade un comentario...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        Button(
            onClick = {
                if (nuevoComentario.isNotBlank() && user != null) {
                    val comentario = Comentario(user.displayName ?: "Usuario", nuevoComentario)
                    db.collection("emprendimientos").document(idEmprendimiento)
                        .update("comentarios", FieldValue.arrayUnion(comentario))
                    comentarios.add(comentario)
                    nuevoComentario = ""
                }
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Comentar")
        }
    }
}


@Composable
fun ProductoSelectionScreenEmprendimiento(navController: NavController, idEmprendimiento: String) {
    val db = FirebaseFirestore.getInstance()
    val productos = remember { mutableStateListOf<Producto>() }
    val context = LocalContext.current

    LaunchedEffect(idEmprendimiento) {
        db.collectionGroup("productos")
            .get()
            .addOnSuccessListener { result ->
                val productosFiltrados = result.documents.mapNotNull { document ->
                    document.toObject(Producto::class.java)
                }.filter { it.idEmprendimiento == idEmprendimiento }

                productos.clear()
                productos.addAll(productosFiltrados)

                Log.d("ProductoSelectionScreen", "Productos filtrados: ${productos.size}")
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error al obtener los productos", Toast.LENGTH_SHORT).show()
                Log.e("ProductoSelectionScreenEmprendimiento", "Error al obtener los productos", it)
            }
    }

    Column(Modifier
        .fillMaxSize()
        .background(color = ErizoHubTheme.Colors.background),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(color = ErizoHubTheme.Colors.background),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Productos",
                fontSize = 24.sp,
                fontFamily = customFontFamily,
                color = Color.White,
                modifier = Modifier
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(modifier = Modifier.fillMaxHeight()
            .background(color = Color.White, RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
        ) {
                items(productos) { producto ->
                    ProductoItem(producto) {
                        navController.navigate("visualizar_producto/${producto.id_producto}")
                    }
                }
            }

    }
}

@Composable
fun VisualizarProductoScreen(navController: NavController, idProducto: String) {
    val context = LocalContext.current
    var producto by remember { mutableStateOf<Producto?>(null) }
    val db = FirebaseFirestore.getInstance()
    val scrollState = rememberScrollState()

    LaunchedEffect(idProducto) {
        db.collectionGroup("productos")
            .whereEqualTo("id_producto", idProducto)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val productoObtenido = result.documents[0].toObject(Producto::class.java)
                    producto = productoObtenido
                } else {
                    Toast.makeText(context, "Producto no encontrado", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Error al cargar el producto", Toast.LENGTH_SHORT).show()
                Log.e("VisualizarProductoScreen", "Error en la consulta: ${exception.message}", exception)
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .verticalScroll(scrollState)
    ) {
        Column(
            modifier = Modifier
                .height(450.dp)
                .background(Color.White)
                .fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                AsyncImage(
                    model = producto?.imagen_producto ?: R.drawable.polygon_2,
                    contentDescription = "Imagen del producto",
                    modifier = Modifier
                        .fillMaxSize()
                        .border(1.dp, Color.Transparent),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(bottom = 50.dp, start = 10.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Bottom,
                ) {
                    Text(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .fillMaxWidth(),
                        text = producto?.nombre_producto ?: "Nombre del Producto",
                        color = Color.Black,
                        fontFamily = customFontFamily,
                        fontSize = 25.sp,
                    )
                    Text(
                        modifier = Modifier
                            .padding(start = 15.dp)
                            .fillMaxWidth(),
                        text = producto?.descripcionProducto ?: "Descripción del Producto",
                        color = Color.Black,
                        fontFamily = customFontFamily,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }

        Column(
            Modifier
                .fillMaxSize()
                .offset(y = (-50).dp)
                .background(color = ErizoHubTheme.Colors.background, RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End,
            ) {

            }

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
