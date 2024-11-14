package com.example.erizohub.Home
import android.content.Context
import android.net.Uri
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
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Text(
                modifier = Modifier.alpha(1f),
                textAlign = TextAlign.Center,
                fontFamily = customFontFamily,
                fontSize = 26.sp,
                color = ErizoHubTheme.Colors.primary,
                text = "Crea tu emprendimiento"
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { galleryLauncher.launch("image/*")
                               },
                contentAlignment = Alignment.Center
            ) {
                if (imagenPerfilEmprendimiento.isNotEmpty()) {
                    AsyncImage(
                        model = imagenPerfilEmprendimiento,
                        contentDescription = "Imagen de perfil del emprendimiento",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "profile",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }

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

        Column(modifier = Modifier
            .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,) {
            TextField(
                value = nombreEmprendimiento,
                onValueChange = { nombreEmprendimiento = it },
                label = {
                    Text(
                        "Nombre del Emprendimiento",
                        color = ErizoHubTheme.Colors.textFieldText,
                        fontFamily = customFontFamily,
                        fontSize = 20.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                ),
                modifier = Modifier
                    .height(61.dp)
                    .width(367.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .border(1.dp, Color.Transparent, RoundedCornerShape(50.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = descripcionEmprendimiento,
                onValueChange = { descripcionEmprendimiento = it },
                label = {
                    Text(
                        "Descripción",
                        color = ErizoHubTheme.Colors.textFieldText,
                        fontFamily = customFontFamily,
                        fontSize = 10.sp
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
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                modifier = Modifier,
                fontFamily = customFontFamily,
                fontSize = 20.sp,
                color = ErizoHubTheme.Colors.primary,
                text = "Imágenes del Emprendimiento"
            )

            Spacer(modifier = Modifier.height(16.dp))

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


            TextField(
                value = urlText,
                onValueChange = { urlText = it },
                label = {
                    Text(
                        "Añadir URL de Imagen",
                        color = ErizoHubTheme.Colors.textFieldText,
                        fontFamily = customFontFamily,
                        fontSize = 10.sp
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
            )

            Spacer(modifier = Modifier.height(26.dp))

            Button(
                onClick = {
                    if (urlText.isNotBlank()) {
                        imagenes.add(urlText)
                        urlText = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Gray) ,
                modifier = Modifier.align(Alignment.CenterHorizontally)
                    .width(225.dp)
                    .height(59.dp)
                    .border(1.dp, Color.Transparent, shape = RoundedCornerShape(30.dp))
                    .shadow(10.dp, shape = RoundedCornerShape(30.dp)),
            ) {
                Text(
                        modifier = Modifier,
                        fontFamily = customFontFamily,
                        fontSize = 15.sp,
                    text = "Añadir Imagen"
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

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
                colors = ButtonDefaults.buttonColors(containerColor = ErizoHubTheme.Colors.primary, contentColor = Color.White),
                modifier = Modifier    .align(Alignment.CenterHorizontally)
                    .width(225.dp)
                    .height(59.dp)
                    .border(1.dp, Color.Transparent, shape = RoundedCornerShape(30.dp))
                    .shadow(10.dp, shape = RoundedCornerShape(30.dp)),

                ) {
                Text(
                    modifier = Modifier.alpha(1f),
                    fontFamily = customFontFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp,
                    text = "Guardar Emprendimiento")
            }
        }
    }
}