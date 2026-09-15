package com.electricalengineeringpro.app.ui.calculations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.electricalengineeringpro.app.core.ProfessionalEngineeringCore
import com.electricalengineeringpro.app.core.model.Phase

@Composable
fun VoltageDropScreen(
    onBack: () -> Unit
) {
    val core = ProfessionalEngineeringCore.instance

    var current by remember { mutableStateOf("100") }
    var length by remember { mutableStateOf("50") }
    var resistance by remember { mutableStateOf("0.125") }
    var reactance by remember { mutableStateOf("0.08") }
    var voltage by remember { mutableStateOf("400") }
    var pf by remember { mutableStateOf("0.9") }

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
            title = "Voltage Drop",
            subtitle = "Feeder voltage-drop calculation"
        )

        EngineeringInputField(
            value = current,
            onValueChange = { current = it },
            label = "Current",
            unit = "A"
        )

        EngineeringInputField(
            value = length,
            onValueChange = { length = it },
            label = "Cable Length",
            unit = "m"
        )

        EngineeringInputField(
            value = resistance,
            onValueChange = { resistance = it },
            label = "Resistance",
            unit = "Ω/km"
        )

        EngineeringInputField(
            value = reactance,
            onValueChange = { reactance = it },
            label = "Reactance",
            unit = "Ω/km"
        )

        EngineeringInputField(
            value = voltage,
            onValueChange = { voltage = it },
            label = "Voltage",
            unit = "V"
        )

        EngineeringInputField(
            value = pf,
            onValueChange = { pf = it },
            label = "Power Factor"
        )

        Button(
            onClick = {
                val r = core.voltageDrop.calculate(
                    currentA = current.toDoubleOrNull() ?: 0.0,
                    lengthM = length.toDoubleOrNull() ?: 0.0,
                    resistanceOhmPerKm =
                        resistance.toDoubleOrNull() ?: 0.0,
                    reactanceOhmPerKm =
                        reactance.toDoubleOrNull() ?: 0.0,
                    voltageV = voltage.toDoubleOrNull() ?: 400.0,
                    powerFactor = pf.toDoubleOrNull() ?: 0.9,
                    phase = Phase.THREE_PHASE
                )

                result = """
                    Voltage Drop : %.2f V
                    Drop Percent : %.2f %%
                    Receiving Voltage : %.2f V
                """.trimIndent().format(
                    r.voltageDropV,
                    r.voltageDropPercent,
                    r.receivingVoltageV
                )
            }
        ) {
            Text("Calculate")
        }

        result?.let {
            CalculationResultCard(
                title = "Voltage Drop Result",
                result = it
            )
        }
    }
}
