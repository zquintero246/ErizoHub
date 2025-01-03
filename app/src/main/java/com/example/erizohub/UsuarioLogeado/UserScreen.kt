package com.example.erizohub.UsuarioLogeado

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.erizohub.AuthActivity
import com.example.erizohub.InicioApp.ErizoHubTheme
import com.example.erizohub.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    navController: NavController,
    uploadImageToDrive: (Uri, (String) -> Unit) -> Unit
)  {
    var userName by remember { mutableStateOf("") }
    var profilePictureUrl by remember { mutableStateOf("") }
    var isEditingProfile by remember { mutableStateOf(false) }
    val context = navController.context
    val user = FirebaseAuth.getInstance().currentUser
    val db = FirebaseFirestore.getInstance()

    fun updateProfilePictureUrl(newUrl: String) {
        user?.uid?.let { uid ->
            db.collection("users").document(uid)
                .update("profilePictureUrl", newUrl)
                .addOnSuccessListener {
                    profilePictureUrl = newUrl
                    isEditingProfile = false
                }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            uploadImageToDrive(uri) { newProfilePictureUrl ->
                updateProfilePictureUrl(newProfilePictureUrl)
            }
        }
    }


    fun updateUserName(newName: String) {
        user?.uid?.let { uid ->
            db.collection("users").document(uid)
                .update("userName", newName)
                .addOnSuccessListener {
                    userName = newName
                    isEditingProfile = false
                }
        }
    }



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(0.dp, 0.dp, 0.dp, 0.dp)
    ) {
        Column(
            modifier = Modifier
                .height(350.dp)
                .fillMaxWidth()
                .background(color = Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(198.dp)
                    .clip(CircleShape)
                    .clickable(enabled = isEditingProfile) { galleryLauncher.launch("image/*") }
            ) {
                if (profilePictureUrl.isNotEmpty()) {
                    AsyncImage(
                        model = profilePictureUrl,
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 1.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                if (isEditingProfile) {
                    TextField(
                        value = userName,
                        onValueChange = { userName = it },
                        label = { Text("") },
                        singleLine = false,
                        modifier = Modifier
                            .width(200.dp)
                            .border(0.dp, Color.Transparent, shape = RoundedCornerShape(30.dp))
                            .background(Color.Transparent),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = Color.Black,
                        ),
                        textStyle = TextStyle(
                            fontSize = 20.sp,
                            fontFamily = ErizoHubTheme.Fonts.customFontFamily,
                            color = Color(0xFFB8B8B8),
                            )
                    )


                } else {
                    Text(
                        fontSize = 20.sp,
                        fontFamily = ErizoHubTheme.Fonts.customFontFamily,
                        color = Color(0xFFB8B8B8),
                        text = userName
                    )
                }
            }
        }

        LaunchedEffect(user) {
            user?.uid?.let { uid ->
                db.collection("users").document(uid).get().addOnSuccessListener { document ->
                    if (document.exists()) {
                        userName = document.getString("userName") ?: ""
                        profilePictureUrl = document.getString("profilePictureUrl") ?: ""
                    }
                }
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(ErizoHubTheme.Colors.background, shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
        ) {
            Button(
                onClick = {
                    if (isEditingProfile) {
                        updateUserName(userName)
                    }
                    isEditingProfile = !isEditingProfile
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(225.dp)
                    .height(59.dp)
                    .offset(y = (-25).dp)
                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(30.dp))
                    .shadow(20.dp, shape = RoundedCornerShape(30.dp)),
                elevation = ButtonDefaults.buttonElevation(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
            ) {
                Text(
                    modifier = Modifier.alpha(0.8f),
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF656262),
                    fontSize = 20.sp,
                    fontFamily = ErizoHubTheme.Fonts.customFontFamily,
                    text = if (isEditingProfile) "Guardar" else "Editar Perfil"
                )
            }
            HorizontalDivider(modifier = Modifier.width(325.dp).padding(top = 10.dp).alpha(0.5f))

            Button(
                onClick = {
                    val sharedPreferences = context.getSharedPreferences("EmprendimientoPrefs", Context.MODE_PRIVATE)
                    val idEmprendimientoGuardado = sharedPreferences.getString("idEmprendimientoGuardado", "")

                    if (!idEmprendimientoGuardado.isNullOrEmpty()) {
                        navController.navigate("emprendimientos_activos/$idEmprendimientoGuardado")
                    } else {
                        Toast.makeText(context, "No hay un emprendimiento asociado", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.padding(top = 11.dp, bottom = 11.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6F04D9))
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.width(280.dp)
                ) {
                    Text(
                        modifier = Modifier.alpha(0.5f),
                        fontWeight = FontWeight.Normal,
                        fontFamily = ErizoHubTheme.Fonts.customFontFamily,
                        fontSize = 15.sp,
                        text = "Emprendimientos Activos"
                    )
                    Spacer(modifier = Modifier.width(0.dp))
                    Icon(
                        modifier = Modifier.alpha(0.5f),
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Menu"
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.width(325.dp).alpha(0.5f))

            Button(onClick = { /*TODO*/ },
                modifier = Modifier.padding(top = 11.dp, bottom = 11.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6F04D9))) {
                Row(horizontalArrangement = Arrangement.SpaceBetween,
                    modifier=Modifier.width(280.dp)) {
                    Text(
                        modifier = Modifier.alpha(0.5f),
                        fontWeight = FontWeight.Normal,
                        fontFamily = ErizoHubTheme.Fonts.customFontFamily,
                        fontSize = 15.sp,
                        text="Seguidos"
                    )
                    Spacer(modifier=  Modifier.width(0.dp))
                    Icon(
                        modifier = Modifier.alpha(0.5f),
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Menu")

                }
            }

            HorizontalDivider(modifier = Modifier.width(325.dp).alpha(0.5f))

            Button(
                onClick = {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(context, AuthActivity::class.java)
                context.startActivity(intent)
                (context as? Activity)?.finish()
            },
                modifier = Modifier.padding(top = 11.dp, bottom = 11.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6F04D9))) {
                Row(horizontalArrangement = Arrangement.SpaceBetween,
                    modifier=Modifier.width(280.dp)) {
                    Text(
                        modifier = Modifier.alpha(0.5f),
                        fontWeight = FontWeight.Normal,
                        fontFamily = ErizoHubTheme.Fonts.customFontFamily,
                        fontSize = 15.sp,
                        text="Cerrar sesion"
                    )
                    Spacer(modifier=  Modifier.width(0.dp))
                    Icon(
                        modifier = Modifier.alpha(0.5f),
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Menu")

                }
            }


            Text(
                modifier = Modifier.alpha(0.5f).padding(top = 24.dp),
                color = Color.White,
                fontFamily = ErizoHubTheme.Fonts.customFontFamily,
                text= "Promedio de Calificacion"
            )


            Text(
                modifier = Modifier.alpha(0.5f),
                color = Color.White,
                fontFamily = ErizoHubTheme.Fonts.customFontFamily,
                text= "0.0"
            )
        }
    }
}