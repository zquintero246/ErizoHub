package com.example.erizohub.Home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.erizohub.R

@Preview(showSystemUi = true)
@Composable
fun EmprendimientoSeleccionado() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.erizo),
            contentDescription = "Descripción de la imagen",
            modifier = Modifier.fillMaxWidth().height(450.dp)
        )
        Text(
            text = "Nombre Producto",
            modifier = Modifier.width(300.dp),
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
        )
        Text(
            text = "Descripcion Producto",
            modifier = Modifier.width(300.dp),
            style = TextStyle(fontSize = 12.sp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(
                    color = Color(0xFFF6F04D9),
                    shape = RoundedCornerShape(26.dp) // Bordes redondeados
                )

                .padding(start = 40.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            // Alineación horizontal de los elementos dentro de la fila
            Icon(
                imageVector = androidx.compose.material.icons.Icons.Default.Favorite,
                contentDescription = "Favorito",
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(10.dp)) // Espacio entre el ícono y el texto
            Text(
                text = "0",
                modifier = Modifier.width(20.dp),
                style = TextStyle(fontSize = 18.sp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Icon(
                imageVector = androidx.compose.material.icons.Icons.Default.Email,
                contentDescription = "Favorito",
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = "0",
                modifier = Modifier.width(10.dp),
                style = TextStyle(fontSize = 18.sp)
            )

            Spacer(modifier = Modifier.width(60.dp))

            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                elevation = null, // Elimina cualquier sombra
                modifier = Modifier.padding(0.dp).width(300.dp) // Opcional: Elimina cualquier padding extra
            ) {
                Text(
                    text = "Productos",
                    style = TextStyle(fontSize = 16.sp, color = Color.Black) // Asegúrate de que el texto sea visible
                )

                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.ArrowForward,
                    contentDescription = "Favorito",
                    modifier = Modifier.size(10.dp).width(10.dp)
                )
            }
        }
        Column(modifier = Modifier.fillMaxWidth().fillMaxHeight().align(Alignment.CenterHorizontally)) {

            Row(modifier = Modifier.fillMaxWidth().padding(top = 50.dp, start = 40.dp, end = 40.dp), verticalAlignment = Alignment.CenterVertically,) {
                Icon(imageVector = androidx.compose.material.icons.Icons.Default.AccountCircle, contentDescription = "Icono usuario", modifier = Modifier.size(30.dp).width(10.dp))
                Column {
                    Text(text = "User", modifier = Modifier.width(300.dp).padding(start = 20.dp), style = TextStyle(fontSize = 12.sp))
                    Text(text = "Lorem ipsum dolor sit amet", modifier = Modifier.width(300.dp).padding(start = 20.dp), style = TextStyle(fontSize = 12.sp))

                }


            }



        }
    }
}