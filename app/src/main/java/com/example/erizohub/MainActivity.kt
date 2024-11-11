package com.example.erizohub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.erizohub.ui.theme.ErizoHubTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ErizoHubTheme {
                val myNavController = rememberNavController()
                var selectedItem by remember { mutableIntStateOf(0) }
                var bottomBarColor by remember { mutableStateOf(Color(0xFFF2A74B)) }
                var bottomBarColorBackground by remember { mutableStateOf(ErizoHubTheme.Colors.background) }
                var bottomBarIcons by remember { mutableStateOf(ErizoHubTheme.Colors.background) }

                LaunchedEffect(myNavController) {
                    myNavController.addOnDestinationChangedListener { _, destination, _ ->
                        bottomBarColor = when (destination.route) {
                            "home" -> ErizoHubTheme.Colors.background
                            "emprende" -> ErizoHubTheme.Colors.background
                            "chat_selection" -> ErizoHubTheme.Colors.background
                            else -> Color(0xFFF2A74B)
                        }

                        bottomBarColorBackground = when (destination.route) {
                            "home" -> Color.White
                            "emprende" -> Color.White
                            "chat_selection" -> Color.White
                            else -> ErizoHubTheme.Colors.background
                        }

                        bottomBarIcons = when (destination.route) {
                            "home" -> Color(0xFFF2A74B)
                            "emprende" -> Color(0xFFF2A74B)
                            "chat_selection" -> Color(0xFFF2A74B)
                            else -> ErizoHubTheme.Colors.background
                        }
                    }
                }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            modifier = Modifier.background(color = Color.White),
                            title = { Text("") },
                            actions = {
                                IconButton(
                                    onClick = {
                                        myNavController.navigate("userscreen") {
                                            popUpTo(myNavController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                        selectedItem = 4
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
                            containerColor = bottomBarColor,
                            modifier = Modifier
                                .background(color = bottomBarColorBackground)
                                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                        ) {
                            NavigationBarItem(
                                selected = selectedItem == 0,
                                onClick = {
                                    myNavController.navigate("home") {
                                        restoreState = true
                                        popUpTo(myNavController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                    }
                                    selectedItem = 0
                                },
                                icon = {
                                    Image(
                                        painter = painterResource(id = R.drawable.homemorado),
                                        contentDescription = "Home",
                                        modifier = Modifier.size(24.dp),
                                        colorFilter = ColorFilter.tint(bottomBarIcons)
                                    )
                                },
                                label = {
                                    Text(
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 15.sp,
                                        color = bottomBarIcons,
                                        fontFamily = ErizoHubTheme.Fonts.customFontFamily,
                                        text = "Home"
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = bottomBarIcons,
                                    unselectedIconColor = bottomBarIcons.copy(alpha = 0.6f),
                                    selectedTextColor = bottomBarIcons,
                                    unselectedTextColor = bottomBarIcons.copy(alpha = 0.6f),
                                    indicatorColor = Color.Transparent
                                )
                            )
                            NavigationBarItem(
                                selected = selectedItem == 1,
                                onClick = {
                                    myNavController.navigate("emprende") {
                                        popUpTo(myNavController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                    selectedItem = 1
                                },
                                icon = {
                                    Image(
                                        painter = painterResource(id = R.drawable.emprendemorado),
                                        contentDescription = "Emprende",
                                        modifier = Modifier.size(24.dp),
                                        colorFilter = ColorFilter.tint(bottomBarIcons)
                                    )
                                },
                                label = {
                                    Text(
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 15.sp,
                                        color = bottomBarIcons,
                                        fontFamily = ErizoHubTheme.Fonts.customFontFamily,
                                        text = "Emprende"
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = bottomBarIcons,
                                    unselectedIconColor = bottomBarIcons.copy(alpha = 0.6f),
                                    selectedTextColor = bottomBarIcons,
                                    unselectedTextColor = bottomBarIcons.copy(alpha = 0.6f),
                                    indicatorColor = Color.Transparent
                                )
                            )
                            NavigationBarItem(
                                selected = selectedItem == 2,
                                onClick = {
                                    myNavController.navigate("chat_selection") {
                                        popUpTo(myNavController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                    selectedItem = 2
                                },
                                icon = {
                                    Image(
                                        painter = painterResource(id = R.drawable.chatmorado),
                                        contentDescription = "Chat",
                                        modifier = Modifier.size(24.dp),
                                        colorFilter = ColorFilter.tint(bottomBarIcons)
                                    )
                                },
                                label = {
                                    Text(
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 15.sp,
                                        color = bottomBarIcons,
                                        fontFamily = ErizoHubTheme.Fonts.customFontFamily,
                                        text = "Chat"
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = bottomBarIcons,
                                    unselectedIconColor = bottomBarIcons.copy(alpha = 0.6f),
                                    selectedTextColor = bottomBarIcons,
                                    unselectedTextColor = bottomBarIcons.copy(alpha = 0.6f),
                                    indicatorColor = Color.Transparent
                                )
                            )
                        }
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .background(color = Color(0xFFF2A74B))
                    ) {
                        NavHost(navController = myNavController, startDestination = "home") {
                            composable("home") {
                                HomeScreen(myNavController)
                            }
                            composable("emprende") {
                                EmprendeScreen(myNavController)
                            }
                            composable("chat_selection") {
                                ChatSelectionScreen(myNavController)
                            }
                            composable("user_selection_screen") {
                                UserSelectionScreen(myNavController)
                            }
                            composable("userscreen") {
                                UserScreen(myNavController)
                            }
                            composable(
                                "emprendimientoScreen/{nombreEmprendimiento}",
                                arguments = listOf(navArgument("nombreEmprendimiento") { type = NavType.StringType })
                            ) { backStackEntry ->
                                val nombreEmprendimiento = backStackEntry.arguments?.getString("nombreEmprendimiento") ?: ""
                                EmprendimientoScreen(myNavController, nombreEmprendimiento)
                            }
                            composable("chat_screen/{chatId}/{otherUserId}") { backStackEntry ->
                                val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
                                val otherUserId = backStackEntry.arguments?.getString("otherUserId") ?: ""
                                ChatScreen(chatId = chatId, otherUserId = otherUserId)
                            }
                        }
                    }
                }
            }
        }
    }
}
