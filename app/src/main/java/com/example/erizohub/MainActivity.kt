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
    private lateinit var auth: FirebaseAuth
    private var pendingUri: Uri? = null

    private fun startUploadImageToDrive(uri: Uri, onUploadComplete: (String) -> Unit, context: Context) {
        lifecycleScope.launch {
            val driveService = getDriveService(context = context)
            uploadImageToDrive(driveService, uri, onUploadComplete, context, lifecycleScope)
        }
    }

    companion object {
        private const val REQUEST_AUTHORIZATION = 1001

        suspend fun getDriveService(context: Context): Drive {
            return withContext(Dispatchers.IO) {
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
        }

        private fun createFolderIfNotExists(
            driveService: Drive,
            lifecycleScope: CoroutineScope,
            folderName: String = "appDataFolder",
            onFolderCreated: (String) -> Unit
        ) {
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val result = driveService.files().list()
                        .setQ("name='$folderName' and mimeType='application/vnd.google-apps.folder' and trashed=false")
                        .setSpaces("drive")
                        .execute()

                    val folderId = if (result.files.isNullOrEmpty()) {
                        val fileMetadata = File().apply {
                            name = folderName
                            mimeType = "application/vnd.google-apps.folder"
                        }
                        val folder = driveService.files().create(fileMetadata)
                            .setFields("id")
                            .execute()
                        folder.id
                    } else {
                        result.files[0].id
                    }

                    onFolderCreated(folderId)
                } catch (e: Exception) {
                    Log.e("MainActivity", "Error al crear o verificar carpeta: ${e.message}")
                }
            }
        }

        fun uploadImageToDrive(
            driveService: Drive,
            uri: Uri,
            onUploadComplete: (String) -> Unit,
            context: Context,
            lifecycleScope: CoroutineScope
        ) {
            createFolderIfNotExists(driveService, lifecycleScope) { folderId ->
                val fileMetadata = File().apply {
                    name = "profile_picture_${FirebaseAuth.getInstance().currentUser?.uid}.jpg"
                    parents = listOf(folderId)
                }

                val inputStream = context.contentResolver.openInputStream(uri)
                val mediaContent = InputStreamContent("image/jpeg", inputStream)

                lifecycleScope.launch {
                    try {
                        val file = withContext(Dispatchers.IO) {
                            driveService.files().create(fileMetadata, mediaContent)
                                .setFields("id, webContentLink")
                                .execute()
                        }
                        inputStream?.close()

                        file?.let {
                            val permission = com.google.api.services.drive.model.Permission().apply {
                                type = "anyone"
                                role = "reader"
                            }

                            withContext(Dispatchers.IO) {
                                driveService.permissions().create(it.id, permission).execute()
                            }

                            val newProfilePictureUrl = it.webContentLink ?: ""
                            onUploadComplete(newProfilePictureUrl)
                        }
                    } catch (e: Exception) {
                        Log.e("MainActivity", "Error al subir imagen: ${e.message}")
                    }
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_AUTHORIZATION) {
            if (resultCode == Activity.RESULT_OK) {
                pendingUri?.let { uri ->
                    startUploadImageToDrive(uri, {
                    }, this@MainActivity)
                    pendingUri = null
                }
            } else {
                Toast.makeText(this, "Permiso denegado para acceder a Google Drive", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this)
        auth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ErizoHubTheme {
                val myNavController = rememberNavController()
                var selectedItem by remember { mutableIntStateOf(0) }
                var bottomBarColor by remember { mutableStateOf(Color(0xFFF2A74B)) }
                var bottomBarColorBackground by remember { mutableStateOf(com.example.erizohub.InicioApp.ErizoHubTheme.Colors.background) }
                var bottomBarIcons by remember { mutableStateOf(com.example.erizohub.InicioApp.ErizoHubTheme.Colors.background) }
                var menuExpanded by remember { mutableStateOf(false) }


                LaunchedEffect(myNavController) {
                    myNavController.addOnDestinationChangedListener { _, destination, _ ->
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

                            else -> Color(0xFFF2A74B)
                        }

                        bottomBarColorBackground = when (destination.route) {
                            "home" -> Color.White
                            "emprende" -> Color.White
                            "chat_selection" -> Color.White
                            "chat_screen/{chatId}/{otherUserId}" -> Color.White
                            "user_selection_screen" -> Color.White
                            "crear_producto" -> Color.White
                            "producto_selection" -> Color.White
                            "visualizar_productos/{idEmprendimiento}" -> Color.White
                            "emprendimientoScreen/{idEmprendimiento}" -> Color.White
                            "visualizar_producto/{idProducto}" -> Color.White
                            "crear_producto/{idEmprendimiento}" -> Color.White
                            "producto_selection/{idEmprendimiento}" -> Color.White
                            "emprendimientos_activos/{idEmprendimiento}" -> Color.White

                            else -> com.example.erizohub.InicioApp.ErizoHubTheme.Colors.background
                        }

                        bottomBarIcons = when (destination.route) {
                            "home" -> Color(0xFFF2A74B)
                            "emprende" -> Color(0xFFF2A74B)
                            "chat_selection" -> Color(0xFFF2A74B)
                            "chat_screen/{chatId}/{otherUserId}" -> Color(0xFFF2A74B)
                            "user_selection_screen" -> Color(0xFFF2A74B)
                            "crear_producto" -> Color(0xFFF2A74B)
                            "producto_selection" -> Color(0xFFF2A74B)
                            "visualizar_productos/{idEmprendimiento}" -> Color(0xFFF2A74B)
                            "emprendimientoScreen/{idEmprendimiento}" -> Color(0xFFF2A74B)
                            "visualizar_producto/{idProducto}" -> Color(0xFFF2A74B)
                            "crear_producto/{idEmprendimiento}" -> Color(0xFFF2A74B)
                            "producto_selection/{idEmprendimiento}" -> Color(0xFFF2A74B)
                            "emprendimientos_activos/{idEmprendimiento}" -> Color(0xFFF2A74B)

                            else -> com.example.erizohub.InicioApp.ErizoHubTheme.Colors.background
                        }
                    }
                }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            modifier = Modifier.background(color = Color.White),
                            title = { Text("") },
                            actions = {
                                IconButton(onClick = { menuExpanded = !menuExpanded }) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "Menú"
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        myNavController.navigate("userscreen") {
                                            popUpTo(myNavController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                        selectedItem = 4
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.AccountCircle,
                                        contentDescription = "UserScreen"
                                    )
                                }

                            }
                        )
                    },
                    bottomBar = {
                        NavigationBar(
                            containerColor = bottomBarColor,
                            modifier = Modifier
                                .background(color = bottomBarColorBackground)
                                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                        ) {
                            NavigationBarItem(
                                selected = selectedItem == 0,
                                onClick = {
                                    myNavController.navigate("home") {
                                        restoreState = true
                                        popUpTo(myNavController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                    }
                                    selectedItem = 0
                                },
                                icon = {
                                    Image(
                                        painter = painterResource(id = R.drawable.homemorado),
                                        contentDescription = "Home",
                                        modifier = Modifier.size(24.dp),
                                        colorFilter = ColorFilter.tint(bottomBarIcons)
                                    )
                                },
                                label = {
                                    Text(
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 15.sp,
                                        color = bottomBarIcons,
                                        fontFamily = com.example.erizohub.InicioApp.ErizoHubTheme.Fonts.customFontFamily,
                                        text = "Home"
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = bottomBarIcons,
                                    unselectedIconColor = bottomBarIcons.copy(alpha = 0.6f),
                                    selectedTextColor = bottomBarIcons,
                                    unselectedTextColor = bottomBarIcons.copy(alpha = 0.6f),
                                    indicatorColor = Color.Transparent
                                )
                            )
                            NavigationBarItem(
                                selected = selectedItem == 1,
                                onClick = {
                                    myNavController.navigate("emprende") {
                                        popUpTo(myNavController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                    selectedItem = 1
                                },
                                icon = {
                                    Image(
                                        painter = painterResource(id = R.drawable.emprendemorado),
                                        contentDescription = "Emprende",
                                        modifier = Modifier.size(24.dp),
                                        colorFilter = ColorFilter.tint(bottomBarIcons)
                                    )
                                },
                                label = {
                                    Text(
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 15.sp,
                                        color = bottomBarIcons,
                                        fontFamily = com.example.erizohub.InicioApp.ErizoHubTheme.Fonts.customFontFamily,
                                        text = "Emprende"
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = bottomBarIcons,
                                    unselectedIconColor = bottomBarIcons.copy(alpha = 0.6f),
                                    selectedTextColor = bottomBarIcons,
                                    unselectedTextColor = bottomBarIcons.copy(alpha = 0.6f),
                                    indicatorColor = Color.Transparent
                                )
                            )
                            NavigationBarItem(
                                selected = selectedItem == 2,
                                onClick = {
                                    myNavController.navigate("chat_selection") {
                                        popUpTo(myNavController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                    selectedItem = 2
                                },
                                icon = {
                                    Image(
                                        painter = painterResource(id = R.drawable.chatmorado),
                                        contentDescription = "Chat",
                                        modifier = Modifier.size(24.dp),
                                        colorFilter = ColorFilter.tint(bottomBarIcons)
                                    )
                                },
                                label = {
                                    Text(
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 15.sp,
                                        color = bottomBarIcons,
                                        fontFamily = com.example.erizohub.InicioApp.ErizoHubTheme.Fonts.customFontFamily,
                                        text = "Chat"
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = bottomBarIcons,
                                    unselectedIconColor = bottomBarIcons.copy(alpha = 0.6f),
                                    selectedTextColor = bottomBarIcons,
                                    unselectedTextColor = bottomBarIcons.copy(alpha = 0.6f),
                                    indicatorColor = Color.Transparent
                                )
                            )
                        }
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .background(color = Color(0xFFF2A74B))
                    ) {
                        NavHost(navController = myNavController, startDestination = "home") {
                            composable("home") {
                                HomeScreen(myNavController)
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


                            composable("crear_producto/{idEmprendimiento}") { backStackEntry ->
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
                        }                    }
                }
            }
        }
    }
}
