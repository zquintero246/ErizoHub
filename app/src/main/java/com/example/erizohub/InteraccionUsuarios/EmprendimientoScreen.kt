package com.example.erizohub.InteraccionUsuarios

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.erizohub.ClasesBD.Emprendimiento
import com.example.erizohub.ClasesBD.Producto
import com.example.erizohub.Home.ProductoItem
import com.example.erizohub.InicioApp.ErizoHubTheme
import com.example.erizohub.InicioApp.ErizoHubTheme.Fonts.customFontFamily
import com.example.erizohub.MainActivity.Companion.uploadImageToDrive
import com.example.erizohub.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun EmprendimientoScreen(navController: NavController, idEmprendimiento: String) {
    val context = LocalContext.current
    val db = Firebase.firestore
    var emprendimiento by remember { mutableStateOf<Emprendimiento?>(null) }
    val scrollState = rememberScrollState()

    LaunchedEffect(idEmprendimiento) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            db.collection("users")
                .document(user.uid)
                .collection("emprendimientos")
                .document(idEmprendimiento)
                .get()
                .addOnSuccessListener { document ->
                    document.toObject(Emprendimiento::class.java)?.let {
                        emprendimiento = it
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error al cargar el emprendimiento", Toast.LENGTH_SHORT).show()
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
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                AsyncImage(
                    model = emprendimiento?.imagenEmprendimiento ?: R.drawable.polygon_2,
                    contentDescription = "Imagen del emprendimiento",
                    modifier = Modifier
                        .fillMaxSize()
                        .border(1.dp, Color.Transparent),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(bottom = 40.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Bottom,
                ) {
                    Text(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .fillMaxWidth(),
                        text = emprendimiento?.nombre_emprendimiento ?: "Nombre del Emprendimiento",
                        color = Color.Black,
                        fontFamily = customFontFamily,
                        fontSize = 25.sp,
                    )
                    Text(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .fillMaxWidth(),
                        text = emprendimiento?.descripcion ?: "Descripción del Emprendimiento",
                        color = Color.Gray,
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
                Button(
                    onClick = {
                        if (idEmprendimiento.isNotBlank()) {
                            navController.navigate("visualizar_productos/$idEmprendimiento")
                        } else {
                            Toast.makeText(context, "Emprendimiento no válido", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = ErizoHubTheme.Colors.primary
                    ),
                    modifier = Modifier.padding(end = 20.dp, top = 10.dp)
                ) {
                    Text(
                        text = "Productos",
                        fontFamily = customFontFamily,
                        fontSize = 15.sp,
                    )
                    Icon(
                        modifier = Modifier.padding(start = 5.dp),
                        painter = painterResource(id = R.drawable.arrow_icon),
                        contentDescription = "Productos",
                        tint = ErizoHubTheme.Colors.primary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}


@Composable
fun ProductoSelectionScreenEmprendimiento(navController: NavController, idEmprendimiento: String) {
    val db = FirebaseFirestore.getInstance()
    val productos = remember { mutableStateListOf<Producto>() }
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("EmprendimientoPrefs", Context.MODE_PRIVATE)
    val idEmprendimientoGuardado = sharedPreferences.getString("idEmprendimientoGuardado", "")
    val idEmprendimientoUsado = idEmprendimiento.ifEmpty { idEmprendimientoGuardado ?: "" }
    Log.d("ProductoSelectionScreen", "ID Emprendimiento usado: $idEmprendimientoUsado")



    LaunchedEffect(Unit) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null && idEmprendimientoUsado.isNotEmpty()) {
            db.collection("users")
                .document(user.uid)
                .collection("emprendimientos")
                .document(idEmprendimientoUsado)
                .collection("productos")
                .get()
                .addOnSuccessListener { result ->
                    productos.clear()
                    result.documents.forEach { document ->
                        val producto = document.toObject(Producto::class.java)
                        producto?.let { productos.add(it) }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error al obtener los productos", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "ID de Emprendimiento inválido", Toast.LENGTH_SHORT).show()
        }

    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(productos) { producto ->
            ProductoItem(producto) {
                navController.navigate("visualizar_producto/${producto.id_producto}")
            }
        }
    }

}



