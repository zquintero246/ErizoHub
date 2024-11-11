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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController



@Composable
fun UserScreen(navController: NavController) {
    var expanded by remember { mutableStateOf(false) }
    var urlText by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth().background(color = Color.White).padding(0.dp, 0.dp, 0.dp, 0.dp)) {


        Button(
            onClick = { expanded = !expanded },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(0.dp, 100.dp, 0.dp, 20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "profile",
                modifier = Modifier.size(150.dp)
            )

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
            }
        }
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

        Spacer(modifier=Modifier.height(75.dp))
        Row(
            modifier = Modifier
                .border(1.dp, Color(0xFF6F04D9), shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .padding(top=0.dp) // Agrega un padding para que el color de fondo no se salga del borde
                .background(Color(0xFF6F04D9), shape = RoundedCornerShape(topStart=30.dp,topEnd=30.dp)) // Cambia el color de fondo aquí
                .fillMaxSize()
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally,modifier=Modifier.fillMaxSize().padding(top= 40.dp)) {

                Spacer(modifier=  Modifier.height(25.dp))
                HorizontalDivider(modifier = Modifier.width(325.dp), color = Color(0xFFCCB5E2))

                Button(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6F04D9), contentColor = Color.White)) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween,modifier=Modifier.width(280.dp)) {
                        Text(text="Emprendimientos Activos", color = Color(0xFFCCB5E2))
                        Spacer(modifier=  Modifier.width(0.dp))
                        Icon(imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = "Menu")

                    }


                }

                HorizontalDivider(modifier = Modifier.width(325.dp), color = Color(0xFFCCB5E2))

                Button(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6F04D9), contentColor = Color.White)) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween,modifier=Modifier.width(280.dp)) {
                        Text(text="Servicios Pagados", color = Color(0xFFCCB5E2))
                        Spacer(modifier=  Modifier.width(0.dp))
                        Icon(imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = "Menu")

                    }


                }

                HorizontalDivider(modifier = Modifier.width(325.dp), color = Color(0xFFCCB5E2))

                Button(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6F04D9), contentColor = Color.White)) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween,modifier=Modifier.width(280.dp)) {
                        Text(text="Historial", color = Color(0xFFCCB5E2))
                        Spacer(modifier=  Modifier.width(0.dp))
                        Icon(imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = "Menu")

                    }


                }
                HorizontalDivider(modifier = Modifier.width(325.dp), color = Color(0xFFCCB5E2))

                Spacer(modifier=  Modifier.height(25.dp))

                Text(text= "Promedio de Calificacion" , color = Color(0xFFCCB5E2))
                Text(text= "0.0", color = Color(0xFFCCB5E2))

                Spacer(modifier=  Modifier.height(40.dp))







            }


        }
    }
}





