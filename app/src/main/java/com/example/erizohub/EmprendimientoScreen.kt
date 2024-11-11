package com.example.erizohub

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun EmprendimientoScreen(navController: NavController, nombreEmprendimiento: String) {
    // Mostrar detalles del emprendimiento usando el nombre recibido
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Detalles de $nombreEmprendimiento", fontSize = 24.sp)
        // Aquí puedes agregar más detalles relacionados al emprendimiento usando otros datos
    }
}
