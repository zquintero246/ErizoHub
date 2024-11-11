package com.example.erizohub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ErizoHubTheme() {
                val myNavcontroller = rememberNavController()
                var selectItem by remember { mutableStateOf(0) }

                Scaffold(
                    topBar = {
                        TopAppBar(
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
                            containerColor = Color(0xFFF2784B),modifier = Modifier.clip(
                                RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))) {
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
                                    Icon(imageVector = Icons.Filled.Home, contentDescription = "Home")
                                },
                                label = {
                                    Text(text = "Home")
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
                                    Icon(imageVector = Icons.Filled.Menu, contentDescription = "Emprende")
                                },
                                label = {
                                    Text(text = "Emprende")
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
                                    Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = "Chat")
                                },
                                label = {
                                    Text(text = "Chat")
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

