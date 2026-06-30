package com.gstcalculator.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gstcalculator.presentation.ui.screens.*

sealed class GstRoute(val route: String, val label: String) {
    data object Calculator : GstRoute("calculator", "Calculator")
    data object RateGuide : GstRoute("rate_guide", "Rate Guide")
    data object History : GstRoute("history", "History")
}

@Composable
fun GstNavHost() {
    val navController = rememberNavController()
    val items = listOf(GstRoute.Calculator, GstRoute.RateGuide, GstRoute.History)
    val navBackStack by navController.currentBackStackEntryAsState()
    val current = navBackStack?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEach { route ->
                    val icon = when (route) {
                        GstRoute.Calculator -> Icons.Default.Calculate
                        GstRoute.RateGuide -> Icons.Default.MenuBook
                        GstRoute.History -> Icons.Default.History
                    }
                    NavigationBarItem(
                        selected = current == route.route,
                        onClick = {
                            navController.navigate(route.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(icon, route.label) },
                        label = { Text(route.label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController,
            startDestination = GstRoute.Calculator.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(GstRoute.Calculator.route) { CalculatorScreen() }
            composable(GstRoute.RateGuide.route) { RateGuideScreen() }
            composable(GstRoute.History.route) { HistoryScreen() }
        }
    }
}
