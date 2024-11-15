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
import com.example.erizohub.ClasesBD.Emprendimiento
import com.example.erizohub.ClasesBD.Producto
import com.example.erizohub.InicioApp.ErizoHubTheme
import com.example.erizohub.InicioApp.ErizoHubTheme.Fonts.customFontFamily
import com.example.erizohub.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

fun getFirstWord(text: String): String {
    return text.split(" ").firstOrNull() ?: ""
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(onSearchTextChanged: (String) -> Unit, placeHolder: String = "") {
    var searchText by remember { mutableStateOf("") }

    TextField(
        value = searchText,
        onValueChange = {
            searchText = it
            onSearchTextChanged(it)
        },
        label = {
            Text(
                text = placeHolder,
                color = ErizoHubTheme.Colors.textFieldText,
                fontFamily = customFontFamily,
                fontSize = 10.sp
            )
        },
        leadingIcon = {
            Image(
                painter = painterResource(id = R.drawable.lupa),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = ErizoHubTheme.Colors.textField,
        ),
        modifier = Modifier
            .height(61.dp)
            .width(367.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(color = ErizoHubTheme.Colors.textField)
            .border(1.dp, ErizoHubTheme.Colors.textField, RoundedCornerShape(50.dp))
            .padding(horizontal = 16.dp)
    )
}

@Composable
fun HomeScreen(navController: NavController) {
    var userName by remember { mutableStateOf("") }
    val primerNombre by remember { derivedStateOf { getFirstWord(userName) } }
    val listEmprendimientos = remember { mutableStateOf<List<Emprendimiento>>(emptyList()) }
    val filteredEmprendimientos = remember { mutableStateOf<List<Emprendimiento>>(emptyList()) }
    val db = Firebase.firestore
    val user = FirebaseAuth.getInstance().currentUser

    Log.d("HomeScreen", "Primer nombre: $primerNombre")

    LaunchedEffect(Unit) {
        db.collectionGroup("emprendimientos").get()
            .addOnSuccessListener { response ->
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
                                        precio = (productoMap["precio"] as? Number)?.toDouble() ?: 0.0,
                                        imagen_producto = productoMap["imagen_producto"] as? String ?: ""
                                    )
                                } catch (e: Exception) {
                                    Log.e("HomeScreen", "Error al parsear producto: $productoMap", e)
                                    null
                                }
                            }?.toMutableList() ?: mutableListOf(),
                            comentarios = (document.get("comentarios") as? List<String>)?.toMutableList() ?: mutableListOf()
                        )
                    } catch (e: Exception) {
                        Log.e("HomeScreen", "Error al parsear emprendimiento: ${document.id}", e)
                        null
                    }
                }
                Log.d("HomeScreen", "Total emprendimientos cargados: ${emprendimientos.size}")
                listEmprendimientos.value = emprendimientos
                filteredEmprendimientos.value = emprendimientos
            }
            .addOnFailureListener { e ->
                Log.e("HomeScreen", "Error al obtener emprendimientos", e)
            }
    }

    LaunchedEffect(user) {
        user?.let { currentUser ->
            db.collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        userName = document.getString("userName") ?: ""
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(top = 20.dp, bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                color = ErizoHubTheme.Colors.primary,
                text = "Bienvenido $primerNombre!",
                fontSize = 24.sp,
                fontFamily = customFontFamily,
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(10.dp))

            SearchField(
                onSearchTextChanged = { query ->
                    val queryLowercase = query.lowercase()
                    filteredEmprendimientos.value = if (queryLowercase.isEmpty()) {
                        listEmprendimientos.value
                    } else {
                        listEmprendimientos.value.filter {
                            it.nombre_emprendimiento.contains(queryLowercase, ignoreCase = true)
                        }
                    }
                },
                placeHolder = "Buscar emprendimiento"
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Emprendimientos recientes",
                fontSize = 20.sp,
                fontFamily = customFontFamily,
                color = ErizoHubTheme.Colors.primary,
            )
            Spacer(modifier = Modifier.height(29.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(filteredEmprendimientos.value) { emprendimiento ->
                    EmprendimientoItem(myEmprendimiento = emprendimiento) {
                        navController.navigate("emprendimientoScreen/${emprendimiento.idEmprendimiento}")
                    }
                }
            }
        }
    }
}

@Composable
fun EmprendimientoItem(myEmprendimiento: Emprendimiento, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(5.dp, shape = RoundedCornerShape(20.dp))
            .border(1.dp, Color.Transparent, RoundedCornerShape(20.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF6F6F6))
                .border(3.dp, Color.Transparent, RoundedCornerShape(20.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = myEmprendimiento.imagenEmprendimiento,
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .padding(end = 12.dp)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                Text(
                    text = myEmprendimiento.nombre_emprendimiento,
                    fontSize = 13.sp,
                    fontFamily = ErizoHubTheme.Fonts.customFontFamily,
                    color = ErizoHubTheme.Colors.background,
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .clickable { onClick() }
                )
                val shortenedDescription = if (myEmprendimiento.descripcion.length > 50) {
                    "${myEmprendimiento.descripcion.take(100)}..."
                } else {
                    myEmprendimiento.descripcion
                }
                Text(
                    text = shortenedDescription,
                    fontSize = 10.sp,
                    fontFamily = customFontFamily,
                    color = Color.Gray
                )
            }
        }
    }
}