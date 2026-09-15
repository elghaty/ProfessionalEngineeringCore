package com.electricalengineeringpro.app.ui.design

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.ElectricalServices
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SettingsInputComponent
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.filled.Transform
import androidx.compose.material.icons.filled.Water
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class DesignModule(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val route: String
)

private val modules = listOf(
    DesignModule(
        "Load Calculation",
        "Connected / demand / design load",
        Icons.Default.Calculate,
        "load"
    ),
    DesignModule(
        "Load Schedule",
        "Complete load schedule",
        Icons.Default.TableChart,
        "load_schedule"
    ),
    DesignModule(
        "Cable Sizing",
        "Cable selection and voltage drop",
        Icons.Default.SettingsInputComponent,
        "cable"
    ),
    DesignModule(
        "Breaker Selection",
        "MCB / MCCB / ACB",
        Icons.Default.Security,
        "breaker"
    ),
    DesignModule(
        "Voltage Drop",
        "Feeder voltage-drop calculation",
        Icons.Default.Bolt,
        "voltage_drop"
    ),
    DesignModule(
        "Short Circuit",
        "Fault-current calculation",
        Icons.Default.ElectricalServices,
        "short_circuit"
    ),
    DesignModule(
        "Transformers",
        "Transformer sizing and fault current",
        Icons.Default.Transform,
        "transformer"
    ),
    DesignModule(
        "Generators",
        "Generator sizing",
        Icons.Default.Engineering,
        "generator"
    ),
    DesignModule(
        "Motors",
        "Motor FLC and starting current",
        Icons.Default.Speed,
        "motor"
    ),
    DesignModule(
        "Pumps",
        "Hydraulic and electrical power",
        Icons.Default.Water,
        "pump"
    ),
    DesignModule(
        "MDB / DB / MCC",
        "Panel sizing and incomer",
        Icons.Default.GridView,
        "panel"
    ),
    DesignModule(
        "Protection",
        "Protection coordination checks",
        Icons.Default.Security,
        "protection"
    ),
    DesignModule(
        "Network",
        "Electrical network calculation",
        Icons.Default.Hub,
        "network"
    ),
    DesignModule(
        "SLD",
        "Automatic single-line diagram",
        Icons.Default.Memory,
        "sld"
    ),
    DesignModule(
        "Engineering Report",
        "Design results and documentation",
        Icons.Default.Build,
        "report"
    )
)

@Composable
fun DesignHomeScreen(
    onModuleSelected: (String) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Electrical Design",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Professional Engineering Workspace",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 155.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(modules) { module ->

                DesignModuleCard(
                    module = module,
                    onClick = {
                        onModuleSelected(module.route)
                    }
                )
            }
        }
    }
}

@Composable
private fun DesignModuleCard(
    module: DesignModule,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .width(46.dp)
                    .height(46.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = module.icon,
                    contentDescription = module.title,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = module.title,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = module.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
