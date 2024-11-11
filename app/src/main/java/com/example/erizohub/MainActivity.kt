package com.example.erizohub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.erizohub.ui.theme.ErizoHubTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ErizoHubTheme() {
                val myNavcontroller = rememberNavController()
                var selectItem by remember { mutableIntStateOf(0) }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            modifier = Modifier.background(color = Color.White),
                            title = { Text("") },
                            actions = {
                                IconButton(
                                    onClick = {
                                        myNavcontroller.navigate("userscreen") {
                                            popUpTo(myNavcontroller.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                        selectItem = 4
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.AccountCircle,
                                        contentDescription = "UserScreen"
                                    )
                                }
                            }
                        )
                    },
                    bottomBar = {
                        NavigationBar(
                            containerColor = Color(0xFFF2A74B),
                            modifier = Modifier
                                .background(color = ErizoHubTheme.Colors.background)
                                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)))
                        {
                            NavigationBarItem(
                                selected = selectItem == 0,
                                onClick = {
                                    myNavcontroller.navigate("home") {
                                        restoreState = true
                                        popUpTo(myNavcontroller.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                    }
                                    selectItem = 0
                                },
                                icon = {
                                    Image(
                                        painter = painterResource(id = R.drawable.homemorado),
                                        contentDescription = "Home",
                                        modifier = Modifier.size(24.dp)
                                    )
                                },
                                label = {
                                    Text(
                                        modifier = Modifier,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 15.sp,
                                        color = ErizoHubTheme.Colors.background,
                                        fontFamily = ErizoHubTheme.Fonts.customFontFamily,
                                        text = "Home"
                                    )
                                }
                            )
                            NavigationBarItem(
                                selected = selectItem == 1,
                                onClick = {
                                    myNavcontroller.navigate("emprende") {
                                        popUpTo(myNavcontroller.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                    selectItem = 1
                                },
                                icon = {
                                    Image(
                                        painter = painterResource(id = R.drawable.emprendemorado),
                                        contentDescription = "Home",
                                        modifier = Modifier.size(24.dp)
                                    )                                },
                                label = {
                                    Text(
                                        modifier = Modifier,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 15.sp,
                                        color = ErizoHubTheme.Colors.background,
                                        fontFamily = ErizoHubTheme.Fonts.customFontFamily,
                                        text = "Emprende"
                                    )
                                }
                            )
                            NavigationBarItem(
                                selected = selectItem == 2,
                                onClick = {
                                    myNavcontroller.navigate("chat") {
                                        popUpTo(myNavcontroller.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                    selectItem = 2
                                },
                                icon = {
                                    Image(
                                        painter = painterResource(id = R.drawable.chatmorado),
                                        contentDescription = "Home",
                                        modifier = Modifier.size(24.dp)
                                    )                                },
                                label = {
                                    Text(
                                        modifier = Modifier,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 15.sp,
                                        color = ErizoHubTheme.Colors.background,
                                        fontFamily = ErizoHubTheme.Fonts.customFontFamily,
                                        text = "Chat"
                                    )
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(0.dp)
                            .background(
                                color = Color(0xFFF2A74B)
                            )
                    ) {
                        NavHost(navController = myNavcontroller, startDestination = "home") {
                            composable("home") {
                                HomeScreen(myNavcontroller)
                            }
                            composable("emprende") {
                                EmprendeScreen(myNavcontroller)
                            }
                            composable("chat") {
                                ChatScreen(myNavcontroller)
                            }
                            composable("userscreen") {
                                UserScreen(myNavcontroller)
                            }
                        }
                    }
                }
            }
        }
    }
}

