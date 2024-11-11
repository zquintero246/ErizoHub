package com.example.erizohub

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
<<<<<<< Updated upstream
=======
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
>>>>>>> Stashed changes
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
<<<<<<< Updated upstream
=======
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
>>>>>>> Stashed changes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore



@Composable
fun UserScreen(navController: NavController) {
    var expanded by remember { mutableStateOf(false) }
    var urlText by remember { mutableStateOf("") }

<<<<<<< Updated upstream
    var userName by remember { mutableStateOf("") }
    var profilePictureUrl by remember { mutableStateOf("") }
=======
    Column(modifier = Modifier.fillMaxWidth().background(color = Color.White).padding(0.dp, 0.dp, 0.dp, 0.dp)) {
        Column (modifier = Modifier
            .height(350.dp)
            .fillMaxWidth()
            .background(color = Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            Button(
                onClick = { expanded = !expanded },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "profile",
                    modifier = Modifier.size(150.dp)
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    TextField(
                        value = urlText,
                        onValueChange = { urlText = it },
                        label = { Text("Insert URL") },
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            Text(
                modifier = Modifier,
                fontSize = 20.sp,
                fontFamily = ErizoHubTheme.Fonts.customFontFamily,
                color = Color(0xFFB8B8B8),
                text = "Nombre usuario"
            )
        }
>>>>>>> Stashed changes

    val user = FirebaseAuth.getInstance().currentUser
    val db = FirebaseFirestore.getInstance()

<<<<<<< Updated upstream
    // Función para actualizar la URL de la imagen en Firestore
    fun updateProfilePictureUrl(newUrl: String) {
        user?.uid?.let { uid ->
            db.collection("users").document(uid)
                .update("profilePictureUrl", newUrl)
                .addOnSuccessListener {
                    profilePictureUrl = newUrl
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
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(0.dp, 0.dp, 0.dp, 0.dp)
    ) {
        Button(
            onClick = { expanded = !expanded },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(0.dp, 100.dp, 0.dp, 20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
        ) {
            if (profilePictureUrl.isNotEmpty()) {
                // Usa AsyncImage de Coil para cargar la imagen desde una URL
                AsyncImage(
                    model = profilePictureUrl,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.size(150.dp)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "profile",
                    modifier = Modifier.size(150.dp)
                )
            }

            // Pequeño menú desplegable que aparece al hacer clic
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                TextField(
                    value = urlText,
                    onValueChange = { urlText = it },
                    label = { Text("Insert URL") },
                    modifier = Modifier.padding(16.dp)
                )
                Button(
                    onClick = {
                        if (urlText.isNotEmpty()) {
                            updateProfilePictureUrl(urlText)
                            expanded = false // Cierra el menú desplegable
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Guardar")
                }
            }
        }

        // Coloca la Row aquí, justo debajo del Image
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 1.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = userName)
        }

        Spacer(modifier = Modifier.height(25.dp))

        Spacer(modifier = Modifier.height(75.dp))
        Row(
            modifier = Modifier
                .border(1.dp, Color(0xFF6F04D9), shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .padding(top = 0.dp)
                .background(Color(0xFF6F04D9), shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize().padding(top = 40.dp)
            ) {
                Spacer(modifier = Modifier.height(25.dp))
                HorizontalDivider(modifier = Modifier.width(325.dp), color = Color(0xFFCCB5E2))

                Button(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6F04D9), contentColor = Color.White)) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.width(280.dp)) {
                        Text(text = "Emprendimientos Activos", color = Color(0xFFCCB5E2))
                        Spacer(modifier = Modifier.width(0.dp))
                        Icon(imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = "Menu")
                    }
                }

                HorizontalDivider(modifier = Modifier.width(325.dp), color = Color(0xFFCCB5E2))

                Button(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6F04D9), contentColor = Color.White)) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.width(280.dp)) {
                        Text(text = "Servicios Pagados", color = Color(0xFFCCB5E2))
                        Spacer(modifier = Modifier.width(0.dp))
                        Icon(imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = "Menu")
                    }
                }

                HorizontalDivider(modifier = Modifier.width(325.dp), color = Color(0xFFCCB5E2))

                Button(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6F04D9), contentColor = Color.White)) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.width(280.dp)) {
                        Text(text = "Historial", color = Color(0xFFCCB5E2))
                        Spacer(modifier = Modifier.width(0.dp))
                        Icon(imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = "Menu")
                    }
                }

                HorizontalDivider(modifier = Modifier.width(325.dp), color = Color(0xFFCCB5E2))

                Spacer(modifier = Modifier.height(25.dp))

                Text(text = "Promedio de Calificacion", color = Color(0xFFCCB5E2))
                Text(text = "0.0", color = Color(0xFFCCB5E2))

                Spacer(modifier = Modifier.height(40.dp))
            }
=======
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier=Modifier
                .fillMaxSize()
                .background(ErizoHubTheme.Colors.background, shape = RoundedCornerShape(topStart=30.dp,topEnd=30.dp))
        ) {
            Button(onClick = { /*TODO*/ },
                modifier = Modifier.align(Alignment.CenterHorizontally)
                    .width(225.dp).height(59.dp)
                    .offset(y= -25.dp)
                    .border(1.dp, Color.Gray,shape= RoundedCornerShape(30.dp))
                    .shadow(20.dp,shape= RoundedCornerShape(30.dp))
                ,
                elevation = ButtonDefaults.buttonElevation(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black))
            {
                Text(
                    modifier = Modifier.alpha(0.8f),
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF656262),
                    fontSize = 20.sp,
                    fontFamily = ErizoHubTheme.Fonts.customFontFamily,
                    text="Editar Perfil")
            }

            HorizontalDivider(modifier = Modifier.width(325.dp).padding(top = 10.dp).alpha(0.5f))

            Button(onClick = { /*TODO*/ },
                modifier = Modifier.padding(top = 11.dp, bottom = 11.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6F04D9))
            ) {
                Row(horizontalArrangement = Arrangement.SpaceBetween,
                    modifier=Modifier.width(280.dp)) {
                    Text(
                        modifier = Modifier.alpha(0.5f),
                        fontWeight = FontWeight.Normal,
                        fontFamily = ErizoHubTheme.Fonts.customFontFamily,
                        fontSize = 15.sp,
                        text="Emprendimientos Activos"
                    )
                    Spacer(modifier=  Modifier.width(0.dp))
                    Icon(
                        modifier = Modifier.alpha(0.5f),
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Menu")

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
                        text="Servicios Pagados"
                    )
                    Spacer(modifier=  Modifier.width(0.dp))
                    Icon(
                        modifier = Modifier.alpha(0.5f),
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Menu")

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
                        text="Historial"
                    )
                    Spacer(modifier=  Modifier.width(0.dp))
                    Icon(
                        modifier = Modifier.alpha(0.5f),
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Menu")

                }
            }

            HorizontalDivider(modifier = Modifier.width(325.dp).alpha(0.5f))

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


>>>>>>> Stashed changes
        }
    }
}