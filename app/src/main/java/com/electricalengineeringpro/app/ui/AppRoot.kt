package com.electricalengineeringpro.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.electricalengineeringpro.app.ui.dashboard.DashboardScreen
import com.electricalengineeringpro.app.ui.projects.ProjectsScreen
import com.electricalengineeringpro.app.ui.ownership.OwnershipScreen

@Composable
fun AppRoot() {
    var selected by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selected == 0,
                    onClick = { selected = 0 },
                    icon = { Icon(Icons.Default.Calculate, null) },
                    label = { Text("Design") }
                )
                NavigationBarItem(
                    selected = selected == 1,
                    onClick = { selected = 1 },
                    icon = { Icon(Icons.Default.Folder, null) },
                    label = { Text("Projects") }
                )
                NavigationBarItem(
                    selected = selected == 2,
                    onClick = { selected = 2 },
                    icon = { Icon(Icons.Default.Info, null) },
                    label = { Text("About") }
                )
            }
        }
    ) { padding ->
        when (selected) {
            0 -> DashboardScreen(Modifier.padding(padding))
            1 -> ProjectsScreen(Modifier.padding(padding))
            else -> OwnershipScreen(Modifier.padding(padding))
        }
    }
}
