package com.example.erizohub

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage


@Composable
fun HomeScreen(navController: NavController){
    val emprendimiento1 = Emprendimiento(
        "Emprendimiento x",
        "Mi emprendimiento se tratatatatattatatatatata",
        "https://www.pexels.com/es-es/foto/mar-blanco-y-negro-hombre-playa-27741970/",
        "https://www.pexels.com/es-es/foto/mar-blanco-y-negro-hombre-playa-27741970/",
    )
    val listEmprendimientos: List<Emprendimiento> = listOf(
        emprendimiento1
    )


    LazyColumn (
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
    ){
        items(listEmprendimientos.size){
            Emprendimientoitem(miEmprendimiento = listEmprendimientos.get(it))
        }
    }
}


@Composable
fun Emprendimientoitem(miEmprendimiento: Emprendimiento){
    Card {
        Row {
            AsyncImage(
                model = miEmprendimiento.banner,
                contentDescription = "Banner image",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
                contentScale = ContentScale.Crop,
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = miEmprendimiento.nombre_emprendimiento,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 6.dp)

                )
                Text(text = miEmprendimiento.description)


            }
        }
    }
}