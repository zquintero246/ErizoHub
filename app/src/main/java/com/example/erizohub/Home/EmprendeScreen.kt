package com.example.erizohub.Home
import android.content.Context
import android.net.Uri
import android.widget.Button
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import coil3.compose.AsyncImage
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmprendeScreen(navController: NavController) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    var urlText by remember { mutableStateOf("") }
    var nombreEmprendimiento by remember { mutableStateOf("") }
    var descripcionEmprendimiento by remember { mutableStateOf("") }
    var imagenPerfilEmprendimiento by remember { mutableStateOf("") }
    val imagenes by remember { mutableStateOf(mutableListOf<String>()) }
    val user = FirebaseAuth.getInstance().currentUser
    val db = FirebaseFirestore.getInstance()
    val scrollState = rememberScrollState()

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
                )
                {
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

        Column(Modifier
            .fillMaxSize()
            .offset(y = (-50).dp)
            .background(color = ErizoHubTheme.Colors.background, RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
        ) {
            Column(modifier = Modifier
                .fillMaxWidth(),
                horizontalAlignment = Alignment.End,) {
                Button(
                    onClick = {
                        navController.navigate("crear-producto")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = ErizoHubTheme.Colors.primary
                    ),
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


                Spacer(modifier = Modifier.height(10.dp))

            Column(modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
                ){
                Spacer(modifier = Modifier.height(100.dp))
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
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                    modifier = Modifier    .align(Alignment.CenterHorizontally)
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
                        text = "Guardar Emprendimiento")
                }
            }

            }
        }
    }
}