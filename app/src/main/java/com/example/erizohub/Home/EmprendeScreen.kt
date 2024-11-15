package com.example.erizohub.Home

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
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
import coil.compose.rememberImagePainter
import coil3.compose.AsyncImage
import com.example.erizohub.ClasesBD.Emprendimiento
import com.example.erizohub.ClasesBD.Producto
import com.example.erizohub.ClasesBD.User
import com.example.erizohub.InicioApp.ErizoHubTheme
import com.example.erizohub.InicioApp.ErizoHubTheme.Fonts.customFontFamily
import com.example.erizohub.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.erizohub.MainActivity.Companion.uploadImageToDrive
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmprendeScreen(navController: NavController, emprendimiento: Emprendimiento) {
    val context = LocalContext.current
    var nombreEmprendimiento by remember { mutableStateOf("") }
    var descripcionEmprendimiento by remember { mutableStateOf("") }
    var imagenPerfilEmprendimiento by remember { mutableStateOf("") }
    val user = FirebaseAuth.getInstance().currentUser
    val db = FirebaseFirestore.getInstance()
    val scrollState = rememberScrollState()
    var isEmprendimientoGuardado by remember { mutableStateOf(false) }


    suspend fun getDriveService(context: Context): Drive = withContext(Dispatchers.IO) {
        val account = GoogleSignIn.getLastSignedInAccount(context)
        val credential = GoogleAccountCredential.usingOAuth2(
            context, listOf(DriveScopes.DRIVE_FILE)
        ).apply {
            selectedAccount = account?.account
        }

        Drive.Builder(
            GoogleNetHttpTransport.newTrustedTransport(),
            GsonFactory.getDefaultInstance(),
            credential
        ).setApplicationName("ErizoHub").build()
    }

    val driveService = remember { mutableStateOf<Drive?>(null) }

    LaunchedEffect(context) {
        val service = getDriveService(context)
        driveService.value = service
    }

    LaunchedEffect(Unit) {
        val sharedPreferences = context.getSharedPreferences("EmprendimientoPrefs", Context.MODE_PRIVATE)
        val idEmprendimientoGuardado = sharedPreferences.getString("idEmprendimientoGuardado", "")

        if (idEmprendimientoGuardado.isNullOrEmpty()) {
            nombreEmprendimiento = ""
            descripcionEmprendimiento = ""
            imagenPerfilEmprendimiento = ""
        }
    }


    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            driveService.value?.let { drive ->
                uploadImageToDrive(
                    driveService = drive,
                    uri = it,
                    onUploadComplete = { newProfilePictureUrl ->
                        imagenPerfilEmprendimiento = newProfilePictureUrl
                    },
                    context = context,
                    lifecycleScope = CoroutineScope(Dispatchers.IO)
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .verticalScroll(scrollState)
    ) {

        Column (
            modifier = Modifier
                .height(450.dp)
                .background(Color.White)
                .fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { galleryLauncher.launch("image/*")
                    },
                contentAlignment = Alignment.TopCenter
            ) {
                if (imagenPerfilEmprendimiento.isNotEmpty()) {
                    AsyncImage(
                        model = imagenPerfilEmprendimiento,
                        contentDescription = "Imagen de perfil del emprendimiento",
                        modifier = Modifier
                            .fillMaxSize()
                            .border(1.dp, Color.Transparent),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.polygon_2),
                        contentDescription = "profile",
                        modifier = Modifier
                            .size(300.dp)
                            .border(1.dp, Color.Transparent)
                            .padding(16.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Column(modifier = Modifier
                    .fillMaxHeight()
                    .padding(bottom = 40.dp)
                    .fillMaxWidth(),
                    verticalArrangement = Arrangement.Bottom,
                ) {
                    TextField(
                        value = nombreEmprendimiento,
                        onValueChange = { nombreEmprendimiento = it },
                        placeholder = {
                            Text(
                                "Nombre Emprendimiento",
                                color = Color.Black,
                                fontFamily = customFontFamily,
                                fontSize = 25.sp,
                                modifier = Modifier.background(Color.Transparent),
                                textAlign = TextAlign.Start
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .fillMaxWidth()
                            .border(1.dp, Color.Transparent),
                        textStyle = TextStyle(
                            color = Color.Black,
                            fontFamily = customFontFamily,
                            fontSize = 25.sp
                        )
                    )

                    TextField(
                        value = descripcionEmprendimiento,
                        onValueChange = { descripcionEmprendimiento = it },
                        placeholder = {
                            Text(
                                "Descripción",
                                color = Color.Black,
                                fontFamily = customFontFamily,
                                fontSize = 15.sp,
                                modifier = Modifier.background(Color.Transparent),
                                textAlign = TextAlign.Start
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .fillMaxWidth()
                            .border(1.dp, Color.Transparent),
                        textStyle = TextStyle(
                            color = Color.Black,
                            fontFamily = customFontFamily,
                            fontSize = 15.sp
                        )
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
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.End,
            ) {
                Button(
                    onClick = {
                        val idEmprendimiento = emprendimiento.idEmprendimiento
                        navController.navigate("crear_producto/$idEmprendimiento")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = ErizoHubTheme.Colors.primary
                    ),
                    enabled = isEmprendimientoGuardado,
                    modifier = Modifier
                        .padding(end = 20.dp, top = 10.dp)
                ) {
                    Text(
                        text = "Crear Producto",
                        fontFamily = customFontFamily,
                        fontSize = 15.sp,
                    )
                    Icon(
                        modifier = Modifier.padding(start = 5.dp),
                        painter = painterResource(id = R.drawable.arrow_icon),
                        contentDescription = "Crear Producto",
                        tint = ErizoHubTheme.Colors.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(100.dp))
                Button(
                    onClick = {
                        if (user != null && nombreEmprendimiento.isNotBlank() && descripcionEmprendimiento.isNotBlank()) {
                            val idEmprendimiento = UUID.randomUUID().toString()
                            val emprendimientoData = hashMapOf(
                                "nombre_emprendimiento" to nombreEmprendimiento,
                                "descripcion" to descripcionEmprendimiento,
                                "imagenEmprendimiento" to imagenPerfilEmprendimiento,
                                "idEmprendimiento" to idEmprendimiento
                            )

                            db.collection("users")
                                .document(user.uid)
                                .collection("emprendimientos")
                                .document(idEmprendimiento)
                                .set(emprendimientoData)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Emprendimiento guardado", Toast.LENGTH_SHORT).show()
                                    val sharedPreferences = context.getSharedPreferences("EmprendimientoPrefs", Context.MODE_PRIVATE)
                                    val editor = sharedPreferences.edit()

                                    editor.putString("idEmprendimientoGuardado", idEmprendimiento)
                                    editor.apply()

                                    isEmprendimientoGuardado = true
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Error al guardar el emprendimiento", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(225.dp)
                        .height(59.dp)
                        .border(1.dp, Color.Transparent, shape = RoundedCornerShape(30.dp))
                        .shadow(10.dp, shape = RoundedCornerShape(30.dp)),
                ) {
                    Text(
                        modifier = Modifier.alpha(0.7f),
                        fontFamily = customFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 12.sp,
                        text = "Guardar Emprendimiento"
                    )
                }
            }
            }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearProductos(navController: NavController, emprendimiento: Emprendimiento) {
    val context = LocalContext.current
    var nombreProducto by remember { mutableStateOf("") }
    var descripcionProducto by remember { mutableStateOf("") }
    var imagenProducto by remember { mutableStateOf("") }
    val user = FirebaseAuth.getInstance().currentUser
    val db = FirebaseFirestore.getInstance()
    val scrollState = rememberScrollState()

    // Obtiene el idEmprendimiento desde sharedPreferences si está disponible
    val sharedPreferences = context.getSharedPreferences("EmprendimientoPrefs", Context.MODE_PRIVATE)
    val idEmprendimientoGuardado = sharedPreferences.getString("idEmprendimientoGuardado", "")
    val idEmprendimiento = idEmprendimientoGuardado ?: emprendimiento.idEmprendimiento

    suspend fun getDriveService(context: Context): Drive = withContext(Dispatchers.IO) {
        val account = GoogleSignIn.getLastSignedInAccount(context)
        val credential = GoogleAccountCredential.usingOAuth2(
            context, listOf(DriveScopes.DRIVE_FILE)
        ).apply {
            selectedAccount = account?.account
        }

        Drive.Builder(
            GoogleNetHttpTransport.newTrustedTransport(),
            GsonFactory.getDefaultInstance(),
            credential
        ).setApplicationName("ErizoHub").build()
    }

    val driveService = remember { mutableStateOf<Drive?>(null) }

    LaunchedEffect(context) {
        val service = getDriveService(context)
        driveService.value = service
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            driveService.value?.let { drive ->
                uploadImageToDrive(
                    driveService = drive,
                    uri = it,
                    onUploadComplete = { newProfilePictureUrl ->
                        imagenProducto = newProfilePictureUrl
                    },
                    context = context,
                    lifecycleScope = CoroutineScope(Dispatchers.IO)
                )
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
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { galleryLauncher.launch("image/*") },
                contentAlignment = Alignment.TopCenter
            ) {
                if (imagenProducto.isNotEmpty()) {
                    AsyncImage(
                        model = imagenProducto,
                        contentDescription = "Imagen del producto",
                        modifier = Modifier
                            .fillMaxSize()
                            .border(1.dp, Color.Transparent),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.polygon_2),
                        contentDescription = "producto",
                        modifier = Modifier
                            .size(300.dp)
                            .border(1.dp, Color.Transparent)
                            .padding(16.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(bottom = 40.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Bottom,
                ) {
                    TextField(
                        value = nombreProducto,
                        onValueChange = { nombreProducto = it },
                        placeholder = {
                            Text(
                                "Nombre producto",
                                color = Color.Black,
                                fontFamily = customFontFamily,
                                fontSize = 25.sp,
                                modifier = Modifier.background(Color.Transparent),
                                textAlign = TextAlign.Start
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .fillMaxWidth()
                            .border(1.dp, Color.Transparent),
                        textStyle = TextStyle(
                            color = Color.Black,
                            fontFamily = customFontFamily,
                            fontSize = 25.sp
                        )
                    )

                    TextField(
                        value = descripcionProducto,
                        onValueChange = { descripcionProducto = it },
                        placeholder = {
                            Text(
                                "Descripción",
                                color = Color.Black,
                                fontFamily = customFontFamily,
                                fontSize = 15.sp,
                                modifier = Modifier.background(Color.Transparent),
                                textAlign = TextAlign.Start
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .fillMaxWidth()
                            .border(1.dp, Color.Transparent),
                        textStyle = TextStyle(
                            color = Color.Black,
                            fontFamily = customFontFamily,
                            fontSize = 15.sp
                        )
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
                        navController.navigate("producto_selection/$idEmprendimiento")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = ErizoHubTheme.Colors.primary
                    ),
                    modifier = Modifier.padding(end = 20.dp, top = 10.dp)
                ) {
                    Text(
                        text = "Ver productos",
                        fontFamily = customFontFamily,
                        fontSize = 15.sp,
                    )
                    Icon(
                        modifier = Modifier.padding(start = 5.dp),
                        painter = painterResource(id = R.drawable.arrow_icon),
                        contentDescription = "Ver productos",
                        tint = ErizoHubTheme.Colors.primary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .background(Color.White, shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(100.dp))
                    Button(
                        onClick = {
                            if (user != null && nombreProducto.isNotBlank() && descripcionProducto.isNotBlank()) {
                                val idProducto = UUID.randomUUID().toString()
                                val productoData = hashMapOf(
                                    "id_producto" to idProducto,
                                    "nombre_producto" to nombreProducto,
                                    "descripcionProducto" to descripcionProducto,
                                    "imagen_producto" to imagenProducto,
                                    "idEmprendimiento" to idEmprendimiento
                                )

                                if (idEmprendimiento.isNotBlank()) {
                                    db.collection("users")
                                        .document(user.uid)
                                        .collection("emprendimientos")
                                        .document(idEmprendimiento)
                                        .collection("productos")
                                        .document(idProducto)
                                        .set(productoData)
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                context,
                                                "Producto guardado",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(
                                                context,
                                                "Error al guardar el producto",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "ID de emprendimiento inválido",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .width(225.dp)
                            .height(59.dp)
                            .border(1.dp, Color.Transparent, shape = RoundedCornerShape(30.dp))
                            .shadow(10.dp, shape = RoundedCornerShape(30.dp))
                    ) {
                        Text(
                            modifier = Modifier.alpha(0.7f),
                            fontFamily = customFontFamily,
                            fontWeight = FontWeight.Light,
                            fontSize = 12.sp,
                            text = "Guardar Producto"
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun ProductoSelectionScreen(navController: NavController, idEmprendimiento: String) {
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = ErizoHubTheme.Colors.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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


        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .background(color = Color.White, RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
        ) {
            items(productos) { producto ->
                ProductoItem(producto) {
                    navController.navigate("visualizar_producto/${producto.id_producto}")
                }
            }
        }

        Button(
            onClick = {
                sharedPreferences.edit().remove("idEmprendimientoGuardado").apply()

                Toast.makeText(context, "Creación de emprendimiento finalizada", Toast.LENGTH_SHORT).show()

                navController.navigate("home") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = ErizoHubTheme.Colors.primary,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(25.dp))
        ) {
            Text(
                text = "Terminar Creación",
                fontFamily = customFontFamily,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }

    }

}


@Composable
fun ProductoItem(producto: Producto, onClick: () -> Unit) {
    Spacer(modifier = Modifier.height(16.dp))

    Card(
        modifier = Modifier
            .width(600.dp)
            .clickable(onClick = onClick)
            .padding(16.dp)
            .background(Color.White, shape = RoundedCornerShape(16.dp))
            .shadow(4.dp, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(16.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = rememberImagePainter(producto.imagen_producto),
                contentDescription = "Foto del producto",
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.width(250.dp)
            ) {
                Text(
                    text = producto.nombre_producto,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF6200EE)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = producto.descripcionProducto,
                    fontFamily = customFontFamily,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

