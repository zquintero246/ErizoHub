package com.example.erizohub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.erizohub.ui.theme.ErizoHubTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ErizoHubTheme(){
                val myNavcontroller = rememberNavController()
                var selectItem by remember { mutableStateOf(0) }

                Scaffold(bottomBar = {
                    NavigationBar {
                        NavigationBarItem(
                            selected = selectItem == 0,
                            onClick = {
                                myNavcontroller.navigate("home"){
                                    restoreState = true
                                    popUpTo(myNavcontroller.graph.findStartDestination().id){
                                        saveState = true
                                    }
                                    launchSingleTop = true

                                }
                                selectItem = 0

                            },
                            icon = {
                                Icon(imageVector = Icons.Filled.Home,
                                    contentDescription = "Home" )
                            },
                            label = {
                                Text(text = "Home")
                            }
                        )
                        NavigationBarItem(
                            selected = selectItem == 1,
                            onClick =
                            {
                                myNavcontroller.navigate("emprende"){
                                    popUpTo(myNavcontroller.graph.findStartDestination().id){
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                                selectItem = 1


                            },
                            icon = {
                                Icon(imageVector = Icons.Filled.Menu,
                                    contentDescription = "Emprende" )
                            },
                            label = {
                                Text(text = "Emprende")
                            }

                        )
                        NavigationBarItem(
                            selected = selectItem == 2,
                            onClick =
                            {
                                myNavcontroller.navigate("chat"){
                                    popUpTo(myNavcontroller.graph.findStartDestination().id){
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                                selectItem = 2


                            },
                            icon = {
                                Icon(imageVector = Icons.Filled.AccountCircle,
                                    contentDescription = "Chat" )
                            },
                            label = {
                                Text(text = "Chat")
                            }

                        )

                    }
                }
                ) { innerPadding->
                    Column (modifier = Modifier
                        .padding(innerPadding)
                        .padding(12.dp)){
                        NavHost(navController = myNavcontroller,
                            startDestination = "profile" )
                        {
                            composable("home")   {
                                HomeScreen(myNavcontroller)
                            }
                            composable("emprende")   {
                                EmprendeScreen(myNavcontroller)
                            }
                            composable("chat")   {
                                ChatScreen(myNavcontroller)
                            }

                        }
                    }
                }

            }
        }
    }
}