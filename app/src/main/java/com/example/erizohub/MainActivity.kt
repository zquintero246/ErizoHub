package com.example.erizohub

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.erizohub.ui.theme.ErizoHubTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.erizohub.ClasesBD.Emprendimiento
import com.example.erizohub.Home.ChatScreen
import com.example.erizohub.Home.ChatSelectionScreen
import com.example.erizohub.Home.CrearProductos
import com.example.erizohub.Home.EmprendeScreen
import com.example.erizohub.Home.HomeScreen
import com.example.erizohub.Home.ProductoSelectionScreen
import com.example.erizohub.Home.UserSelectionScreen
import com.example.erizohub.InteraccionUsuarios.EmprendimientoScreen
import com.example.erizohub.InteraccionUsuarios.ProductoSelectionScreenEmprendimiento
import com.example.erizohub.InteraccionUsuarios.VisualizarProductoScreen
import com.example.erizohub.UsuarioLogeado.EmprendimientosActivos
import com.example.erizohub.UsuarioLogeado.UserScreen
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.GoogleApiAvailability
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.InputStreamContent
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : ComponentActivity() {
    // Declaración de variables globales
    private lateinit var auth: FirebaseAuth // Variable para manejar la autenticación de Firebase
    private var pendingUri: Uri? = null // Variable para almacenar temporalmente un URI pendiente

    // Función para iniciar la subida de una imagen a Google Drive
    private fun startUploadImageToDrive(uri: Uri, onUploadComplete: (String) -> Unit, context: Context) {
        lifecycleScope.launch { // Lanza una coroutine en el scope del ciclo de vida actual
            val driveService = getDriveService(context = context) // Obtiene el servicio de Google Drive
            uploadImageToDrive(driveService, uri, onUploadComplete, context, lifecycleScope) // Llama a la función para subir la imagen
        }
    }

    // Bloque companion para constantes y funciones estáticas
    companion object {
        private const val REQUEST_AUTHORIZATION = 1001 // Constante para manejar solicitudes de autorización

        // Función suspendida para obtener el servicio de Google Drive
        suspend fun getDriveService(context: Context): Drive {
            return withContext(Dispatchers.IO) { // Cambia a un hilo de I/O para operaciones intensivas
                val account = GoogleSignIn.getLastSignedInAccount(context) // Obtiene la cuenta de Google autenticada
                val credential = GoogleAccountCredential.usingOAuth2(
                    context, listOf(DriveScopes.DRIVE_FILE) // Define el alcance DRIVE_FILE para acceder a archivos
                ).apply {
                    selectedAccount = account?.account // Asigna la cuenta seleccionada al credential
                }

                // Construye y devuelve el servicio de Google Drive
                Drive.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    credential
                ).setApplicationName("ErizoHub").build()
            }
        }

        // Crea una carpeta en Google Drive si no existe
        private fun createFolderIfNotExists(
            driveService: Drive,
            lifecycleScope: CoroutineScope,
            folderName: String = "appDataFolder", // Nombre de la carpeta (por defecto "appDataFolder")
            onFolderCreated: (String) -> Unit // Callback con el ID de la carpeta creada
        ) {
            lifecycleScope.launch(Dispatchers.IO) { // Lanza una coroutine en un hilo de I/O
                try {
                    // Verifica si la carpeta ya existe
                    val result = driveService.files().list()
                        .setQ("name='$folderName' and mimeType='application/vnd.google-apps.folder' and trashed=false")
                        .setSpaces("drive")
                        .execute()

                    // Si la carpeta no existe, la crea
                    val folderId = if (result.files.isNullOrEmpty()) {
                        val fileMetadata = File().apply {
                            name = folderName // Asigna el nombre de la carpeta
                            mimeType = "application/vnd.google-apps.folder" // Especifica el tipo de archivo como carpeta
                        }
                        val folder = driveService.files().create(fileMetadata)
                            .setFields("id")
                            .execute()
                        folder.id // Devuelve el ID de la carpeta creada
                    } else {
                        result.files[0].id // Devuelve el ID de la carpeta existente
                    }

                    onFolderCreated(folderId) // Llama al callback con el ID de la carpeta
                } catch (e: Exception) {
                    Log.e("MainActivity", "Error al crear o verificar carpeta: ${e.message}") // Manejo de errores
                }
            }
        }

        // Función para subir una imagen a Google Drive
        fun uploadImageToDrive(
            driveService: Drive,
            uri: Uri,
            onUploadComplete: (String) -> Unit,
            context: Context,
            lifecycleScope: CoroutineScope
        ) {
            // Verifica o crea la carpeta antes de subir el archivo
            createFolderIfNotExists(driveService, lifecycleScope) { folderId ->
                val fileMetadata = File().apply {
                    name = "profile_picture_${FirebaseAuth.getInstance().currentUser?.uid}.jpg" // Nombre dinámico del archivo
                    parents = listOf(folderId) // Asigna la carpeta como contenedora del archivo
                }

                val inputStream = context.contentResolver.openInputStream(uri) // Obtiene el InputStream del archivo
                val mediaContent = InputStreamContent("image/jpeg", inputStream) // Crea un contenido de tipo imagen JPEG

                lifecycleScope.launch { // Lanza una coroutine para subir el archivo
                    try {
                        // Subida del archivo a Google Drive
                        val file = withContext(Dispatchers.IO) {
                            driveService.files().create(fileMetadata, mediaContent)
                                .setFields("id, webContentLink")
                                .execute()
                        }
                        inputStream?.close() // Cierra el InputStream después de usarlo

                        file?.let {
                            // Permiso público para que el archivo sea visible por cualquiera
                            val permission = com.google.api.services.drive.model.Permission().apply {
                                type = "anyone"
                                role = "reader"
                            }

                            withContext(Dispatchers.IO) {
                                driveService.permissions().create(it.id, permission).execute()
                            }

                            val newProfilePictureUrl = it.webContentLink ?: "" // Obtiene el enlace público del archivo
                            onUploadComplete(newProfilePictureUrl) // Llama al callback con el enlace del archivo
                        }
                    } catch (e: Exception) {
                        Log.e("MainActivity", "Error al subir imagen: ${e.message}") // Manejo de errores
                    }
                }
            }
        }
    }


    // Función para manejar los resultados de actividades iniciadas con startActivityForResult
    @Deprecated("Deprecated in Java") // Indica que este método está obsoleto en Java y recomienda alternativas
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data) // Llama al método de la superclase
        if (requestCode == REQUEST_AUTHORIZATION) { // Verifica si el resultado es de una solicitud de autorización
            if (resultCode == Activity.RESULT_OK) { // Si el usuario concedió permiso
                pendingUri?.let { uri -> // Comprueba si hay un URI pendiente
                    startUploadImageToDrive(uri, {
                        // Callback vacío en este caso
                    }, this@MainActivity) // Llama a la función para subir la imagen al Drive
                    pendingUri = null // Limpia el URI pendiente después de subirlo
                }
            } else {
                // Muestra un mensaje si el permiso fue denegado
                Toast.makeText(this, "Permiso denegado para acceder a Google Drive", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Función principal del ciclo de vida al crear la actividad
    @OptIn(ExperimentalMaterial3Api::class) // Marca que esta función utiliza una API experimental de Material Design 3
    override fun onCreate(savedInstanceState: Bundle?) {
        // Configuración inicial de la interfaz de usuario
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) // Desactiva el modo oscuro
        GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this) // Asegura que Google Play Services esté disponible
        auth = FirebaseAuth.getInstance() // Inicializa la instancia de FirebaseAuth
        super.onCreate(savedInstanceState) // Llama al método de la superclase
        enableEdgeToEdge() // Activa el diseño Edge-to-Edge (sin bordes visibles)

        // Configura la interfaz de usuario usando Jetpack Compose
        setContent {
            ErizoHubTheme { // Aplica el tema ErizoHubTheme
                val myNavController = rememberNavController() // Crea un controlador de navegación
                var selectedItem by remember { mutableIntStateOf(0) } // Estado para el ítem seleccionado en el Bottom Navigation
                var bottomBarColor by remember { mutableStateOf(Color(0xFFF2A74B)) } // Estado del color de la barra inferior
                var bottomBarColorBackground by remember { // Estado del color de fondo de la barra inferior
                    mutableStateOf(com.example.erizohub.InicioApp.ErizoHubTheme.Colors.background)
                }
                var bottomBarIcons by remember { // Estado del color de los íconos de la barra inferior
                    mutableStateOf(com.example.erizohub.InicioApp.ErizoHubTheme.Colors.background)
                }
                var menuExpanded by remember { mutableStateOf(false) } // Estado para controlar la expansión del menú

                // Listener para detectar cambios en la navegación
                LaunchedEffect(myNavController) {
                    myNavController.addOnDestinationChangedListener { _, destination, _ ->
                        // Cambia el color de la barra inferior según la ruta actual
                        bottomBarColor = when (destination.route) {
                            "home" -> com.example.erizohub.InicioApp.ErizoHubTheme.Colors.background
                            "emprende" -> com.example.erizohub.InicioApp.ErizoHubTheme.Colors.background
                            "chat_selection" -> com.example.erizohub.InicioApp.ErizoHubTheme.Colors.background
                            "chat_screen/{chatId}/{otherUserId}" -> com.example.erizohub.InicioApp.ErizoHubTheme.Colors.background
                            "user_selection_screen" -> com.example.erizohub.InicioApp.ErizoHubTheme.Colors.background
                            "crear_producto" -> com.example.erizohub.InicioApp.ErizoHubTheme.Colors.background
                            "producto_selection" -> com.example.erizohub.InicioApp.ErizoHubTheme.Colors.background
                            "visualizar_productos/{idEmprendimiento}" -> com.example.erizohub.InicioApp.ErizoHubTheme.Colors.background
                            "emprendimientoScreen/{idEmprendimiento}" -> com.example.erizohub.InicioApp.ErizoHubTheme.Colors.background
                            "visualizar_producto/{idProducto}" -> com.example.erizohub.InicioApp.ErizoHubTheme.Colors.background
                            "crear_producto/{idEmprendimiento}" -> com.example.erizohub.InicioApp.ErizoHubTheme.Colors.background
                            "producto_selection/{idEmprendimiento}" -> com.example.erizohub.InicioApp.ErizoHubTheme.Colors.background
                            "emprendimientos_activos/{idEmprendimiento}" -> com.example.erizohub.InicioApp.ErizoHubTheme.Colors.background
                            else -> Color(0xFFF2A74B) // Color predeterminado
                        }

                        bottomBarColorBackground = when (destination.route) {
                            "home" -> Color.White
                            "emprende" -> Color.White
                            // Rutas que comparten el mismo color de fondo
                            "chat_selection", "chat_screen/{chatId}/{otherUserId}",
                            "user_selection_screen", "crear_producto", "producto_selection",
                            "visualizar_productos/{idEmprendimiento}", "emprendimientoScreen/{idEmprendimiento}",
                            "visualizar_producto/{idProducto}", "crear_producto/{idEmprendimiento}",
                            "producto_selection/{idEmprendimiento}", "emprendimientos_activos/{idEmprendimiento}" -> Color.White
                            else -> com.example.erizohub.InicioApp.ErizoHubTheme.Colors.background // Color predeterminado
                        }

// Configuración del color de los íconos de la barra inferior según la ruta de navegación actual
                        bottomBarIcons = when (destination.route) {
                            "home", "emprende", "chat_selection", "chat_screen/{chatId}/{otherUserId}",
                            "user_selection_screen", "crear_producto", "producto_selection",
                            "visualizar_productos/{idEmprendimiento}", "emprendimientoScreen/{idEmprendimiento}",
                            "visualizar_producto/{idProducto}", "crear_producto/{idEmprendimiento}",
                            "producto_selection/{idEmprendimiento}", "emprendimientos_activos/{idEmprendimiento}" -> Color(0xFFF2A74B)
                            else -> com.example.erizohub.InicioApp.ErizoHubTheme.Colors.background // Color predeterminado
                        }
                    }
                }

                // Contenedor principal de la navegación y pantallas
                Scaffold(
                    bottomBar = {
                        // Configuración de la barra de navegación inferior
                        NavigationBar(
                            containerColor = bottomBarColor, // Color de fondo de la barra
                            modifier = Modifier
                                .background(color = bottomBarColorBackground) // Fondo dinámico
                                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)) // Bordes redondeados
                        ) {
                            // Aquí se definen otros ítems de navegación (como "Home", "Emprende", etc.)
                        }
                    }
                ) { innerPadding ->
                    // Contenedor principal para el contenido de las pantallas
                    Column(
                        modifier = Modifier
                            .padding(innerPadding) // Aplica padding dinámico según el Scaffold
                            .background(color = Color(0xFFF2A74B)) // Color de fondo de la pantalla
                    ) {
                        // Controlador de navegación
                        NavHost(navController = myNavController, startDestination = "home") {
                            composable("home") {
                                HomeScreen(myNavController) // Componente de la pantalla "Home"
                            }
                            composable("emprende") {
                                EmprendeScreen(
                                    myNavController,
                                    emprendimiento = Emprendimiento(
                                        idEmprendimiento = "",
                                        nombre_emprendimiento = "",
                                        descripcion = "",
                                        imagenEmprendimiento = "",
                                        listaProductos = emptyList(),
                                        comentarios = emptyList()
                                    )
                                )
                            }
                            composable("chat_selection") {
                                ChatSelectionScreen(myNavController)
                            }
                            composable("user_selection_screen") {
                                UserSelectionScreen(myNavController)
                            }
                            composable(
                                "visualizar_producto/{idProducto}",
                                arguments = listOf(navArgument("idProducto") { type = NavType.StringType })
                            ) { backStackEntry ->
                                val idProducto = backStackEntry.arguments?.getString("idProducto") ?: ""
                                VisualizarProductoScreen(myNavController, idProducto)
                            }
                            composable("userscreen") {
                                UserScreen(
                                    navController = myNavController,
                                    uploadImageToDrive = { uri, onUploadComplete ->
                                        startUploadImageToDrive(uri, onUploadComplete, this@MainActivity)
                                    }
                                )
                            }
                            composable("emprendimientos_activos/{idEmprendimiento}") { backStackEntry ->
                                val idEmprendimiento = backStackEntry.arguments?.getString("idEmprendimiento") ?: ""
                                EmprendimientosActivos(myNavController, idEmprendimiento)
                            }
                            composable("producto_selection/{idEmprendimiento}") { backStackEntry ->
                                val idEmprendimiento = backStackEntry.arguments?.getString("idEmprendimiento") ?: ""
                                ProductoSelectionScreen(myNavController, idEmprendimiento)
                            }
                            composable("crear_producto/{idEmprendimiento}") {
                                CrearProductos(
                                    myNavController,
                                    emprendimiento = Emprendimiento(
                                        idEmprendimiento = "",
                                        nombre_emprendimiento = "",
                                        descripcion = "",
                                        imagenEmprendimiento = "",
                                        listaProductos = emptyList(),
                                        comentarios = emptyList()
                                    )
                                )
                            }
                            composable(
                                "emprendimientoScreen/{idEmprendimiento}",
                                arguments = listOf(navArgument("idEmprendimiento") { type = NavType.StringType })
                            ) { backStackEntry ->
                                val idEmprendimiento = backStackEntry.arguments?.getString("idEmprendimiento") ?: ""
                                EmprendimientoScreen(myNavController, idEmprendimiento)
                            }
                            composable(
                                "visualizar_productos/{idEmprendimiento}",
                                arguments = listOf(navArgument("idEmprendimiento") { type = NavType.StringType })
                            ) { backStackEntry ->
                                val idEmprendimiento = backStackEntry.arguments?.getString("idEmprendimiento") ?: ""
                                ProductoSelectionScreenEmprendimiento(myNavController, idEmprendimiento)
                            }
                            composable("chat_screen/{chatId}/{otherUserId}") { backStackEntry ->
                                val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
                                val otherUserId = backStackEntry.arguments?.getString("otherUserId") ?: ""
                                ChatScreen(chatId = chatId, otherUserId = otherUserId)

                                if (menuExpanded) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.TopStart
                                    ) {
                                        Card(
                                            modifier = Modifier.fillMaxWidth(0.5f),
                                            elevation = CardDefaults.cardElevation(8.dp)
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(16.dp),
                                                verticalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                TextButton(onClick = { /* Acción botón 1 */ }) {
                                                    Text("Botón 1")
                                                }
                                                TextButton(onClick = { /* Acción botón 2 */ }) {
                                                    Text("Botón 2")
                                                }
                                                TextButton(onClick = { /* Acción botón 3 */ }) {
                                                    Text("Botón 3")
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}
