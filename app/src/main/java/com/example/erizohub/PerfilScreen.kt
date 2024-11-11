package com.example.erizohub

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun Perfil(navController: NavController) {

    Column(modifier = Modifier.fillMaxWidth().background(color = Color.White))
    {

        Column (modifier = Modifier
            .height(350.dp)
            .fillMaxWidth()
            .background(color = Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
        )
        {
            Image(painter = painterResource(id = R.drawable.profile),
            contentDescription = "profile",
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.CenterHorizontally)
                .padding(0.dp, 100.dp, 0.dp, 20.dp)
            )
            Text(
                modifier = Modifier,
                fontSize = 20.sp,
                fontFamily = ErizoHubTheme.Fonts.customFontFamily,
                color = Color(0xFFB8B8B8),
                text = "Nombre usuario"
            )
        }

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
                        text="Seguir")
                }

                HorizontalDivider(modifier = Modifier.width(325.dp).padding(top = 40.dp).alpha(0.5f))

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



                Row(horizontalArrangement = Arrangement.SpaceBetween,modifier=Modifier
                    .width(325.dp)
                    .padding(top = 56.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        modifier = Modifier.alpha(0.5f),
                        fontFamily = ErizoHubTheme.Fonts.customFontFamily,
                        fontSize = 20.sp,
                        text="CALIFICAR:",
                        color=Color.White)
                    Button(
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6F04D9), contentColor = Color(0xFFF2784B)),
                        modifier = Modifier.size(30.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Star,
                            contentDescription = "Star",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Button(
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6F04D9), contentColor = Color(0xFFF2784B)),
                        modifier = Modifier.size(30.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Star",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Button(
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6F04D9), contentColor = Color(0xFFF2784B)),
                        modifier = Modifier.size(30.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Star",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Button(
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6F04D9), contentColor = Color(0xFFF2784B)),
                        modifier = Modifier.size(30.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Star",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Button(
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6F04D9), contentColor = Color(0xFFF2784B)),
                        modifier = Modifier.size(30.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Star",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
    }
}





