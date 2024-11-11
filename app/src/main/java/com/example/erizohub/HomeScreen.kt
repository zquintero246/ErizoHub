package com.example.erizohub

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await


@Composable
fun HomeScreen(navController: NavController){
    val listEmprendimientos = remember {
        mutableStateOf<List<Emprendimiento>>(emptyList())
    }
    LaunchedEffect(Unit) {
        val db = Firebase.firestore
        val response = db.collection("emprendimientos").get().await()
        val emprendimientos = response.documents.map{ document ->
            Emprendimiento(
                nombre_emprendimiento = document.getString("nombre_emprendimiento")?: "",
                descripcion= document.getString("descripcion")?: "",
                imagenEmprendimiento = document.getString("imagenEmprendimiento")?: "",
                imagenes = document.get("imagenes") as List<String>? ?: emptyList()

            )
        }
        listEmprendimientos.value= emprendimientos
    }

    LazyColumn (
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ){
        items(listEmprendimientos.value.size){
            Emprendimientoitem(myEmprendimiento = listEmprendimientos.value[it])
        }
    }
}




@Composable
fun Emprendimientoitem(myEmprendimiento: Emprendimiento) {
    Card(modifier = Modifier.padding(8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = myEmprendimiento.imagenEmprendimiento,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .padding(end = 12.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = myEmprendimiento.nombre_emprendimiento,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Email: ${myEmprendimiento.descripcion}",
                    fontSize = 16.sp
                )
            }
        }
    }
}