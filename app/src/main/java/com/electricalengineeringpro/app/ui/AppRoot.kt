package com.electricalengineeringpro.app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DesignServices
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.electricalengineeringpro.app.ui.calculations.CableSizingScreen
import com.electricalengineeringpro.app.ui.calculations.GeneratorScreen
import com.electricalengineeringpro.app.ui.calculations.LoadCalculationScreen
import com.electricalengineeringpro.app.ui.calculations.LoadScheduleScreen
import com.electricalengineeringpro.app.ui.calculations.MotorScreen
import com.electricalengineeringpro.app.ui.calculations.PanelScreen
import com.electricalengineeringpro.app.ui.calculations.ProtectionScreen
import com.electricalengineeringpro.app.ui.calculations.PumpScreen
import com.electricalengineeringpro.app.ui.calculations.ShortCircuitScreen
import com.electricalengineeringpro.app.ui.calculations.TransformerScreen
import com.electricalengineeringpro.app.ui.calculations.VoltageDropScreen
import com.electricalengineeringpro.app.ui.dashboard.DashboardScreen
import com.electricalengineeringpro.app.ui.design.DesignHomeScreen
import com.electricalengineeringpro.app.ui.network.NetworkScreen
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

                        null ->
                            DesignHomeScreen(
                                onModuleSelected = {
                                    selectedModule = it
                                }
                            )

                        "load" ->
                            LoadCalculationScreen {
                                selectedModule = null
                            }

                        "load_schedule" ->
                            LoadScheduleScreen {
                                selectedModule = null
                            }

                        "cable" ->
                            CableSizingScreen {
                                selectedModule = null
                            }

                        "voltage_drop" ->
                            VoltageDropScreen {
                                selectedModule = null
                            }

                        "short_circuit" ->
                            ShortCircuitScreen {
                                selectedModule = null
                            }

                        "transformer" ->
                            TransformerScreen {
                                selectedModule = null
                            }

                        "generator" ->
                            GeneratorScreen {
                                selectedModule = null
                            }

                        "motor" ->
                            MotorScreen {
                                selectedModule = null
                            }

                        "pump" ->
                            PumpScreen {
                                selectedModule = null
                            }

                        "panel" ->
                            PanelScreen {
                                selectedModule = null
                            }

                        "protection" ->
                            ProtectionScreen {
                                selectedModule = null
                            }

                        "network" ->
                            NetworkScreen {
                                selectedModule = null
                            }

                        "sld" ->
                            SldScreen {
                                selectedModule = null
                            }

                        "breaker" ->
                            ModulePlaceholderScreen(
                                title = "Breaker Selection",
                                onBack = {
                                    selectedModule = null
                                }
                            )

                        "report" ->
                            ModulePlaceholderScreen(
                                title = "Engineering Report",
                                onBack = {
                                    selectedModule = null
                                }
                            )

                        else ->
                            selectedModule = null
                    }
                }

                MainSection.PROJECTS ->
                    ProjectsScreen()

                MainSection.ABOUT ->
                    OwnershipScreen()
            }
        }
    }
}

@Composable
private fun ModulePlaceholderScreen(
    title: String,
    onBack: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        androidx.compose.material3.Button(
            onClick = onBack
        ) {
            Text("← Back")
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = title,
            style =
                androidx.compose.material3.MaterialTheme
                    .typography
                    .headlineMedium
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "Professional Engineering Module"
        )
    }
}
