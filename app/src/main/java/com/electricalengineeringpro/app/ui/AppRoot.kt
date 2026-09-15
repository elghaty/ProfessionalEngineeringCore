package com.electricalengineeringpro.app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DesignServices
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.electricalengineeringpro.app.ui.dashboard.DashboardScreen
import com.electricalengineeringpro.app.ui.design.DesignHomeScreen
import com.electricalengineeringpro.app.ui.ownership.OwnershipScreen
import com.electricalengineeringpro.app.ui.projects.ProjectsScreen
import com.electricalengineeringpro.app.ui.sld.SldScreen

private enum class MainSection {
    DESIGN,
    PROJECTS,
    ABOUT
}

@Composable
fun AppRoot() {

    var section by remember {
        mutableStateOf(MainSection.DESIGN)
    }

    var selectedModule by remember {
        mutableStateOf<String?>(null)
    }

    Scaffold(
        bottomBar = {

            NavigationBar {

                NavigationBarItem(
                    selected = section == MainSection.DESIGN,
                    onClick = {
                        section = MainSection.DESIGN
                        selectedModule = null
                    },
                    icon = {
                        Icon(
                            Icons.Default.DesignServices,
                            contentDescription = "Design"
                        )
                    },
                    label = {
                        Text("Design")
                    }
                )

                NavigationBarItem(
                    selected = section == MainSection.PROJECTS,
                    onClick = {
                        section = MainSection.PROJECTS
                        selectedModule = null
                    },
                    icon = {
                        Icon(
                            Icons.Default.Folder,
                            contentDescription = "Projects"
                        )
                    },
                    label = {
                        Text("Projects")
                    }
                )

                NavigationBarItem(
                    selected = section == MainSection.ABOUT,
                    onClick = {
                        section = MainSection.ABOUT
                        selectedModule = null
                    },
                    icon = {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = "About"
                        )
                    },
                    label = {
                        Text("About")
                    }
                )
            }
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            when (section) {

                MainSection.DESIGN -> {

                    when (selectedModule) {

                        "sld" -> {
                            SldScreen(
                                onBack = {
                                    selectedModule = null
                                }
                            )
                        }

                        null -> {
                            DesignHomeScreen(
                                onModuleSelected = {
                                    selectedModule = it
                                }
                            )
                        }

                        else -> {
                            ModulePlaceholderScreen(
                                title = moduleTitle(selectedModule!!),
                                onBack = {
                                    selectedModule = null
                                }
                            )
                        }
                    }
                }

                MainSection.PROJECTS -> {
                    ProjectsScreen()
                }

                MainSection.ABOUT -> {
                    OwnershipScreen()
                }
            }
        }
    }
}

private fun moduleTitle(route: String): String {
    return when (route) {
        "load" -> "Load Calculation"
        "load_schedule" -> "Load Schedule"
        "cable" -> "Cable Sizing"
        "breaker" -> "Breaker Selection"
        "voltage_drop" -> "Voltage Drop"
        "short_circuit" -> "Short Circuit"
        "transformer" -> "Transformers"
        "generator" -> "Generators"
        "motor" -> "Motors"
        "pump" -> "Pumps"
        "panel" -> "MDB / DB / MCC"
        "protection" -> "Protection"
        "network" -> "Electrical Network"
        "report" -> "Engineering Report"
        else -> "Engineering Module"
    }
}

@Composable
private fun ModulePlaceholderScreen(
    title: String,
    onBack: () -> Unit
) {

    androidx.compose.foundation.layout.Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        androidx.compose.material3.TextButton(
            onClick = onBack
        ) {
            Text("← Back")
        }

        androidx.compose.foundation.layout.Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = title,
            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
        )

        androidx.compose.foundation.layout.Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Engineering module"
        )

        androidx.compose.foundation.layout.Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = "This module is connected to ProfessionalEngineeringCore."
        )
    }
}
