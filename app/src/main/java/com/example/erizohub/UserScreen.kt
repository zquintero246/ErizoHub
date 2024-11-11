package com.example.erizohub

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp



@Preview(showSystemUi = true)
@Composable
fun UserPerfil() {

    Column(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = R.drawable.profile),
            contentDescription = "profile",
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.CenterHorizontally)
                .padding(0.dp, 100.dp, 0.dp, 20.dp)
        )
        // Coloca la Row aquí, justo debajo del Image
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 1.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Nombre del usuario")
        }

        Spacer(modifier=Modifier.height(25.dp))
        Button(onClick = { /*TODO*/ }, modifier = Modifier.align(Alignment.CenterHorizontally).width(200.dp).height(50.dp).border(2.dp, Color.Gray,shape= RoundedCornerShape(30.dp)), colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)) { Text(text="Seguir") }
        Spacer(modifier=Modifier.height(25.dp))
        Row(
            modifier = Modifier
                .border(1.dp, Color(0xFF6F04D9), shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .padding(top=0.dp) // Agrega un padding para que el color de fondo no se salga del borde
                .background(Color(0xFF6F04D9), shape = RoundedCornerShape(topStart=30.dp,topEnd=30.dp)) // Cambia el color de fondo aquí
                .fillMaxSize()
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally,modifier=Modifier.fillMaxSize().padding(top= 40.dp)) {

                Spacer(modifier=  Modifier.height(25.dp))
                HorizontalDivider(modifier = Modifier.width(325.dp))

                Button(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6F04D9), contentColor = Color.White)) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween,modifier=Modifier.width(280.dp)) {
                        Text(text="Emprendimientos Activos")
                        Spacer(modifier=  Modifier.width(0.dp))
                        Icon(imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = "Menu")

                    }


                }

                HorizontalDivider(modifier = Modifier.width(325.dp))

                Button(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6F04D9), contentColor = Color.White)) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween,modifier=Modifier.width(280.dp)) {
                        Text(text="Servicios Pagados")
                        Spacer(modifier=  Modifier.width(0.dp))
                        Icon(imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = "Menu")

                    }


                }

                HorizontalDivider(modifier = Modifier.width(325.dp))

                Button(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6F04D9), contentColor = Color.White)) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween,modifier=Modifier.width(280.dp)) {
                        Text(text="Historial")
                        Spacer(modifier=  Modifier.width(0.dp))
                        Icon(imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = "Menu")

                    }


                }
                HorizontalDivider(modifier = Modifier.width(325.dp))

                Spacer(modifier=  Modifier.height(25.dp))

                Text(text= "Promedio de Calificacion")
                Text(text= "0.0")

                Spacer(modifier=  Modifier.height(40.dp))

                Row(horizontalArrangement = Arrangement.SpaceBetween,modifier=Modifier.width(325.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(text="Calificar:",color=Color.White)
                    Button(
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6F04D9), contentColor = Color.White),
                        modifier = Modifier.size(30.dp),
                        contentPadding = PaddingValues(0.dp) // Esto elimina el padding interno
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Star,
                            contentDescription = "Star",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Button(
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6F04D9), contentColor = Color.White),
                        modifier = Modifier.size(30.dp),
                        contentPadding = PaddingValues(0.dp) // Esto elimina el padding interno
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Star",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Button(
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6F04D9), contentColor = Color.White),
                        modifier = Modifier.size(30.dp),
                        contentPadding = PaddingValues(0.dp) // Esto elimina el padding interno
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Star",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Button(
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6F04D9), contentColor = Color.White),
                        modifier = Modifier.size(30.dp),
                        contentPadding = PaddingValues(0.dp) // Esto elimina el padding interno
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Star",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Button(
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6F04D9), contentColor = Color.White),
                        modifier = Modifier.size(30.dp),
                        contentPadding = PaddingValues(0.dp) // Esto elimina el padding interno
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Star",
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                }





            }
            // Contenido de la Row
        }
    }
}





