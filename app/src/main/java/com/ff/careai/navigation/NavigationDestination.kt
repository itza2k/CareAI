package com.ff.careai.navigation

sealed class NavigationDestination(val route: String) {
    object Profile : NavigationDestination("profile")
    object Chat : NavigationDestination("chat")
    object ProfileSetup : NavigationDestination("profile_setup")
}
