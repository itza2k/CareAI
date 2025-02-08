package com.ff.careai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ff.careai.navigation.NavigationDestination
import com.ff.careai.screens.*
import com.ff.careai.ui.theme.CareAITheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Person
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.graphics.vector.ImageVector
import com.ff.careai.data.UserProfileManager
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userProfileManager = UserProfileManager(this)

        setContent {
            CareAITheme {
                val navController = rememberNavController()
                val isProfileSetup by userProfileManager.isProfileSetup.collectAsState(initial = false)
                val viewModel: HealthcareViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

                // Check profile setup status and navigate accordingly
                LaunchedEffect(isProfileSetup) {
                    if (!isProfileSetup) {
                        navController.navigate(NavigationDestination.ProfileSetup.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }

                Scaffold(
                    bottomBar = {
                        if (isProfileSetup) {
                            NavigationBar {
                                val navBackStackEntry by navController.currentBackStackEntryAsState()
                                val currentDestination = navBackStackEntry?.destination

                                NavigationBarItem(
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Filled.Build,
                                            contentDescription = "Chat"
                                        )
                                    },
                                    label = { Text("Chat") },
                                    selected = currentDestination?.hierarchy?.any {
                                        it.route == NavigationDestination.Chat.route
                                    } == true,
                                    onClick = {
                                        navController.navigate(NavigationDestination.Chat.route) {
                                            // Pop up to the start destination of the graph to
                                            // avoid building up a large stack of destinations
                                            popUpTo(NavigationDestination.Chat.route) {
                                                saveState = true
                                            }
                                            // Avoid multiple copies of the same destination
                                            launchSingleTop = true
                                            // Restore state when reselecting a previously selected item
                                            restoreState = true
                                        }
                                    }
                                )
                                NavigationBarItem(
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Filled.Person,
                                            contentDescription = "Profile"
                                        )
                                    },
                                    label = { Text("Profile") },
                                    selected = currentDestination?.hierarchy?.any {
                                        it.route == NavigationDestination.Profile.route
                                    } == true,
                                    onClick = {
                                        navController.navigate(NavigationDestination.Profile.route) {
                                            popUpTo(NavigationDestination.Profile.route) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = if (isProfileSetup)
                            NavigationDestination.Chat.route
                        else
                            NavigationDestination.ProfileSetup.route,
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        composable(NavigationDestination.Chat.route) {
                            ChatScreen(viewModel = viewModel)
                        }
                        composable(NavigationDestination.Profile.route) {
                            ProfileScreen(userProfileManager = userProfileManager)
                        }
                        composable(NavigationDestination.ProfileSetup.route) {
                            val profileSetupNavController = rememberNavController()
                            ProfileSetupScreen(
                                navController = profileSetupNavController,
                                userProfileManager = userProfileManager,
                                onProfileSetupComplete = {
                                    navController.navigate(NavigationDestination.Chat.route) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}