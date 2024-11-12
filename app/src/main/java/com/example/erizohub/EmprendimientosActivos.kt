package com.example.erizohub

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.erizohub.ErizoHubTheme.Fonts.customFontFamily
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun EmprendimientosActivos(navController: NavController) {
    var userName by remember { mutableStateOf("") }
    val listEmprendimientos = remember { mutableStateOf<List<Emprendimiento>>(emptyList()) }
    val user = FirebaseAuth.getInstance().currentUser
    val db = Firebase.firestore

    LaunchedEffect(user) {
        user?.let { currentUser ->
            db.collection("users").document(currentUser.uid).get().addOnSuccessListener { document ->
                if (document.exists()) {
                    userName = document.getString("userName") ?: ""
                }
            }

            db.collection("users").document(currentUser.uid).collection("emprendimientos").get()
                .addOnSuccessListener { response ->
                    val emprendimientos = response.documents.map { document ->
                        Emprendimiento(
                            nombre_emprendimiento = document.getString("nombre_emprendimiento") ?: "",
                            descripcion = document.getString("descripcion") ?: "",
                            imagenEmprendimiento = document.getString("imagenEmprendimiento") ?: "",
                            imagenes = document.get("imagenes") as List<String>? ?: emptyList()
                        )
                    }
                    listEmprendimientos.value = emprendimientos
                }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Emprendimientos Activos",
            fontSize = 24.sp,
            fontFamily = ErizoHubTheme.Fonts.customFontFamily,
            color = ErizoHubTheme.Colors.primary,
            modifier = Modifier.padding(top = 20.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            items(listEmprendimientos.value.size) { index ->
                EmprendimientoItem(myEmprendimiento = listEmprendimientos.value[index]) {
                    // Navegar a la pantalla de detalles del emprendimiento al hacer clic
                    navController.navigate("emprendimientoScreen/${listEmprendimientos.value[index].nombre_emprendimiento}")
                }
            }
        }
    }
}

