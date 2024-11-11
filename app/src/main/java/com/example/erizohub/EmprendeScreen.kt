package com.example.erizohub

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun EmprendeScreen(navController: NavController) {

    var expanded by remember { mutableStateOf(false) }
    var urlText by remember { mutableStateOf("") }

    Column(modifier= Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth(), // Para permitir la alineación en el centro horizontal
            horizontalArrangement = Arrangement.Center // Centra todos los elementos en la fila
        ) {
            Button(
                onClick = { expanded = !expanded },
                modifier = Modifier
                    .padding(0.dp, 100.dp, 0.dp, 20.dp).fillMaxSize(),
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




        }




        Row (modifier = Modifier.fillMaxHeight())
        {



        }

    }
}
