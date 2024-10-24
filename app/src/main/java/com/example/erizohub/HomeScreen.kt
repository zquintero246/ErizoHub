package com.example.erizohub

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import coil3.compose.AsyncImage

@Preview(showBackground = true)
@Composable
fun HomeScreen(){
    val emprendimiento1 = Emprendimiento(
        "Emprendimiento x",
        "Mi emprendimiento se tratatatatattatatatatata",
        "https://www.usergioarboleda.edu.co/wp-content/uploads/2020/12/banner-innovacio%CC%81n-business-thinkers.jpg",
        "https://www.usergioarboleda.edu.co/wp-content/uploads/2020/12/banner-innovacio%CC%81n-business-thinkers.jpg"
    )
    val listEmprendimientos: List<Emprendimiento> = listOf(
        emprendimiento1
    )


    LazyColumn (
        verticalArrangement = Arrangement.spacedBy(14.dp)
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
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = miEmprendimiento.nombre_emprendimiento,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 6.dp)

                )
                Text(text = miEmprendimiento.description)
                AsyncImage(

                    model = miEmprendimiento.imagen,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop
                )

            }
        }
    }
}