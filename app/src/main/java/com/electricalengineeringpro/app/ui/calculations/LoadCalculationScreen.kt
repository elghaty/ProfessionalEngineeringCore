package com.electricalengineeringpro.app.ui.calculations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.electricalengineeringpro.app.core.ProfessionalEngineeringCore
import com.electricalengineeringpro.app.core.model.ElectricalLoad
import com.electricalengineeringpro.app.core.model.LoadType
import com.electricalengineeringpro.app.core.model.Phase

@Composable
fun LoadCalculationScreen(
    onBack: () -> Unit
) {
    val core = ProfessionalEngineeringCore.instance

    var name by remember { mutableStateOf("Load 01") }
    var quantity by remember { mutableStateOf("1") }
    var power by remember { mutableStateOf("10") }
    var pf by remember { mutableStateOf("0.9") }
    var demand by remember { mutableStateOf("0.8") }

    var result by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(onClick = onBack) {
            Text("← Back")
        }

        EngineeringModuleHeader(
            title = "Load Calculation",
            subtitle = "Connected load, demand load and design current"
        )

        EngineeringInputField(
            value = name,
            onValueChange = { name = it },
            label = "Load Name"
        )

        EngineeringInputField(
            value = quantity,
            onValueChange = { quantity = it },
            label = "Quantity"
        )

        EngineeringInputField(
            value = power,
            onValueChange = { power = it },
            label = "Unit Power",
            unit = "kW"
        )

        EngineeringInputField(
            value = pf,
            onValueChange = { pf = it },
            label = "Power Factor"
        )

        EngineeringInputField(
            value = demand,
            onValueChange = { demand = it },
            label = "Demand Factor"
        )

        Button(
            onClick = {
                val load = ElectricalLoad(
                    id = "LOAD-${System.currentTimeMillis()}",
                    name = name,
                    type = LoadType.OTHER,
                    quantity = quantity.toIntOrNull() ?: 1,
                    unitPowerKw = power.toDoubleOrNull() ?: 0.0,
                    powerFactor = pf.toDoubleOrNull() ?: 0.9,
                    demandFactor = demand.toDoubleOrNull() ?: 0.8,
                    phase = Phase.THREE_PHASE,
                    voltageV = 400.0,
                    panelName = "MDB"
                )

                val r = core.loads.calculate(load)

                result = """
                    Connected Load : %.2f kW
                    Demand Load    : %.2f kW
                    Design Load    : %.2f kW
                    Design Current : %.2f A
                """.trimIndent().format(
                    r.connectedLoadKw,
                    r.demandLoadKw,
                    r.designLoadKw,
                    r.designCurrentA
                )
            }
        ) {
            Text("Calculate")
        }

        result?.let {
            CalculationResultCard(
                title = "Load Result",
                result = it
            )
        }
    }
}
