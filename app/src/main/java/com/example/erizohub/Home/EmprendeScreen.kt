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
    val context = LocalContext.current // Contexto local para usar funciones de Android.
    var nombreEmprendimiento by remember { mutableStateOf("") } // Estado para el nombre del emprendimiento.
    var descripcionEmprendimiento by remember { mutableStateOf("") } // Estado para la descripción del emprendimiento.
    var imagenPerfilEmprendimiento by remember { mutableStateOf("") } // URL de la imagen del emprendimiento.
    val user = FirebaseAuth.getInstance().currentUser // Usuario autenticado en Firebase.
    val db = FirebaseFirestore.getInstance() // Instancia de Firestore.
    val scrollState = rememberScrollState() // Estado para manejar el desplazamiento de la pantalla.
    var isEmprendimientoGuardado by remember { mutableStateOf(false) } // Indica si el emprendimiento fue guardado.

    // Función para obtener el servicio de Google Drive con credenciales de OAuth2.
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

    val driveService = remember { mutableStateOf<Drive?>(null) } // Estado para el servicio de Google Drive.

    // Inicializa el servicio de Google Drive al cargar la pantalla.
    LaunchedEffect(context) {
        val service = getDriveService(context)
        driveService.value = service
    }

    // Verifica si hay un emprendimiento guardado en las preferencias compartidas.
    LaunchedEffect(Unit) {
        val sharedPreferences = context.getSharedPreferences("EmprendimientoPrefs", Context.MODE_PRIVATE)
        val idEmprendimientoGuardado = sharedPreferences.getString("idEmprendimientoGuardado", "")

        if (idEmprendimientoGuardado.isNullOrEmpty()) {
            nombreEmprendimiento = ""
            descripcionEmprendimiento = ""
            imagenPerfilEmprendimiento = ""
        }
    }

    // Lanza un selector de galería para cargar una imagen.
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            driveService.value?.let { drive ->
                uploadImageToDrive(
                    driveService = drive,
                    uri = it,
                    onUploadComplete = { newProfilePictureUrl ->
                        imagenPerfilEmprendimiento = newProfilePictureUrl // Actualiza la URL de la imagen.
                    },
                    context = context,
                    lifecycleScope = CoroutineScope(Dispatchers.IO)
                )
            }
        }
    }

    // Contenedor principal.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .verticalScroll(scrollState) // Habilita el desplazamiento.
    ) {
        // Sección superior para mostrar y seleccionar la imagen de perfil.
        Column(
            modifier = Modifier
                .height(450.dp)
                .background(Color.White)
                .fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { galleryLauncher.launch("image/*") }, // Lanza el selector de imágenes.
                contentAlignment = Alignment.TopCenter
            ) {
                if (imagenPerfilEmprendimiento.isNotEmpty()) {
                    // Si hay una imagen, se muestra.
                    AsyncImage(
                        model = imagenPerfilEmprendimiento,
                        contentDescription = "Imagen de perfil del emprendimiento",
                        modifier = Modifier
                            .fillMaxSize()
                            .border(1.dp, Color.Transparent),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Imagen predeterminada si no hay ninguna seleccionada.
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

                // Campos para el nombre y la descripción del emprendimiento.
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(bottom = 40.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Bottom,
                ) {
                    // Campo de texto para el nombre.
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

                    // Campo de texto para la descripción.
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

        // Sección inferior para botones y acciones adicionales.
        Column(
            Modifier
                .fillMaxSize()
                .offset(y = (-50).dp) // Ajusta la posición vertical.
                .background(color = ErizoHubTheme.Colors.background, RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
        ) {
            // Botón para crear productos dentro del emprendimiento.
            Column(
                modifier = Modifier.fillMaxWidth(),
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

            // Botón para guardar el emprendimiento.
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
                            // Verifica si el usuario está autenticado y si los campos requeridos están completos.
                            if (user != null && nombreEmprendimiento.isNotBlank() && descripcionEmprendimiento.isNotBlank()) {
                                // Genera un ID único para el emprendimiento.
                                val idEmprendimiento = UUID.randomUUID().toString()

                                // Crea un mapa de datos para el emprendimiento con la información ingresada.
                                val emprendimientoData = hashMapOf(
                                    "nombre_emprendimiento" to nombreEmprendimiento, // Nombre del emprendimiento.
                                    "descripcion" to descripcionEmprendimiento, // Descripción del emprendimiento.
                                    "imagenEmprendimiento" to imagenPerfilEmprendimiento, // URL de la imagen seleccionada.
                                    "idEmprendimiento" to idEmprendimiento // ID único del emprendimiento.
                                )

                                // Guarda el emprendimiento en Firestore dentro de la colección del usuario actual.
                                db.collection("users")
                                    .document(user.uid) // ID del usuario actual.
                                    .collection("emprendimientos") // Subcolección "emprendimientos".
                                    .document(idEmprendimiento) // Documento con el ID generado.
                                    .set(emprendimientoData) // Guarda los datos del emprendimiento.
                                    .addOnSuccessListener {
                                        // Muestra un mensaje de éxito si el emprendimiento se guarda correctamente.
                                        Toast.makeText(context, "Emprendimiento guardado", Toast.LENGTH_SHORT).show()

                                        // Guarda el ID del emprendimiento en las preferencias compartidas.
                                        val sharedPreferences = context.getSharedPreferences("EmprendimientoPrefs", Context.MODE_PRIVATE)
                                        val editor = sharedPreferences.edit()
                                        editor.putString("idEmprendimientoGuardado", idEmprendimiento) // Guarda el ID.
                                        editor.apply() // Aplica los cambios.

                                        // Actualiza el estado para indicar que el emprendimiento fue guardado.
                                        isEmprendimientoGuardado = true
                                    }
                                    .addOnFailureListener {
                                        // Muestra un mensaje de error si ocurre algún problema al guardar.
                                        Toast.makeText(context, "Error al guardar el emprendimiento", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                // Muestra un mensaje de advertencia si los campos requeridos no están completos.
                                Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                            }
                        }
                    ,
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


@OptIn(ExperimentalMaterial3Api::class) // Habilita el uso de funciones experimentales de Material Design 3.
@Composable
fun CrearProductos(navController: NavController, emprendimiento: Emprendimiento) {
    val context = LocalContext.current // Contexto local para operaciones específicas de Android.
    var nombreProducto by remember { mutableStateOf("") } // Estado para el nombre del producto.
    var descripcionProducto by remember { mutableStateOf("") } // Estado para la descripción del producto.
    var imagenProducto by remember { mutableStateOf("") } // Estado para la URL de la imagen del producto.
    val user = FirebaseAuth.getInstance().currentUser // Usuario autenticado en Firebase.
    val db = FirebaseFirestore.getInstance() // Instancia de Firestore.
    val scrollState = rememberScrollState() // Estado para manejar el desplazamiento vertical.

    // Obtiene el ID del emprendimiento desde preferencias compartidas o del parámetro recibido.
    val sharedPreferences = context.getSharedPreferences("EmprendimientoPrefs", Context.MODE_PRIVATE)
    val idEmprendimientoGuardado = sharedPreferences.getString("idEmprendimientoGuardado", "")
    val idEmprendimiento = idEmprendimientoGuardado ?: emprendimiento.idEmprendimiento

    // Función para obtener el servicio de Google Drive con credenciales OAuth2.
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

    val driveService = remember { mutableStateOf<Drive?>(null) } // Estado para el servicio de Google Drive.

    // Carga el servicio de Google Drive al iniciar la pantalla.
    LaunchedEffect(context) {
        val service = getDriveService(context)
        driveService.value = service
    }

    // Selector de galería para subir imágenes al producto.
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            driveService.value?.let { drive ->
                uploadImageToDrive(
                    driveService = drive,
                    uri = it,
                    onUploadComplete = { newProfilePictureUrl ->
                        imagenProducto = newProfilePictureUrl // Actualiza la URL de la imagen cargada.
                    },
                    context = context,
                    lifecycleScope = CoroutineScope(Dispatchers.IO)
                )
            }
        }
    }

    // Contenedor principal de la pantalla.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .verticalScroll(scrollState) // Habilita el desplazamiento vertical.
    ) {
        // Sección superior para mostrar y seleccionar la imagen del producto.
        Column(
            modifier = Modifier
                .height(450.dp)
                .background(Color.White)
                .fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { galleryLauncher.launch("image/*") }, // Lanza el selector de imágenes.
                contentAlignment = Alignment.TopCenter
            ) {
                if (imagenProducto.isNotEmpty()) {
                    // Muestra la imagen seleccionada si está disponible.
                    AsyncImage(
                        model = imagenProducto,
                        contentDescription = "Imagen del producto",
                        modifier = Modifier
                            .fillMaxSize()
                            .border(1.dp, Color.Transparent),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Imagen predeterminada si no se ha cargado ninguna.
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

                // Campos de texto para el nombre y descripción del producto.
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(bottom = 40.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Bottom,
                ) {
                    // Campo para el nombre del producto.
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

                    // Campo para la descripción del producto.
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

        // Botones y acciones para guardar el producto o navegar.
        Column(
            Modifier
                .fillMaxSize()
                .offset(y = (-50).dp)
                .background(color = ErizoHubTheme.Colors.background, RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
        ) {
            // Botón para ver los productos existentes.
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End,
            ) {
                Button(
                    onClick = {
                        navController.navigate("producto_selection/$idEmprendimiento") // Navega a la selección de productos.
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

                // Botón para guardar un nuevo producto.
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
                            // Validación de campos antes de guardar.
                            if (user != null && nombreProducto.isNotBlank() && descripcionProducto.isNotBlank()) {
                                val idProducto = UUID.randomUUID().toString() // Genera un ID único para el producto.
                                val productoData = hashMapOf(
                                    "id_producto" to idProducto,
                                    "nombre_producto" to nombreProducto,
                                    "descripcionProducto" to descripcionProducto,
                                    "imagen_producto" to imagenProducto,
                                    "idEmprendimiento" to idEmprendimiento
                                )

                                // Guarda el producto en Firestore.
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
    val db = FirebaseFirestore.getInstance() // Instancia de Firestore para la base de datos.
    val productos = remember { mutableStateListOf<Producto>() } // Lista observable de productos.
    val context = LocalContext.current // Contexto local para mostrar `Toast` o acceder a preferencias compartidas.
    val sharedPreferences = context.getSharedPreferences("EmprendimientoPrefs", Context.MODE_PRIVATE)

    // Obtiene el ID del emprendimiento desde las preferencias compartidas o usa el parámetro proporcionado.
    val idEmprendimientoGuardado = sharedPreferences.getString("idEmprendimientoGuardado", "")
    val idEmprendimientoUsado = idEmprendimiento.ifEmpty { idEmprendimientoGuardado ?: "" }
    Log.d("ProductoSelectionScreen", "ID Emprendimiento usado: $idEmprendimientoUsado") // Log para depuración.

    // Carga los productos asociados al emprendimiento al iniciar la pantalla.
    LaunchedEffect(Unit) {
        val user = FirebaseAuth.getInstance().currentUser // Usuario autenticado en Firebase.
        if (user != null && idEmprendimientoUsado.isNotEmpty()) {
            // Consulta a Firestore para obtener la lista de productos.
            db.collection("users")
                .document(user.uid) // Documento del usuario autenticado.
                .collection("emprendimientos")
                .document(idEmprendimientoUsado) // Documento del emprendimiento.
                .collection("productos") // Subcolección de productos.
                .get()
                .addOnSuccessListener { result ->
                    productos.clear() // Limpia la lista antes de agregar nuevos elementos.
                    result.documents.forEach { document ->
                        val producto = document.toObject(Producto::class.java) // Convierte el documento en un objeto Producto.
                        producto?.let { productos.add(it) } // Agrega el producto a la lista si no es nulo.
                    }
                }
                .addOnFailureListener {
                    // Muestra un mensaje de error si falla la consulta.
                    Toast.makeText(context, "Error al obtener los productos", Toast.LENGTH_SHORT).show()
                }
        } else {
            // Muestra un mensaje si el ID del emprendimiento es inválido.
            Toast.makeText(context, "ID de Emprendimiento inválido", Toast.LENGTH_SHORT).show()
        }
    }

    // Estructura principal de la pantalla.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = ErizoHubTheme.Colors.background), // Fondo de la pantalla.
        horizontalAlignment = Alignment.CenterHorizontally // Alinea el contenido horizontalmente al centro.
    ) {
        // Encabezado de la pantalla.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(color = ErizoHubTheme.Colors.background), // Fondo del encabezado.
            horizontalArrangement = Arrangement.Center, // Centra el texto horizontalmente.
            verticalAlignment = Alignment.CenterVertically, // Alinea el texto verticalmente al centro.
        ) {
            Text(
                text = "Productos", // Título de la pantalla.
                fontSize = 24.sp, // Tamaño del texto.
                fontFamily = customFontFamily, // Fuente personalizada.
                color = Color.White // Color del texto.
            )
        }

        Spacer(modifier = Modifier.height(10.dp)) // Espacio entre el encabezado y la lista.

        // Lista de productos en un contenedor desplazable.
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .background(color = Color.White, RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)) // Fondo blanco con esquinas redondeadas.
        ) {
            // Recorre la lista de productos y muestra cada uno como un `ProductoItem`.
            items(productos) { producto ->
                ProductoItem(producto) {
                    // Navega a la pantalla de visualización del producto seleccionado.
                    navController.navigate("visualizar_producto/${producto.id_producto}")
                }
            }
        }

        // Botón para terminar la creación del emprendimiento.
        Button(
            onClick = {
                // Elimina el ID del emprendimiento guardado en las preferencias compartidas.
                sharedPreferences.edit().remove("idEmprendimientoGuardado").apply()

                // Muestra un mensaje indicando que la creación ha terminado.
                Toast.makeText(context, "Creación de emprendimiento finalizada", Toast.LENGTH_SHORT).show()

                // Navega de vuelta a la pantalla de inicio y limpia el stack de navegación.
                navController.navigate("home") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true // Elimina las pantallas anteriores del stack.
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = ErizoHubTheme.Colors.primary, // Color de fondo del botón.
                contentColor = Color.White // Color del texto del botón.
            ),
            modifier = Modifier
                .fillMaxWidth() // El botón ocupa todo el ancho.
                .padding(16.dp) // Margen alrededor del botón.
                .clip(RoundedCornerShape(25.dp)) // Bordes redondeados del botón.
        ) {
            Text(
                text = "Terminar Creación", // Texto del botón.
                fontFamily = customFontFamily, // Fuente personalizada.
                fontSize = 16.sp, // Tamaño del texto.
                textAlign = TextAlign.Center // Alineación centrada del texto.
            )
        }
    }
}



@Composable
fun ProductoItem(producto: Producto, onClick: () -> Unit) {
    // Espaciador para separar este elemento de otros elementos superiores.
    Spacer(modifier = Modifier.height(16.dp))

    // Contenedor principal del producto en forma de tarjeta.
    Card(
        modifier = Modifier
            .width(600.dp) // Ancho fijo para la tarjeta.
            .clickable(onClick = onClick) // Hace que toda la tarjeta sea clickeable.
            .padding(16.dp) // Espaciado externo alrededor de la tarjeta.
            .background(Color.White, shape = RoundedCornerShape(16.dp)) // Fondo blanco con esquinas redondeadas.
            .shadow(4.dp, shape = RoundedCornerShape(16.dp)) // Sombra para darle efecto de elevación.
            .clip(RoundedCornerShape(16.dp)), // Bordes redondeados para la tarjeta.
        shape = RoundedCornerShape(16.dp), // Forma de la tarjeta.
    ) {
        // Disposición horizontal del contenido de la tarjeta.
        Row(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(16.dp)) // Fondo blanco con esquinas redondeadas.
                .padding(16.dp) // Espaciado interno.
                .fillMaxSize(), // Ocupa todo el tamaño asignado.
            verticalAlignment = Alignment.CenterVertically // Alinea los elementos verticalmente al centro.
        ) {
            // Imagen del producto.
            Image(
                painter = rememberImagePainter(producto.imagen_producto), // Carga la imagen desde la URL.
                contentDescription = "Foto del producto", // Descripción para accesibilidad.
                modifier = Modifier
                    .size(40.dp) // Tamaño fijo de la imagen.
                    .clip(RoundedCornerShape(20.dp)) // Bordes redondeados para la imagen.
                    .background(Color.White) // Fondo blanco por si no se carga la imagen.
            )

            // Espaciador horizontal entre la imagen y el texto.
            Spacer(modifier = Modifier.width(16.dp))

            // Columna para mostrar los textos del producto.
            Column(
                verticalArrangement = Arrangement.Center, // Centra los textos verticalmente.
                modifier = Modifier.width(250.dp) // Limita el ancho de la columna.
            ) {
                // Nombre del producto.
                Text(
                    text = producto.nombre_producto, // Texto con el nombre del producto.
                    fontWeight = FontWeight.Bold, // Texto en negrita.
                    fontSize = 18.sp, // Tamaño de la fuente.
                    color = Color(0xFF6200EE) // Color morado para destacar.
                )

                // Espaciador entre el nombre y la descripción.
                Spacer(modifier = Modifier.height(4.dp))

                // Descripción del producto.
                Text(
                    text = producto.descripcionProducto, // Texto con la descripción del producto.
                    fontFamily = customFontFamily, // Fuente personalizada.
                    fontSize = 14.sp, // Tamaño de la fuente.
                    color = Color.Gray // Color gris para menor énfasis.
                )
            }
        }
    }
}


