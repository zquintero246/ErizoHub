package com.example.erizohub
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun EmprendeScreen(navController: NavController) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    var urlText by remember { mutableStateOf("") }
    var nombreEmprendimiento by remember { mutableStateOf("") }
    var descripcionEmprendimiento by remember { mutableStateOf("") }
    var imagenPerfilEmprendimiento by remember { mutableStateOf("") }
    var imagenes by remember { mutableStateOf(mutableListOf<String>()) }
    val user = FirebaseAuth.getInstance().currentUser
    val db = FirebaseFirestore.getInstance()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Espacio para la imagen de perfil del emprendimiento
        Row(
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { expanded = !expanded },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
            ) {
                if (imagenPerfilEmprendimiento.isNotEmpty()) {
                    AsyncImage(
                        model = imagenPerfilEmprendimiento,
                        contentDescription = "Imagen de perfil del emprendimiento",
                        modifier = Modifier.size(150.dp)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "profile",
                        modifier = Modifier.size(150.dp)
                    )
                }

                // Menú desplegable para ingresar la URL de la imagen de perfil
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        TextField(
                            value = urlText,
                            onValueChange = { urlText = it },
                            label = { Text("Insert URL") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                if (urlText.isNotBlank()) {
                                    imagenPerfilEmprendimiento = urlText
                                    urlText = ""
                                    expanded = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6F04D9), contentColor = Color.White)
                        ) {
                            Text("Cargar Imagen")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para ingresar el nombre del emprendimiento
        TextField(
            value = nombreEmprendimiento,
            onValueChange = { nombreEmprendimiento = it },
            label = { Text("Nombre del Emprendimiento") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo para ingresar la descripción del emprendimiento
        TextField(
            value = descripcionEmprendimiento,
            onValueChange = { descripcionEmprendimiento = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Espacio para agregar múltiples imágenes
        Text("Imágenes del Emprendimiento")
        LazyRow {
            items(imagenes.size) { index ->
                AsyncImage(
                    model = imagenes[index],
                    contentDescription = "Imagen $index",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(4.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = urlText,
            onValueChange = { urlText = it },
            label = { Text("Añadir URL de Imagen") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                if (urlText.isNotBlank()) {
                    imagenes.add(urlText)
                    urlText = ""
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6F04D9), contentColor = Color.White)
        ) {
            Text("Añadir Imagen")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para guardar el emprendimiento en Firestore
        Button(
            onClick = {
                if (user != null && nombreEmprendimiento.isNotBlank() && descripcionEmprendimiento.isNotBlank()) {
                    val emprendimientoData = hashMapOf(
                        "nombre_emprendimiento" to nombreEmprendimiento,
                        "descripcion" to descripcionEmprendimiento,
                        "imagenEmprendimiento" to imagenPerfilEmprendimiento,
                        "imagenes" to imagenes
                    )
                    db.collection("users").document(user.uid)
                        .collection("emprendimientos")
                        .add(emprendimientoData)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Emprendimiento guardado", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Error al guardar el emprendimiento", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6F04D9), contentColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Emprendimiento")
        }
    }
}